package com.iabacus.salespro.web.role.response;

import lombok.Builder;
import lombok.Data;

import com.iabacus.salespro.web.role.domain.Authority;

@Data
public class AuthorityResponse {

    private String name;
    private String range;

    @Builder
    public AuthorityResponse(String name, String range) {
        this.name = name;
        this.range = range;
    }

    public static AuthorityResponse from(Authority authority) {
        return AuthorityResponse.builder()
            .name(authority.getName())
            .range(authority.getAuthorityRangeList().get(0).getRange() != null ? authority.getAuthorityRangeList().get(0).getRange().getName() : null)
            .build();
    }

}
