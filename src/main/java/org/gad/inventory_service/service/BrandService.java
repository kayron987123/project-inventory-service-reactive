package org.gad.inventory_service.service;

import org.gad.inventory_service.dto.BrandDTO;
import org.gad.inventory_service.dto.request.CreateBrandRequest;
import org.gad.inventory_service.dto.request.UpdateBrandRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BrandService {
    Mono<BrandDTO> findBrandById(String id);
    Mono<BrandDTO> findBrandByName(String name);
    Flux<BrandDTO> findAllBrands();
    Mono<BrandDTO> saveBrand(CreateBrandRequest createBrandRequest);
    Mono<BrandDTO> updateBrand(String id, UpdateBrandRequest updateBrandRequest);
    Mono<Void> deleteBrandById(String id);
}
