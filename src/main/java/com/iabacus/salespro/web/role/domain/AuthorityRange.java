package com.iabacus.salespro.web.role.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "TB_AUTHORITY_RANGE")
public class AuthorityRange {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "RANGE_ID")
    private Long id;

    @Column(name = "RANGE_NAME", unique = true, nullable = false)
    private String name;

    private AuthorityRange(String name) {
        this.name = name;
    }

    public static AuthorityRange createAuthorityRange(String name) {
        return new AuthorityRange(name);
    }
}
