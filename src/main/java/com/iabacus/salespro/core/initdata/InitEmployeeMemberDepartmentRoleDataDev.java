package com.iabacus.salespro.core.initdata;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.department.domain.DepartmentType;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.domain.EmployeeGrade;
import com.iabacus.salespro.web.employee.domain.EmployeeRank;
import com.iabacus.salespro.web.employee.domain.EmployeeType;
import com.iabacus.salespro.web.member.domain.Member;
import com.iabacus.salespro.web.member.repository.MemberRepository;
import com.iabacus.salespro.web.role.domain.Authority;
import com.iabacus.salespro.web.role.domain.AuthorityRange;
import com.iabacus.salespro.web.role.domain.Role;
import com.iabacus.salespro.web.role.domain.RoleAuthority;
import com.iabacus.salespro.web.role.repository.RoleRepository;

@Profile("dev")
@RequiredArgsConstructor
@Component
public class InitEmployeeMemberDepartmentRoleDataDev {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        Authority authority1 = createAuthority("프로젝트 조회");
        Authority authority2 = createAuthority("프로젝트 편집");
        Authority authority3 = createAuthority("구성원 조회");
        Authority authority4 = createAuthority("구성원 편집");
        Authority authority5 = createAuthority("협력사 조회");
        Authority authority7 = createAuthority("협력사 편집");
        Authority authority8 = createAuthority("매출 조회");
        Authority authority9 = createAuthority("매출 편집");
        Authority authority10 = createAuthority("권한 조회");
        Authority authority11 = createAuthority("권한 편집");
        Authority authority12 = createAuthority("휴가 조회");
        Authority authority13 = createAuthority("휴가 편집");

        AuthorityRange authorityRange1 = createAuthorityRange("전체");
        AuthorityRange authorityRange2 = createAuthorityRange("팀");
        AuthorityRange authorityRange3 = createAuthorityRange("프로젝트");
        AuthorityRange authorityRange4 = createAuthorityRange("본인");

        Role admin = createRole("관리자");
        Role basicRole = createRole("일반관리자");
        RoleAuthority roleAuthority1 = createRoleAuthority(authority1, admin);
        RoleAuthority roleAuthority2 = createRoleAuthority(authority2, admin);
        RoleAuthority roleAuthority3 = createRoleAuthority(authority3, admin);
        RoleAuthority roleAuthority4 = createRoleAuthority(authority4, admin);
        RoleAuthority roleAuthority5 = createRoleAuthority(authority5, admin);
        RoleAuthority roleAuthority7 = createRoleAuthority(authority7, admin);
        RoleAuthority roleAuthority8 = createRoleAuthority(authority8, admin);
        RoleAuthority roleAuthority9 = createRoleAuthority(authority9, admin);
        RoleAuthority roleAuthority10 = createRoleAuthority(authority10, admin);
        RoleAuthority roleAuthority11 = createRoleAuthority(authority11, admin);
        RoleAuthority roleAuthority12 = createRoleAuthority(authority12, admin);
        RoleAuthority roleAuthority13 = createRoleAuthority(authority13, admin);

        admin.addRoleAuthorities(roleAuthority1);
        admin.addRoleAuthorities(roleAuthority2);
        admin.addRoleAuthorities(roleAuthority3);
        admin.addRoleAuthorities(roleAuthority4);
        admin.addRoleAuthorities(roleAuthority5);
        admin.addRoleAuthorities(roleAuthority7);
        admin.addRoleAuthorities(roleAuthority8);
        admin.addRoleAuthorities(roleAuthority9);
        admin.addRoleAuthorities(roleAuthority10);
        admin.addRoleAuthorities(roleAuthority11);
        admin.addRoleAuthorities(roleAuthority12);
        admin.addRoleAuthorities(roleAuthority13);
        roleRepository.save(admin);

        createRoleAuthority(authority1, basicRole);
        createRoleAuthority(authority3, basicRole);
        createRoleAuthority(authority5, basicRole);
        createRoleAuthority(authority12, basicRole);
        createRoleAuthority(authority13, basicRole);
        roleRepository.save(basicRole);
    }

    private AuthorityRange createAuthorityRange(String name) {
        return AuthorityRange.builder()
            .name(name)
            .build();
    }

    private static RoleAuthority createRoleAuthority(Authority authority, Role role) {
        return RoleAuthority.builder()
            .authority(authority)
            .role(role)
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

    private Authority createAuthority(String name) {
        return Authority.builder()
            .name(name)
            .authorityRanges(
                List.of(
                    createAuthorityRange("전체"),
                    createAuthorityRange("팀"),
                    createAuthorityRange("프로젝트"),
                    createAuthorityRange("본인")
                ))
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
            .comment("최근 입사한 신입사원")
            .build();
    }

}
