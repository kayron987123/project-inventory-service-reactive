package org.gad.inventory_service.repository.custom.impl;

import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.model.User;
import org.gad.inventory_service.repository.custom.UserRepositoryCustom;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final ReactiveMongoTemplate reactiveMongoTemplate;


    @Override
    public Flux<User> findUsersByNameOrLastName(String name, String lastName) {
        Query query = new Query();
        if (name != null) {
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        }
        if (lastName != null) {
            query.addCriteria(Criteria.where("last_name").regex(lastName, "i"));
        }
        return reactiveMongoTemplate.find(query, User.class);
    }

    @Override
    public Mono<User> findUserByUsernameOrEmail(String username, String email) {
        Query query = new Query();
        if (username != null) {
            query.addCriteria(Criteria.where("username"));
        }
        if (email != null) {
            query.addCriteria(Criteria.where("email"));
        }
        return reactiveMongoTemplate.findOne(query, User.class);
    }
}
