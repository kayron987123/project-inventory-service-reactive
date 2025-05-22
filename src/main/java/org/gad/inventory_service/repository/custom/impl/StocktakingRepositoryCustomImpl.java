package org.gad.inventory_service.repository.custom.impl;

import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.model.Stocktaking;
import org.gad.inventory_service.repository.custom.StocktakingRepositoryCustom;
import org.gad.inventory_service.utils.QueryUtils;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

import static org.gad.inventory_service.utils.Constants.TEXT_STOCKTAKING_DATE;

@Repository
@RequiredArgsConstructor
public class StocktakingRepositoryCustomImpl implements StocktakingRepositoryCustom {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<Stocktaking> findByOptionalDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        Query query = QueryUtils.buildOptionalRangeQuery(TEXT_STOCKTAKING_DATE, startDate, endDate);
        return reactiveMongoTemplate.find(query, Stocktaking.class);
    }
}
