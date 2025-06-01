package org.gad.inventory_service.repository.custom.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gad.inventory_service.model.Product;
import org.gad.inventory_service.repository.custom.ProductRepositoryCustom;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

import java.util.regex.Pattern;


@Slf4j
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
        query.addCriteria(Criteria.where("name").regex(Pattern.quote(name), "i"));
        query.addCriteria(Criteria.where("category_id").is(categoryId));
        query.addCriteria(Criteria.where("brand_id").is(brandId));
        query.addCriteria(Criteria.where("provider_id").is(providerId));

        return reactiveMongoTemplate.find(query, Product.class);
    }
}
