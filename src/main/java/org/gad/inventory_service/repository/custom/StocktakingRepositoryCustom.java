package org.gad.inventory_service.repository.custom;

import org.gad.inventory_service.model.Stocktaking;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface StocktakingRepositoryCustom {
    Flux<Stocktaking> findByOptionalDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
