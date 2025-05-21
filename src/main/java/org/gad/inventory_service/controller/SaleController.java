package org.gad.inventory_service.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.dto.request.CreateSaleRequest;
import org.gad.inventory_service.dto.request.UpdateSaleRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.service.SaleService;
import org.gad.inventory_service.utils.UtilsMethods;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.net.URI;

import static org.gad.inventory_service.utils.Constants.*;
import static org.gad.inventory_service.utils.UtilsMethods.datetimeNowFormatted;

@Validated
@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SaleController {
    private final SaleService saleService;

    @GetMapping
    public Mono<ResponseEntity<DataResponse>> getAllSales() {
        return saleService.getAllSales()
                .collectList()
                .map(sales -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("Sales retrieved successfully")
                                .data(sales)
                                .timestamp(datetimeNowFormatted())
                                .build()
                ));
    }

    @GetMapping("/name-product")
    public Mono<ResponseEntity<DataResponse>> getSalesByNameProduct(@RequestParam @NotBlank(message = MESSAGE_PRODUCT_EMPTY) String nameProduct) {
        return saleService.getSaleByNameProduct(nameProduct)
                .collectList()
                .map(sales -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("Sales retrieved successfully")
                                .data(sales)
                                .timestamp(datetimeNowFormatted())
                                .build()
                ));
    }

    @GetMapping("/price-range")
    public Mono<ResponseEntity<DataResponse>> getSalesByPriceRange(@RequestParam(required = false) BigDecimal minPrice,
                                                                   @RequestParam(required = false) BigDecimal maxPrice) {
        return saleService.getSaleByTotalPriceRange(minPrice, maxPrice)
                .collectList()
                .map(sales -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("Sales retrieved successfully")
                                .data(sales)
                                .timestamp(datetimeNowFormatted())
                                .build()
                ));
    }

    @GetMapping("/date-range")
    public Mono<ResponseEntity<DataResponse>> getSalesByDateRange(@RequestParam(required = false) String startDate,
                                                                  @RequestParam(required = false) String endDate) {
        return saleService.getSaleByDateRange(startDate, endDate)
                .collectList()
                .map(sales -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("Sales retrieved successfully")
                                .data(sales)
                                .timestamp(datetimeNowFormatted())
                                .build()
                ));
    }

    @GetMapping("/{uuid}")
    public Mono<ResponseEntity<DataResponse>> getSaleByUuid(@PathVariable @Pattern(regexp = REGEX_UUID, message = MESSAGE_INCORRECT_UUID_FORMAT) String uuid) {
        return saleService.getSaleByUuid(uuid)
                .map(sale -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("Sale retrieved successfully")
                                .data(sale)
                                .timestamp(datetimeNowFormatted())
                                .build()
                ));
    }

    @PostMapping
    public Mono<ResponseEntity<DataResponse>> createSale(@RequestBody @Validated CreateSaleRequest createSaleRequest) {
        return saleService.createSale(createSaleRequest)
                .map(sale -> {
                    URI location = UtilsMethods.createUri("/api/v1/sales", sale.uuidSale());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.CREATED.value())
                            .message("Sale created successfully")
                            .data(sale)
                            .timestamp(datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @PutMapping("/{uuid}")
    public Mono<ResponseEntity<DataResponse>> updateSale(@PathVariable @Pattern(regexp = REGEX_UUID, message = MESSAGE_INCORRECT_UUID_FORMAT) String uuid,
                                                         @RequestBody @Validated UpdateSaleRequest updateSaleRequest) {
        return saleService.updateSale(uuid, updateSaleRequest)
                .map(sale -> {
                    URI location = UtilsMethods.createUri("/api/v1/sales", sale.uuidSale());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.OK.value())
                            .message("Sale updated successfully")
                            .data(sale)
                            .timestamp(datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @DeleteMapping("/{uuid}")
    public Mono<ResponseEntity<Void>> deleteSale(@PathVariable @Pattern(regexp = REGEX_UUID, message = MESSAGE_INCORRECT_UUID_FORMAT) String uuid) {
        return saleService.deleteSale(uuid)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
