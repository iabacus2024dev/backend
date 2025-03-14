package com.iabacus.salespro.web.partners.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.core.error.BusinessException;
import com.iabacus.salespro.core.error.ErrorCode;
import com.iabacus.salespro.web.common.PageResponse;
import com.iabacus.salespro.web.partners.domain.Partners;
import com.iabacus.salespro.web.partners.repository.PartnersRepository;
import com.iabacus.salespro.web.partners.request.PartnersCreateRequest;
import com.iabacus.salespro.web.partners.request.PartnersSearchCondition;
import com.iabacus.salespro.web.partners.request.PartnersUpdateRequest;
import com.iabacus.salespro.web.partners.response.PartnersDetailResponse;
import com.iabacus.salespro.web.partners.response.PartnersSearchResponse;

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

}
