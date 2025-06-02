package org.gad.inventory_service.service.impl;

import org.gad.inventory_service.dto.SaleDTO;
import org.gad.inventory_service.exception.ExcelReportGenerationException;
import org.gad.inventory_service.utils.UtilsMethods;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.gad.inventory_service.utils.UtilsMethods.datetimeNowFormatted;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExcelReportServiceImplTest {
    @InjectMocks
    private ExcelReportServiceImpl excelReportService;

    private List<SaleDTO> sales;

    @BeforeEach
    void setUp() {
        sales = List.of(
                SaleDTO.builder()
                        .idSale("sale-1")
                        .nameProduct("Product 1")
                        .quantity(3)
                        .totalPrice(BigDecimal.valueOf(150.00))
                        .saleDate(datetimeNowFormatted())
                        .build(),
                SaleDTO.builder()
                        .idSale("sale-2")
                        .nameProduct("Product 2")
                        .quantity(5)
                        .totalPrice(BigDecimal.valueOf(250.00))
                        .saleDate(datetimeNowFormatted())
                        .build(),
                SaleDTO.builder()
                        .idSale("sale-3")
                        .nameProduct("Product 3")
                        .quantity(2)
                        .totalPrice(BigDecimal.valueOf(100.00))
                        .saleDate(datetimeNowFormatted())
                        .build()
        );
    }

    @Test
    void generateSalesReport_ShouldReturnExcelFile_WhenDateRangeIsValid() {
        Mono<byte[]> result = excelReportService.generateSalesReport(sales);

        StepVerifier.create(result)
                .assertNext(bytes -> {
                    assertNotNull(bytes);
                    assertTrue(bytes.length > 0);
                })
                .verifyComplete();
    }

    @Test
    void generateSalesReport_ShouldReturnError_WhenIOExceptionOccurs() {
        Mono<byte[]> result = excelReportService.generateSalesReport(null);

        StepVerifier.create(result)
                .expectError(ExcelReportGenerationException.class)
                .verify();
    }

    @Test
    void generateSaleReport_ShouldCreateCorrectExcelStructure() {
        byte[] result = excelReportService.generateSalesReport(sales).block();

        assertNotNull(result);

        assertEquals(0x50, result[0] & 0xFF);
        assertEquals(0x4B, result[1] & 0xFF);
        assertEquals(0x03, result[2] & 0xFF);
        assertEquals(0x04, result[3] & 0xFF);
    }
}