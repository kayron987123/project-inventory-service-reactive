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

@Repository
@RequiredArgsConstructor
public class StocktakingRepositoryCustomImpl implements StocktakingRepositoryCustom {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<Stocktaking> findByOptionalDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        Criteria criteria = new Criteria();

        if (startDate != null && endDate != null) {
            criteria = Criteria.where("stocktaking_date").gte(startDate).lte(endDate);
        } else if (startDate != null) {
            criteria = Criteria.where("stocktaking_date").gte(startDate);
        } else if (endDate != null) {
            criteria = Criteria.where("stocktaking_date").lte(endDate);
        }

        Query query = new Query(criteria);

        return reactiveMongoTemplate.find(query, Stocktaking.class);
    }
}
