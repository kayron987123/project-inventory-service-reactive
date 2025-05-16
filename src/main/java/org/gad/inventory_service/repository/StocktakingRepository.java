package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Stocktaking;
import org.gad.inventory_service.repository.custom.StocktakingRepositoryCustom;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface StocktakingRepository extends ReactiveMongoRepository<Stocktaking, UUID>, StocktakingRepositoryCustom {
    Flux<Stocktaking> findStocktakingByProductName(String productName);
}
