package com.iabacus.salespro.web.role.response;

import lombok.Builder;
import lombok.Data;

import com.iabacus.salespro.web.role.domain.Authority;

@Data
public class AuthorityResponse {

    private String name;

    @Builder
    public AuthorityResponse(String name) {
        this.name = name;
    }

    public static AuthorityResponse from(Authority authority) {
        return AuthorityResponse.builder()
            .name(authority.getName())
            .build();
    }

}
