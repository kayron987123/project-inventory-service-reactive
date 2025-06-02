package org.gad.inventory_service.service.impl;

import org.gad.inventory_service.dto.request.CreateBrandRequest;
import org.gad.inventory_service.dto.request.UpdateBrandRequest;
import org.gad.inventory_service.exception.BrandNotFoundException;
import org.gad.inventory_service.model.Brand;
import org.gad.inventory_service.repository.BrandRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandServiceImplTest {
    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandServiceImpl brandService;

    Brand brand;
    CreateBrandRequest createBrandRequest;
    UpdateBrandRequest updateBrandRequest;

    @BeforeEach
    void setUp() {
        brand = Brand.builder()
                .idBrand("test-id")
                .name("Test Brand")
                .build();
        createBrandRequest = CreateBrandRequest
                .builder()
                .brandName("Create Test Brand")
                .build();
        updateBrandRequest = UpdateBrandRequest
                .builder()
                .brandName("Updated Test Brand")
                .build();
    }

    @Test
    void findBrandById_ShouldReturnBrandDTO_WhenBrandExists() {
        when(brandRepository.findById(anyString()))
                .thenReturn(Mono.just(brand));

        StepVerifier.create(brandService.findBrandById("test-id"))
                .expectNextMatches(dto ->
                        dto.idBrand().equals("test-id") &&
                                dto.name().equals("Test Brand"))
                .verifyComplete();

        verify(brandRepository, times(1)).findById(anyString());
    }

    @Test
    void findBrandById_ShouldThrow_WhenBrandDoesNotExist() {
        when(brandRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(brandService.findBrandById("non-existent-id"))
                .expectErrorMatches(throwable ->
                        throwable instanceof BrandNotFoundException &&
                                throwable.getMessage().equals(BRAND_NOT_FOUND_ID + "non-existent-id"))
                .verify();

        verify(brandRepository, times(1)).findById(anyString());
    }

    @Test
    void findBrandByName_ShouldReturnBrandDTO_WhenBrandExists() {
        when(brandRepository.findBrandByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(brand));

        StepVerifier.create(brandService.findBrandByName("Test"))
                .expectNextMatches(dto ->
                        dto.idBrand().equals("test-id") &&
                                dto.name().equals("Test Brand"))
                .verifyComplete();

        verify(brandRepository, times(1)).findBrandByNameContainingIgnoreCase(anyString());
    }

    @Test
    void findBrandByName_ShouldThrow_WhenBrandDoesNotExist() {
        when(brandRepository.findBrandByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(brandService.findBrandByName("NonExistent"))
                .expectErrorMatches(throwable ->
                        throwable instanceof BrandNotFoundException &&
                                throwable.getMessage().equals(BRAND_NOT_FOUND_NAME + "NonExistent"))
                .verify();

        verify(brandRepository, times(1)).findBrandByNameContainingIgnoreCase(anyString());
    }

    @Test
    void findAllBrands_ShouldReturnFluxOfBrandDTO_WhenBrandsExist() {
        when(brandRepository.findAll())
                .thenReturn(Flux.just(brand));

        StepVerifier.create(brandService.findAllBrands())
                .expectNextMatches(dto ->
                        dto.idBrand().equals("test-id") &&
                                dto.name().equals("Test Brand"))
                .verifyComplete();

        verify(brandRepository, times(1)).findAll();
    }

    @Test
    void findAllBrands_ShouldThrow_WhenNoBrandsExist() {
        when(brandRepository.findAll())
                .thenReturn(Flux.empty());

        StepVerifier.create(brandService.findAllBrands())
                .expectErrorMatches(throwable ->
                        throwable instanceof BrandNotFoundException &&
                                throwable.getMessage().equals(BRAND_NOT_FOUND_FLUX))
                .verify();

        verify(brandRepository, times(1)).findAll();
    }

    @Test
    void saveBrand_ShouldReturnBrandDTO_WhenBrandIsSaved() {
        when(brandRepository.save(any(Brand.class)))
                .thenReturn(Mono.just(brand));

        StepVerifier.create(brandService.saveBrand(createBrandRequest))
                .expectNextMatches(dto ->
                        dto.idBrand().equals("test-id") &&
                                dto.name().equals("Test Brand"))
                .verifyComplete();

        verify(brandRepository, times(1)).save(any(Brand.class));
    }

    @Test
    void updateBrand_ShouldReturnUpdatedBrandDTO_WhenBrandIsUpdated() {
        when(brandRepository.findById(anyString()))
                .thenReturn(Mono.just(brand));
        when(brandRepository.save(any(Brand.class)))
                .thenReturn(Mono.just(brand));

        StepVerifier.create(brandService.updateBrand("test-id", updateBrandRequest))
                .expectNextMatches(dto ->
                        dto.idBrand().equals("test-id") &&
                                dto.name().equals("Updated Test Brand"))
                .verifyComplete();

        verify(brandRepository, times(1)).findById(anyString());
        verify(brandRepository, times(1)).save(any(Brand.class));
    }

    @Test
    void updateBrand_ShouldThrow_WhenBrandDoesNotExist() {
        when(brandRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(brandService.updateBrand("non-existent-id", updateBrandRequest))
                .expectErrorMatches(throwable ->
                        throwable instanceof BrandNotFoundException &&
                                throwable.getMessage().equals(BRAND_NOT_FOUND_ID + "non-existent-id"))
                .verify();

        verify(brandRepository, times(1)).findById(anyString());
        verify(brandRepository, never()).save(any(Brand.class));
    }

    @Test
    void deleteBrandById_ShouldReturnMonoVoid_WhenBrandIsDeleted() {
        when(brandRepository.findById(anyString()))
                .thenReturn(Mono.just(brand));
        when(brandRepository.deleteById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(brandService.deleteBrandById("test-id"))
                .verifyComplete();

        verify(brandRepository, times(1)).findById(anyString());
        verify(brandRepository, times(1)).deleteById(anyString());
    }

    @Test
    void deleteBrandById_ShouldThrow_WhenBrandDoesNotExist() {
        when(brandRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(brandService.deleteBrandById("non-existent-id"))
                .expectErrorMatches(throwable ->
                        throwable instanceof BrandNotFoundException &&
                                throwable.getMessage().equals(BRAND_NOT_FOUND_ID + "non-existent-id"))
                .verify();

        verify(brandRepository, times(1)).findById(anyString());
        verify(brandRepository, never()).deleteById(anyString());
    }
}