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
@Table(name = "TB_AUTHORITY_ACTION")
public class AuthorityAction {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "AUTHORITY_ACTION_ID")
    private Long id;

    @Setter
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "AUTHORITY_ID")
    private Authority authority;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ACTION_ID")
    private Action action;

    private AuthorityAction(Action action) {
        this.action = action;
    }

    public static AuthorityAction createAuthorityAction(Action action) {
        return new AuthorityAction(action);
    }
}
