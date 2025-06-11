package org.gad.inventory_service.controller;

import org.gad.inventory_service.TestConfig;
import org.gad.inventory_service.dto.ProviderDTO;
import org.gad.inventory_service.dto.request.CreateProviderRequest;
import org.gad.inventory_service.dto.request.UpdateProviderRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.dto.response.ErrorResponse;
import org.gad.inventory_service.exception.ProviderNotFoundException;
import org.gad.inventory_service.service.ProviderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.gad.inventory_service.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(ProviderController.class)
@Import(TestConfig.class)
@WithMockUser
class ProviderControllerTest {
    @Autowired
    private ProviderService providerService;

    @Autowired
    private WebTestClient webTestClient;

    private final String idProvider = "665c2e2f8b3e2a6b7c8d9e0f";
    private final String ruc = "12345678901";
    private final String dni = "12345678";
    private final String email = "provider@gmail.com";
    private Flux<ProviderDTO> providers;
    private Mono<ProviderDTO> provider;
    private CreateProviderRequest createProviderRequest;
    private UpdateProviderRequest updateProviderRequest;

    @BeforeEach
    void setUp() {
        providers = Flux.just(
                ProviderDTO.builder()
                        .name("Provider 1")
                        .address("123 Main St")
                        .build(),
                ProviderDTO.builder()
                        .name("Provider 2")
                        .address("456 Elm St")
                        .build()
        );
        provider = Mono.just(
                ProviderDTO.builder()
                        .name("Provider 1")
                        .address("123 Main St")
                        .build()
        );
        createProviderRequest = CreateProviderRequest.builder()
                .name("New Provider")
                .ruc("12345678901")
                .dni("12345678")
                .address("789 Oak St")
                .phone("987654321")
                .email("providercreate@gmail.com")
                .build();
        updateProviderRequest = UpdateProviderRequest.builder()
                .name("Updated Provider")
                .ruc("10987654321")
                .dni("87654321")
                .address("321 Pine St")
                .phone("123456789")
                .email("providerupdate@gmail.com")
                .build();
        Mockito.reset(providerService);
    }

