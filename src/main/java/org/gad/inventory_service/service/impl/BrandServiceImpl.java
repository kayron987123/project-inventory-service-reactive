package org.gad.inventory_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gad.inventory_service.dto.BrandDTO;
import org.gad.inventory_service.dto.request.CreateBrandRequest;
import org.gad.inventory_service.dto.request.UpdateBrandRequest;
import org.gad.inventory_service.exception.BrandNotFoundException;
import org.gad.inventory_service.model.Brand;
import org.gad.inventory_service.repository.BrandRepository;
import org.gad.inventory_service.service.BrandService;
import org.gad.inventory_service.utils.Constants;
import org.gad.inventory_service.utils.Mappers;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.gad.inventory_service.utils.Constants.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public Mono<BrandDTO> findBrandById(String id) {
        return findBrand(findById(id),
                BRAND_NOT_FOUND_ID + id);
    }

    @Override
    public Mono<BrandDTO> findBrandByName(String name) {
        return findBrand(brandRepository.findBrandByNameContainingIgnoreCase(name),
                BRAND_NOT_FOUND_NAME + name);
    }

    @Override
    public Flux<BrandDTO> findAllBrands() {
        return brandRepository.findAll()
                .switchIfEmpty(Flux.error(new BrandNotFoundException(BRAND_NOT_FOUND_FLUX)))
                .map(Mappers::brandToDTO)
                .doOnError(error -> log.error(ERROR_SEARCHING_BRANDS, error.getMessage()));
    }

    @Override
    public Mono<BrandDTO> saveBrand(CreateBrandRequest createBrandRequest) {
        Brand brandToSave = Brand.builder()
                .name(createBrandRequest.brandName())
                .build();

        return brandRepository.save(brandToSave)
                .map(Mappers::brandToDTO)
                .doOnError(error -> log.error(ERROR_SAVING_BRAND, error.getMessage()));
    }

    @Override
    public Mono<BrandDTO> updateBrand(String uuid, UpdateBrandRequest updateBrandRequest) {
        Mono<Brand> brandMono = findById(uuid);

        return brandMono
                .flatMap(brand -> {
                    brand.setName(updateBrandRequest.brandName());
                    return brandRepository.save(brand);
                })
                .map(Mappers::brandToDTO)
                .doOnError(error -> log.error(ERROR_UPDATING_BRAND, error.getMessage()));
    }

    @Override
    public Mono<Void> deleteBrandById(String id) {
        return findById(id)
                .flatMap(brand -> brandRepository.deleteById(brand.getIdBrand()))
                .doOnError(error -> log.error(ERROR_DELETING_BRAND, error.getMessage()));
    }

    private Mono<Brand> findById(String id) {
        return brandRepository.findById(id)
                .switchIfEmpty(Mono.error(new BrandNotFoundException(BRAND_NOT_FOUND_ID + id)))
                .doOnError(error -> log.error(ERROR_SEARCHING_BRAND, error.getMessage()));
    }

    private Mono<BrandDTO> findBrand(Mono<Brand> brandMono, String errorMessage) {
        return brandMono
                .switchIfEmpty(Mono.error(new BrandNotFoundException(errorMessage)))
                .map(Mappers::brandToDTO)
                .doOnError(error -> log.error(Constants.ERROR_SEARCHING_BRAND, error.getMessage()));
    }
}
