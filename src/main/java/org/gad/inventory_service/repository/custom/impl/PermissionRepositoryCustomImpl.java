package org.gad.inventory_service.repository.custom.impl;

import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.model.Permission;
import org.gad.inventory_service.repository.custom.PermissionRepositoryCustom;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Set;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class PermissionRepositoryCustomImpl implements PermissionRepositoryCustom {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<Permission> findByNamesLikeIgnoreCase(Set<String> names) {
        Criteria criteria = new Criteria().orOperator(
                names.stream()
                        .map(name -> Criteria.where("name").regex(".*" + Pattern.quote(name) + ".*", "i"))
                        .toArray(Criteria[]::new)
        );
        Query query = new Query(criteria);
        return reactiveMongoTemplate.find(query, Permission.class);
    }
}
