package kr.co.iabacus.sales.web.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;

@Data
public class PasswordFindRequest {

    @Email
    @NotBlank
    private String email;

    @Builder
    private PasswordFindRequest(String email) {
        this.email = email;
    }

}
