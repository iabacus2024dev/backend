package kr.co.iabacus.sales.web.project.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import kr.co.iabacus.sales.core.common.entity.BaseEntity;
import kr.co.iabacus.sales.web.common.Money;
import kr.co.iabacus.sales.web.common.Phone;
import kr.co.iabacus.sales.web.common.converter.MoneyConverter;
import kr.co.iabacus.sales.web.common.converter.PhoneConverter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_PROJECT")
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "PROJECT_ID")
    private UUID id;

    @Column(name = "TEAM_ID")
    private Long teamId;

    @Column(name = "PROJECT_CODE")
    private String code;

    @Column(name = "PROJECT_NAME")
    private String name;

    @Column(name = "PROJECT_CONTRACT_DATE")
    private LocalDate contractDate;

    @Column(name = "PROJECT_START_DATE")
    private LocalDate startDate;

    @Column(name = "PROJECT_END_DATE")
    private LocalDate endDate;

    @Column(name = "PROJECT_ACTUAL_START_DATE")
    private LocalDate actualStartDate;

    @Column(name = "PROJECT_ACTUAL_END_DATE")
    private LocalDate actualEndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROJECT_TYPE")
    private ProjectType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROJECT_STATUS")
    private ProjectStatus status;

    @Column(name = "PROJECT_MAIN_COMPANY")
    private String mainCompany;

    @Column(name = "PROJECT_ORDERING_COMPANY")
    private String orderingCompany;

    @Convert(converter = MoneyConverter.class)
    @Column(name = "PROJECT_EXPECTED_AMOUNT", precision = 10, scale = 0)
    private Money expectedAmount;

    @Convert(converter = MoneyConverter.class)
    @Column(name = "PROJECT_ACTUAL_AMOUNT", precision = 10, scale = 0)
    private Money actualAmount;

    @Column(name = "PROJECT_MAIN_COMPANY_REP")
    private String mainCompanyRep;

    @Column(name = "PROJECT_ORDERING_COMPANY_REP")
    private String orderingCompanyRep;

    @Convert(converter = PhoneConverter.class)
    @Column(name = "PROJECT_MAIN_COMPANY_REP_PHONE")
    private Phone mainCompanyRepPhone;

    @Convert(converter = PhoneConverter.class)
    @Column(name = "PROJECT_ORDERING_COMPANY_REP_PHONE")
    private Phone orderingCompanyRepPhone;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts = new ArrayList<>();

    @Builder
    private Project(Long teamId, String code, String name, LocalDate contractDate, LocalDate startDate, LocalDate endDate,
                    LocalDate actualStartDate, LocalDate actualEndDate, ProjectType type, ProjectStatus status, String mainCompany,
                    String orderingCompany, Money expectedAmount, Money actualAmount, String mainCompanyRep, String orderingCompanyRep,
                    Phone mainCompanyRepPhone, Phone orderingCompanyRepPhone, List<Contract> contracts) {
        this.teamId = teamId;
        this.code = code;
        this.name = name;
        this.contractDate = contractDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.actualStartDate = actualStartDate;
        this.actualEndDate = actualEndDate;
        this.type = type;
        this.status = status;
        this.mainCompany = mainCompany;
        this.orderingCompany = orderingCompany;
        this.expectedAmount = expectedAmount;
        this.actualAmount = actualAmount;
        this.mainCompanyRep = mainCompanyRep;
        this.orderingCompanyRep = orderingCompanyRep;
        this.mainCompanyRepPhone = mainCompanyRepPhone;
        this.orderingCompanyRepPhone = orderingCompanyRepPhone;
        if (contracts != null) {
            contracts.forEach(this::addContract);
        }
    }

    public void addContract(Contract contract) {
        contracts.add(contract);
        contract.changeProject(this);
    }

}
