package org.gad.inventory_service.controller;

import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.dto.request.CreateStocktakingRequest;
import org.gad.inventory_service.dto.request.UpdateStocktakingRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.service.StocktakingService;
import org.gad.inventory_service.utils.UtilsMethods;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.gad.inventory_service.utils.Constants.*;

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
                                .build()));
    }

    @GetMapping("/date-range")
    public Mono<ResponseEntity<DataResponse>> getStocktakingByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        return stocktakingService.findAllStocktakingByDateBetween(startDate, endDate)
                .collectList()
                .map(stocktaking -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_STOCKTAKING_OK)
                                .data(stocktaking)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build()));
    }

    @GetMapping("/product-name")
    public Mono<ResponseEntity<DataResponse>> getStocktakingByProductName(@RequestParam String productName) {
        return stocktakingService.findAllStocktakingByProductName(productName)
                .collectList()
                .map(stocktaking -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_STOCKTAKING_OK)
                                .data(stocktaking)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build()));
    }

    @GetMapping("/{uuid}")
    public Mono<ResponseEntity<DataResponse>> getStocktakingByUuid(@PathVariable String uuid) {
        return stocktakingService.findStocktakingByUuid(uuid)
                .map(stocktaking -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_STOCKTAKING_OK)
                                .data(stocktaking)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build()));
    }

    @PostMapping
    public Mono<ResponseEntity<DataResponse>> createStocktaking(@RequestBody CreateStocktakingRequest createStocktakingRequest) {
        return stocktakingService.createStocktaking(createStocktakingRequest)
                .map(stocktaking -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(DataResponse.builder()
                                .status(HttpStatus.CREATED.value())
                                .message(MESSAGE_STOCKTAKING_CREATED)
                                .data(stocktaking)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build()));
    }

    @PutMapping("/{uuid}")
    public Mono<ResponseEntity<DataResponse>> updateStocktaking(@PathVariable String uuid, @RequestBody UpdateStocktakingRequest updateStocktakingRequest) {
        return stocktakingService.updateStocktaking(uuid, updateStocktakingRequest)
                .map(stocktaking -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_STOCKTAKING_UPDATED)
                                .data(stocktaking)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build()));
    }

    @DeleteMapping("/{uuid}")
    public Mono<ResponseEntity<DataResponse>> deleteStocktaking(@PathVariable String uuid) {
        return stocktakingService.deleteStocktaking(uuid)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
