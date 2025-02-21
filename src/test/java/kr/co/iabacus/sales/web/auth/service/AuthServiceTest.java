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
        String email = "example@exmple.com";
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
    }

    @Test
    @DisplayName("존재하지 않는 구성원을 회원으로 등록하면 에러가 발생한다.")
    void registerMember_memberNotFound() {
        // given
        memberRepository.save(Member.builder()
            .name("박상철")
            .email("example@exmple.com")
            .build());

        // when
        MemberRegisterRequest request = MemberRegisterRequest.builder()
            .name("박상철")
            .email("another@exmple.com")
            .build();

        // then
        assertThatThrownBy(() -> authService.registerMember(request))
            .isInstanceOf(BusinessException.class)
            .hasMessage("구성원을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("메일로 전송된 토큰으로 비밀번호 초기화를 하면 비밀번호가 초기화된다.")
    void initializePassword() {
        // given
        String name = "박상철";
        String email = "example@exmple.com";
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

}