    @Test
    void findProviderById_ShouldReturnProviderAndStatus200_WhenProviderExists() {
        when(providerService.findProviderById(anyString()))
                .thenReturn(provider);

        webTestClient.get()
                .uri("/api/v1/providers/{id}", idProvider)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Provider retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void findProviderById_ShouldThrowAndReturnStatus404_WhenProviderDoesNotExist() {
        when(providerService.findProviderById(anyString()))
                .thenThrow(new ProviderNotFoundException(PROVIDER_NOT_FOUND_ID + "665c2e2f8b3e2a6b7c8d9e0f"));

        webTestClient.get()
                .uri("/api/v1/providers/{id}", "665c2e2f8b3e2a6b7c8d9e0f")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Provider not found with id: 665c2e2f8b3e2a6b7c8d9e0f", errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void findProviderByRuc_ShouldReturnProviderAndStatus200_WhenProviderExists() {
        when(providerService.findProviderByRuc(anyString()))
                .thenReturn(provider);

        webTestClient.get()
                .uri("/api/v1/providers/ruc/{ruc}", ruc)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Provider retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void findProviderByRuc_ShouldThrowAndReturnStatus404_WhenProviderDoesNotExist() {
        when(providerService.findProviderByRuc(anyString()))
                .thenThrow(new ProviderNotFoundException(PROVIDER_NOT_FOUND_RUC + "12345678901"));

        webTestClient.get()
                .uri("/api/v1/providers/ruc/{ruc}", "12345678901")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Provider not found with ruc: 12345678901", errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void findProviderByName_ShouldReturnProviderAndStatus200_WhenProviderExists() {
        when(providerService.findProviderByName(anyString()))
                .thenReturn(provider);

        webTestClient.get()
                .uri("/api/v1/providers/name/{name}", "Provider")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Provider retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void findProviderByName_ShouldThrowAndReturnStatus404_WhenProviderDoesNotExist() {
        when(providerService.findProviderByName(anyString()))
                .thenThrow(new ProviderNotFoundException(PROVIDER_NOT_FOUND_NAME + "Provider"));

        webTestClient.get()
                .uri("/api/v1/providers/name/{name}", "Provider")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Provider not found with name : Provider", errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void findProviderByEmail_ShouldReturnProviderAndStatus200_WhenProviderExists() {
        when(providerService.findProviderByEmail(anyString()))
                .thenReturn(provider);

        webTestClient.get()
                .uri("/api/v1/providers/email/{email}", email)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Provider retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void findProviderByEmail_ShouldThrowAndReturnStatus404_WhenProviderDoesNotExist() {
        when(providerService.findProviderByEmail(anyString()))
                .thenThrow(new ProviderNotFoundException(PROVIDER_NOT_FOUND_EMAIL + email));

        webTestClient.get()
                .uri("/api/v1/providers/email/{email}", email)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Provider not found with email: " + email, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void findProviderByDni_ShouldReturnProviderAndStatus200_WhenProviderExists() {
        when(providerService.findProviderByDni(anyString()))
                .thenReturn(provider);

        webTestClient.get()
                .uri("/api/v1/providers/dni/{dni}", dni)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Provider retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void findProviderByDni_ShouldThrowAndReturnStatus404_WhenProviderDoesNotExist() {
        when(providerService.findProviderByDni(anyString()))
                .thenThrow(new ProviderNotFoundException(PROVIDER_NOT_FOUND_DNI + dni));

        webTestClient.get()
                .uri("/api/v1/providers/dni/{dni}", dni)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Provider not found with dni: " + dni, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void findAllProviders_ShouldReturnAllProviderAndStatus200_WhenProvidersExist() {
        when(providerService.findAllProviders())
                .thenReturn(providers);

        webTestClient.get()
                .uri("/api/v1/providers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(200, dataResponse.status());
                    assertEquals("Providers retrieved successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void findAllProviders_ShouldThrowAndReturnStatus404_WhenNoProvidersExist() {
        when(providerService.findAllProviders())
                .thenReturn(Flux.error(new ProviderNotFoundException(PROVIDER_NOT_FOUND_FLUX)));

        webTestClient.get()
                .uri("/api/v1/providers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("No providers found", errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void createProvider_ShouldReturnCreatedProviderAndStatus201_WhenProviderIsCreated() {
        when(providerService.saveProvider(any(CreateProviderRequest.class)))
                .thenReturn(provider);

        webTestClient.post()
                .uri("/api/v1/providers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createProviderRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(201, dataResponse.status());
                    assertEquals("Provider created successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void updateProvider_ShouldReturnUpdatedProviderAndStatus201_WhenProviderIsUpdated() {
        when(providerService.updateProvider(anyString(), any(UpdateProviderRequest.class)))
                .thenReturn(provider);

        webTestClient.put()
                .uri("/api/v1/providers/{id}", idProvider)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateProviderRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(DataResponse.class)
                .value(dataResponse -> {
                    assertNotNull(dataResponse);
                    assertEquals(201, dataResponse.status());
                    assertEquals("Provider updated successfully", dataResponse.message());
                    assertNotNull(dataResponse.data());
                    assertNotNull(dataResponse.timestamp());
                });
    }

    @Test
    void updateProvider_ShouldThrowAndReturnStatus404_WhenProviderDoesNotExist() {
        when(providerService.updateProvider(anyString(), any(UpdateProviderRequest.class)))
                .thenThrow(new ProviderNotFoundException(PROVIDER_NOT_FOUND_ID + idProvider));

        webTestClient.put()
                .uri("/api/v1/providers/{id}", idProvider)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateProviderRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Provider not found with id: " + idProvider, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }

    @Test
    void deleteProviderById_ShouldReturnVoidAndStatus204_WhenProviderIsDeleted() {
        when(providerService.deleteProviderById(anyString()))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/providers/{id}", idProvider)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class)
                .consumeWith(response -> assertNull(response.getResponseBody()));
    }

    @Test
    void deleteProviderById_ShouldThrowAndReturnStatus404_WhenProviderDoesNotExist() {
        when(providerService.deleteProviderById(anyString()))
                .thenThrow(new ProviderNotFoundException(PROVIDER_NOT_FOUND_ID + idProvider));

        webTestClient.delete()
                .uri("/api/v1/providers/{id}", idProvider)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertNotNull(errorResponse);
                    assertEquals(404, errorResponse.status());
                    assertEquals("Provider not found with id: " + idProvider, errorResponse.message());
                    assertNull(errorResponse.errors());
                    assertNotNull(errorResponse.timestamp());
                    assertNotNull(errorResponse.path());
                });
    }
}