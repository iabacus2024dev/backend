package com.iabacus.salespro.web.auth.request;

import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
public class PasswordChangeRequest {

    @NotNull
    private String password;

    @NotNull
    private String newPassword;

    @NotNull
    private String newPasswordConfirm;

    @Builder
    public PasswordChangeRequest(String password, String newPassword, String newPasswordConfirm) {
        this.password = password;
        this.newPassword = newPassword;
        this.newPasswordConfirm = newPasswordConfirm;
    }

}
