package org.gad.inventory_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.dto.request.CreateBrandRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.dto.request.UpdateBrandRequest;
import org.gad.inventory_service.service.BrandService;
import org.gad.inventory_service.utils.Constants;
import org.gad.inventory_service.utils.UtilsMethods;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

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
                .map(brands -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_BRANDS_OK)
                                .data(brands)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }

    @GetMapping("/search")
    public Mono<ResponseEntity<DataResponse>> findBrandByName(@RequestParam @NotBlank(message = Constants.MESSAGE_NAME_CANNOT_BE_EMPTY)
                                                              @Pattern(regexp = REGEX_ONLY_TEXT, message = MESSAGE_PARAMETER_NAME) String name) {
        return brandService.findBrandByName(name)
                .map(brand -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_BRAND_OK)
                                .data(brand)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> findBrandById(@PathVariable @Pattern(regexp = REGEX_ID, message = MESSAGE_INCORRECT_ID_FORMAT)
                                                            @NotBlank(message = MESSAGE_ID_CANNOT_BE_EMPTY) String id) {
        return brandService.findBrandById(id)
                .map(brand -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_BRAND_OK)
                                .data(brand)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }

    @PostMapping
    public Mono<ResponseEntity<DataResponse>> createBrand(@RequestBody @Valid CreateBrandRequest createBrandRequest) {
        return brandService.saveBrand(createBrandRequest)
                .map(brand -> {
                    URI location = UtilsMethods.createUri(BRAND_URI, brand.idBrand());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.CREATED.value())
                            .message(MESSAGE_BRAND_CREATED)
                            .data(brand)
                            .timestamp(UtilsMethods.datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> updateBrand(@PathVariable @Pattern(regexp = REGEX_ID, message = MESSAGE_INCORRECT_ID_FORMAT)
                                                          @NotBlank(message = MESSAGE_ID_CANNOT_BE_EMPTY) String id,
                                                          @RequestBody @Valid UpdateBrandRequest updateBrandRequest) {
        return brandService.updateBrand(id, updateBrandRequest)
                .map(brand -> {
                    URI location = UtilsMethods.createUri(BRAND_URI, brand.idBrand());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.CREATED.value())
                            .message(MESSAGE_BRAND_UPDATED)
                            .data(brand)
                            .timestamp(UtilsMethods.datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteBrandById(@PathVariable @Pattern(regexp = REGEX_ID, message = MESSAGE_INCORRECT_ID_FORMAT)
                                                      @NotBlank(message = MESSAGE_ID_CANNOT_BE_EMPTY) String id) {
        return brandService.deleteBrandById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
