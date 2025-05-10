package org.gad.inventory_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gad.inventory_service.dto.BrandDTO;
import org.gad.inventory_service.dto.CreateBrandRequest;
import org.gad.inventory_service.dto.UpdateBrandRequest;
import org.gad.inventory_service.exception.BrandNotFoundException;
import org.gad.inventory_service.model.Brand;
import org.gad.inventory_service.repository.BrandRepository;
import org.gad.inventory_service.service.BrandService;
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
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public Mono<BrandDTO> findByUuid(String uuid) {
        return brandRepository.findById(UtilsMethods.convertStringToUUID(uuid))
                .switchIfEmpty(Mono.error(new BrandNotFoundException(BRAND_NOT_FOUND_UUID + uuid)))
                .map(Mappers::toDTO)
                .doOnError(error -> log.error(Constants.ERROR_SEARCHING_BRAND, error.getMessage()));
    }

    @Override
    public Mono<BrandDTO> findBrandByName(String name) {
        return brandRepository.findBrandByNameContainingIgnoreCase(name)
                .switchIfEmpty(Mono.error(new BrandNotFoundException(BRAND_NOT_FOUND_NAME + name)))
                .map(Mappers::toDTO)
                .doOnError(error -> log.error(Constants.ERROR_SEARCHING_BRAND, error.getMessage()));
    }

    @Override
    public Flux<BrandDTO> findAllBrands() {
        return brandRepository.findAll()
                .switchIfEmpty(Flux.error(new BrandNotFoundException(BRAND_NOT_FOUND_FLUX)))
                .map(Mappers::toDTO)
                .doOnError(error -> log.error(ERROR_SEARCHING_BRANDS, error.getMessage()));
    }

    @Override
    public Mono<BrandDTO> saveBrand(CreateBrandRequest createBrandRequest) {
        Brand brandToSave = new Brand();
        brandToSave.setIdBrand(UtilsMethods.generateUUID());
        brandToSave.setName(createBrandRequest.brandName());

        return brandRepository.save(brandToSave)
                .map(Mappers::toDTO)
                .doOnError(error -> log.error(ERROR_SAVING_BRAND, error.getMessage()));
    }

    @Override
    public Mono<BrandDTO> updateBrand(String uuid, UpdateBrandRequest updateBrandRequest) {
        Mono<Brand> brandMono = findBrandByUuidString(uuid);

        return brandMono
                .flatMap(brand -> {
                    brand.setName(updateBrandRequest.brandName());
                    return brandRepository.save(brand);
                })
                .map(Mappers::toDTO)
                .doOnError(error -> log.error(ERROR_UPDATING_BRAND, error.getMessage()));
    }

    @Override
    public Mono<Void> deleteBrandByUuid(String uuid) {
        Mono<Brand> brandMono = findBrandByUuidString(uuid);

        return brandMono
                .flatMap(brandRepository::delete)
                .doOnError(error -> log.error(ERROR_DELETING_BRAND, error.getMessage()));
    }

    private Mono<Brand> findBrandByUuidString(String uuid){
        return brandRepository.findById(UtilsMethods.convertStringToUUID(uuid))
                .switchIfEmpty(Mono.error(new BrandNotFoundException(BRAND_NOT_FOUND_UUID + uuid)))
                .doOnError(error -> log.error(Constants.ERROR_SEARCHING_BRAND, error.getMessage()));
    }
}
