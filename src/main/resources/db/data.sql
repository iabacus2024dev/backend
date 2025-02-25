INSERT INTO TB_CLASSIFICATION (
    IS_ACTIVATED, CLASSIFICATION_ID, CLASSIFICATION_PARENT_ID, CREATED_DATE_TIME,
    INACTIVATED_DATE_TIME, MODIFIED_DATE_TIME, CLASSIFICATION_NAME, CREATED_BY,
    MODIFIED_BY, CLASSIFICATION_CODE
) VALUES
    -- RANK (직급)
    (TRUE, 1, 2, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', '사원', 'admin', 'admin', 'R'),
    (TRUE, 2, 3, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', '선임', 'admin', 'admin', 'R'),
    (TRUE, 3, 4, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', '책임', 'admin', 'admin', 'R'),
    (TRUE, 4, 5, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', '수석', 'admin', 'admin', 'R'),
    (TRUE, 5, 6, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', '팀장', 'admin', 'admin', 'R'),
    (TRUE, 6, 7, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', '이사', 'admin', 'admin', 'R'),
    (TRUE, 7, 8, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', '부사장', 'admin', 'admin', 'R'),
    (TRUE, 8, 0, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', '사장', 'admin', 'admin', 'R'),

    -- GRADE (등급)
    (TRUE, 9, 0, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', '초급', 'admin', 'admin', 'G'),
    (TRUE, 10, 0, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', '중급', 'admin', 'admin', 'G'),
    (TRUE, 11, 0, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', '고급', 'admin', 'admin', 'G'),
    (TRUE, 12, 0, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', '특급', 'admin', 'admin', 'G'),

    -- TYPE (고용 형태)
    (TRUE, 13, 0, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', '정직원', 'admin', 'admin', 'T'),
    (TRUE, 14, 0, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', '프리랜서', 'admin', 'admin', 'T'),
    (TRUE, 15, 0, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', '외주', 'admin', 'admin', 'T'),
    (TRUE, 16, 0, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', '반프리', 'admin', 'admin', 'T');


INSERT INTO TB_TEAM (
    IS_ACTIVATED, CREATED_DATE_TIME, INACTIVATED_DATE_TIME, MODIFIED_DATE_TIME,
    TEAM_ID, CREATED_BY, MODIFIED_BY, TEAM_HEADQUARTERS, TEAM_MANAGE_PART, TEAM_NAME
) VALUES
    (TRUE, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', 1, 'admin', 'admin', '미래기술연구원', '플랫폼담당', '플랫폼연구개발팀'),
    (TRUE, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', 2, 'admin', 'admin', '미래사업본부', '전략사업담당', '컨버전스사업팀'),
    (TRUE, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', 3, 'admin', 'admin', '미래사업본부', '전략사업담당', '핀테크사업팀'),
    (TRUE, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', 4, 'admin', 'admin', '미래사업본부', '전략사업담당', 'IoT융합사업팀'),
    (TRUE, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', 5, 'admin', 'admin', '미래사업본부', '전략사업담당', '융합서비스사업팀'),
    (TRUE, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', 6, 'admin', 'admin', '미래사업본부', 'AI/Data사업담당', 'Data플랫폼사업팀'),
    (TRUE, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', 7, 'admin', 'admin', '미래사업본부', 'AI/Data사업담당', '지능플랫폼사업팀'),
    (TRUE, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', 8, 'admin', 'admin', '미래사업본부', 'AI/Data사업담당', 'Data서비스운영팀'),
    (TRUE, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', 9, 'admin', 'admin', '통신사업본부', '통신이행담당', '고객정보팀'),
    (TRUE, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', 10, 'admin', 'admin', '통신사업본부', '통신이행담당', '가입정보팀'),
    (TRUE, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', 11, 'admin', 'admin', '통신사업본부', '통신이행담당', '빌링시스템팀'),
    (TRUE, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', 12, 'admin', 'admin', '통신사업본부', '통신이행담당', '영업정보팀'),
    (TRUE, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', 13, 'admin', 'admin', '통신사업본부', '통신이행담당', '기반기술팀'),
    (TRUE, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', 14, 'admin', 'admin', '통신사업본부', '경영빌링담당', '경영플랫폼팀'),
    (TRUE, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', 15, 'admin', 'admin', '통신사업본부', '경영빌링담당', 'CRM팀'),
    (TRUE, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', 16, 'admin', 'admin', '통신사업본부', '경영빌링담당', '경영정보팀'),
    (TRUE, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', 17, 'admin', 'admin', '통신사업본부', '경영빌링담당', 'NMS사업팀'),
    (TRUE, '2025-02-20 16:19:37.665402', NULL, '2025-02-20 16:19:37.665402', 18, 'admin', 'admin', '통신사업본부', '경영빌링담당', '융합데이터분석팀');


INSERT INTO TB_PROJECT (
    PROJECT_ID, TEAM_ID, PROJECT_CODE, PROJECT_NAME,
    PROJECT_START_DATE, PROJECT_END_DATE, PROJECT_CONTRACT_DATE,
    PROJECT_ACTUAL_START_DATE, PROJECT_ACTUAL_END_DATE,
    PROJECT_EXPECTED_AMOUNT, PROJECT_ACTUAL_AMOUNT,
    PROJECT_MAIN_COMPANY, PROJECT_MAIN_COMPANY_REP, PROJECT_MAIN_COMPANY_REP_PHONE,
    PROJECT_ORDERING_COMPANY, PROJECT_ORDERING_COMPANY_REP, PROJECT_ORDERING_COMPANY_REP_PHONE,
    PROJECT_STATUS, PROJECT_TYPE,
    IS_ACTIVATED, CREATED_DATE_TIME, MODIFIED_DATE_TIME,
    INACTIVATED_DATE_TIME, CREATED_BY, MODIFIED_BY
) VALUES
(
    '123e4567-e89b-12d3-a456-426614174000', -- 프로젝트 ID (UUID)
     1, -- 팀 ID (UUID)
    'PRJ-20240225-001', '스마트 계약 시스템 개발',
    '2024-02-01', '2025-02-01', '2024-01-25',
    '2024-02-05', '2025-01-28',
    500000000, 450000000,
    '메인 IT 회사', '김인철', '010-1234-5678',
    '윙윙 IT 회사', '박윙윙', '010-8765-4321',
    'IN_PROGRESS', 'SI',
    TRUE, '2024-02-01 10:00:00', '2024-02-10 12:00:00',
    NULL, 'admin', NULL
);


INSERT INTO TB_CONTRACT (
    CONTRACT_ID, PROJECT_ID, CONTRACT_CODE,
    CONTRACT_START_DATE, CONTRACT_END_DATE,
    CONTRACT_ACTUAL_START_DATE, CONTRACT_ACTUAL_END_DATE,
    CONTRACT_TYPE, CONTRACT_STATUS,
    IS_ACTIVATED, CREATED_DATE_TIME, MODIFIED_DATE_TIME,
    INACTIVATED_DATE_TIME, CREATED_BY, MODIFIED_BY
) VALUES
(
    '550e8400-e29b-41d4-a716-446655440000', -- 계약 ID (UUID)
    '123e4567-e89b-12d3-a456-426614174000', -- 프로젝트 ID (UUID)
    'PRJ-20240225-001-CNT-001',
    '2024-02-01', '2025-02-01',
    '2024-02-05', '2025-01-28',
    'INITIAL', 'IN_PROGRESS',
    TRUE, '2024-02-01 10:00:00', '2024-02-10 12:00:00',
    NULL, 'admin', NULL
),
(
    '550e8400-e29b-41d4-a716-446655440001', -- 계약 ID (UUID)
    '123e4567-e89b-12d3-a456-426614174000', -- 프로젝트 ID (UUID)
    'PRJ-20240225-001-CNT-002',
    '2024-03-01', '2025-04-01',
    NULL, NULL,
    'CHANGED', 'RESERVED',
    TRUE, '2024-02-01 10:00:00', '2024-02-10 12:00:00',
    NULL, 'admin', NULL
);
