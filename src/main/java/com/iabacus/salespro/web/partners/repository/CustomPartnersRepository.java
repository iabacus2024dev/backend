package com.iabacus.salespro.web.partners.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.iabacus.salespro.web.partners.domain.Partners;
import com.iabacus.salespro.web.partners.request.PartnersSearchCondition;

public interface CustomPartnersRepository {

    Page<Partners> search(PartnersSearchCondition condition, Pageable pageable);

}
