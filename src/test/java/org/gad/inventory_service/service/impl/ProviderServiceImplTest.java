package org.gad.inventory_service.service.impl;

import org.gad.inventory_service.dto.request.CreateProviderRequest;
import org.gad.inventory_service.dto.request.UpdateProviderRequest;
import org.gad.inventory_service.exception.ProviderAlreadyExistsException;
import org.gad.inventory_service.exception.ProviderNotFoundException;
import org.gad.inventory_service.model.Provider;
import org.gad.inventory_service.repository.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.gad.inventory_service.utils.Constants.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class ProviderServiceImplTest {
    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private ProviderServiceImpl providerService;

    private Provider provider;
    private CreateProviderRequest createProviderRequest;
    private UpdateProviderRequest updateProviderRequest;

    @BeforeEach
    void setUp() {
        provider = Provider.builder()
                .idProvider("prov123")
                .name("TechProvider")
                .ruc("12345678901")
                .dni("12345678")
                .address("123 Tech Street")
                .phone("123456789")
                .email("test@gmail.com")
                .build();

        createProviderRequest = CreateProviderRequest.builder()
                .name("TechProvider")
                .ruc("12345678901")
                .dni("12345678")
                .address("123 Tech Street")
                .phone("123456789")
                .email("test@gmail.com")
                .build();

        updateProviderRequest = UpdateProviderRequest.builder()
                .name("TechProvider")
                .ruc("12345678901")
                .dni("12345678")
                .address("123 Tech Street")
                .phone("123456789")
                .email("testUpdate@gmail.com")
                .build();
    }

    @Test
    void findProviderById_ShouldReturnProviderDTO_WhenProviderExists() {
        when(providerRepository.findById(anyString()))
                .thenReturn(Mono.just(provider));

        StepVerifier.create(providerService.findProviderById("prov123"))
                .expectNextMatches(dto ->
                        dto.idProvider().equals("prov123") &&
                                dto.name().equals("TechProvider") &&
                                dto.address().equals("123 Tech Street") &&
                                dto.phone().equals("123456789"))
                .verifyComplete();

        verify(providerRepository, times(1)).findById(anyString());
    }

    @Test
    void findProviderById_ShouldThrow_WhenProviderDoesNotExist() {
        when(providerRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(providerService.findProviderById("non-existent-id"))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProviderNotFoundException &&
                                throwable.getMessage().equals(PROVIDER_NOT_FOUND_ID + "non-existent-id"))
                .verify();

        verify(providerRepository, times(1)).findById(anyString());
    }

    @Test
    void findProviderByRuc_ShouldReturnProviderDTO_WhenProviderExists() {
        when(providerRepository.findProviderByRuc(anyString()))
                .thenReturn(Mono.just(provider));

        StepVerifier.create(providerService.findProviderByRuc("12345678901"))
                .expectNextMatches(dto ->
                        dto.idProvider().equals("prov123") &&
                                dto.name().equals("TechProvider") &&
                                dto.address().equals("123 Tech Street") &&
                                dto.phone().equals("123456789"))
                .verifyComplete();

        verify(providerRepository, times(1)).findProviderByRuc(anyString());
    }

    @Test
    void findProviderByRuc_ShouldThrow_WhenProviderDoesNotExist() {
        when(providerRepository.findProviderByRuc(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(providerService.findProviderByRuc("non-existent-ruc"))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProviderNotFoundException &&
                                throwable.getMessage().equals(PROVIDER_NOT_FOUND_RUC + "non-existent-ruc"))
                .verify();

        verify(providerRepository, times(1)).findProviderByRuc(anyString());
    }

    @Test
    void findProviderByName_ShouldReturnProviderDTO_WhenProviderExists() {
        when(providerRepository.findProviderByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(provider));

        StepVerifier.create(providerService.findProviderByName("Tech"))
                .expectNextMatches(dto ->
                        dto.idProvider().equals("prov123") &&
                                dto.name().equals("TechProvider") &&
                                dto.address().equals("123 Tech Street") &&
                                dto.phone().equals("123456789"))
                .verifyComplete();

        verify(providerRepository, times(1)).findProviderByNameContainingIgnoreCase(anyString());
    }

    @Test
    void findProviderByName_ShouldThrow_WhenProviderDoesNotExist() {
        when(providerRepository.findProviderByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(providerService.findProviderByName("NonExistent"))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProviderNotFoundException &&
                                throwable.getMessage().equals(PROVIDER_NOT_FOUND_NAME + "NonExistent"))
                .verify();

        verify(providerRepository, times(1)).findProviderByNameContainingIgnoreCase(anyString());
    }

    @Test
    void findProviderByEmail_ShouldReturnProviderDTO_WhenProviderExists() {
        when(providerRepository.findProviderByEmail(anyString()))
                .thenReturn(Mono.just(provider));

        StepVerifier.create(providerService.findProviderByEmail("test@gmail.com"))
                .expectNextMatches(dto ->
                        dto.idProvider().equals("prov123") &&
                                dto.name().equals("TechProvider") &&
                                dto.address().equals("123 Tech Street") &&
                                dto.phone().equals("123456789"))
                .verifyComplete();
        verify(providerRepository, times(1)).findProviderByEmail(anyString());
    }

    @Test
    void findProviderByEmail_ShouldThrow_WhenProviderDoesNotExist() {
        when(providerRepository.findProviderByEmail(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(providerService.findProviderByEmail("testNonExist@@gmail.com"))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProviderNotFoundException &&
                                throwable.getMessage().equals(PROVIDER_NOT_FOUND_EMAIL + "testNonExist@@gmail.com"))
                .verify();
        verify(providerRepository, times(1)).findProviderByEmail(anyString());
    }

    @Test
    void findProviderByDni_ShouldReturnProviderDTO_WhenProviderExists() {
        when(providerRepository.findProviderByDni(anyString()))
                .thenReturn(Mono.just(provider));

        StepVerifier.create(providerService.findProviderByDni("12345678"))
                .expectNextMatches(dto ->
                        dto.idProvider().equals("prov123") &&
                                dto.name().equals("TechProvider") &&
                                dto.address().equals("123 Tech Street") &&
                                dto.phone().equals("123456789"))
                .verifyComplete();

        verify(providerRepository, times(1)).findProviderByDni(anyString());
    }

    @Test
    void findProviderByDni_ShouldThrow_WhenProviderDoesNotExist() {
        when(providerRepository.findProviderByDni(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(providerService.findProviderByDni("non-existent-dni"))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProviderNotFoundException &&
                                throwable.getMessage().equals(PROVIDER_NOT_FOUND_DNI + "non-existent-dni"))
                .verify();

        verify(providerRepository, times(1)).findProviderByDni(anyString());
    }

    @Test
    void findAllProviders_ShouldReturnFluxOfProviderDTO_WhenProvidersExist() {
        when(providerRepository.findAll())
                .thenReturn(Flux.just(provider));

        StepVerifier.create(providerService.findAllProviders())
                .expectNextMatches(dto ->
                        dto.idProvider().equals("prov123") &&
                                dto.name().equals("TechProvider") &&
                                dto.address().equals("123 Tech Street") &&
                                dto.phone().equals("123456789"))
                .verifyComplete();

        verify(providerRepository, times(1)).findAll();
    }

    @Test
    void findAllProviders_ShouldReturnEmptyFlux_WhenNoProvidersExist() {
        when(providerRepository.findAll())
                .thenReturn(Flux.empty());

        StepVerifier.create(providerService.findAllProviders())
                .expectErrorMatches(throwable ->
                        throwable instanceof ProviderNotFoundException &&
                                throwable.getMessage().equals(PROVIDER_NOT_FOUND_FLUX))
                .verify();

        verify(providerRepository, times(1)).findAll();
    }

    @Test
    void saveProvider_ShouldReturnProviderDTO_WhenProviderIsSaved() {
        when(providerRepository.findProviderByRuc(anyString()))
                .thenReturn(Mono.empty());
        when(providerRepository.findProviderByDni(anyString()))
                .thenReturn(Mono.empty());
        when(providerRepository.findProviderByEmail(anyString()))
                .thenReturn(Mono.empty());
        when(providerRepository.save(any(Provider.class)))
                .thenReturn(Mono.just(provider));

        StepVerifier.create(providerService.saveProvider(createProviderRequest))
                .expectNextMatches(dto ->
                        dto.idProvider().equals("prov123") &&
                                dto.name().equals("TechProvider") &&
                                dto.address().equals("123 Tech Street") &&
                                dto.phone().equals("123456789"))
                .verifyComplete();

        verify(providerRepository, times(1)).save(any(Provider.class));
    }

    @Test
    void saveProvider_ShouldThrow_WhenEmailAlreadyExists() {
        when(providerRepository.findProviderByRuc(anyString())).thenReturn(Mono.empty());
        when(providerRepository.findProviderByDni(anyString())).thenReturn(Mono.empty());
        when(providerRepository.findProviderByEmail(anyString())).thenReturn(Mono.just(provider));

        StepVerifier.create(providerService.saveProvider(createProviderRequest))
                .expectError(ProviderAlreadyExistsException.class)
                .verify();
    }

    @Test
    void updateProvider_ShouldReturnUpdatedProviderDTO_WhenProviderIsUpdated() {
        when(providerRepository.findById(anyString()))
                .thenReturn(Mono.just(provider));
        when(providerRepository.findProviderByRuc(anyString()))
                .thenReturn(Mono.just(provider));
        when(providerRepository.findProviderByDni(anyString()))
                .thenReturn(Mono.just(provider));
        when(providerRepository.findProviderByEmail(anyString()))
                .thenReturn(Mono.just(provider));
        when(providerRepository.save(any(Provider.class)))
                .thenReturn(Mono.just(provider));

        StepVerifier.create(providerService.updateProvider("prov123", updateProviderRequest))
                .expectNextMatches(dto ->
                        dto.idProvider().equals("prov123") &&
                                dto.name().equals("TechProvider") &&
                                dto.address().equals("123 Tech Street") &&
                                dto.phone().equals("123456789"))
                .verifyComplete();
        verify(providerRepository, times(1)).findById(anyString());
    }

    @Test
    void deleteProviderById_ShouldDeleteProvider_WhenProviderExists() {
        when(providerRepository.findById(anyString()))
                .thenReturn(Mono.just(provider));
        when(providerRepository.deleteById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(providerService.deleteProviderById("prov123"))
                .verifyComplete();

        verify(providerRepository, times(1)).findById(anyString());
        verify(providerRepository, times(1)).deleteById(anyString());
    }

    @Test
    void deleteProviderById_ShouldThrow_WhenProviderDoesNotExist() {
        when(providerRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(providerService.deleteProviderById("non-existent-id"))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProviderNotFoundException &&
                                throwable.getMessage().equals(PROVIDER_NOT_FOUND_ID + "non-existent-id"))
                .verify();

        verify(providerRepository, times(1)).findById(anyString());
        verify(providerRepository, never()).deleteById(anyString());
    }
}