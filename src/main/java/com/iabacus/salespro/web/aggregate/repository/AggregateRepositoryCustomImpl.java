package com.iabacus.salespro.web.aggregate.repository;

import com.iabacus.salespro.web.aggregate.response.AggregateResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class AggregateRepositoryCustomImpl implements AggregateRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AggregateResponse> getAggregate(String inputYear) {
        String sql = """
            WITH
            /**********
            * 부서별 비용 및 인력 집계:
            * 지정 연도에 해당하는 인건비, 판관비, 제경비, 총 비용 및 인력 유형별 집계
            **********/
            dept_cost AS (
                SELECT
                    project_owner_department_id,
                    SUM(monthly_wage)       AS monthly_wage,       -- 부서별 월 인건비 합계
                    SUM(sgae_amount)        AS sgae_amount,        -- 부서별 판관비 합계
                    SUM(ovhe_amount)        AS ovhe_amount,        -- 부서별 제경비 합계
                    SUM(total_cost)         AS total_cost,         -- 부서별 총 비용 합계
                    -- 인력 유형별 인원 수
                    COUNT(DISTINCT CASE WHEN personnel_type = '정직원' THEN personnel_id END) AS fulltime_count,
                    COUNT(DISTINCT CASE WHEN personnel_type = '외주' THEN personnel_id END) AS outsource_count,
                    COUNT(DISTINCT CASE WHEN personnel_type = '프리랜서' THEN personnel_id END) AS freelancer_count,
                    -- 인력 유형별 인건비
                    SUM(CASE WHEN personnel_type = '정직원' THEN monthly_wage ELSE 0 END) AS fulltime_cost,
                    SUM(CASE WHEN personnel_type = '외주' THEN monthly_wage ELSE 0 END) AS outsource_cost,
                    SUM(CASE WHEN personnel_type = '프리랜서' THEN monthly_wage ELSE 0 END) AS freelancer_cost,
                    -- 사업 유형별 매출
                    SUM(CASE WHEN project_type = 'SI' THEN monthly_wage ELSE 0 END) AS si_cost,
                    SUM(CASE WHEN project_type = 'SM' THEN monthly_wage ELSE 0 END) AS sm_cost
                FROM tb_monthly_employee_cost_aggregate
                WHERE YEAR(project_start_date) = :inputYear OR YEAR(project_end_date) = :inputYear
                GROUP BY project_owner_department_id
            ),
            /**********
            * 부서별 총 계약금액:
            * 각 프로젝트별 중복 계산을 방지하기 위해 프로젝트 id와 부서 id로 그룹화 후,
            * 부서별 계약금액을 합산
            **********/
            dept_contract AS (
                SELECT
                    project_owner_department_id,
                    SUM(project_contract_amount) AS total_contract_amount
                FROM (
                    SELECT project_id, project_owner_department_id, project_contract_amount
                    FROM tb_monthly_employee_cost_aggregate
                    WHERE YEAR(project_start_date) = :inputYear OR YEAR(project_end_date) = :inputYear
                    GROUP BY project_id, project_owner_department_id
                ) t
                GROUP BY project_owner_department_id
            ),
            /**********
            * 부서별 매출 목표:
            * 각 부서당 하나의 연간 매출 목표 추출
            **********/
            dept_sales AS (
                SELECT
                    project_owner_department_id,
                    team_sales_goal_amount_by_year
                FROM tb_monthly_employee_cost_aggregate
                WHERE YEAR(project_start_date) = :inputYear OR YEAR(project_end_date) = :inputYear
                GROUP BY project_owner_department_id
            ),
            /**********
            * 위의 집계들을 부서별로 결합하여 최종 부서 집계 테이블 생성
            **********/
            agg_department AS (
                SELECT
                    c.project_owner_department_id,
                    d.total_contract_amount,
                    s.team_sales_goal_amount_by_year,
                    c.monthly_wage,
                    c.sgae_amount,
                    c.ovhe_amount,
                    c.total_cost,
                    c.fulltime_count,
                    c.outsource_count,
                    c.freelancer_count,
                    c.fulltime_cost,
                    c.outsource_cost,
                    c.freelancer_cost,
                    c.si_cost,
                    c.sm_cost
                FROM dept_cost c
                LEFT JOIN dept_contract d ON c.project_owner_department_id = d.project_owner_department_id
                LEFT JOIN dept_sales s    ON c.project_owner_department_id = s.project_owner_department_id
            ),
            /**********
            * 월별 실제 작업일 기반 계약금액 산출:
            * 지정 연도(:inputYear) 내 각 프로젝트의 실제 적용기간(프로젝트 기간과 입력 연도 범위의 교집합)을
            * 이용해 전체 프로젝트 기간 기준 하루당 계약금액을 계산하고, 월별로 해당 월에 적용된 일수만큼 일할 계산
            **********/
            monthly_breakdown AS (
                /**********
                * 12개월(월) 목록 생성:
                * 최종 결과에서 모든 월(01~12)을 포함하기 위해 사용
                **********/
                WITH months AS (
                    SELECT '01' AS mon UNION ALL SELECT '02' UNION ALL SELECT '03' UNION ALL
                    SELECT '04' UNION ALL SELECT '05' UNION ALL SELECT '06' UNION ALL
                    SELECT '07' UNION ALL SELECT '08' UNION ALL SELECT '09' UNION ALL
                    SELECT '10' UNION ALL SELECT '11' UNION ALL SELECT '12'
                ),
                /**********
                * 프로젝트 기본 정보 및 효과적 기간 산출:
                * - 전체 프로젝트 기간: start_date ~ end_date 기준
                * - 효과적 적용 기간: 입력 연도 내에서 실제로 적용되는 시작일과 종료일을 계산
                *   (프로젝트가 전년도에 시작하거나 다음 해에 끝난 경우, 입력 연도의 경계로 조정)
                **********/
                project_base AS (
                    SELECT DISTINCT
                        project_id,
                        project_owner_department_id,
                        DATE(project_start_date) AS start_date,
                        DATE(project_end_date)   AS end_date,
                        project_contract_amount,
                        DATEDIFF(project_end_date, project_start_date) + 1 AS total_project_days,
                        CASE 
                            WHEN project_start_date < DATE(CONCAT(:inputYear, '-01-01')) 
                            THEN DATE(CONCAT(:inputYear, '-01-01'))
                            ELSE DATE(project_start_date)
                        END AS effective_start,
                        CASE 
                            WHEN project_end_date > DATE(CONCAT(:inputYear, '-12-31')) 
                            THEN DATE(CONCAT(:inputYear, '-12-31'))
                            ELSE DATE(project_end_date)
                        END AS effective_end
                    FROM tb_monthly_employee_cost_aggregate
                    -- 프로젝트 기간이 입력 연도와 겹치는 것만 선택
                    WHERE project_end_date >= DATE(CONCAT(:inputYear, '-01-01'))
                    AND project_start_date <= DATE(CONCAT(:inputYear, '-12-31'))
                ),
                /**********
                * 각 프로젝트에 대해 지정 연도 내 월별 행 생성:
                * 효과적 적용 기간(effective_start ~ effective_end)에서 월별로 행을 생성하여,
                * 해당 프로젝트가 몇 개의 월에 걸쳐 진행되었는지 계산
                * (최대 12개월까지 커버)
                **********/
                project_months AS (
                    SELECT
                        pb.project_id,
                        pb.project_owner_department_id,
                        pb.project_contract_amount,
                        pb.effective_start,
                        pb.effective_end,
                        pb.total_project_days,
                        ADDDATE(pb.effective_start, INTERVAL seq MONTH) AS active_month
                    FROM project_base pb
                    JOIN (
                        SELECT 0 AS seq UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
                        UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9
                        UNION ALL SELECT 10 UNION ALL SELECT 11
                    ) seqs
                    WHERE DATE_FORMAT(ADDDATE(pb.effective_start, INTERVAL seq MONTH), '%Y-%m')<= DATE_FORMAT(pb.effective_end, '%Y-%m')
                ),
                /**********
                * 각 월별 실제 작업일수와 월별 계약금액 계산:
                * - 각 프로젝트의 해당 월(active_month)에 대해,
                *   해당 월의 시작일(month_start)과 마지막일(month_end)을 구함
                * - 효과적 적용 기간과 월의 경계 사이의 교집합 일수를 계산하여,
                *   전체 프로젝트 기간(total_project_days) 기준 하루당 계약금액과 곱해 월별 계약금액 산출
                **********/
                monthly_value AS (
                    SELECT
                        pm.project_id,
                        pm.project_owner_department_id,
                        DATE_FORMAT(pm.active_month, '%m') AS mon,
                        pm.project_contract_amount,
                        pm.total_project_days,
                        pm.effective_start,
                        pm.effective_end,
                        DATE_FORMAT(pm.active_month, '%Y-%m-01') AS month_start,
                        LAST_DAY(pm.active_month) AS month_end,
                        DATEDIFF(
                            LEAST(pm.effective_end, LAST_DAY(pm.active_month)),
                            GREATEST(pm.effective_start, DATE_FORMAT(pm.active_month, '%Y-%m-01'))
                        ) + 1 AS active_days_in_month,
                        (pm.project_contract_amount / pm.total_project_days) *
                        (DATEDIFF(
                            LEAST(pm.effective_end, LAST_DAY(pm.active_month)),
                            GREATEST(pm.effective_start, DATE_FORMAT(pm.active_month, '%Y-%m-01'))
                        ) + 1) AS monthly_contract_amount
                    FROM project_months pm
                )
                /**********
                * 부서별, 월별 집계:
                * 모든 부서에 대해 12개월(월) 목록을 CROSS JOIN한 후,
                * 각 부서의 월별 월간 계약금액을 합산하여 산출
                **********/
                SELECT 
                    d.project_owner_department_id,
                    m.mon,
                    SUM(mv.monthly_contract_amount) AS monthly_contract_amount
                FROM (SELECT DISTINCT project_owner_department_id FROM project_base) d
                CROSS JOIN months m
                LEFT JOIN monthly_value mv
                    ON mv.project_owner_department_id = d.project_owner_department_id
                    AND mv.mon = m.mon
                GROUP BY d.project_owner_department_id, m.mon
            ),
            /**********
            * 부서별 월별 매출액 피벗:
            * 월별 집계 결과를 부서별로 피벗하여 각 월의 매출액(sales_01 ~ sales_12) 컬럼으로 전환
            **********/
            monthly_pivot AS (
                SELECT 
                    project_owner_department_id,
                    ROUND(SUM(CASE WHEN mon = '01' THEN monthly_contract_amount ELSE 0 END), 0) AS sales_01,
                    ROUND(SUM(CASE WHEN mon = '02' THEN monthly_contract_amount ELSE 0 END), 0) AS sales_02,
                    ROUND(SUM(CASE WHEN mon = '03' THEN monthly_contract_amount ELSE 0 END), 0) AS sales_03,
                    ROUND(SUM(CASE WHEN mon = '04' THEN monthly_contract_amount ELSE 0 END), 0) AS sales_04,
                    ROUND(SUM(CASE WHEN mon = '05' THEN monthly_contract_amount ELSE 0 END), 0) AS sales_05,
                    ROUND(SUM(CASE WHEN mon = '06' THEN monthly_contract_amount ELSE 0 END), 0) AS sales_06,
                    ROUND(SUM(CASE WHEN mon = '07' THEN monthly_contract_amount ELSE 0 END), 0) AS sales_07,
                    ROUND(SUM(CASE WHEN mon = '08' THEN monthly_contract_amount ELSE 0 END), 0) AS sales_08,
                    ROUND(SUM(CASE WHEN mon = '09' THEN monthly_contract_amount ELSE 0 END), 0) AS sales_09,
                    ROUND(SUM(CASE WHEN mon = '10' THEN monthly_contract_amount ELSE 0 END), 0) AS sales_10,
                    ROUND(SUM(CASE WHEN mon = '11' THEN monthly_contract_amount ELSE 0 END), 0) AS sales_11,
                    ROUND(SUM(CASE WHEN mon = '12' THEN monthly_contract_amount ELSE 0 END), 0) AS sales_12
                FROM monthly_breakdown
                GROUP BY project_owner_department_id
            )
            /**********
            * 최종 결과 출력:
            * 부서별 집계(계약금액, 비용, 인력 등)와 월별 매출액을 결합하여 출력
            **********/
            SELECT
                a.project_owner_department_id AS 부서범위,
                a.total_contract_amount AS 매출합계,
                a.team_sales_goal_amount_by_year AS 매출목표,
                ROUND(a.total_contract_amount / a.team_sales_goal_amount_by_year * 100, 2) AS 달성률,
                a.monthly_wage       AS 인건비,
                a.sgae_amount       AS 판관비,
                a.ovhe_amount       AS 제경비,
                (a.total_contract_amount - a.total_cost) AS 영업이익,
                ROUND((a.total_contract_amount - a.total_cost) / a.total_contract_amount * 100, 2) AS 영업이익률,
                a.fulltime_count    AS 정직원,
                a.outsource_count   AS 외주,
                a.freelancer_count  AS 프리랜서,
                a.fulltime_cost     AS 정직원인건비,
                a.outsource_cost    AS 외주인건비,
                a.freelancer_cost   AS 프리랜서인건비,
                a.si_cost AS SI,
                a.sm_cost AS SM,
                p.sales_01,
                p.sales_02,
                p.sales_03,
                p.sales_04,
                p.sales_05,
                p.sales_06,
                p.sales_07,
                p.sales_08,
                p.sales_09,
                p.sales_10,
                p.sales_11,
                p.sales_12
            FROM agg_department a
            LEFT JOIN monthly_pivot p
                ON a.project_owner_department_id = p.project_owner_department_id
            ORDER BY a.project_owner_department_id;
            """;

        Query query = entityManager.createNativeQuery(sql, "AggregateResponseMapping");
        query.setParameter("inputYear", inputYear);
        return query.getResultList();
    }
}