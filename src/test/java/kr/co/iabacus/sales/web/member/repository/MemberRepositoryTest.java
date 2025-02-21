package kr.co.iabacus.sales.web.member.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import kr.co.iabacus.sales.web.common.Money;
import kr.co.iabacus.sales.web.member.domain.Member;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("활성화된 회원 조회 테스트")
    void findByIdAndIsActivatedTrue() {
        // given
        Member member1 = Member.builder()
            .name("홍길동")
            .build();

        Member member2 = Member.builder()
            .name("김철수")
            .build();

        member2.inactivate(LocalDateTime.of(2025, 1, 1, 0, 0, 0));
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);

        // when
        Member findMember1 = memberRepository.findByIdAndIsActivatedTrue(savedMember1.getId()).orElseThrow();
        Member findMember2 = memberRepository.findByIdAndIsActivatedTrue(savedMember2.getId()).orElse(null);

        // then
        assertThat(findMember1).isEqualTo(savedMember1);
        assertThat(findMember2).isNull();
    }

    @Test
    @DisplayName("이메일, 이름, 활성화된 회원 조회 테스트")
    void findByEmailAndNameAndIsActivatedTrue() {
        // given
        Member member1 = Member.builder()
            .name("홍길동")
            .email("hong@iabacus.co.kr")
            .salary(Money.wons(1000000))
            .build();

        Member member2 = Member.builder()
            .name("김철수")
            .email("kim@iabacus.co.kr")
            .build();

        Member savedMember1 = memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        Member findMember1 = memberRepository.findByEmailAndNameAndIsActivatedTrue("hong@iabacus.co.kr", "홍길동").orElseThrow();

        // then
        assertThat(findMember1).isEqualTo(savedMember1);
    }

}
