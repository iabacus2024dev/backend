package com.iabacus.salespro.web.partners.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.common.PageResponse;
import com.iabacus.salespro.web.partners.domain.Partners;
import com.iabacus.salespro.web.partners.repository.PartnersRepository;
import com.iabacus.salespro.web.partners.request.PartnersCreateRequest;
import com.iabacus.salespro.web.partners.request.PartnersSearchCondition;
import com.iabacus.salespro.web.partners.request.PartnersUpdateRequest;
import com.iabacus.salespro.web.partners.response.PartnersDetailResponse;
import com.iabacus.salespro.web.partners.response.PartnersExcelResponse;
import com.iabacus.salespro.web.partners.response.PartnersSearchResponse;
import com.iabacus.salespro.web.partners.response.PartnersStatsResponse;
import com.iabacus.salespro.web.partners.domain.PartnersGrade;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PartnersService {

    private final PartnersRepository partnersRepository;

    public PageResponse<PartnersSearchResponse> searchPartners(PartnersSearchCondition condition, Pageable pageable) {
        Page<PartnersSearchResponse> page = partnersRepository.search(condition, pageable).map(PartnersSearchResponse::from);
        return new PageResponse<>(page);
    }

    public PartnersDetailResponse getPartnersDetail(Long id) {
        Partners partners = partnersRepository.findByIdAndIsActivatedTrue(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.PARTNERS_NOT_FOUND));
        return PartnersDetailResponse.from(partners);
    }

    @Transactional
    public void createPartners(PartnersCreateRequest request) {
        partnersRepository.save(request.toEntity());
    }

    @Transactional
    public void updatePartners(Long id, PartnersUpdateRequest request) {
        Partners partners = partnersRepository.findByIdAndIsActivatedTrue(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.PARTNERS_NOT_FOUND));
        if (!partners.getModifiedDateTime().isEqual(request.getModifiedDateTime())) {
            throw new BusinessException(ErrorCode.CONFLICT_MODIFIED_TIME);
        }
        partners.update(request);
    }

    @Transactional
    public void deletePartners(Long id) {
        Partners partners = partnersRepository.findByIdAndIsActivatedTrue(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.PARTNERS_NOT_FOUND));

        partners.inactivate(LocalDateTime.now());
        partnersRepository.save(partners);
    }

    public List<PartnersExcelResponse> getPartners(PartnersSearchCondition condition, Pageable pageable) {
        return partnersRepository.searchWithoutPage(condition, pageable).stream()
            .map(PartnersExcelResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public PartnersStatsResponse getPartnersStats() {
        // 활성화된 모든 협력사 조회
        List<Partners> allPartners = partnersRepository.findAllByIsActivatedTrueOrderByCreatedDateTimeDesc();
        
        // 총 협력사 수
        long totalPartners = allPartners.size();
        
        // 활성 협력사 수 (가정: 등급이 있고 연락처가 있는 협력사를 활성으로 간주)
        long activePartners = allPartners.stream()
            .filter(partner -> partner.getGrade() != null && 
                              partner.getCeoName() != null &&
                              !partner.getCeoName().trim().isEmpty())
            .count();
        
        // 평균 등급 계산
        String averageGrade = calculateAverageGrade(allPartners);
        
        // 매출 기여도 (임시로 고정값 설정 - 실제로는 프로젝트와 연결해서 계산해야 함)
        double revenueContribution = calculateRevenueContribution(allPartners);
        
        return PartnersStatsResponse.builder()
            .totalPartners(totalPartners)
            .activePartners(activePartners)
            .averageGrade(averageGrade)
            .revenueContribution(revenueContribution)
            .build();
    }

    private String calculateAverageGrade(List<Partners> partners) {
        if (partners.isEmpty()) {
            return "N/A";
        }
        
        // 등급별 점수 매핑
        Map<PartnersGrade, Integer> gradeScores = Map.of(
            PartnersGrade.A, 5,
            PartnersGrade.B, 4,
            PartnersGrade.C, 3,
            PartnersGrade.D, 2,
            PartnersGrade.E, 1
        );
        
        OptionalDouble averageScore = partners.stream()
            .filter(partner -> partner.getGrade() != null)
            .mapToInt(partner -> gradeScores.getOrDefault(partner.getGrade(), 0))
            .average();
        
        if (averageScore.isEmpty()) {
            return "N/A";
        }
        
        // 평균 점수를 등급으로 변환
        double score = averageScore.getAsDouble();
        if (score >= 4.5) return "A";
        else if (score >= 3.5) return "B";
        else if (score >= 2.5) return "C";
        else if (score >= 1.5) return "D";
        else return "E";
    }

    private double calculateRevenueContribution(List<Partners> partners) {
        // 임시로 협력사 수에 따른 기여도 계산 (실제로는 프로젝트 데이터와 연결 필요)
        if (partners.isEmpty()) {
            return 0.0;
        }
        
        // A등급: 15%, B등급: 12%, C등급: 8%, D등급: 5%, E등급: 2%의 기여도로 가정
        Map<PartnersGrade, Double> gradeContribution = Map.of(
            PartnersGrade.A, 15.0,
            PartnersGrade.B, 12.0,
            PartnersGrade.C, 8.0,
            PartnersGrade.D, 5.0,
            PartnersGrade.E, 2.0
        );
        
        double totalContribution = partners.stream()
            .filter(partner -> partner.getGrade() != null)
            .mapToDouble(partner -> gradeContribution.getOrDefault(partner.getGrade(), 0.0))
            .sum();
        
        return Math.round(totalContribution * 10) / 10.0; // 소수점 첫째자리까지
    }

}
