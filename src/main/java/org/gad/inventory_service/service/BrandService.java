package org.gad.inventory_service.service;

import org.gad.inventory_service.dto.BrandDTO;
import org.gad.inventory_service.dto.CreateBrandRequest;
import org.gad.inventory_service.dto.UpdateBrandRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BrandService {
    Mono<BrandDTO> findByUuid(String uuid);
    Mono<BrandDTO> findBrandByName(String name);
    Flux<BrandDTO> findAllBrands();
    Mono<BrandDTO> saveBrand(CreateBrandRequest createBrandRequest);
    Mono<BrandDTO> updateBrand(String uuid, UpdateBrandRequest updateBrandRequest);
    Mono<Void> deleteBrandByUuid(String uuid);
}
