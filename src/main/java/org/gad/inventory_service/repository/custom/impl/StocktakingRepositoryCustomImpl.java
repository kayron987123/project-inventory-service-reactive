package org.gad.inventory_service.repository.custom.impl;

import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.model.Stocktaking;
import org.gad.inventory_service.repository.custom.StocktakingRepositoryCustom;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
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
        Query query;

        if (startDate != null && endDate != null) {
            query = new Query(Criteria.where(TEXT_STOCKTAKING_DATE).gte(startDate).lte(endDate));
        } else if (startDate != null) {
            query = new Query(Criteria.where(TEXT_STOCKTAKING_DATE).gte(startDate));
        } else if (endDate != null) {
            query = new Query(Criteria.where(TEXT_STOCKTAKING_DATE).lte(endDate));
        } else {
            query = new Query();
        }
        return reactiveMongoTemplate.find(query, Stocktaking.class);
    }
}
