package com.iabacus.salespro.web.role.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "TB_RANGE")
public class Range {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "RANGE_ID")
    private Long id;

    @Column(name = "RANGE_NAME", unique = true, nullable = false)
    private String name;

    private Range(String name) {
        this.name = name;
    }

    public static Range createAuthorityRange(String name) {
        return new Range(name);
    }
}
