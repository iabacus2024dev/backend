package kr.co.iabacus.sales;

import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import kr.co.iabacus.sales.web.member.domain.Member;
import kr.co.iabacus.sales.web.member.repository.MemberRepository;

@Profile("local")
@RequiredArgsConstructor
@Component
public class InitData {

    private final MemberRepository memberRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void memberInit() {
        Member member1 = Member.builder()
            .name("박상철")
            .email("scpark0698@gmail.com")
            .build();

        Member member2 = Member.builder()
            .name("유하진")
            .email("abacushajin@gmail.com")
            .build();

        Member member3 = Member.builder()
            .name("임세인")
            .email("vicent7723@gmail.com")
            .build();
        memberRepository.saveAll(List.of(member1, member2, member3));
    }
    
}
