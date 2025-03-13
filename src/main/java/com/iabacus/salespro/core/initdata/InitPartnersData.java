package com.iabacus.salespro.core.initdata;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.web.common.Address;
import com.iabacus.salespro.web.common.Phone;
import com.iabacus.salespro.web.common.Ratio;
import com.iabacus.salespro.web.partners.domain.Partners;
import com.iabacus.salespro.web.partners.domain.PartnersGrade;
import com.iabacus.salespro.web.partners.repository.PartnersRepository;

@Profile("local")
@RequiredArgsConstructor
@Component
public class InitPartnersData {

    private final PartnersRepository partnersRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        List<Partners> partnersList = new ArrayList<>();
        partnersList.add(createPartners("LG UPLUS", "홍길동", "김철수", "lguplus@example.co.kr", "01000000000", PartnersGrade.B));
        partnersList.add(createPartners("LG CNS", "김지수", "김이사", "lgcns@example.co.kr", "01011111111", PartnersGrade.A));
        partnersList.add(createPartners("SK Telecom", "박영희", "이대리", "sktelecom@example.co.kr", "01022222222", PartnersGrade.C));
        partnersList.add(createPartners("KT", "최민호", "박부장", "kt@example.co.kr", "01033333333", PartnersGrade.A));
        partnersList.add(createPartners("Samsung SDS", "김하늘", "정과장", "samsungsds@example.co.kr", "01044444444", PartnersGrade.B));
        partnersList.add(createPartners("Hyundai AutoEver", "이성진", "최이사", "hyundaiautoever@example.co.kr", "01055555555", PartnersGrade.C));
        partnersList.add(createPartners("Naver Cloud", "윤서연", "신사장", "navercloud@example.co.kr", "01066666666", PartnersGrade.A));
        partnersList.add(createPartners("Kakao Enterprise", "정우성", "한팀장", "kakaoenterprise@example.co.kr", "01077777777", PartnersGrade.B));
        partnersList.add(createPartners("POSCO ICT", "서지혜", "고이사", "poscoict@example.co.kr", "01088888888", PartnersGrade.C));
        partnersList.add(createPartners("Lotte Data Communication", "조민기", "조부장", "lottedata@example.co.kr", "01099999999", PartnersGrade.A));
        partnersList.add(createPartners("CJ OliveNetworks", "이강현", "신부장", "cjolive@example.co.kr", "01100000000", PartnersGrade.B));
        partnersList.add(createPartners("Hanwha Systems", "박수진", "이본부장", "hanwhasys@example.co.kr", "01111111111", PartnersGrade.A));
        partnersList.add(createPartners("Daewoo Information Systems", "김태우", "박부장", "daewoois@example.co.kr", "01122222222", PartnersGrade.C));
        partnersList.add(createPartners("Shinsegae I&C", "최영석", "고이사", "shinsegaeic@example.co.kr", "01133333333", PartnersGrade.B));
        partnersList.add(createPartners("NHN", "윤지훈", "최사장", "nhn@example.co.kr", "01144444444", PartnersGrade.A));
        partnersList.add(createPartners("Hyosung ITX", "한예린", "김과장", "hyosungitx@example.co.kr", "01155555555", PartnersGrade.C));
        partnersList.add(createPartners("LG Electronics", "장도영", "이부장", "lge@example.co.kr", "01166666666", PartnersGrade.B));
        partnersList.add(createPartners("Samsung Electronics", "박형준", "조부장", "samsungelec@example.co.kr", "01177777777", PartnersGrade.A));
        partnersList.add(createPartners("Hyundai Mobis", "고진우", "김사장", "hyundaimobis@example.co.kr", "01188888888", PartnersGrade.C));
        partnersList.add(createPartners("Lotte Mart", "서준혁", "박본부장", "lottemart@example.co.kr", "01199999999", PartnersGrade.B));
        partnersList.add(createPartners("E-Mart", "이태성", "김대리", "emart@example.co.kr", "01000000000", PartnersGrade.A));
        partnersList.add(createPartners("Coupang", "조하영", "이사장", "coupang@example.co.kr", "01011111111", PartnersGrade.C));
        partnersList.add(createPartners("WeMakePrice", "김나희", "최팀장", "wemakeprice@example.co.kr", "01022222222", PartnersGrade.B));
        partnersList.add(createPartners("Baemin", "박지수", "신부장", "baemin@example.co.kr", "01033333333", PartnersGrade.A));
        partnersList.add(createPartners("Toss", "서강현", "고사장", "toss@example.co.kr", "01044444444", PartnersGrade.C));
        partnersList.add(createPartners("KakaoBank", "한서진", "장부장", "kakaobank@example.co.kr", "0255555555", PartnersGrade.B));
        partnersList.add(createPartners("Naver Financial", "이준호", "이본부장", "naverfinancial@example.co.kr", "0266666666", PartnersGrade.A));
        partnersList.add(createPartners("Shinhan Bank", "김도영", "박사장", "shinhan@example.co.kr", "0277777777", PartnersGrade.C));
        partnersList.add(createPartners("KB Kookmin Bank", "정한울", "최부장", "kbbank@example.co.kr", "0288888888", PartnersGrade.B));
        partnersList.add(createPartners("Woori Bank", "강지훈", "신대리", "wooribank@example.co.kr", "0299999999", PartnersGrade.A));
        partnersList.add(createPartners("Hana Bank", "윤다혜", "고과장", "hanabank@example.co.kr", "03100000000", PartnersGrade.C));
        partnersList.add(createPartners("IBK", "박선호", "김부장", "ibk@example.co.kr", "03111111111", PartnersGrade.B));
        partnersList.add(createPartners("NH Nonghyup Bank", "조윤성", "이부장", "nonghyup@example.co.kr", "03122222222", PartnersGrade.A));
        partnersRepository.saveAll(partnersList);
    }

    private Partners createPartners(String name, String ceoName, String salesRepName, String email, String phoneNumber, PartnersGrade partnersGrade) {
        return Partners.builder()
            .name(name)
            .ceoName(ceoName)
            .salesRepName(salesRepName)
            .salesRepEmail(email)
            .salesRepPhone(Phone.of(phoneNumber))
            .grade(partnersGrade)
            .address(createAddress("서울시 마포구 월드컵로13길 9", "1층 2호"))
            .commissionRate(Ratio.valueOf(9.12))
            .build();
    }

    private Address createAddress(String street, String detail) {
        return Address.builder()
            .street(street)
            .detail(detail)
            .zipcode("12345")
            .build();
    }

}
