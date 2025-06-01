package org.gad.inventory_service.repository.custom.impl;

import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.model.User;
import org.gad.inventory_service.repository.custom.UserRepositoryCustom;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final ReactiveMongoTemplate reactiveMongoTemplate;


    @Override
    public Flux<User> findUsersByNameLastName(String name, String lastName) {
        Query query = new Query();
        if (name != null){
            query.addCriteria(Criteria.where("name").regex(".*" + Pattern.quote(name.trim()) + ".*", "i"));
        }

        if (lastName != null) {
            query.addCriteria(Criteria.where("lastName").regex(".*" + Pattern.quote(lastName.trim()) + ".*", "i"));
        }
        return reactiveMongoTemplate.find(query, User.class);
    }

    @Override
    public Mono<User> findUserByUsernameEmail(String username, String email) {
        Query query = new Query();
        if (StringUtils.hasText(username)) {
            query.addCriteria(Criteria.where("username").is(username.trim()));
        }

        if (StringUtils.hasText(email)) {
            query.addCriteria(Criteria.where("email").is(email.trim()));
        }
        return reactiveMongoTemplate.findOne(query, User.class);
    }
}
