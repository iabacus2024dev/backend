package com.iabacus.salespro.web.aggregate.repository;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.springframework.stereotype.Repository;

import com.iabacus.salespro.web.aggregate.response.AggregateResponse;

@Repository
public class AggregateRepositoryCustomImpl implements AggregateRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AggregateResponse> getAggregate(String inputYear, String inputDepartmentType) {
        String sql = """
            WITH
            dept_cost AS (
            	SELECT
            		project_owner_department_id,
                    owner_department_name,
                    project_owner_parent_department_name,
                    project_owner_grandparent_department_name,
            		SUM(monthly_wage)       AS monthly_wage,
            		SUM(sgae_amount)        AS sgae_amount,
            		SUM(ovhe_amount)        AS ovhe_amount,
            		SUM(total_cost)         AS total_cost,
            		COUNT(DISTINCT CASE WHEN personnel_type = '정직원' THEN personnel_id END) AS fulltime_count,
            		COUNT(DISTINCT CASE WHEN personnel_type = '외주' THEN personnel_id END) AS outsource_count,
            		COUNT(DISTINCT CASE WHEN personnel_type = '프리랜서' THEN personnel_id END) AS freelancer_count,
            		SUM(CASE WHEN personnel_type = '정직원' THEN monthly_wage ELSE 0 END) AS fulltime_cost,
            		SUM(CASE WHEN personnel_type = '외주' THEN monthly_wage ELSE 0 END) AS outsource_cost,
            		SUM(CASE WHEN personnel_type = '프리랜서' THEN monthly_wage ELSE 0 END) AS freelancer_cost,
            		SUM(CASE WHEN project_type = 'SI' THEN monthly_wage ELSE 0 END) AS si_cost,
            		SUM(CASE WHEN project_type = 'SM' THEN monthly_wage ELSE 0 END) AS sm_cost
            	FROM tb_monthly_employee_cost_aggregate
            	WHERE YEAR(project_start_date) = :inputYear OR YEAR(project_end_date) = :inputYear
            	GROUP BY project_owner_department_id
            ),
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
            dept_sales AS (
            	SELECT
            		project_owner_department_id,
            		team_sales_goal_amount_by_year
            	FROM tb_monthly_employee_cost_aggregate
            	WHERE YEAR(project_start_date) = :inputYear OR YEAR(project_end_date) = :inputYear
            	GROUP BY project_owner_department_id
            ),
            agg_department AS (
            	SELECT
            		C.project_owner_department_id,
            		d.total_contract_amount,
            		s.team_sales_goal_amount_by_year,
            		C.monthly_wage,
            		C.sgae_amount,
            		C.ovhe_amount,
            		C.total_cost,
            		C.fulltime_count,
            		C.outsource_count,
            		C.freelancer_count,
            		C.fulltime_cost,
            		C.outsource_cost,
            		C.freelancer_cost,
            		C.si_cost,
            		C.sm_cost,
                    C.owner_department_name,
                    C.project_owner_parent_department_name,
                    C.project_owner_grandparent_department_name
            	FROM dept_cost C
            	LEFT JOIN dept_contract d ON C.project_owner_department_id = d.project_owner_department_id
            	LEFT JOIN dept_sales s    ON C.project_owner_department_id = s.project_owner_department_id
            ),
            monthly_breakdown AS (
            	WITH months AS (
            		SELECT '01' AS mon UNION ALL SELECT '02' UNION ALL SELECT '03' UNION ALL
            		SELECT '04' UNION ALL SELECT '05' UNION ALL SELECT '06' UNION ALL
            		SELECT '07' UNION ALL SELECT '08' UNION ALL SELECT '09' UNION ALL
            		SELECT '10' UNION ALL SELECT '11' UNION ALL SELECT '12'
            	),
            	project_base AS (
            		SELECT DISTINCT
            			project_id,
            			project_owner_department_id,
            			CAST(project_start_date AS DATE) AS start_date,
            			CAST(project_end_date AS DATE)   AS end_date,
            			project_contract_amount,
            			DATEDIFF(CAST(project_end_date AS DATE), CAST(project_start_date AS DATE)) + 1 AS total_project_days,
            			CASE\s
            				WHEN project_start_date < CAST(CONCAT(:inputYear, '-01-01') AS DATE)\s
            				THEN CAST(CONCAT(:inputYear, '-01-01') AS DATE)
            				ELSE CAST(project_start_date AS DATE)
            			END AS effective_start,
            			CASE\s
            				WHEN project_end_date > CAST(CONCAT(:inputYear, '-12-31') AS DATE)\s
            				THEN CAST(CONCAT(:inputYear, '-12-31') AS DATE)
            				ELSE CAST(project_end_date AS DATE)
            			END AS effective_end
            		FROM tb_monthly_employee_cost_aggregate
            		WHERE project_end_date >= CAST(CONCAT(:inputYear, '-01-01') AS DATE)
            		AND project_start_date <= CAST(CONCAT(:inputYear, '-12-31') AS DATE)
            	),
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
            	SELECT\s
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
            monthly_pivot AS (
            	SELECT\s
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
            ),
            team_result AS (
            	SELECT
            		1 AS level_order,\s
            		a.project_owner_department_id AS 부서범위,
            		a.project_owner_department_id AS 부서아이디,
            		a.owner_department_name AS 부서이름,
            		a.total_contract_amount AS 매출합계,
            		a.team_sales_goal_amount_by_year AS 매출목표,
            		round(a.total_contract_amount / a.team_sales_goal_amount_by_year * 100, 2) AS 달성률,
            		a.monthly_wage       AS 인건비,
            		a.sgae_amount       AS 판관비,
            		a.ovhe_amount       AS 제경비,
            		(a.total_contract_amount - a.total_cost) AS 영업이익,
            		round((a.total_contract_amount - a.total_cost) / a.total_contract_amount * 100, 2) AS 영업이익률,
            		a.fulltime_count    AS 정직원,
            		a.outsource_count   AS 외주,
            		a.freelancer_count  AS 프리랜서,
            		a.fulltime_cost     AS 정직원인건비,
            		a.outsource_cost    AS 외주인건비,
            		a.freelancer_cost   AS 프리랜서인건비,
            		a.si_cost AS si,
            		a.sm_cost AS sm,
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
            	ORDER BY a.project_owner_department_id
            ),
            parent_result AS (
            	SELECT
            		2 AS level_order,\s
            		MAX(a.project_owner_department_id) AS 부서범위,
            		MAX(a.project_owner_department_id) AS 부서아이디,
            		a.project_owner_parent_department_name AS 부서이름,
            		SUM(a.total_contract_amount) AS 매출합계,
            		SUM(a.team_sales_goal_amount_by_year) AS 매출목표,
            		round(SUM(a.total_contract_amount) / SUM(a.team_sales_goal_amount_by_year) * 100, 2) AS 달성률,
            		SUM(a.monthly_wage)       AS 인건비,
            		SUM(a.sgae_amount)       AS 판관비,
            		SUM(a.ovhe_amount)       AS 제경비,
            		SUM(a.total_contract_amount) - SUM(a.total_cost) AS 영업이익,
            		round((SUM(a.total_contract_amount) - SUM(a.total_cost)) / SUM(a.total_contract_amount * 100), 2) AS 영업이익률,
            		SUM(a.fulltime_count)    AS 정직원,
            		SUM(a.outsource_count)   AS 외주,
            		SUM(a.freelancer_count)  AS 프리랜서,
            		SUM(a.fulltime_cost)     AS 정직원인건비,
            		SUM(a.outsource_cost)    AS 외주인건비,
            		SUM(a.freelancer_cost)   AS 프리랜서인건비,
            		SUM(a.si_cost) AS si,
            		SUM(a.sm_cost) AS sm,
            		SUM(p.sales_01) AS sales_01,
            		SUM(p.sales_02) AS sales_02,
            		SUM(p.sales_03) AS sales_03,
            		SUM(p.sales_04) AS sales_04,
            		SUM(p.sales_05) AS sales_05,
            		SUM(p.sales_06) AS sales_06,
            		SUM(p.sales_07) AS sales_07,
            		SUM(p.sales_08) AS sales_08,
            		SUM(p.sales_09) AS sales_09,
            		SUM(p.sales_10) AS sales_10,
            		SUM(p.sales_11) AS sales_11,
            		SUM(p.sales_12) AS sales_12
            	FROM agg_department a
            	LEFT JOIN monthly_pivot p
            		ON a.project_owner_department_id = p.project_owner_department_id
            	GROUP BY a.project_owner_parent_department_name
            	ORDER BY a.project_owner_department_id
            ),
            grandparent_result AS (
            	SELECT
            		3 AS level_order,\s
            		MAX(a.project_owner_department_id) AS 부서범위,
            		MAX(a.project_owner_department_id) AS 부서아이디,
            		a.project_owner_grandparent_department_name AS 부서이름,
            		SUM(a.total_contract_amount) AS 매출합계,
            		SUM(a.team_sales_goal_amount_by_year) AS 매출목표,
            		round(SUM(a.total_contract_amount) / SUM(a.team_sales_goal_amount_by_year) * 100, 2) AS 달성률,
            		SUM(a.monthly_wage)       AS 인건비,
            		SUM(a.sgae_amount)       AS 판관비,
            		SUM(a.ovhe_amount)       AS 제경비,
            		SUM((a.total_contract_amount - a.total_cost)) AS 영업이익,
            		round((SUM(a.total_contract_amount) - SUM(a.total_cost)) / SUM(a.total_contract_amount * 100), 2) AS 영업이익률,
            		SUM(a.fulltime_count)    AS 정직원,
            		SUM(a.outsource_count)   AS 외주,
            		SUM(a.freelancer_count)  AS 프리랜서,
            		SUM(a.fulltime_cost)     AS 정직원인건비,
            		SUM(a.outsource_cost)    AS 외주인건비,
            		SUM(a.freelancer_cost)   AS 프리랜서인건비,
            		SUM(a.si_cost) AS si,
            		SUM(a.sm_cost) AS sm,
            		SUM(p.sales_01) AS sales_01,
            		SUM(p.sales_02) AS sales_02,
            		SUM(p.sales_03) AS sales_03,
            		SUM(p.sales_04) AS sales_04,
            		SUM(p.sales_05) AS sales_05,
            		SUM(p.sales_06) AS sales_06,
            		SUM(p.sales_07) AS sales_07,
            		SUM(p.sales_08) AS sales_08,
            		SUM(p.sales_09) AS sales_09,
            		SUM(p.sales_10) AS sales_10,
            		SUM(p.sales_11) AS sales_11,
            		SUM(p.sales_12) AS sales_12
            	FROM agg_department a
            	LEFT JOIN monthly_pivot p
            		ON a.project_owner_department_id = p.project_owner_department_id
            	GROUP BY a.project_owner_grandparent_department_name
            	ORDER BY a.project_owner_department_id
            )
            
            SELECT
              부서범위,
              부서아이디,
              부서이름,
              매출합계,
              매출목표,
              달성률,
              인건비,
              판관비,
              제경비,
              영업이익,
              영업이익률,
              정직원,
              외주,
              프리랜서,
              정직원인건비,
              외주인건비,
              프리랜서인건비,
              si,
              sm,
              sales_01,
              sales_02,
              sales_03,
              sales_04,
              sales_05,
              sales_06,
              sales_07,
              sales_08,
              sales_09,
              sales_10,
              sales_11,
              sales_12
            FROM (
                SELECT *
                FROM team_result
                WHERE FIND_IN_SET('팀', :inputDepartmentType) > 0
            
                UNION ALL
            
                SELECT *
                FROM parent_result
                WHERE FIND_IN_SET('담당', :inputDepartmentType) > 0
                UNION ALL
            
                SELECT *
                FROM grandparent_result
                WHERE FIND_IN_SET('본부', :inputDepartmentType) > 0
            ) AS combined_result
            ORDER BY 부서아이디, level_order
            """;

        Query query = entityManager.createNativeQuery(sql, "AggregateResponseMapping");
        query.setParameter("inputYear", inputYear);
        query.setParameter("inputDepartmentType", inputDepartmentType);
        return query.getResultList();
    }

}
