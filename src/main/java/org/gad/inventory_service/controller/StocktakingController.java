package org.gad.inventory_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.dto.request.CreateStocktakingRequest;
import org.gad.inventory_service.dto.request.UpdateStocktakingRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.service.StocktakingService;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/stocktaking")
public class StocktakingController {
    private final StocktakingService stocktakingService;

    @GetMapping
    public Mono<ResponseEntity<DataResponse>> getAllStocktaking() {
        return stocktakingService.findAllStocktaking()
                .collectList()
                .map(stocktaking -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_STOCKTAKING_OK)
                                .data(stocktaking)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }

    @GetMapping("/date-range")
    public Mono<ResponseEntity<DataResponse>> getStocktakingByDateRange(@RequestParam(required = false)
                                                                        @Pattern(regexp = REGEX_DATE_OR_TIME, message = MESSAGE_INVALID_DATE_OR_FORMAT) String startDate,
                                                                        @RequestParam(required = false)
                                                                        @Pattern(regexp = REGEX_DATE_OR_TIME, message = MESSAGE_INVALID_DATE_OR_FORMAT) String endDate) {
        return stocktakingService.findAllStocktakingByDateBetween(startDate, endDate)
                .collectList()
                .map(stocktaking -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_STOCKTAKING_OK)
                                .data(stocktaking)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }

    @GetMapping("/product-name")
    public Mono<ResponseEntity<DataResponse>> getStocktakingByProductName(@RequestParam @NotBlank(message = MESSAGE_PRODUCT_EMPTY)
                                                                          @Pattern(regexp = REGEX_ONLY_TEXT, message = MESSAGE_PARAMETER_NAME) String productName) {
        return stocktakingService.findAllStocktakingByProductName(productName)
                .collectList()
                .map(stocktaking -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_STOCKTAKING_OK)
                                .data(stocktaking)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> getStocktakingById(@PathVariable @Pattern(regexp = REGEX_ID, message = MESSAGE_INCORRECT_ID_FORMAT)
                                                                 @NotBlank(message = MESSAGE_ID_CANNOT_BE_EMPTY) String id) {
        return stocktakingService.findStocktakingById(id)
                .map(stocktaking -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_STOCKTAKING_OK)
                                .data(stocktaking)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }

    @PostMapping
    public Mono<ResponseEntity<DataResponse>> createStocktaking(@RequestBody @Valid CreateStocktakingRequest createStocktakingRequest) {
        return stocktakingService.createStocktaking(createStocktakingRequest)
                .map(stocktaking -> {
                    URI location = UtilsMethods.createUri(STOCKTAKING_URI, stocktaking.idStocktaking());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.CREATED.value())
                            .message(MESSAGE_STOCKTAKING_CREATED)
                            .data(stocktaking)
                            .timestamp(UtilsMethods.datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> updateStocktaking(@PathVariable @Pattern(regexp = REGEX_ID, message = MESSAGE_INCORRECT_ID_FORMAT)
                                                                @NotBlank(message = MESSAGE_ID_CANNOT_BE_EMPTY) String id,
                                                                @RequestBody @Valid UpdateStocktakingRequest updateStocktakingRequest) {
        return stocktakingService.updateStocktaking(id, updateStocktakingRequest)
                .map(stocktaking -> {
                    URI location = UtilsMethods.createUri(STOCKTAKING_URI, stocktaking.idStocktaking());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.CREATED.value())
                            .message(MESSAGE_STOCKTAKING_UPDATED)
                            .data(stocktaking)
                            .timestamp(UtilsMethods.datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> deleteStocktakingById(@PathVariable @Pattern(regexp = REGEX_ID, message = MESSAGE_INCORRECT_ID_FORMAT)
                                                                    @NotBlank(message = MESSAGE_ID_CANNOT_BE_EMPTY) String id) {
        return stocktakingService.deleteStocktakingById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
