package org.gad.inventory_service.repository.custom;

import org.gad.inventory_service.model.Product;
import reactor.core.publisher.Flux;


public interface ProductRepositoryCustom {
    Flux<Product> findByCriteria(String name,
                                 String categoryName,
                                 String brandName,
                                 String providerName);
}
