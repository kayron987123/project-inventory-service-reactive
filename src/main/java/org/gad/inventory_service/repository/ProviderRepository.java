package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Provider;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProviderRepository extends ReactiveMongoRepository<Provider, UUID> {
    Mono<Provider> findProviderByNameIgnoreCase(String name);
}
