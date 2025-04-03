package com.iabacus.salespro.web.role.domain;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public static Range createRange(String name) {
        return new Range(name);
    }

}
