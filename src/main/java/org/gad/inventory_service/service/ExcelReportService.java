package org.gad.inventory_service.service;

import org.gad.inventory_service.dto.SaleDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ExcelReportService {
    Mono<byte[]> generateSalesReport(List<SaleDTO> sales);
}
