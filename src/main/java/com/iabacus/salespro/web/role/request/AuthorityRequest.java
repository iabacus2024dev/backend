package com.iabacus.salespro.web.role.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorityRequest {
    private String authorityPage;
    private String authorityAction;
    private String authorityRange;

    public static AuthorityRequest of(String authorityPage, String authorityAction, String authorityRange) {
        return AuthorityRequest.builder()
                .authorityPage(authorityPage)
                .authorityAction(authorityAction)
                .authorityRange(authorityRange)
                .build();
    }
}
