package kr.co.iabacus.sales;

import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import kr.co.iabacus.sales.web.member.domain.Classification;
import kr.co.iabacus.sales.web.member.domain.ClassificationCode;
import kr.co.iabacus.sales.web.member.domain.Member;
import kr.co.iabacus.sales.web.member.repository.ClassificationRepository;
import kr.co.iabacus.sales.web.member.repository.MemberRepository;

@Profile("local")
@RequiredArgsConstructor
@Component
public class InitData {

    private final MemberRepository memberRepository;
    private final ClassificationRepository classificationRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initMember() {
        List<Classification> classifications = classificationRepository.findByCode(ClassificationCode.R);

        Member member1 = Member.builder()
            .name("박상철")
            .email("tkdcjf38@iabacus.co.kr")
            .teamId(1L)
            .grade(classifications.get(0))
            .build();

        Member member2 = Member.builder()
            .name("유하진")
            .email("abacushajin@gmail.com")
            .teamId(1L)
            .grade(classifications.get(1))
            .build();

        Member member3 = Member.builder()
            .name("임세인")
            .email("vicent7723@gmail.com")
            .teamId(2L)
            .grade(classifications.get(2))
            .build();
        memberRepository.saveAll(List.of(member1, member2, member3));
    }

}
