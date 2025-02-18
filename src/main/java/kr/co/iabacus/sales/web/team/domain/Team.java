package kr.co.iabacus.sales.web.team.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "TB_TEAM")
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    private Long id;

    @Column(name = "TEAM_NAME")
    private String name;

    @Column(name = "TEAM_MANAGE_PART")
    private String managePart;

    @Column(name = "TEAM_HEADQUARTERS")
    private String headquarters;

    @Builder
    private Team(String name, String managePart, String headquarters) {
        this.name = name;
        this.managePart = managePart;
        this.headquarters = headquarters;
    }

}
