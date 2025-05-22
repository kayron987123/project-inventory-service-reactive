package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Sale;
import org.gad.inventory_service.repository.custom.SaleRepositoryCustom;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface SaleRepository extends ReactiveMongoRepository<Sale, UUID>, SaleRepositoryCustom {
    Flux<Sale> findSalesByProductName(String nameProduct);
}
