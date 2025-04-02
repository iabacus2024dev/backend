package com.iabacus.salespro.web.role.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "TB_AUTHORITY_ACTION")
public class AuthorityAction {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ACTION_ID")
    private Long id;

    @Column(name = "ACTION_NAME", unique = true, nullable = false)
    private String name;

    private AuthorityAction(String name) {
        this.name = name;
    }

    public static AuthorityAction createAuthorityAction(String name) {
        return new AuthorityAction(name);
    }
}
