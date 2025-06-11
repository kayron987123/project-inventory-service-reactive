package org.gad.inventory_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.dto.request.CreateSaleRequest;
import org.gad.inventory_service.dto.request.UpdateSaleRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.service.ExcelReportService;
import org.gad.inventory_service.service.SaleService;
import org.gad.inventory_service.utils.UtilsMethods;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
    private final ExcelReportService excelReportService;

    @GetMapping
    public Mono<ResponseEntity<DataResponse>> getAllSales() {
        return saleService.getAllSales()
                .collectList()
                .map(sales -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_SALES_OK)
                                .data(sales)
                                .timestamp(datetimeNowFormatted())
                                .build()
                ));
    }

    @GetMapping("/name-product")
    public Mono<ResponseEntity<DataResponse>> getSalesByNameProduct(@RequestParam @NotBlank(message = MESSAGE_PRODUCT_EMPTY)
                                                                    @Pattern(regexp = REGEX_ONLY_TEXT, message = MESSAGE_PARAMETER_NAME) String nameProduct) {
        return saleService.getSaleByNameProduct(nameProduct)
                .collectList()
                .map(sales -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_SALES_OK)
                                .data(sales)
                                .timestamp(datetimeNowFormatted())
                                .build()
                ));
    }

    @GetMapping("/price-range")
    public Mono<ResponseEntity<DataResponse>> getSalesByPriceRange(@RequestParam(required = false)
                                                                   @DecimalMin("0.00") BigDecimal minPrice,
                                                                   @RequestParam(required = false)
                                                                   @DecimalMax("100000001.00") BigDecimal maxPrice) {
        return saleService.getSaleByTotalPriceRange(minPrice, maxPrice)
                .collectList()
                .map(sales -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_SALES_OK)
                                .data(sales)
                                .timestamp(datetimeNowFormatted())
                                .build()
                ));
    }

    @GetMapping("/date-range")
    public Mono<ResponseEntity<DataResponse>> getSalesByDateRange(@RequestParam(required = false)
                                                                  @Pattern(regexp = REGEX_DATE_OR_TIME, message = MESSAGE_INVALID_DATE_OR_FORMAT) String startDate,
                                                                  @RequestParam(required = false)
                                                                  @Pattern(regexp = REGEX_DATE_OR_TIME, message = MESSAGE_INVALID_DATE_OR_FORMAT) String endDate) {
        return saleService.getSaleByDateRange(startDate, endDate)
                .collectList()
                .map(sales -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_SALES_OK)
                                .data(sales)
                                .timestamp(datetimeNowFormatted())
                                .build()
                ));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> getSaleById(@PathVariable @Pattern(regexp = REGEX_ID, message = MESSAGE_INCORRECT_ID_FORMAT)
                                                          @NotBlank(message = MESSAGE_ID_CANNOT_BE_EMPTY) String id) {
        return saleService.getSaleById(id)
                .map(sale -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_SALE_OK)
                                .data(sale)
                                .timestamp(datetimeNowFormatted())
                                .build()
                ));
    }

    @GetMapping("/excel")
    public Mono<ResponseEntity<ByteArrayResource>> generateExcelReport(@RequestParam(required = false)
                                                                       @Pattern(regexp = REGEX_DATE_OR_TIME, message = MESSAGE_INVALID_DATE_OR_FORMAT) String startDate,
                                                                       @RequestParam(required = false)
                                                                       @Pattern(regexp = REGEX_DATE_OR_TIME, message = MESSAGE_INVALID_DATE_OR_FORMAT) String endDate) {
        return saleService.getSaleByDateRange(startDate, endDate)
                .collectList()
                .flatMap(excelReportService::generateSalesReport)
                .publishOn(Schedulers.boundedElastic())
                .map(excelBytes -> {
                    ByteArrayResource resource = new ByteArrayResource(excelBytes);
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, CONTENT_DISPOSITION_EXCEL)
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .contentLength(excelBytes.length)
                            .body(resource);
                });
    }

    @PostMapping
    public Mono<ResponseEntity<DataResponse>> createSale(@RequestBody @Valid CreateSaleRequest createSaleRequest) {
        return saleService.createSale(createSaleRequest)
                .map(sale -> {
                    URI location = UtilsMethods.createUri(SALE_URI, sale.idSale());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.CREATED.value())
                            .message(MESSAGE_SALE_CREATED)
                            .data(sale)
                            .timestamp(datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> updateSale(@PathVariable @Pattern(regexp = REGEX_ID, message = MESSAGE_INCORRECT_ID_FORMAT)
                                                         @NotBlank(message = MESSAGE_ID_CANNOT_BE_EMPTY) String id,
                                                         @RequestBody @Valid UpdateSaleRequest updateSaleRequest) {
        return saleService.updateSale(id, updateSaleRequest)
                .map(sale -> {
                    URI location = UtilsMethods.createUri(SALE_URI, sale.idSale());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.OK.value())
                            .message(MESSAGE_SALE_UPDATED)
                            .data(sale)
                            .timestamp(datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSaleById(@PathVariable @Pattern(regexp = REGEX_ID, message = MESSAGE_INCORRECT_ID_FORMAT)
                                                     @NotBlank(message = MESSAGE_ID_CANNOT_BE_EMPTY) String id) {
        return saleService.deleteSaleById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
