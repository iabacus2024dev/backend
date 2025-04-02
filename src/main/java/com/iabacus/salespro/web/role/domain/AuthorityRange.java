package com.iabacus.salespro.web.role.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "TB_AUTHORITY_RANGE")
public class AuthorityRange {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "AUTHORITY_RANGE_ID")
    private Long id;

    @Setter
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "AUTHORITY_ID")
    private Authority authority;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "RANGE_ID")
    private Range range;

    private AuthorityRange(Range range) {
        this.range = range;
    }

    public static AuthorityRange createAuthorityRange(Range range) {
        return new AuthorityRange(range);
    }
}
