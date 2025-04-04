package com.iabacus.salespro.web.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.IntegrationTestSupport;
import com.iabacus.salespro.web.auth.domain.Auth;
import com.iabacus.salespro.web.auth.repository.AuthRepository;
import com.iabacus.salespro.web.auth.request.MemberRegisterRequest;
import com.iabacus.salespro.web.auth.request.PasswordFindRequest;
import com.iabacus.salespro.web.auth.request.PasswordInitializeRequest;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.repository.EmployeeRepository;
import com.iabacus.salespro.web.member.domain.Member;
import com.iabacus.salespro.web.member.repository.MemberRepository;

class AuthServiceTest extends IntegrationTestSupport {

    @Autowired
    private AuthService authService;

    @MockitoBean
    private AuthMailService mailService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("이름과 이메일을 입력받아 회원을 등록하면 auth 테이블에 이메일과 토큰이 저장되고 메일이 전송된다.")
    void registerEmployee() {
        // given
        String name = "박상철";
        String email = "example@iabacus.co.kr";
        employeeRepository.save(Employee.builder()
            .name(name)
            .email(email)
            .build());

        MemberRegisterRequest request = MemberRegisterRequest.builder()
            .name(name)
            .email(email)
            .build();

        // stubbing
        doNothing().when(mailService).sendInitializePasswordLink(anyString(), anyString());

        // when
        authService.registerMember(request);
        Auth auths = authRepository.findByEmail(email).orElseThrow();

        // then
        assertThat(auths.getEmail()).isEqualTo(email);
        verify(mailService, times(1))
            .sendInitializePasswordLink(email, authRepository.findByEmail(email).orElseThrow().getToken());
    }

    @Test
    @DisplayName("존재하지 않는 구성원을 회원으로 등록하면 에러가 발생한다.")
    void registeremployee_employeeNotFound() {
        // given
        employeeRepository.save(Employee.builder()
            .name("박상철")
            .email("example@iabacus.co.kr")
            .build());

        // when
        MemberRegisterRequest request = MemberRegisterRequest.builder()
            .name("박상철")
            .email("another@iabacus.co.kr")
            .build();

        // then
        assertThatThrownBy(() -> authService.registerMember(request))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.EMPLOYEE_NOT_FOUND);
    }

    @Test
    @DisplayName("이메일 도메인이 iabacus.co.kr이 아닌 경우 회원 등록에 실패한다.")
    void registeremployee_invalidEmailDomain() {
        // given
        String name = "박상철";
        String email = "example@another.com";
        employeeRepository.save(Employee.builder()
            .name(name)
            .email(email)
            .build());

        MemberRegisterRequest request = MemberRegisterRequest.builder()
            .name(name)
            .email(email)
            .build();

        // when then
        assertThatThrownBy(() -> authService.registerMember(request))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_EMAIL_DOMAIN);
    }

    @Test
    @DisplayName("메일로 전송된 토큰으로 비밀번호 초기화를 하면 비밀번호가 초기화된다.")
    void initializePassword() {
        // given
        String name = "박상철";
        String email = "example@iabacus.co.kr";
        employeeRepository.save(Employee.builder()
            .name(name)
            .email(email)
            .build());

        authService.registerMember(MemberRegisterRequest.builder()
            .name(name)
            .email(email)
            .build());

        PasswordInitializeRequest request = PasswordInitializeRequest.builder()
            .token(authRepository.findByEmail(email).orElseThrow().getToken())
            .newPassword("Password1234!")
            .newPasswordConfirm("Password1234!")
            .build();

        // when
        authService.initializePassword(request, LocalDateTime.of(2025, 1, 1, 0, 0, 0));
        Member member = memberRepository.findByUsernameAndIsActivatedTrue(email).orElseThrow();

        // then
        assertThat(member.getPassword()).isNotNull();
    }

    @Test
    @DisplayName("토큰이 만료된 경우 비밀번호 초기화에 실패한다.")
    void initializePassword_expiredToken() {
        // given
        String name = "박상철";
        String email = "example@iabacus.co.kr";
        String token = "token";
        employeeRepository.save(Employee.builder()
            .name(name)
            .email(email)
            .build());

        authRepository.save(Auth.builder()
            .email(email)
            .token(token)
            .expiredDateTime(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
            .build()
        );

        PasswordInitializeRequest request = PasswordInitializeRequest.builder()
            .token("token")
            .newPassword("Password1234!")
            .newPasswordConfirm("Password1234!")
            .build();

        // when then
        LocalDateTime currentDateTime = LocalDateTime.of(2025, 1, 1, 0, 31, 0);
        assertThatThrownBy(() -> authService.initializePassword(request, currentDateTime))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INITIALIZE_TOKEN_NOT_FOUND);
    }

    @Test
    @DisplayName("토큰이 일치하지 않는 경우 비밀번호 초기화에 실패한다.")
    void initializePassword_invalidToken() {
        // given
        String name = "박상철";
        String email = "example@iabacus.co.kr";
        String token = "token";
        employeeRepository.save(Employee.builder()
            .name(name)
            .email(email)
            .build());

        authRepository.save(Auth.builder()
            .email(email)
            .token(token)
            .expiredDateTime(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
            .build()
        );

        PasswordInitializeRequest request = PasswordInitializeRequest.builder()
            .token("failedToken")
            .newPassword("Password1234!")
            .newPasswordConfirm("Password1234!")
            .build();

        // when then
        LocalDateTime currentDateTime = LocalDateTime.of(2025, 1, 1, 0, 1, 0);
        assertThatThrownBy(() -> authService.initializePassword(request, currentDateTime))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INITIALIZE_TOKEN_NOT_FOUND);
    }

    @Test
    @DisplayName("회원이 비밀번호 찾기를 하면 비밀번호 변경 링크를 전송한다.")
    void findPassword() {
        // given
        String name = "박상철";
        String email = "example@iabacus.co.kr";

        Employee employee = Employee.builder()
            .name(name)
            .email(email)
            .build();
        employeeRepository.save(employee);

        Member member = Member.builder()
            .employeeId(employee.getId())
            .username(email)
            .build();
        memberRepository.save(member);

        // stubbing
        doNothing().when(mailService).sendInitializePasswordLink(anyString(), anyString());

        // when
        authService.findPassword(PasswordFindRequest.builder()
            .email(email)
            .build());
        Auth auth = authRepository.findByEmail(email).orElseThrow();

        // then
        assertThat(auth.getEmail()).isEqualTo(email);
        verify(mailService, times(1))
            .sendInitializePasswordLink(email, authRepository.findByEmail(email).orElseThrow().getToken());
    }

    @Test
    @DisplayName("회원이 비밀번호를 찾을 때 회원으로 등록되지 않은 이메일을 입력하면 에러가 발생한다.")
    void findPassword_employeeNotFound() {
        // given
        String name = "박상철";
        String email = "example@iabacus.co.kr";

        employeeRepository.save(Employee.builder()
            .name(name)
            .email(email)
            .build());

        // when
        PasswordFindRequest request = PasswordFindRequest.builder()
            .email(email)
            .build();

        // then
        assertThatThrownBy(() -> authService.findPassword(request))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MEMBER_NOT_FOUND);
    }

}
