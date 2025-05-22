package org.gad.inventory_service.repository.custom.impl;

import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.model.Sale;
import org.gad.inventory_service.repository.custom.SaleRepositoryCustom;
import org.gad.inventory_service.utils.QueryUtils;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.gad.inventory_service.utils.Constants.TEXT_SALE_DATE;
import static org.gad.inventory_service.utils.Constants.TEXT_SALE_TOTAL_PRICE;

@Repository
@RequiredArgsConstructor
public class SaleRepositoryCustomImpl implements SaleRepositoryCustom {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<Sale> findByOptionalDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        Query query = QueryUtils.buildOptionalRangeQuery(TEXT_SALE_DATE, startDate, endDate);
        return reactiveMongoTemplate.find(query, Sale.class);
    }

    @Override
    public Flux<Sale> findByOptionalPriceTotalRange(BigDecimal minPrice, BigDecimal maxPrice) {
        Query query = QueryUtils.buildOptionalRangeQuery(TEXT_SALE_TOTAL_PRICE, minPrice, maxPrice);
        return reactiveMongoTemplate.find(query, Sale.class);
    }
}
