package org.gad.inventory_service.service.impl;

import org.gad.inventory_service.dto.request.CreateSaleRequest;
import org.gad.inventory_service.dto.request.UpdateSaleRequest;
import org.gad.inventory_service.exception.ProductNotFoundException;
import org.gad.inventory_service.exception.SalesNotFoundException;
import org.gad.inventory_service.model.Product;
import org.gad.inventory_service.model.Sale;
import org.gad.inventory_service.repository.ProductRepository;
import org.gad.inventory_service.repository.SaleRepository;
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
import static org.gad.inventory_service.utils.Constants.TEXT_AND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceImplTest {
    @Mock
    private SaleRepository saleRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SaleServiceImpl saleService;

    private Sale sale;
    private Product product;
    private CreateSaleRequest createSaleRequest;
    private UpdateSaleRequest updateSaleRequest;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .idProduct("test-product-id")
                .name("Test Product")
                .price(BigDecimal.valueOf(100.00))
                .build();

        createSaleRequest = CreateSaleRequest.builder()
                .nameProduct("Test Product")
                .quantity(1)
                .build();

        updateSaleRequest = UpdateSaleRequest.builder()
                .nameProduct("Updated Product")
                .quantity(2)
                .build();

        sale = Sale.builder()
                .idSale("test-sale-id")
                .productId("test-product-id")
                .totalPrice(BigDecimal.valueOf(100.00))
                .build();
    }

    @Test
    void getAllSales_ShouldReturnFluxOfSalesDTO_WhenSalesExist() {
        when(productRepository.findById(anyString()))
                .thenReturn(Mono.just(product));
        when(saleRepository.findAll())
                .thenReturn(Flux.just(sale));

        StepVerifier.create(saleService.getAllSales())
                .expectNextMatches(saleDTO ->
                        saleDTO.idSale().equals("test-sale-id") &&
                                saleDTO.totalPrice().equals(BigDecimal.valueOf(100.00)))
                .verifyComplete();

        verify(saleRepository, times(1)).findAll();
    }

    @Test
    void getAllSales_shouldThrow_WhenSalesIsEmpty() {
        when(saleRepository.findAll())
                .thenReturn(Flux.empty());

        StepVerifier.create(saleService.getAllSales())
                .expectErrorMatches(throwable -> throwable instanceof SalesNotFoundException &&
                        throwable.getMessage().equals("No sales found"))
                .verify();

        verify(saleRepository, times(1)).findAll();
    }

    @Test
    void getAllSales_ShouldThrow_WhenProductNotFound() {
        when(productRepository.findById(anyString()))
                .thenReturn(Mono.empty());
        when(saleRepository.findAll())
                .thenReturn(Flux.just(sale));


        StepVerifier.create(saleService.getAllSales())
                .expectErrorMatches(throwable -> throwable instanceof ProductNotFoundException &&
                        throwable.getMessage().equals(PRODUCT_NOT_FOUND_ID + "test-product-id"))
                .verify();

        verify(productRepository, times(1)).findById(anyString());
    }

    @Test
    void getSaleByNameProduct_ShouldReturnFluxOfSalesDTO_WhenProductNameMatches() {
        when(productRepository.findProductByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(product));
        when(productRepository.findById(anyString()))
                .thenReturn(Mono.just(product));
        when(saleRepository.findSalesByProductId(anyString()))
                .thenReturn(Flux.just(sale));

        StepVerifier.create(saleService.getSaleByNameProduct("Test Product"))
                .expectNextMatches(saleDTO ->
                        saleDTO.idSale().equals("test-sale-id") &&
                                saleDTO.totalPrice().equals(BigDecimal.valueOf(100.00)))
                .verifyComplete();

        verify(productRepository, times(1)).findProductByNameContainingIgnoreCase(anyString());
    }

    @Test
    void getSaleByNameProduct_ShouldThrow_WhenProductNotFound() {
        when(productRepository.findProductByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(saleService.getSaleByNameProduct("NonExistent Product"))
                .expectErrorMatches(throwable -> throwable instanceof ProductNotFoundException &&
                        throwable.getMessage().equals(PRODUCT_NOT_FOUND_NAME + "NonExistent Product"))
                .verify();

        verify(productRepository, times(1)).findProductByNameContainingIgnoreCase(anyString());
    }

    @Test
    void getSaleByTotalPriceRange_ShouldReturnFluxOfSalesDTO_WhenPriceRangeMatches() {
        when(productRepository.findById(anyString()))
                .thenReturn(Mono.just(product));
        when(saleRepository.findByOptionalPriceTotalRange(any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(Flux.just(sale));

        StepVerifier.create(saleService.getSaleByTotalPriceRange(BigDecimal.ZERO, BigDecimal.valueOf(200.00)))
                .expectNextMatches(saleDTO ->
                        saleDTO.idSale().equals("test-sale-id") &&
                                saleDTO.totalPrice().equals(BigDecimal.valueOf(100.00)))
                .verifyComplete();

        verify(saleRepository, times(1)).findByOptionalPriceTotalRange(any(BigDecimal.class), any(BigDecimal.class));
    }

    @Test
    void getSaleByTotalPriceRange_ShouldThrow_WhenNoSalesFound() {
        when(saleRepository.findByOptionalPriceTotalRange(any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(Flux.empty());

        StepVerifier.create(saleService.getSaleByTotalPriceRange(BigDecimal.ZERO, BigDecimal.valueOf(200.00)))
                .expectErrorMatches(throwable -> throwable instanceof SalesNotFoundException &&
                        throwable.getMessage().equals(SALE_NOT_FOUND_BETWEEN_PRICES + BigDecimal.ZERO + TEXT_AND + BigDecimal.valueOf(200.00)))
                .verify();

        verify(saleRepository, times(1)).findByOptionalPriceTotalRange(any(BigDecimal.class), any(BigDecimal.class));
    }

    @Test
    void getSaleByTotalPriceRange_ShouldThrow_WhenProductNotFound() {
        when(saleRepository.findByOptionalPriceTotalRange(any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(Flux.just(sale));
        when(productRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(saleService.getSaleByTotalPriceRange(BigDecimal.ZERO, BigDecimal.valueOf(200.00)))
                .expectErrorMatches(throwable -> throwable instanceof ProductNotFoundException &&
                        throwable.getMessage().equals(PRODUCT_NOT_FOUND_ID + "test-product-id"))
                .verify();

        verify(productRepository, times(1)).findById(anyString());
    }

    @Test
    void getSaleByDateRange_ShouldReturnFluxOfSalesDTO_WhenDateRangeMatches() {
        when(productRepository.findById(anyString()))
                .thenReturn(Mono.just(product));
        when(saleRepository.findByOptionalDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Flux.just(sale));

        StepVerifier.create(saleService.getSaleByDateRange(LocalDateTime.now().toString(), LocalDateTime.now().plusDays(1).toString()))
                .expectNextMatches(saleDTO ->
                        saleDTO.idSale().equals("test-sale-id") &&
                                saleDTO.totalPrice().equals(BigDecimal.valueOf(100.00)))
                .verifyComplete();

        verify(saleRepository, times(1)).findByOptionalDateRange(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void getSaleByDateRange_ShouldThrow_WhenNoSalesFound() {
        when(saleRepository.findByOptionalDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Flux.empty());

        StepVerifier.create(saleService.getSaleByDateRange(LocalDateTime.of(2023, 10, 1, 0, 0).toString(), LocalDateTime.of(2023, 10, 2, 0, 0).toString()))
                .expectErrorMatches(throwable -> throwable instanceof SalesNotFoundException &&
                        throwable.getMessage().equals(SALE_NOT_FOUND_BETWEEN_DATES + LocalDateTime.of(2023, 10, 1, 0, 0) + TEXT_AND + LocalDateTime.of(2023, 10, 2, 0, 0)))
                .verify();

        verify(saleRepository, times(1)).findByOptionalDateRange(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void getSaleByDateRange_ShouldThrow_WhenProductNotFound() {
        when(saleRepository.findByOptionalDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Flux.just(sale));
        when(productRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(saleService.getSaleByDateRange(LocalDateTime.now().toString(), LocalDateTime.now().plusDays(1).toString()))
                .expectErrorMatches(throwable -> throwable instanceof ProductNotFoundException &&
                        throwable.getMessage().equals(PRODUCT_NOT_FOUND_ID + "test-product-id"))
                .verify();
        verify(productRepository, times(1)).findById(anyString());
    }

    @Test
    void getSaleById_ShouldReturnSaleDTO_WhenSaleExists() {
        when(saleRepository.findById(anyString()))
                .thenReturn(Mono.just(sale));
        when(productRepository.findById(anyString()))
                .thenReturn(Mono.just(product));

        StepVerifier.create(saleService.getSaleById("test-sale-id"))
                .expectNextMatches(saleDTO ->
                        saleDTO.idSale().equals("test-sale-id") &&
                                saleDTO.totalPrice().equals(BigDecimal.valueOf(100.00)))
                .verifyComplete();

        verify(saleRepository, times(1)).findById(anyString());
    }

    @Test
    void getSaleById_ShouldThrow_WhenSaleDoesNotExist() {
        when(saleRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(saleService.getSaleById("non-existent-sale-id"))
                .expectErrorMatches(throwable ->
                        throwable instanceof SalesNotFoundException &&
                                throwable.getMessage().equals(SALE_NOT_FOUND_ID + "non-existent-sale-id"))
                .verify();

        verify(saleRepository, times(1)).findById(anyString());
    }

    @Test
    void getSaleById_ShouldThrow_WhenProductNotFound() {
        when(saleRepository.findById(anyString()))
                .thenReturn(Mono.just(sale));
        when(productRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(saleService.getSaleById("test-sale-id"))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProductNotFoundException &&
                                throwable.getMessage().equals(PRODUCT_NOT_FOUND_ID + "test-product-id"))
                .verify();

        verify(productRepository, times(1)).findById(anyString());
    }

    @Test
    void createSale_ShouldReturnSaleDTO_WhenSaleIsCreated() {
        when(productRepository.findProductByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(product));
        when(saleRepository.save(any(Sale.class)))
                .thenReturn(Mono.just(sale));

        StepVerifier.create(saleService.createSale(createSaleRequest))
                .expectNextMatches(saleDTO ->
                        saleDTO.idSale().equals("test-sale-id") &&
                                saleDTO.totalPrice().equals(BigDecimal.valueOf(100.00)))
                .verifyComplete();

        verify(saleRepository, times(1)).save(any(Sale.class));
    }

    @Test
    void createSale_ShouldThrow_WhenProductNotFound() {
        when(productRepository.findProductByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(saleService.createSale(createSaleRequest))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProductNotFoundException &&
                                throwable.getMessage().equals(PRODUCT_NOT_FOUND_NAME + "Test Product"))
                .verify();

        verify(productRepository, times(1)).findProductByNameContainingIgnoreCase(anyString());
    }

    @Test
    void updateSale_ShouldReturnUpdatedSaleDTO_WhenSaleIsUpdated() {
        when(saleRepository.findById(anyString()))
                .thenReturn(Mono.just(sale));
        when(productRepository.findProductByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(product));
        when(saleRepository.save(any(Sale.class)))
                .thenReturn(Mono.just(sale));

        StepVerifier.create(saleService.updateSale("test-sale-id", updateSaleRequest))
                .expectNextMatches(saleDTO ->
                        saleDTO.idSale().equals("test-sale-id") &&
                                saleDTO.totalPrice().equals(BigDecimal.valueOf(200.00)))
                .verifyComplete();

        verify(saleRepository, times(1)).save(any(Sale.class));
    }

    @Test
    void updateSale_ShouldThrow_WhenSaleDoesNotExist() {
        when(saleRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(saleService.updateSale("non-existent-sale-id", updateSaleRequest))
                .expectErrorMatches(throwable ->
                        throwable instanceof SalesNotFoundException &&
                                throwable.getMessage().equals(SALE_NOT_FOUND_ID + "non-existent-sale-id"))
                .verify();

        verify(saleRepository, times(1)).findById(anyString());
    }

    @Test
    void updateSale_ShouldThrow_WhenProductNotFound() {
        when(saleRepository.findById(anyString()))
                .thenReturn(Mono.just(sale));
        when(productRepository.findProductByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(saleService.updateSale("test-sale-id", updateSaleRequest))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProductNotFoundException &&
                                throwable.getMessage().equals(PRODUCT_NOT_FOUND_NAME + "Updated Product"))
                .verify();

        verify(productRepository, times(1)).findProductByNameContainingIgnoreCase(anyString());
    }

    @Test
    void deleteSaleById_ShouldReturnMonoVoid_WhenSaleIsDeleted() {
        when(saleRepository.findById(anyString()))
                .thenReturn(Mono.just(sale));
        when(saleRepository.deleteById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(saleService.deleteSaleById("test-sale-id"))
                .verifyComplete();

        verify(saleRepository, times(1)).deleteById("test-sale-id");
    }

    @Test
    void deleteSaleById_ShouldThrow_WhenSaleDoesNotExist() {
        when(saleRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(saleService.deleteSaleById("non-existent-sale-id"))
                .expectErrorMatches(throwable ->
                        throwable instanceof SalesNotFoundException &&
                                throwable.getMessage().equals(SALE_NOT_FOUND_ID + "non-existent-sale-id"))
                .verify();

        verify(saleRepository, times(1)).findById("non-existent-sale-id");
    }
}