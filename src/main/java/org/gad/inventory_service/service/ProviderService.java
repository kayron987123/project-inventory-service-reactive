package org.gad.inventory_service.service;

import org.gad.inventory_service.dto.CreateProviderRequest;
import org.gad.inventory_service.dto.ProviderDTO;
import org.gad.inventory_service.dto.UpdateProviderRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProviderService {
    Mono<ProviderDTO> findByUuid(String uuid);
    Mono<ProviderDTO> findProviderByRuc(String ruc);
    Mono<ProviderDTO> findProviderByName(String name);
    Mono<ProviderDTO> findProviderByEmail(String email);
    Mono<ProviderDTO> findProviderByDni(String dni);
    Flux<ProviderDTO> findAllProviders();
    Mono<ProviderDTO> saveProvider(CreateProviderRequest createProviderRequest);
    Mono<ProviderDTO> updateProvider(String uuid, UpdateProviderRequest updateProviderRequest);
    Mono<Void> deleteProviderByUuid(String uuid);
}