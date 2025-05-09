package org.gad.inventory_service.repository.custom.impl;

import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.model.Product;
import org.gad.inventory_service.repository.custom.ProductRepositoryCustom;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<Product> findByCriteria(String name,
                                        String categoryName,
                                        String brandName,
                                        String providerName) {
        Query query = new Query();
        if (name != null) {
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        }
        if (categoryName != null) {
            query.addCriteria(Criteria.where("category.name").regex(categoryName, "i"));
        }
        if (brandName != null) {
            query.addCriteria(Criteria.where("brand.name").regex(brandName, "i"));
        }
        if (providerName != null) {
            query.addCriteria(Criteria.where("provider.name").regex(providerName, "i"));
        }
        return reactiveMongoTemplate.find(query, Product.class);
    }
}
