package org.gad.inventory_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gad.inventory_service.dto.CreateProviderRequest;
import org.gad.inventory_service.dto.ProviderDTO;
import org.gad.inventory_service.dto.UpdateProviderRequest;
import org.gad.inventory_service.exception.ProviderNotFoundException;
import org.gad.inventory_service.model.Provider;
import org.gad.inventory_service.repository.ProviderRepository;
import org.gad.inventory_service.service.ProviderService;
import org.gad.inventory_service.utils.Constants;
import org.gad.inventory_service.utils.Mappers;
import org.gad.inventory_service.utils.UtilsMethods;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.gad.inventory_service.utils.Constants.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {
    private final ProviderRepository providerRepository;

    @Override
    public Mono<ProviderDTO> findByUuid(String uuid) {
        return findProvider(providerRepository.findById(UtilsMethods.convertStringToUUID(uuid)),
                PROVIDER_NOT_FOUND_UUID + uuid);
    }

    @Override
    public Mono<ProviderDTO> findProviderByRuc(String ruc) {
        return findProvider(providerRepository.findProviderByRuc(ruc),
                PROVIDER_NOT_FOUND_RUC + ruc);
    }

    @Override
    public Mono<ProviderDTO> findProviderByName(String name) {
        return findProvider(providerRepository.findProviderByNameContainingIgnoreCase(name),
                PROVIDER_NOT_FOUND_NAME + name);
    }

    @Override
    public Mono<ProviderDTO> findProviderByEmail(String email) {
        return findProvider(providerRepository.findProviderByEmail(email),
                PROVIDER_NOT_FOUND_EMAIL + email);
    }

    @Override
    public Mono<ProviderDTO> findProviderByDni(String dni) {
        return findProvider(providerRepository.findProviderByDni(dni),
                PROVIDER_NOT_FOUND_DNI + dni);
    }

    @Override
    public Flux<ProviderDTO> findAllProviders() {
        return providerRepository.findAll()
                .switchIfEmpty(Flux.error(new ProviderNotFoundException(PROVIDER_NOT_FOUND_FLUX)))
                .map(Mappers::providerToDTO)
                .doOnError(error -> log.error(Constants.ERROR_SEARCHING_PROVIDER, error.getMessage()));
    }

    @Override
    public Mono<ProviderDTO> saveProvider(CreateProviderRequest createProviderRequest) {
        Provider providerToSave = Provider.builder()
                .idProvider(UtilsMethods.generateUUID())
                .name(createProviderRequest.name())
                .ruc(createProviderRequest.ruc())
                .dni(createProviderRequest.dni())
                .address(createProviderRequest.address())
                .phone(createProviderRequest.phone())
                .email(createProviderRequest.email())
                .build();

        return providerRepository.save(providerToSave)
                .map(Mappers::providerToDTO)
                .doOnError(error -> log.error(ERROR_SAVING_PROVIDER, error.getMessage()));
    }

    @Override
    public Mono<ProviderDTO> updateProvider(String uuid, UpdateProviderRequest updateProviderRequest) {
        Mono<Provider> providerMono = findProviderByUuidString(uuid);

        return providerMono.
                flatMap(provider -> {
                    provider.setName(updateProviderRequest.name());
                    provider.setRuc(updateProviderRequest.ruc());
                    provider.setDni(updateProviderRequest.dni());
                    provider.setAddress(updateProviderRequest.address());
                    provider.setPhone(updateProviderRequest.phone());
                    provider.setEmail(updateProviderRequest.email());
                    return providerRepository.save(provider);
                })
                .map(Mappers::providerToDTO)
                .doOnError(error -> log.error(ERROR_UPDATING_PROVIDER, error.getMessage()));
    }

    @Override
    public Mono<Void> deleteProviderByUuid(String uuid) {
        Mono<Provider> providerMono = findProviderByUuidString(uuid);

        return providerMono
                .flatMap(provider -> providerRepository.deleteById(provider.getIdProvider()))
                .doOnError(error -> log.error(ERROR_DELETING_PROVIDER, error.getMessage()));
    }

    private Mono<Provider> findProviderByUuidString(String uuid) {
        return providerRepository.findById(UtilsMethods.convertStringToUUID(uuid))
                .switchIfEmpty(Mono.error(new ProviderNotFoundException(PROVIDER_NOT_FOUND_UUID + uuid)))
                .doOnError(error -> log.error(Constants.ERROR_SEARCHING_PROVIDER, error.getMessage()));
    }

    private Mono<ProviderDTO> findProvider(Mono<Provider> providerMono, String errorMessage) {
        return providerMono
                .switchIfEmpty(Mono.error(new ProviderNotFoundException(errorMessage)))
                .map(Mappers::providerToDTO)
                .doOnError(error -> log.error(ERROR_SEARCHING_PROVIDER, error.getMessage()));
    }
}
