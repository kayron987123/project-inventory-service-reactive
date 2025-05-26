package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Stocktaking;
import org.gad.inventory_service.repository.custom.StocktakingRepositoryCustom;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;


public interface StocktakingRepository extends ReactiveMongoRepository<Stocktaking, String>, StocktakingRepositoryCustom {
    Flux<Stocktaking> findStocktakingByProductId(String productId);
}
