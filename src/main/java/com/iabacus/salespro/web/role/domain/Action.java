package com.iabacus.salespro.web.role.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "TB_ACTION")
public class Action {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ACTION_ID")
    private Long id;

    @Column(name = "ACTION_NAME", unique = true, nullable = false)
    private String name;

    private Action(String name) {
        this.name = name;
    }

    public static Action createAction(String name) {
        return new Action(name);
    }
}
