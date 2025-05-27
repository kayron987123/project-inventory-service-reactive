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
                                        String categoryId,
                                        String brandId,
                                        String providerId) {
        Query query = new Query();
        if (name != null) {
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        }
        if (categoryId != null) {
            query.addCriteria(Criteria.where("category_id"));
        }
        if (brandId != null) {
            query.addCriteria(Criteria.where("brand_id"));
        }
        if (providerId != null) {
            query.addCriteria(Criteria.where("provider_id"));
        }
        return reactiveMongoTemplate.find(query, Product.class);
    }
}
