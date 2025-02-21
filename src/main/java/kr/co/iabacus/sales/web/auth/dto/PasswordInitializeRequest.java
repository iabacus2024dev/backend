package kr.co.iabacus.sales.web.auth.dto;

import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
public class PasswordInitializeRequest {

    @NotNull
    private String token;

    @NotNull
    private String newPassword;

    @NotNull
    private String newPasswordConfirm;

    @Builder
    public PasswordInitializeRequest(String token, String newPassword, String newPasswordConfirm) {
        this.token = token;
        this.newPassword = newPassword;
        this.newPasswordConfirm = newPasswordConfirm;
    }

}
