package com.iabacus.salespro.core.initdata;

import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.department.domain.DepartmentType;
import com.iabacus.salespro.web.department.repository.DepartmentRepository;
import com.iabacus.salespro.web.employee.domain.*;
import com.iabacus.salespro.web.employee.repository.EmployeeRepository;
import com.iabacus.salespro.web.member.domain.Member;
import com.iabacus.salespro.web.member.repository.MemberRepository;
import com.iabacus.salespro.web.role.domain.*;
import com.iabacus.salespro.web.role.repository.ActionRepository;
import com.iabacus.salespro.web.role.repository.RangeRepository;
import com.iabacus.salespro.web.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.iabacus.salespro.web.role.domain.Authority.createAuthority;
import static com.iabacus.salespro.web.role.domain.Page.*;

@Profile("local")
@RequiredArgsConstructor
@Transactional
@Component
public class InitEmployeeMemberDepartmentRoleData {

    private final EmployeeRepository employeeRepository;
    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final RangeRepository rangeRepository;
    private final ActionRepository actionRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        // 팀
        Department department1 = createDepartment("애버커스", DepartmentType.최상위, null);

        Department department2 = createDepartment("통신사업본부", DepartmentType.본부, department1);
        Department department3 = createDepartment("미래사업본부", DepartmentType.본부, department1);
        Department department4 = createDepartment("연구개발본부", DepartmentType.본부, department1);

        Department department5 = createDepartment("통신이행담당", DepartmentType.담당, department2);
        Department department6 = createDepartment("경영빌링담당", DepartmentType.담당, department2);

        Department department7 = createDepartment("고객정보팀", DepartmentType.팀, department5);
        Department department8 = createDepartment("가입정보팀", DepartmentType.팀, department5);
        Department department9 = createDepartment("빌링시스템팀", DepartmentType.팀, department5);
        Department department10 = createDepartment("영업정보팀", DepartmentType.팀, department5);
        Department department11 = createDepartment("기반기술팀", DepartmentType.팀, department5);

        Department department12 = createDepartment("경영정보팀", DepartmentType.팀, department6);
        Department department13 = createDepartment("융합데이터분석팀", DepartmentType.팀, department6);
        departmentRepository.saveAll(List.of(department1, department2, department3, department4, department5, department6, department7, department8, department9, department10, department11, department12, department13));

        // 구성원
        Employee employee1 = createEmployee("박상철", "tkdcjf38@iabacus.co.kr", department7.getId());
        Employee employee2 = createEmployee("임세인", "vicent77@iabacus.co.kr", department7.getId());
        Employee employee3 = createEmployee("김진규", "jingyu10@iabacus.co.kr", department8.getId());
        Employee employee4 = createEmployee("유하진", "qwertyv@iabacus.co.kr", department8.getId());
        Employee employee5 = createEmployee("김봉재", "55330m@iabacus.co.kr", department12.getId());
        Employee employee6 = createEmployee("이지수", "dlwltn6604@iabacus.co.kr", department13.getId());
        Employee employee7 = createEmployee("이동욱", "ledu202@iabacus.co.kr", department13.getId());
        Employee employee8 = createEmployee("이사", "abc@iabacus.co.kr", department2.getId());
        employeeRepository.saveAll(List.of(employee1, employee2, employee3, employee4, employee5, employee6, employee7, employee8));

        // 권한 범위
        Range range1 = Range.createRange("전체");
        Range range2 = Range.createRange("팀");
        Range range3 = Range.createRange("프로젝트");
        Range range4 = Range.createRange("본인");
        rangeRepository.saveAll(List.of(range1, range2, range3, range4));

        // 권한 액션(조회, 편집)
        Action action1 = Action.createAction("조회");
        Action action2 = Action.createAction("편집");
        actionRepository.saveAll(List.of(action1, action2));

