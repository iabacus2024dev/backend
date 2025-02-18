package kr.co.iabacus.sales.web.member.domain;

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

import kr.co.iabacus.sales.core.common.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_CLASSIFICATION")
public class Classification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLASSIFICATION_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "CLASSIFICATION_CODE")
    private ClassificationCode code;

    @Column(name = "CLASSIFICATION_NAME")
    private String name;

    @Column(name = "CLASSIFICATION_PARENT_ID")
    private Long parent;

    @Builder
    private Classification(ClassificationCode code, String name, Long parent) {
        this.code = code;
        this.name = name;
        this.parent = parent;
    }

}
