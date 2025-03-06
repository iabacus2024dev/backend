package kr.co.iabacus.sales.web.partners.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.co.iabacus.sales.core.common.error.ErrorCode;
import kr.co.iabacus.sales.core.common.error.exception.BusinessException;
import kr.co.iabacus.sales.web.partners.domain.Partners;
import kr.co.iabacus.sales.web.partners.dto.PartnersCreateRequest;
import kr.co.iabacus.sales.web.partners.dto.PartnersResponse;
import kr.co.iabacus.sales.web.partners.dto.PartnersSearchCondition;
import kr.co.iabacus.sales.web.partners.repository.PartnersRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnersService {

    private final PartnersRepository partnersRepository;

    @Transactional(readOnly = true)
    public Page<PartnersResponse> searchPartners(PartnersSearchCondition condition, Pageable pageable) {
        return partnersRepository.search(condition, pageable)
            .map(PartnersResponse::from);
    }

    @Transactional(readOnly = true)
    public PartnersResponse getPartnersDetail(Long partnersId) {
        Partners partners = partnersRepository.findByIdAndIsActivatedTrue(partnersId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PARTNERS_NOT_FOUND));

        return PartnersResponse.from(partners);
    }

    @Transactional
    public void createPartners(PartnersCreateRequest request) {
        validateDuplicateName(request.getName());
        partnersRepository.save(request.toEntity());
    }

    private void validateDuplicateName(String name) {
        if (partnersRepository.findByName(name).isPresent()) {
            throw new BusinessException(ErrorCode.PARTNERS_NAME_ALREADY_EXISTS);
        }
    }

}
