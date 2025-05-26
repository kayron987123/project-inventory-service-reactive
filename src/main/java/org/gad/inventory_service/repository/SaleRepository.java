package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Sale;
import org.gad.inventory_service.repository.custom.SaleRepositoryCustom;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;


public interface SaleRepository extends ReactiveMongoRepository<Sale, String>, SaleRepositoryCustom {
    Flux<Sale> findSalesByProductId(String idProduct);
}
