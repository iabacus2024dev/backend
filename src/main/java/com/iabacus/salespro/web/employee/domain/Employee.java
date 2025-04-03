package com.iabacus.salespro.web.employee.domain;

import com.iabacus.salespro.web.common.BaseEntity;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.employee.request.EmployeeUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_EMPLOYEE")
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMPLOYEE_ID")
    private Long id;

    @Column(name = "EMPLOYEE_NAME")
    private String name;

    @Column(name = "DEPARTMENT_ID")
    private Long departmentId;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @AttributeOverride(name = "number", column = @Column(name = "PHONE"))
    private Phone phone;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    @AttributeOverride(name = "amount", column = @Column(name = "ANNUAL_SALARY", precision = 10, scale = 2))
    private Money annualSalary;

    @Enumerated(EnumType.STRING)
    @Column(name = "HR_STATUS")
    private EmployeeStatus HrStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "RANK")
    private EmployeeRank rank;

    @Enumerated(EnumType.STRING)
    @Column(name = "GRADE")
    private EmployeeGrade grade;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private EmployeeType type;

    @Column(name = "PARTNERS_ID")
    private Long partnersId;

    @Column(name = "JOIN_DATE")
    private LocalDate joinDate;

    @Column(name = "LEAVE_DATE")
    private LocalDate leaveDate;

    @Column(name = "COMMENT")
    private String comment;

    @Builder
    public Employee(Long departmentId, Long partnersId, String name, String email, EmployeeRank rank, EmployeeGrade grade,
                    EmployeeType type, Phone phone, LocalDate birthDate, LocalDate joinDate, Money annualSalary, String comment,
                    EmployeeStatus hrStatus) {
        this.departmentId = departmentId;
        this.partnersId = partnersId;
        this.name = name;
        this.email = email;
        this.rank = rank;
        this.grade = grade;
        this.type = type;
        this.phone = phone;
        this.birthDate = birthDate;
        this.joinDate = joinDate;
        this.annualSalary = annualSalary;
        this.comment = comment;
        this.HrStatus = hrStatus;
    }

    public void leave(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
        this.HrStatus = EmployeeStatus.퇴사;
    }

    public Money getMonthlyPay() {
        if (annualSalary != null) {
            return annualSalary.divide(12);
        }
        return null;
    }

    public void update(EmployeeUpdateRequest request) {
        this.partnersId = request.getPartnersId();
        this.departmentId = request.getDepartmentId();
        this.name = request.getName();
        this.email = request.getEmail();
        this.rank = request.getRank();
        this.grade = request.getGrade();
        this.type = request.getType();
        this.phone = phone != null ? Phone.of(request.getPhone()) : null;
        this.birthDate = request.getBirthDate();
        this.joinDate = request.getJoinDate();
        this.comment = request.getComment();
    }

}
