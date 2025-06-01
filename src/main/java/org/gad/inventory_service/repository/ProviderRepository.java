package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Provider;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;


public interface ProviderRepository extends ReactiveMongoRepository<Provider, String> {
    Mono<Provider> findProviderByNameContainingIgnoreCase(String name);
    Mono<Provider> findProviderByEmail(String email);
    Mono<Provider> findProviderByRuc(String ruc);
    Mono<Provider> findProviderByDni(String dni);
}
