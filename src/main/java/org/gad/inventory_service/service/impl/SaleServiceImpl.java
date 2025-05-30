package org.gad.inventory_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gad.inventory_service.dto.SaleDTO;
import org.gad.inventory_service.dto.request.CreateSaleRequest;
import org.gad.inventory_service.dto.request.UpdateSaleRequest;
import org.gad.inventory_service.exception.*;
import org.gad.inventory_service.model.Product;
import org.gad.inventory_service.model.Sale;
import org.gad.inventory_service.repository.ProductRepository;
import org.gad.inventory_service.repository.SaleRepository;
import org.gad.inventory_service.service.SaleService;
import org.gad.inventory_service.utils.Constants;
import org.gad.inventory_service.utils.Mappers;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.gad.inventory_service.utils.Constants.*;
import static org.gad.inventory_service.utils.UtilsMethods.*;
import static org.gad.inventory_service.utils.UtilsMethods.parseFlexibleDateEnd;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    @Override
    public Flux<SaleDTO> getAllSales() {
        return findSales(saleRepository.findAll(), SALE_NOT_FOUND_FLUX, ERROR_SEARCHING_SALE);
    }

    @Override
    public Flux<SaleDTO> getSaleByNameProduct(String nameProduct) {
        return productRepository.findProductByNameContainingIgnoreCase(nameProduct)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_NOT_FOUND_NAME + nameProduct)))
                .flatMapMany(product -> findSales(saleRepository.findSalesByProductId(product.getIdProduct()), SALE_NOT_FOUND_NAME + nameProduct, ERROR_SEARCHING_SALE_NAME));
    }

    @Override
    public Flux<SaleDTO> getSaleByTotalPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        BigDecimal priceMinFormatted = null;
        BigDecimal priceMaxFormatted = null;
        if (minPrice != null) {
            priceMinFormatted = formatPrice(minPrice);
        }
        if (maxPrice != null) {
            priceMaxFormatted = formatPrice(maxPrice);
        }

        if (minPrice != null && maxPrice != null) {
            return validatePriceTotal(priceMinFormatted, priceMaxFormatted)
                    .thenMany(saleRepository.findByOptionalPriceTotalRange(priceMinFormatted, priceMaxFormatted))
                    .switchIfEmpty(Mono.error(new SalesNotFoundException(SALE_NOT_FOUND_BETWEEN_PRICES + minPrice + TEXT_AND + maxPrice)))
                    .flatMap(sale -> {
                        Mono<Product> productMono = productRepository.findById(sale.getProductId())
                                .switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + sale.getProductId())));
                        return productMono.map(product -> Mappers.saleToDTO(sale, product.getName()));
                    })
                    .doOnError(error -> log.error(Constants.ERROR_SEARCHING_SALE_BETWEEN_PRICES, error.getMessage()));
        } else {
            return saleRepository.findByOptionalPriceTotalRange(priceMinFormatted, priceMaxFormatted)
                    .switchIfEmpty(Mono.error(new SalesNotFoundException(SALE_NOT_FOUND_BETWEEN_PRICES + minPrice + TEXT_AND + maxPrice)))
                    .flatMap(sale -> {
                        Mono<Product> productMono = productRepository.findById(sale.getProductId())
                                .switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + sale.getProductId())));
                        return productMono.map(product -> Mappers.saleToDTO(sale, product.getName()));
                    })
                    .doOnError(error -> log.error(ERROR_SEARCHING_SALE_BETWEEN_PRICES, error.getMessage()));
        }
    }

    @Override
    public Flux<SaleDTO> getSaleByDateRange(String startDate, String endDate) {
        LocalDateTime start = null;
        LocalDateTime end = null;

        if (startDate != null) {
            start = parseFlexibleDateStart(startDate);
        }
        if (endDate != null) {
            end = parseFlexibleDateEnd(endDate);
        }

        if (startDate != null && endDate != null) {
            return validateStocktakingDates(start, end)
                    .thenMany(saleRepository.findByOptionalDateRange(start, end))
                    .switchIfEmpty(Mono.error(new SalesNotFoundException(SALE_NOT_FOUND_BETWEEN_DATES + startDate + TEXT_AND + endDate)))
                    .flatMap(sale -> {
                        Mono<Product> productMono = productRepository.findById(sale.getProductId())
                                .switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + sale.getProductId())));
                        return productMono.map(product -> Mappers.saleToDTO(sale, product.getName()));
                    })
                    .doOnError(error -> log.error(ERROR_SEARCHING_SALE_BETWEEN_DATES, error.getMessage()));
        } else {
            return saleRepository.findByOptionalDateRange(start, end)
                    .switchIfEmpty(Mono.error(new SalesNotFoundException(SALE_NOT_FOUND_BETWEEN_DATES + startDate + TEXT_AND + endDate)))
                    .flatMap(sale -> {
                        Mono<Product> productMono = productRepository.findById(sale.getProductId())
                                .switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + sale.getProductId())));
                        return productMono.map(product -> Mappers.saleToDTO(sale, product.getName()));
                    })
                    .doOnError(error -> log.error(Constants.ERROR_SEARCHING_SALE_BETWEEN_DATES, error.getMessage()));
        }
    }

    @Override
    public Mono<SaleDTO> getSaleById(String id) {
        return saleRepository.findById(id)
                .switchIfEmpty(Mono.error(new SalesNotFoundException(SALE_NOT_FOUND_ID + id)))
                .flatMap(sale -> {
                    Mono<Product> productMono = productRepository.findById(sale.getProductId())
                            .switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + sale.getProductId())));
                    return productMono.map(product -> Mappers.saleToDTO(sale, product.getName()));
                })
                .doOnError(error -> log.error(ERROR_SEARCHING_SALE_UUID, error.getMessage()));
    }

    @Override
    public Mono<SaleDTO> createSale(CreateSaleRequest createSaleRequest) {
        return productRepository.findProductByNameContainingIgnoreCase(createSaleRequest.nameProduct())
                .switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_NOT_FOUND_NAME + createSaleRequest.nameProduct())))
                .flatMap(product -> {
                    Sale sale = Sale.builder()
                            .productId(product.getIdProduct())
                            .saleDate(LocalDateTime.now())
                            .quantity(createSaleRequest.quantity())
                            .totalPrice(calculateTotalPrice(createSaleRequest.quantity(), product.getPrice()))
                            .build();
                    return saleRepository.save(sale)
                            .map(saleSaved -> Mappers.saleToDTO(saleSaved, product.getName()));
                })
                .doOnError(error -> log.error(ERROR_CREATING_SALE, error.getMessage()));
    }

    @Override
    public Mono<SaleDTO> updateSale(String id, UpdateSaleRequest updateSaleRequest) {
        return saleRepository.findById(id)
                .switchIfEmpty(Mono.error(new SalesNotFoundException(SALE_NOT_FOUND_ID + id)))
                .flatMap(sale ->
                        productRepository.findProductByNameContainingIgnoreCase(updateSaleRequest.nameProduct())
                                .switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_NOT_FOUND_NAME + updateSaleRequest.nameProduct())))
                                .flatMap(product -> {
                                    sale.setProductId(product.getIdProduct());
                                    sale.setQuantity(updateSaleRequest.quantity());
                                    sale.setTotalPrice(calculateTotalPrice(updateSaleRequest.quantity(), product.getPrice()));
                                    return saleRepository.save(sale)
                                            .map(saleUpdated -> Mappers.saleToDTO(saleUpdated, product.getName()));
                                })
                )
                .doOnError(error -> log.error(ERROR_UPDATING_SALE, error.getMessage()));
    }

    @Override
    public Mono<Void> deleteSaleById(String id) {
        return saleRepository.findById(id)
                .switchIfEmpty(Mono.error(new SalesNotFoundException(SALE_NOT_FOUND_ID + id)))
                .flatMap(sale -> saleRepository.deleteById(sale.getIdSale()))
                .doOnError(error -> log.error(ERROR_DELETING_SALE, error.getMessage()));
    }

    private BigDecimal calculateTotalPrice(Integer quantity, BigDecimal priceProduct) {
        return priceProduct.multiply(BigDecimal.valueOf(quantity));
    }

    private Mono<Void> validateStocktakingDates(LocalDateTime dateStart, LocalDateTime dateEnd) {
        if (dateStart.isAfter(dateEnd)) {
            return Mono.error(new InvalidDateRangeException(MESSAGE_INVALID_DATE_RANGE));
        }

        return Mono.empty();
    }

    private Mono<Void> validatePriceTotal(BigDecimal priceMin, BigDecimal priceMax) {
        if (priceMin.compareTo(priceMax) > 0) {
            return Mono.error(new InvalidPriceRangeException(MESSAGE_INVALID_PRICE_RANGE));
        }
        return Mono.empty();
    }

    private Flux<SaleDTO> findSales(Flux<Sale> saleFlux, String errorMessage, String logMessage) {
        return saleFlux
                .switchIfEmpty(Mono.error(new SalesNotFoundException(errorMessage)))
                .flatMap(sale -> {
                    Mono<Product> productMono = productRepository.findById(sale.getProductId())
                            .switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + sale.getProductId())));
                    return productMono.map(product -> Mappers.saleToDTO(sale, product.getName()));
                })
                .doOnError(error -> log.error(logMessage, error.getMessage()));
    }
}
