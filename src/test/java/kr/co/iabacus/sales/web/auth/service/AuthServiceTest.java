package kr.co.iabacus.sales.web.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import kr.co.iabacus.sales.core.common.error.exception.BusinessException;
import kr.co.iabacus.sales.web.auth.domain.Auth;
import kr.co.iabacus.sales.web.auth.dto.MemberRegisterRequest;
import kr.co.iabacus.sales.web.auth.dto.PasswordFindRequest;
import kr.co.iabacus.sales.web.auth.dto.PasswordInitializeRequest;
import kr.co.iabacus.sales.web.auth.repository.AuthRepository;
import kr.co.iabacus.sales.web.mail.MailService;
import kr.co.iabacus.sales.web.member.domain.Member;
import kr.co.iabacus.sales.web.member.repository.MemberRepository;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockitoBean
    private MailService mailService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthRepository authRepository;

    @Test
    @DisplayName("이름과 이메일을 입력받아 회원을 등록하면 auth 테이블에 이메일과 토큰이 저장되고 메일이 전송된다.")
    void registerMember() {
        // given
        String name = "박상철";
        String email = "example@iabacus.co.kr";
        memberRepository.save(Member.builder()
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
    void registerMember_memberNotFound() {
        // given
        memberRepository.save(Member.builder()
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
            .hasMessage("구성원을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("이메일 도메인이 iabacus.co.kr이 아닌 경우 회원 등록에 실패한다.")
    void registerMember_invalidEmailDomain() {
        // given
        String name = "박상철";
        String email = "example@another.com";
        memberRepository.save(Member.builder()
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
            .hasMessage("유효하지 않은 이메일 도메인입니다.");
    }

    @Test
    @DisplayName("메일로 전송된 토큰으로 비밀번호 초기화를 하면 비밀번호가 초기화된다.")
    void initializePassword() {
        // given
        String name = "박상철";
        String email = "example@iabacus.co.kr";
        memberRepository.save(Member.builder()
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
        Member member = memberRepository.findByEmailAndIsActivatedTrue(email).orElseThrow();

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
        memberRepository.save(Member.builder()
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
            .hasMessage("비밀번호 초기화 토큰이 존재하지 않거나 만료되었습니다.");
    }

    @Test
    @DisplayName("토큰이 일치하지 않는 경우 비밀번호 초기화에 실패한다.")
    void initializePassword_invalidToken() {
        // given
        String name = "박상철";
        String email = "example@iabacus.co.kr";
        String token = "token";
        memberRepository.save(Member.builder()
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
            .hasMessage("비밀번호 초기화 토큰이 존재하지 않거나 만료되었습니다.");
    }

    @Test
    @DisplayName("회원이 비밀번호 찾기를 하면 비밀번호 변경 링크를 전송한다.")
    void findPassword() {
        // given
        String name = "박상철";
        String email = "example@iabacus.co.kr";

        Member member = Member.builder()
            .name(name)
            .email(email)
            .build();
        memberRepository.save(member);

        member.initializePassword("Password1234!");

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
    void findPassword_memberNotFound() {
        // given
        String name = "박상철";
        String email = "example@iabacus.co.kr";

        memberRepository.save(Member.builder()
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
            .hasMessage("가입되지 않은 회원입니다.");
    }

}
