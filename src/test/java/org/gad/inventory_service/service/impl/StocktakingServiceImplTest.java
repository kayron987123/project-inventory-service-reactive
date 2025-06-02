package org.gad.inventory_service.service.impl;

import org.gad.inventory_service.dto.request.CreateStocktakingRequest;
import org.gad.inventory_service.dto.request.UpdateStocktakingRequest;
import org.gad.inventory_service.exception.ProductNotFoundException;
import org.gad.inventory_service.exception.StockTakingNotFoundException;
import org.gad.inventory_service.model.Product;
import org.gad.inventory_service.model.Stocktaking;
import org.gad.inventory_service.repository.ProductRepository;
import org.gad.inventory_service.repository.StocktakingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.gad.inventory_service.utils.Constants.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StocktakingServiceImplTest {
    @Mock
    private StocktakingRepository stocktakingRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private StocktakingServiceImpl stocktakingService;

    Stocktaking stocktaking;
    Product product;
    CreateStocktakingRequest createStocktakingRequest;
    UpdateStocktakingRequest updateStocktakingRequest;

    @BeforeEach
    void setUp() {
        stocktaking = Stocktaking.builder()
                .idStocktaking("1")
                .productId("product1")
                .quantity(100)
                .performedBy("admin")
                .build();

        product = Product.builder()
                .idProduct("product1")
                .name("Test Product")
                .price(BigDecimal.valueOf(10.99))
                .build();
        createStocktakingRequest = CreateStocktakingRequest.builder()
                .productName("Test Product")
                .quantity(50)
                .build();
        updateStocktakingRequest = UpdateStocktakingRequest.builder()
                .productName("Test Product")
                .quantity(75)
                .build();
    }

    @Test
    void findAllStocktaking_ShouldReturnFluxOfStocktakingDTO_WhenStocktakingExists() {
        when(productRepository.findById(anyString()))
                .thenReturn(Mono.just(product));

        when(stocktakingRepository.findAll())
                .thenReturn(Flux.just(stocktaking));

        StepVerifier.create(stocktakingService.findAllStocktaking())
                .expectNextMatches(stocktakingDTO ->
                        stocktakingDTO.idStocktaking().equals("1") &&
                                stocktakingDTO.productName().equals("Test Product") &&
                                stocktakingDTO.quantity() == 100 &&
                                stocktakingDTO.performedBy().equals("admin"))
                .verifyComplete();
        verify(stocktakingRepository, times(1)).findAll();
    }

    @Test
    void finAllStocktaking_ShouldThrow_WhenNoStocktakingExists() {
        when(stocktakingRepository.findAll())
                .thenReturn(Flux.empty());

        StepVerifier.create(stocktakingService.findAllStocktaking())
                .expectErrorMatches(throwable -> throwable instanceof StockTakingNotFoundException &&
                        throwable.getMessage().equals(STOCKTAKING_NOT_FOUND))
                .verify();

        verify(stocktakingRepository, times(1)).findAll();
    }

    @Test
    void finAllStocktaking_ShouldThrow_WhenProductNotFound() {
        when(stocktakingRepository.findAll())
                .thenReturn(Flux.just(stocktaking));

        when(productRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(stocktakingService.findAllStocktaking())
                .expectErrorMatches(throwable -> throwable instanceof ProductNotFoundException &&
                        throwable.getMessage().equals(PRODUCT_NOT_FOUND_ID + stocktaking.getProductId()))
                .verify();

        verify(productRepository, times(1)).findById(anyString());
    }

    @Test
    void findAllStocktakingByDateBetween_ShouldReturnFluxOfStocktakingDTO_WhenDatesAreValid() {
        String dateStart = "2023-01-01";
        String dateEnd = "2023-12-31";

        when(productRepository.findById(anyString()))
                .thenReturn(Mono.just(product));

        when(stocktakingRepository.findByOptionalDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Flux.just(stocktaking));

        StepVerifier.create(stocktakingService.findAllStocktakingByDateBetween(dateStart, dateEnd))
                .expectNextMatches(stocktakingDTO ->
                        stocktakingDTO.idStocktaking().equals("1") &&
                                stocktakingDTO.productName().equals("Test Product") &&
                                stocktakingDTO.quantity() == 100 &&
                                stocktakingDTO.performedBy().equals("admin"))
                .verifyComplete();

        verify(stocktakingRepository, times(1)).findByOptionalDateRange(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void findAllStocktakingByDateBetween_ShouldThrow_WhenStocktakingIsEmpty() {
        String dateStart = "2023-01-01";
        String dateEnd = "2023-12-31";

        when(stocktakingRepository.findByOptionalDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Flux.empty());

        StepVerifier.create(stocktakingService.findAllStocktakingByDateBetween(dateStart, dateEnd))
                .expectErrorMatches(throwable ->
                        throwable instanceof StockTakingNotFoundException &&
                                throwable.getMessage().equals(STOCKTAKING_NOT_FOUND_BETWEEEN_DATES + dateStart + " and " + dateEnd))
                .verify();

        verify(stocktakingRepository, times(1)).findByOptionalDateRange(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void findAllStocktakingByDateBetween_ShouldThrow_WhenProductNotFound() {
        String dateStart = "2023-01-01";
        String dateEnd = "2023-12-31";

        when(stocktakingRepository.findByOptionalDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Flux.just(stocktaking));

        when(productRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(stocktakingService.findAllStocktakingByDateBetween(dateStart, dateEnd))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProductNotFoundException &&
                                throwable.getMessage().equals(PRODUCT_NOT_FOUND_ID + stocktaking.getProductId()))
                .verify();

        verify(productRepository, times(1)).findById(anyString());
    }

    @Test
    void findAllStocktakingByProductName_ShouldReturnFluxOfStocktakingDTO_WhenProductExists() {
        String productName = "Test Product";

        when(productRepository.findById(anyString()))
                .thenReturn(Mono.just(product));

        when(productRepository.findProductByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(product));

        when(stocktakingRepository.findStocktakingByProductId(anyString()))
                .thenReturn(Flux.just(stocktaking));

        StepVerifier.create(stocktakingService.findAllStocktakingByProductName(productName))
                .expectNextMatches(stocktakingDTO ->
                        stocktakingDTO.idStocktaking().equals("1") &&
                                stocktakingDTO.productName().equals("Test Product") &&
                                stocktakingDTO.quantity() == 100 &&
                                stocktakingDTO.performedBy().equals("admin"))
                .verifyComplete();

        verify(productRepository, times(1)).findProductByNameContainingIgnoreCase(anyString());
    }

    @Test
    void finAllStocktakingByProductName_ShouldThrow_WhenProductNotFoundById() {
        String productName = "Test Product";

        when(productRepository.findProductByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(stocktakingService.findAllStocktakingByProductName(productName))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProductNotFoundException &&
                                throwable.getMessage().equals(STOCKTAKING_NOT_FOUND_NAME + productName))
                .verify();

        verify(productRepository, times(1)).findProductByNameContainingIgnoreCase(anyString());
    }

    @Test
    void findAllStocktakingByProductName_ShouldThrow_WhenProductNotFound() {
        String productName = "NonExistent Product";

        when(productRepository.findProductByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(stocktakingService.findAllStocktakingByProductName(productName))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProductNotFoundException &&
                                throwable.getMessage().equals(STOCKTAKING_NOT_FOUND_NAME + productName))
                .verify();

        verify(productRepository, times(1)).findProductByNameContainingIgnoreCase(anyString());
    }

    @Test
    void findAllStocktakingByProductName_ShouldThrow_WhenStocktakingIsEmpty() {
        String productName = "Test Product";

        when(productRepository.findProductByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(product));

        when(stocktakingRepository.findStocktakingByProductId(anyString()))
                .thenReturn(Flux.empty());

        StepVerifier.create(stocktakingService.findAllStocktakingByProductName(productName))
                .expectErrorMatches(throwable ->
                        throwable instanceof StockTakingNotFoundException &&
                                throwable.getMessage().equals(STOCKTAKING_NOT_FOUND_NAME + productName))
                .verify();

        verify(stocktakingRepository, times(1)).findStocktakingByProductId(anyString());
    }

    @Test
    void findStocktakingById_ShouldReturnStocktakingDTO_WhenStocktakingExists() {
        String stocktakingId = "1";

        when(stocktakingRepository.findById(anyString()))
                .thenReturn(Mono.just(stocktaking));

        when(productRepository.findById(stocktaking.getProductId()))
                .thenReturn(Mono.just(product));

        StepVerifier.create(stocktakingService.findStocktakingById(stocktakingId))
                .expectNextMatches(stocktakingDTO ->
                        stocktakingDTO.idStocktaking().equals("1") &&
                                stocktakingDTO.productName().equals("Test Product") &&
                                stocktakingDTO.quantity() == 100 &&
                                stocktakingDTO.performedBy().equals("admin"))
                .verifyComplete();

        verify(stocktakingRepository, times(1)).findById(anyString());
    }

    @Test
    void findStocktakingById_ShouldThrow_WhenStocktakingDoesNotExist() {
        String stocktakingId = "1";

        when(stocktakingRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(stocktakingService.findStocktakingById(stocktakingId))
                .expectErrorMatches(throwable ->
                        throwable instanceof StockTakingNotFoundException &&
                                throwable.getMessage().equals(STOCKTAKING_NOT_FOUND_ID + stocktakingId))
                .verify();

        verify(stocktakingRepository, times(1)).findById(anyString());
    }

    @Test
    void findStocktakingById_ShouldThrow_WhenProductNotFound() {
        String stocktakingId = "1";

        when(stocktakingRepository.findById(anyString()))
                .thenReturn(Mono.just(stocktaking));

        when(productRepository.findById(stocktaking.getProductId()))
                .thenReturn(Mono.empty());
        StepVerifier.create(stocktakingService.findStocktakingById(stocktakingId))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProductNotFoundException &&
                                throwable.getMessage().equals(PRODUCT_NOT_FOUND_ID + stocktaking.getProductId()))
                .verify();
        verify(productRepository, times(1)).findById(stocktaking
                .getProductId());
    }

    @Test
    void createStocktaking_ShouldReturnStocktakingDTO_WhenProductExists() {
        when(productRepository.findProductByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(product));

        when(stocktakingRepository.save(any(Stocktaking.class)))
                .thenReturn(Mono.just(stocktaking));

        StepVerifier.create(stocktakingService.createStocktaking(createStocktakingRequest))
                .expectNextMatches(stocktakingDTO ->
                        stocktakingDTO.productName().equals("Test Product") &&
                                stocktakingDTO.quantity() == 100 &&
                                stocktakingDTO.performedBy().equals("admin"))
                .verifyComplete();

        verify(stocktakingRepository, times(1)).save(any(Stocktaking.class));
    }

    @Test
    void createStocktaking_ShouldThrow_WhenProductNotFound() {
        when(productRepository.findProductByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(stocktakingService.createStocktaking(createStocktakingRequest))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProductNotFoundException &&
                                throwable.getMessage().equals(STOCKTAKING_NOT_FOUND_NAME + "Test Product"))
                .verify();

        verify(productRepository, times(1)).findProductByNameContainingIgnoreCase(anyString());
    }

    @Test
    void updateStocktaking_ShouldReturnUpdatedStocktakingDTO_WhenStocktakingExists() {
        String stocktakingId = "1";

        when(stocktakingRepository.findById(anyString()))
                .thenReturn(Mono.just(stocktaking));

        when(productRepository.findProductByNameContainingIgnoreCase(updateStocktakingRequest.productName()))
                .thenReturn(Mono.just(product));

        Stocktaking updatedStocktaking = Stocktaking.builder()
                .idStocktaking(stocktakingId)
                .productId(product.getIdProduct())
                .quantity(updateStocktakingRequest.quantity())
                .performedBy("admin")
                .build();

        when(stocktakingRepository.save(any(Stocktaking.class)))
                .thenReturn(Mono.just(updatedStocktaking));

        StepVerifier.create(stocktakingService.updateStocktaking(stocktakingId, updateStocktakingRequest))
                .expectNextMatches(stocktakingDTO ->
                        stocktakingDTO.idStocktaking().equals("1") &&
                                stocktakingDTO.productName().equals("Test Product") &&
                                stocktakingDTO.quantity() == 75 &&
                                stocktakingDTO.performedBy().equals("admin"))
                .verifyComplete();

        verify(stocktakingRepository, times(1)).save(any(Stocktaking.class));
    }

    @Test
    void updateStocktaking_ShouldThrow_WhenStocktakingDoesNotExist() {
        String stocktakingId = "1";

        when(stocktakingRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(stocktakingService.updateStocktaking(stocktakingId, updateStocktakingRequest))
                .expectErrorMatches(throwable ->
                        throwable instanceof StockTakingNotFoundException &&
                                throwable.getMessage().equals(STOCKTAKING_NOT_FOUND_ID + stocktakingId))
                .verify();

        verify(stocktakingRepository, times(1)).findById(anyString());
    }

    @Test
    void updateStocktaking_ShouldThrow_WhenProductNotFound() {
        String stocktakingId = "1";

        when(stocktakingRepository.findById(anyString()))
                .thenReturn(Mono.just(stocktaking));

        when(productRepository.findProductByNameContainingIgnoreCase(updateStocktakingRequest.productName()))
                .thenReturn(Mono.empty());

        StepVerifier.create(stocktakingService.updateStocktaking(stocktakingId, updateStocktakingRequest))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProductNotFoundException &&
                                throwable.getMessage().equals(STOCKTAKING_NOT_FOUND_NAME + updateStocktakingRequest.productName()))
                .verify();

        verify(productRepository, times(1)).findProductByNameContainingIgnoreCase(updateStocktakingRequest.productName());
    }

    @Test
    void deleteStocktakingById_ShouldReturnMonoVoid_WhenStocktakingExists() {
        String stocktakingId = "1";

        when(stocktakingRepository.findById(anyString()))
                .thenReturn(Mono.just(stocktaking));

        when(stocktakingRepository.deleteById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(stocktakingService.deleteStocktakingById(stocktakingId))
                .verifyComplete();

        verify(stocktakingRepository, times(1)).deleteById(anyString());
    }

    @Test
    void deleteStocktakingById_ShouldThrow_WhenStocktakingDoesNotExist() {
        String stocktakingId = "1";

        when(stocktakingRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(stocktakingService.deleteStocktakingById(stocktakingId))
                .expectErrorMatches(throwable ->
                        throwable instanceof StockTakingNotFoundException &&
                                throwable.getMessage().equals(STOCKTAKING_NOT_FOUND_ID + stocktakingId))
                .verify();

        verify(stocktakingRepository, times(1)).findById(anyString());
    }
}