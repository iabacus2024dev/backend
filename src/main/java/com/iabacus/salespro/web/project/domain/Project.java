package com.iabacus.salespro.web.project.domain;

import java.time.LocalDate;
import java.util.UUID;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "PROJECT_ID")
    private UUID id;

    @Column(name = "PROJECT_OWNER_TEAM_ID")
    private Long ownerTeamId;

    @Column(name = "PROJECT_CODE", unique = true)
    private String code;

    @Column(name = "PROJECT_NAME")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROJECT_TYPE")
    private ProjectType type;

    @AttributeOverride(name = "amount", column = @Column(name = "PROJECT_EXPECTED_AMOUNT", precision = 10, scale = 0))
    private Money expectedAmount;

    @AttributeOverride(name = "amount", column = @Column(name = "PROJECT_CONTRACT_AMOUNT", precision = 10, scale = 0))
    private Money contractAmount;

    @Column(name = "PROJECT_CONTRACT_DATE")
    private LocalDate contractDate;

    @Column(name = "PROJECT_START_DATE")
    private LocalDate startDate;

    @Column(name = "PROJECT_END_DATE")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROJECT_STATUS")
    private ProjectStatus status;

    @Column(name = "PROJECT_CLIENT_COMPANY")
    private String clientCompany;

    @Column(name = "PROJECT_CLIENT_COMPANY_REF")
    private String clientCompanyRef;

    @AttributeOverride(name = "number", column = @Column(name = "PROJECT_CLIENT_COMPANY_REF_PHONE"))
    private Phone clientCompanyRefPhone;

    @Column(name = "PROJECT_MAIN_COMPANY")
    private String mainCompany;

    @Column(name = "PROJECT_MAIN_COMPANY_REF")
    private String mainCompanyRef;

    @AttributeOverride(name = "number", column = @Column(name = "PROJECT_MAIN_COMPANY_PHONE"))
    private Phone mainCompanyRefPhone;

    @Column(name = "PROJECT_PM_NAME")
    private String pmName;

    @AttributeOverride(name = "number", column = @Column(name = "PROJECT_PM_PHONE"))
    private Phone pmPhone;

    @Builder
    public Project(Long ownerTeamId, String code, String name, ProjectType type, Money expectedAmount, Money contractAmount,
                   LocalDate contractDate, LocalDate startDate, LocalDate endDate, String clientCompany, String clientCompanyRef,
                   Phone clientCompanyRefPhone, String mainCompany, String mainCompanyRef, Phone mainCompanyRefPhone,
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
        this.clientCompanyRef = clientCompanyRef;
        this.clientCompanyRefPhone = clientCompanyRefPhone;
        this.mainCompany = mainCompany;
        this.mainCompanyRef = mainCompanyRef;
        this.mainCompanyRefPhone = mainCompanyRefPhone;
        this.pmName = pmName;
        this.pmPhone = pmPhone;
        this.status = ProjectStatus.fromDate(LocalDate.now(), this.startDate, this.endDate);
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
        this.clientCompanyRef = request.getClientCompanyRep();
        this.clientCompanyRefPhone = Phone.of(request.getClientCompanyRepPhone());
        this.mainCompany = request.getMainCompany();
        this.mainCompanyRef = request.getMainCompanyRep();
        this.mainCompanyRefPhone = Phone.of(request.getMainCompanyRepPhone());
        this.pmName = request.getPmName();
        this.pmPhone = Phone.of(request.getPmPhone());
        this.status = request.getStatus();
    }

}
