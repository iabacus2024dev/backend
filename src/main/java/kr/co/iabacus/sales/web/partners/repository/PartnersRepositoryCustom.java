package kr.co.iabacus.sales.web.partners.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.co.iabacus.sales.web.partners.domain.Partners;
import kr.co.iabacus.sales.web.partners.dto.PartnersSearchCondition;

public interface PartnersRepositoryCustom {

    Page<Partners> search(PartnersSearchCondition condition, Pageable pageable);

}
