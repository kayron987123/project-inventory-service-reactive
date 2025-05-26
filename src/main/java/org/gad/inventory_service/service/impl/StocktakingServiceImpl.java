package org.gad.inventory_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gad.inventory_service.dto.StocktakingDTO;
import org.gad.inventory_service.dto.request.CreateStocktakingRequest;
import org.gad.inventory_service.dto.request.UpdateStocktakingRequest;
import org.gad.inventory_service.exception.InvalidDateRangeException;
import org.gad.inventory_service.exception.ProductNotFoundException;
import org.gad.inventory_service.exception.StockTakingNotFoundException;
import org.gad.inventory_service.model.Product;
import org.gad.inventory_service.model.Stocktaking;
import org.gad.inventory_service.repository.ProductRepository;
import org.gad.inventory_service.repository.StocktakingRepository;
import org.gad.inventory_service.service.StocktakingService;
import org.gad.inventory_service.utils.Constants;
import org.gad.inventory_service.utils.Mappers;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.gad.inventory_service.utils.Constants.*;
import static org.gad.inventory_service.utils.UtilsMethods.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class StocktakingServiceImpl implements StocktakingService {
    private final StocktakingRepository stocktakingRepository;
    private final ProductRepository productRepository;

    @Override
    public Flux<StocktakingDTO> findAllStocktaking() {
        return findStocktaking(stocktakingRepository.findAll(), STOCKTAKING_NOT_FOUND);
    }

    @Override
    public Flux<StocktakingDTO> findAllStocktakingByDateBetween(String dateStart, String dateEnd) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (dateStart != null) {
            startDate = parseFlexibleDateStart(dateStart);
        }
        if (dateEnd != null) {
            endDate = parseFlexibleDateEnd(dateEnd);
        }

        if (startDate != null && endDate != null) {
            return validateStocktakingDates(startDate, endDate)
                    .thenMany(stocktakingRepository.findByOptionalDateRange(startDate, endDate))
                    .switchIfEmpty(Mono.error(new StockTakingNotFoundException(STOCKTAKING_NOT_FOUND_BETWEEEN_DATES + dateStart + " and " + dateEnd)))
                    .flatMap(stocktaking -> {
                        Mono<Product> productMono = productRepository.findById(stocktaking.getProductId())
                                .switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_NOT_FOUND_UUID + stocktaking.getProductId())));
                        return productMono.map(product -> Mappers.stocktakingToDTO(stocktaking, product.getName()));
                    })
                    .doOnError(error -> log.error(ERROR_SEARCHING_STOCKTAKING, error.getMessage()));
        } else {
            return stocktakingRepository.findByOptionalDateRange(startDate, endDate)
                    .switchIfEmpty(Mono.error(new StockTakingNotFoundException(STOCKTAKING_NOT_FOUND_BETWEEEN_DATES + dateStart + " and " + dateEnd)))
                    .flatMap(stocktaking -> {
                        Mono<Product> productMono = productRepository.findById(stocktaking.getProductId())
                                .switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_NOT_FOUND_UUID + stocktaking.getProductId())));
                        return productMono.map(product -> Mappers.stocktakingToDTO(stocktaking, product.getName()));
                    })
                    .doOnError(error -> log.error(ERROR_SEARCHING_STOCKTAKING, error.getMessage()));
        }
    }

    @Override
    public Flux<StocktakingDTO> findAllStocktakingByProductName(String productName) {
        return productRepository.findProductByNameContainingIgnoreCase(productName)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(STOCKTAKING_NOT_FOUND_NAME + productName)))
                .flatMapMany(product -> findStocktaking(stocktakingRepository.findStocktakingByProductId(product.getIdProduct()),
                        STOCKTAKING_NOT_FOUND_NAME + productName));
    }

    @Override
    public Mono<StocktakingDTO> findStocktakingById(String id) {
        return findStocktaking(stocktakingRepository.findById(id),
                STOCKTAKING_NOT_FOUND_UUID + id);
    }

    @Override
    public Mono<StocktakingDTO> createStocktaking(CreateStocktakingRequest createStocktakingDTO) {
        return productRepository.findProductByNameContainingIgnoreCase(createStocktakingDTO.productName())
                .switchIfEmpty(Mono.error(new ProductNotFoundException(STOCKTAKING_NOT_FOUND_NAME + createStocktakingDTO.productName())))
                .flatMap(product -> {
                    Stocktaking stocktakingToSave = Stocktaking.builder()
                            .productId(product.getIdProduct())
                            .quantity(createStocktakingDTO.quantity())
                            .stocktakingDate(LocalDateTime.now())
                            .build();
                    return stocktakingRepository.save(stocktakingToSave)
                            .map(savedStocktaking -> Mappers.stocktakingToDTO(savedStocktaking, product.getName()));
                })
                .doOnError(error -> log.error(ERROR_CREATING_STOCKTAKING, error.getMessage()));
    }

    @Override
    public Mono<StocktakingDTO> updateStocktaking(String id, UpdateStocktakingRequest updateStocktakingRequest) {
        return stocktakingRepository.findById(id)
                .switchIfEmpty(Mono.error(new StockTakingNotFoundException(STOCKTAKING_NOT_FOUND_UUID + id)))
                .flatMap(stocktaking ->
                        productRepository.findProductByNameContainingIgnoreCase(updateStocktakingRequest.productName())
                                .switchIfEmpty(Mono.error(new ProductNotFoundException(STOCKTAKING_NOT_FOUND_NAME + updateStocktakingRequest.productName())))
                                .flatMap(product -> {
                                    stocktaking.setProductId(product.getIdProduct());
                                    stocktaking.setQuantity(updateStocktakingRequest.quantity());
                                    return stocktakingRepository.save(stocktaking)
                                            .map(savedStocktaking -> Mappers.stocktakingToDTO(savedStocktaking, product.getName()));
                                })
                )
                .doOnError(error -> log.error(ERROR_UPDATING_STOCKTAKING, error.getMessage()));
    }

    @Override
    public Mono<Void> deleteStocktakingById(String id) {
        return stocktakingRepository.findById(id)
                .switchIfEmpty(Mono.error(new StockTakingNotFoundException(STOCKTAKING_NOT_FOUND_UUID + id)))
                .flatMap(stocktaking -> stocktakingRepository.deleteById(stocktaking.getIdStocktaking()))
                .doOnError(error -> log.error(ERROR_DELETING_STOCKTAKING, error.getMessage()));
    }

    private Mono<Void> validateStocktakingDates(LocalDateTime dateStart, LocalDateTime dateEnd) {
        if (dateStart.isAfter(dateEnd)) {
            return Mono.error(new InvalidDateRangeException(MESSAGE_INVALID_DATE_RANGE));
        }

        return Mono.empty();
    }

    private Mono<StocktakingDTO> findStocktaking(Mono<Stocktaking> stocktakingMono, String errorMessage) {
        return stocktakingMono
                .switchIfEmpty(Mono.error(new StockTakingNotFoundException(errorMessage)))
                .flatMap(stocktaking -> {
                    Mono<Product> productMono = productRepository.findById(stocktaking.getProductId())
                            .switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_NOT_FOUND_UUID + stocktaking.getProductId())));
                    return productMono.map(product -> Mappers.stocktakingToDTO(stocktaking, product.getName()));
                })
                .doOnError(error -> log.error(Constants.ERROR_SEARCHING_STOCKTAKING, error.getMessage()));
    }

    private Flux<StocktakingDTO> findStocktaking(Flux<Stocktaking> stocktakingFlux, String errorMessage) {
        return stocktakingFlux
                .switchIfEmpty(Flux.error(new StockTakingNotFoundException(errorMessage)))
                .flatMap(stocktaking -> {
                    Mono<Product> productMono = productRepository.findById(stocktaking.getProductId())
                            .switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_NOT_FOUND_UUID + stocktaking.getProductId())));
                    return productMono.map(product -> Mappers.stocktakingToDTO(stocktaking, product.getName()));
                })
                .doOnError(error -> log.error(Constants.ERROR_SEARCHING_STOCKTAKING, error.getMessage()));
    }
}
