package com.iabacus.salespro.web.aggregate.service;

import com.iabacus.salespro.web.aggregate.response.AggregateResponse;
import com.iabacus.salespro.web.aggregate.repository.AggregateRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AggregateService {


    private final AggregateRepositoryCustom aggregateRepository;

    public List<AggregateResponse> getAggregate(String year) {
        return aggregateRepository.getAggregateData(year);
    }
}