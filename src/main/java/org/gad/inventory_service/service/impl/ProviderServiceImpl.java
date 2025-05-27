package org.gad.inventory_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gad.inventory_service.dto.request.CreateProviderRequest;
import org.gad.inventory_service.dto.ProviderDTO;
import org.gad.inventory_service.dto.request.UpdateProviderRequest;
import org.gad.inventory_service.exception.ProviderAlreadyExistsException;
import org.gad.inventory_service.exception.ProviderNotFoundException;
import org.gad.inventory_service.model.Provider;
import org.gad.inventory_service.repository.ProviderRepository;
import org.gad.inventory_service.service.ProviderService;
import org.gad.inventory_service.utils.Constants;
import org.gad.inventory_service.utils.Mappers;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.Objects;

import static org.gad.inventory_service.utils.Constants.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {
    private final ProviderRepository providerRepository;

    @Override
    public Mono<ProviderDTO> findProviderById(String id) {
        return findProvider(providerRepository.findById(id),
                PROVIDER_NOT_FOUND_ID + id);
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
        return validateUniqueProviderFields(createProviderRequest.ruc(),
                createProviderRequest.dni(),
                createProviderRequest.email(),
                null)
                .then(Mono.defer(() -> {
                    Provider providerToSave = Provider.builder()
                            .name(createProviderRequest.name())
                            .ruc(createProviderRequest.ruc())
                            .dni(createProviderRequest.dni())
                            .address(createProviderRequest.address())
                            .phone(createProviderRequest.phone())
                            .email(createProviderRequest.email())
                            .build();

                    return providerRepository.save(providerToSave)
                            .map(Mappers::providerToDTO);
                }))
                .doOnError(error -> log.error(ERROR_SAVING_PROVIDER, error.getMessage()));
    }

    @Override
    public Mono<ProviderDTO> updateProvider(String id, UpdateProviderRequest updateProviderRequest) {
        Mono<Provider> providerMono = findById(id);

        return providerMono
                .flatMap(existingProvider -> validateUniqueProviderFields(
                                updateProviderRequest.ruc(),
                                updateProviderRequest.dni(),
                                updateProviderRequest.email(),
                                existingProvider.getIdProvider()
                        )
                                .then(Mono.defer(() -> {
                                    existingProvider.setName(updateProviderRequest.name());
                                    existingProvider.setRuc(updateProviderRequest.ruc());
                                    existingProvider.setDni(updateProviderRequest.dni());
                                    existingProvider.setAddress(updateProviderRequest.address());
                                    existingProvider.setPhone(updateProviderRequest.phone());
                                    existingProvider.setEmail(updateProviderRequest.email());

                                    return providerRepository.save(existingProvider);
                                }))
                )
                .map(Mappers::providerToDTO)
                .doOnError(error -> log.error(ERROR_UPDATING_PROVIDER, error.getMessage()));
    }

    @Override
    public Mono<Void> deleteProviderById(String id) {
        Mono<Provider> providerMono = findById(id);

        return providerMono
                .flatMap(provider -> providerRepository.deleteById(provider.getIdProvider()))
                .doOnError(error -> log.error(ERROR_DELETING_PROVIDER, error.getMessage()));
    }

    private Mono<Provider> findById(String id) {
        return providerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProviderNotFoundException(PROVIDER_NOT_FOUND_ID + id)))
                .doOnError(error -> log.error(Constants.ERROR_SEARCHING_PROVIDER, error.getMessage()));
    }

    private Mono<ProviderDTO> findProvider(Mono<Provider> providerMono, String errorMessage) {
        return providerMono
                .switchIfEmpty(Mono.error(new ProviderNotFoundException(errorMessage)))
                .map(Mappers::providerToDTO)
                .doOnError(error -> log.error(ERROR_SEARCHING_PROVIDER, error.getMessage()));
    }

    private Mono<Void> validateUniqueProviderFields(String ruc, String dni, String email, String excludeId) {
        Mono<Boolean> rucExists = providerRepository.findProviderByRuc(ruc)
                .filter(p -> !Objects.equals(p.getIdProvider(), excludeId))
                .hasElement();
        Mono<Boolean> dniExists = providerRepository.findProviderByDni(dni)
                .filter(p -> !Objects.equals(p.getIdProvider(), excludeId))
                .hasElement();
        Mono<Boolean> emailExists = providerRepository.findProviderByEmail(email)
                .filter(p -> !Objects.equals(p.getIdProvider(), excludeId))
                .hasElement();

        return Mono.zip(rucExists, dniExists, emailExists)
                .flatMap(tuple -> {
                    boolean rucExistsValue = tuple.getT1();
                    boolean dniExistsValue = tuple.getT2();
                    boolean emailExistsValue = tuple.getT3();

                    if (rucExistsValue || dniExistsValue || emailExistsValue) {
                        StringBuilder sb = new StringBuilder(TEXT_PROVIDER);
                        if (rucExistsValue) sb.append(TEXT_RUC_ALREADY_EXISTS);
                        if (dniExistsValue) sb.append(TEXT_DNI_ALREADY_EXISTS);
                        if (emailExistsValue) sb.append(TEXT_EMAIL_ALREADY_EXISTS);
                        String message = sb.substring(0, sb.length() - 2) + TEXT_ALREADY_EXISTS;
                        return Mono.error(new ProviderAlreadyExistsException(message));
                    }
                    return Mono.empty();
                });
    }
}