        // 권한
        Authority authority1 = createAuthority("프로젝트 조회", 프로젝트, AuthorityAction.createAuthorityAction(action1), createAuthorityRange(range1));
        Authority authority2 = createAuthority("프로젝트 편집", 프로젝트, AuthorityAction.createAuthorityAction(action2),  createAuthorityRange(range2));
        Authority authority3 = createAuthority("구성원 조회", 구성원, AuthorityAction.createAuthorityAction(action1),  createAuthorityRange(range3));
        Authority authority4 = createAuthority("구성원 편집", 구성원, AuthorityAction.createAuthorityAction(action2),  createAuthorityRange(range4));
        Authority authority5 = createAuthority("협력사 조회", 협력사, AuthorityAction.createAuthorityAction(action1), createAuthorityRange(range1));
        Authority authority7 = createAuthority("협력사 편집", 협력사, AuthorityAction.createAuthorityAction(action2), createAuthorityRange(range2));
        Authority authority8 = createAuthority("매출 조회", 매출, AuthorityAction.createAuthorityAction(action1), createAuthorityRange(range3));
        Authority authority9 = createAuthority("매출 편집", 매출, AuthorityAction.createAuthorityAction(action2), createAuthorityRange(range4));
        Authority authority10 = createAuthority("권한 조회", 권한, AuthorityAction.createAuthorityAction(action1), createAuthorityRange(range1));
        Authority authority11 = createAuthority("권한 편집", 권한, AuthorityAction.createAuthorityAction(action2), createAuthorityRange(range2));
        Authority authority12 = createAuthority("휴가 조회", 휴가, AuthorityAction.createAuthorityAction(action1), createAuthorityRange(range3));
        Authority authority13 = createAuthority("휴가 편집", 휴가, AuthorityAction.createAuthorityAction(action2), createAuthorityRange(range4));

        // 역할
        Role admin = createRole("관리자");
        RoleAuthority roleAuthority1 = createRoleAuthority(authority1);
        RoleAuthority roleAuthority2 = createRoleAuthority(authority2);
        RoleAuthority roleAuthority3 = createRoleAuthority(authority3);
        RoleAuthority roleAuthority4 = createRoleAuthority(authority4);
        RoleAuthority roleAuthority5 = createRoleAuthority(authority5);
        RoleAuthority roleAuthority7 = createRoleAuthority(authority7);
        RoleAuthority roleAuthority8 = createRoleAuthority(authority8);
        RoleAuthority roleAuthority9 = createRoleAuthority(authority9);
        RoleAuthority roleAuthority10 = createRoleAuthority(authority10);
        RoleAuthority roleAuthority11 = createRoleAuthority(authority11);
        RoleAuthority roleAuthority12 = createRoleAuthority(authority12);
        RoleAuthority roleAuthority13 = createRoleAuthority(authority13);

        admin.addRoleAuthorities(List.of(roleAuthority1, roleAuthority2, roleAuthority3, roleAuthority4, roleAuthority5, roleAuthority7, roleAuthority8, roleAuthority8, roleAuthority9, roleAuthority10, roleAuthority11, roleAuthority12, roleAuthority13));
        roleRepository.save(admin);

        RoleAuthority roleAuthority14 = createRoleAuthority(authority12);
        RoleAuthority roleAuthority15 = createRoleAuthority(authority13);
        Role basicRole = createRole("일반 사용자");
        basicRole.addRoleAuthorities(List.of(roleAuthority14, roleAuthority15));
        roleRepository.save(basicRole);

        // 회원 정보
        Member member1 = createMember(employee1, admin);
        Member member2 = createMember(employee2, admin);
        Member member3 = createMember(employee3, admin);
        Member member4 = createMember(employee4, admin);
        Member member5 = createMember(employee5, admin);
        Member member6 = createMember(employee6, admin);
        Member member7 = createMember(employee7, admin);
        memberRepository.saveAll(List.of(member1, member2, member3, member4, member5, member6, member7));
    }

    private AuthorityRange createAuthorityRange(Range range) {
        return AuthorityRange.createAuthorityRange(range);
    }

    private RoleAuthority createRoleAuthority(Authority authority) {
        return RoleAuthority.builder()
            .authority(authority)
            .build();
    }

    private Member createMember(Employee employee1, Role role) {
        return Member.builder()
            .employeeId(employee1.getId())
            .roleId(role.getId())
            .username(employee1.getEmail())
            .password(passwordEncoder.encode("Password!"))
            .build();
    }

    private Role createRole(String name) {
        return Role.builder()
            .name(name)
            .isDefaultRole(true)
            .build();
    }

    private Department createDepartment(String name, DepartmentType type, Department parent) {
        return Department.builder()
            .name(name)
            .type(type)
            .parent(parent)
            .build();
    }

    private Employee createEmployee(String name, String email, Long departmentId) {
        return Employee.builder()
            .name(name)
            .email(email)
            .departmentId(departmentId)
            .annualSalary(Money.wons(30_000_000))
            .phone(Phone.of("01012341234"))
            .birthDate(LocalDate.of(1998, 6, 8))
            .joinDate(LocalDate.of(2024, 11, 25))
            .rank(EmployeeRank.사원)
            .grade(EmployeeGrade.초급)
            .type(EmployeeType.정직원)
            .hrStatus(EmployeeStatus.재직)
            .comment("최근 입사한 신입사원")
            .build();
    }

}