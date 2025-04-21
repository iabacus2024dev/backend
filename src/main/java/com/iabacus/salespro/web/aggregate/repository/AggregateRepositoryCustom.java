package com.iabacus.salespro.web.aggregate.repository;

import com.iabacus.salespro.web.aggregate.response.AggregateResponse;
import java.util.List;

public interface AggregateRepositoryCustom {
    List<AggregateResponse> getAggregate(String inputYear);
}
