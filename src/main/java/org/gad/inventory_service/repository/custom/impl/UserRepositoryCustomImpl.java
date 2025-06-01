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
    public Flux<User> findUsersByNameOrLastName(String name, String lastName) {
        Query query = new Query();

        if (StringUtils.hasText(name) && StringUtils.hasText(lastName)) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("name").regex(".*" + Pattern.quote(name.trim()) + ".*", "i"),
                    Criteria.where("last_name").regex(".*" + Pattern.quote(lastName.trim()) + ".*", "i")
            ));
        } else if (StringUtils.hasText(name)) {
            query.addCriteria(Criteria.where("name").regex(".*" + Pattern.quote(name.trim()) + ".*", "i"));
        } else if (StringUtils.hasText(lastName)) {
            query.addCriteria(Criteria.where("last_name").regex(".*" + Pattern.quote(lastName.trim()) + ".*", "i"));
        }
        return reactiveMongoTemplate.find(query, User.class);
    }

    @Override
    public Mono<User> findUserByUsernameOrEmail(String username, String email) {
        Query query = new Query();
        if (username != null && email != null) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("username").is(username),
                    Criteria.where("email").is(email)
            ));
        } else if (username != null) {
            query.addCriteria(Criteria.where("username").is(username));
        } else if (email != null) {
            query.addCriteria(Criteria.where("email").is(email));
        }
        return reactiveMongoTemplate.findOne(query, User.class);
    }
}
