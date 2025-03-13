package com.iabacus.salespro.web.employee.domain;

import java.time.LocalDate;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.iabacus.salespro.web.common.BaseEntity;
import com.iabacus.salespro.web.common.Money;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.employee.request.EmployeeUpdateRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_EMPLOYEE")
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMPLOYEE_ID")
    private Long id;

    @Column(name = "DEPARTMENT_ID")
    private Long departmentId;

    @Column(name = "PARTNERS_ID")
    private Long partnersId;

    @Column(name = "EMPLOYEE_NAME")
    private String name;

    @Column(name = "EMPLOYEE_EMAIL", unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "EMPLOYEE_RANK")
    private EmployeeRank rank;

    @Enumerated(EnumType.STRING)
    @Column(name = "EMPLOYEE_GRADE")
    private EmployeeGrade grade;

    @Enumerated(EnumType.STRING)
    @Column(name = "EMPLOYEE_TYPE")
    private EmployeeType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "EMPLOYEE_STATUS")
    private EmployeeStatus status;

    @AttributeOverride(name = "number", column = @Column(name = "EMPLOYEE_PHONE"))
    private Phone phone;

    @Column(name = "EMPLOYEE_BIRTH_DATE")
    private LocalDate birthDate;

    @Column(name = "EMPLOYEE_JOIN_DATE")
    private LocalDate joinDate;

    @Column(name = "EMPLOYEE_LEAVE_DATE")
    private LocalDate leaveDate;

    @AttributeOverride(name = "amount", column = @Column(name = "EMPLOYEE_MONEY", precision = 10, scale = 2))
    private Money salary;

    @Column(name = "EMPLOYEE_COMMENT")
    private String comment;

    @Builder
    public Employee(Long departmentId, Long partnersId, String name, String email, EmployeeRank rank, EmployeeGrade grade,
                    EmployeeType type, Phone phone, LocalDate birthDate, LocalDate joinDate, Money salary, String comment) {
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
        this.salary = salary;
        this.comment = comment;
        this.status = EmployeeStatus.재직;
    }

    public void leave(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
        this.status = EmployeeStatus.퇴사;
    }

    public Money getMonthlyPay() {
        if (salary != null) {
            return salary.divide(12);
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
