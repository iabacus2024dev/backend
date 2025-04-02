package com.iabacus.salespro.web.role.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "TB_AUTHORITY_PAGE")
public class AuthorityPage {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "PAGE_ID")
    private Long id;

    @Column(name = "PAGE_NAME", unique = true, nullable = false)
    private String name;

    private AuthorityPage(String name) {
        this.name = name;
    }

    public static AuthorityPage createAuthorityPage(String pageName) {
        return new AuthorityPage(pageName);
    }
}

