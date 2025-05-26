package org.gad.inventory_service.service;

import org.gad.inventory_service.dto.SaleDTO;
import org.gad.inventory_service.dto.request.CreateSaleRequest;
import org.gad.inventory_service.dto.request.UpdateSaleRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface SaleService {
    Flux<SaleDTO> getAllSales();
    Flux<SaleDTO> getSaleByNameProduct(String nameProduct);
    Flux<SaleDTO> getSaleByTotalPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    Flux<SaleDTO> getSaleByDateRange(String startDate, String endDate);
    Mono<SaleDTO> getSaleById(String id);
    Mono<SaleDTO> createSale(CreateSaleRequest createSaleRequest);
    Mono<SaleDTO> updateSale(String id,   UpdateSaleRequest updateSaleRequest);
    Mono<Void> deleteSaleById(String id);
}
