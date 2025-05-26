package org.gad.inventory_service.service;

import org.gad.inventory_service.dto.request.CreateProviderRequest;
import org.gad.inventory_service.dto.ProviderDTO;
import org.gad.inventory_service.dto.request.UpdateProviderRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProviderService {
    Mono<ProviderDTO> findProviderById(String id);
    Mono<ProviderDTO> findProviderByRuc(String ruc);
    Mono<ProviderDTO> findProviderByName(String name);
    Mono<ProviderDTO> findProviderByEmail(String email);
    Mono<ProviderDTO> findProviderByDni(String dni);
    Flux<ProviderDTO> findAllProviders();
    Mono<ProviderDTO> saveProvider(CreateProviderRequest createProviderRequest);
    Mono<ProviderDTO> updateProvider(String id, UpdateProviderRequest updateProviderRequest);
    Mono<Void> deleteProviderById(String id);
}