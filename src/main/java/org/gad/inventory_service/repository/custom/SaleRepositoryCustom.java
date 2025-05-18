package org.gad.inventory_service.repository.custom;

import org.gad.inventory_service.model.Sale;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface SaleRepositoryCustom {
    Flux<Sale> findByOptionalDateRange(LocalDateTime startDate, LocalDateTime endDate);
    Flux<Sale> findByOptionalPriceTotalRange(BigDecimal minPrice, BigDecimal maxPrice);
}
