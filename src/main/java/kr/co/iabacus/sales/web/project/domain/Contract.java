package kr.co.iabacus.sales.web.project.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import kr.co.iabacus.sales.core.common.entity.BaseEntity;
import kr.co.iabacus.sales.web.project.dto.ContractUpdateRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_CONTRACT")
public class Contract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "CONTRACT_ID")
    private UUID id;

    @JoinColumn(name = "PROJECT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @Column(name = "CONTRACT_CODE")
    private String code;

    @Column(name = "CONTRACT_START_DATE")
    private LocalDate startDate;

    @Column(name = "CONTRACT_END_DATE")
    private LocalDate endDate;

    @Column(name = "CONTRACT_ACTUAL_START_DATE")
    private LocalDate actualStartDate;

    @Column(name = "CONTRACT_ACTUAL_END_DATE")
    private LocalDate actualEndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "CONTRACT_TYPE")
    private ContractType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "CONTRACT_STATUS")
    private ContractStatus status;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractMember> contractMembers = new ArrayList<>();

    @Builder
    private Contract(Project project, String code, LocalDate startDate, LocalDate endDate, LocalDate actualStartDate,
                     LocalDate actualEndDate, ContractType type, ContractStatus status, List<ContractMember> contractMembers) {
        this.project = project;
        this.code = code;
        this.startDate = startDate;
        this.endDate = endDate;
        this.actualStartDate = actualStartDate;
        this.actualEndDate = actualEndDate;
        this.type = type;
        this.status = status;
        if (contractMembers != null) {
            contractMembers.forEach(this::addContractMember);
        }
    }

    public void addContractMember(ContractMember contractMember) {
        this.contractMembers.add(contractMember);
        contractMember.changeContract(this);
    }

    void changeProject(Project project) {
        this.project = project;
    }

    public void updateContract(ContractUpdateRequest request) {
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
        this.status = request.getStatus();
        this.actualStartDate = request.getActualStartDate();
        this.actualEndDate = request.getActualEndDate();
    }

}
