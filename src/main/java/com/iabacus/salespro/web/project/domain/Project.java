package com.iabacus.salespro.web.project.domain;

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
import com.iabacus.salespro.web.project.request.ProjectUpdateRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_PROJECT")
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_ID")
    private Long id;

    @Column(name = "PROJECT_CODE", unique = true)
    private String code;

    @Column(name = "PROJECT_NAME")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROJECT_TYPE")
    private ProjectType type;

    @AttributeOverride(name = "amount", column = @Column(name = "EXPECTED_AMOUNT", precision = 10, scale = 0))
    private Money expectedAmount;

    @AttributeOverride(name = "amount", column = @Column(name = "CONTRACT_AMOUNT", precision = 10, scale = 0))
    private Money contractAmount;

    @Column(name = "PROJECT_CONTRACT_DATE")
    private LocalDate contractDate;

    @Column(name = "OWNER_TEAM_ID")
    private Long ownerTeamId;

    @Column(name = "PROJECT_START_DATE")
    private LocalDate startDate;

    @Column(name = "PROJECT_END_DATE")
    private LocalDate endDate;

    @Column(name = "CLIENT_COMPANY")
    private String clientCompany;

    @Column(name = "CLIENT_COMPANY_REP")
    private String clientCompanyRep;

    @AttributeOverride(name = "number", column = @Column(name = "CLIENT_COMPANY_REP_PHONE"))
    private Phone clientCompanyRepPhone;

    @Column(name = "MAIN_COMPANY")
    private String mainCompany;

    @Column(name = "MAIN_COMPANY_REP")
    private String mainCompanyRep;

    @AttributeOverride(name = "number", column = @Column(name = "MAIN_COMPANY_REP_PHONE"))
    private Phone mainCompanyRepPhone;

    @Column(name = "PM_NAME")
    private String pmName;

    @AttributeOverride(name = "number", column = @Column(name = "PM_PHONE"))
    private Phone pmPhone;

    @Builder
    public Project(Long ownerTeamId, String code, String name, ProjectType type, Money expectedAmount, Money contractAmount,
                   LocalDate contractDate, LocalDate startDate, LocalDate endDate, String clientCompany, String clientCompanyRep,
                   Phone clientCompanyRepPhone, String mainCompany, String mainCompanyRep, Phone mainCompanyRepPhone,
                   String pmName, Phone pmPhone) {
        this.ownerTeamId = ownerTeamId;
        this.code = code;
        this.name = name;
        this.type = type;
        this.expectedAmount = expectedAmount;
        this.contractAmount = contractAmount;
        this.contractDate = contractDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.clientCompany = clientCompany;
        this.clientCompanyRep = clientCompanyRep;
        this.clientCompanyRepPhone = clientCompanyRepPhone;
        this.mainCompany = mainCompany;
        this.mainCompanyRep = mainCompanyRep;
        this.mainCompanyRepPhone = mainCompanyRepPhone;
        this.pmName = pmName;
        this.pmPhone = pmPhone;
    }

    public void update(ProjectUpdateRequest request) {
        this.name = request.getName();
        this.code = request.getCode();
        this.type = request.getType();
        this.ownerTeamId = request.getOwnerTeamId();
        this.expectedAmount = Money.wons(request.getExpectedAmount());
        this.contractAmount = Money.wons(request.getContractAmount());
        this.contractDate = request.getContractDate();
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
        this.clientCompany = request.getClientCompany();
        this.clientCompanyRep = request.getClientCompanyRep();
        this.clientCompanyRepPhone = Phone.of(request.getClientCompanyRepPhone());
        this.mainCompany = request.getMainCompany();
        this.mainCompanyRep = request.getMainCompanyRep();
        this.mainCompanyRepPhone = Phone.of(request.getMainCompanyRepPhone());
        this.pmName = request.getPmName();
        this.pmPhone = Phone.of(request.getPmPhone());
    }

    public ProjectStatus getStatus() {
        return ProjectStatus.fromDate(LocalDate.now(), startDate, endDate);
    }

}
