package kr.co.iabacus.sales.web.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;

@Data
public class MemberRegisterRequest {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @Builder
    public MemberRegisterRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }

}
