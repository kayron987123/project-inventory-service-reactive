package org.gad.inventory_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.dto.CreateBrandRequest;
import org.gad.inventory_service.dto.DataResponse;
import org.gad.inventory_service.dto.UpdateBrandRequest;
import org.gad.inventory_service.service.BrandService;
import org.gad.inventory_service.utils.UtilsMethods;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.gad.inventory_service.utils.Constants.*;

@Validated
@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    public Mono<ResponseEntity<DataResponse>> findAllBrands() {
        return brandService.findAllBrands()
                .collectList()
                .map(brands -> DataResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message(MESSAGE_BRANDS_OK)
                        .data(brands)
                        .timestamp(UtilsMethods.datetimeNowFormatted())
                        .build())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/name")
    public Mono<ResponseEntity<DataResponse>> findBrandByName(@RequestParam @NotBlank(message = "Name cannot be empty") String name) {
        return brandService.findBrandByName(name)
                .map(brand -> DataResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message(MESSAGE_BRAND_OK)
                        .data(brand)
                        .timestamp(UtilsMethods.datetimeNowFormatted())
                        .build())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{uuid}")
    public Mono<ResponseEntity<DataResponse>> findBrandByUuid(@PathVariable @Pattern(regexp = REGEX_UUID, message = MESSAGE_INCORRECT_UUID_FORMAT) String uuid) {
        return brandService.findByUuid(uuid)
                .map(brand -> DataResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message(MESSAGE_BRAND_OK)
                        .data(brand)
                        .timestamp(UtilsMethods.datetimeNowFormatted())
                        .build())
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<DataResponse>> createBrand(@RequestBody @Valid CreateBrandRequest createBrandRequest) {
        return brandService.saveBrand(createBrandRequest)
                .map(brand -> DataResponse.builder()
                        .status(HttpStatus.CREATED.value())
                        .message(MESSAGE_BRAND_CREATED)
                        .data(brand)
                        .timestamp(UtilsMethods.datetimeNowFormatted())
                        .build())
                .map(dataResponse -> ResponseEntity.status(HttpStatus.CREATED).body(dataResponse));
    }

    @PutMapping("/{uuid}")
    public Mono<ResponseEntity<DataResponse>> updateBrand(@PathVariable @Pattern(regexp = REGEX_UUID, message = MESSAGE_INCORRECT_UUID_FORMAT) String uuid,
                                                          @RequestBody @Valid UpdateBrandRequest updateBrandRequest) {
        return brandService.updateBrand(uuid, updateBrandRequest)
                .map(brand -> DataResponse.builder()
                        .status(HttpStatus.CREATED.value())
                        .message(MESSAGE_BRAND_UPDATED)
                        .data(brand)
                        .timestamp(UtilsMethods.datetimeNowFormatted())
                        .build())
                .map(dataResponse -> ResponseEntity.status(HttpStatus.CREATED).body(dataResponse));
    }

    @PutMapping("/{uuid}")
    public Mono<ResponseEntity<Void>> deleteBrand(@PathVariable @Pattern(regexp = REGEX_UUID, message = MESSAGE_INCORRECT_UUID_FORMAT) String uuid) {
        return brandService.deleteBrandByUuid(uuid)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
