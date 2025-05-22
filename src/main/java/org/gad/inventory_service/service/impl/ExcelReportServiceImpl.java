package org.gad.inventory_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.gad.inventory_service.dto.SaleDTO;
import org.gad.inventory_service.exception.ExcelReportGenerationException;
import org.gad.inventory_service.service.ExcelReportService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelReportServiceImpl implements ExcelReportService {

    @Override
    public Mono<byte[]> generateSalesReport(List<SaleDTO> sales) {
        return Mono.fromCallable(() -> {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Sales Report");

                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);

                Row headerRow = sheet.createRow(0);
                String[] headers = {"ID Sale", "Product", "Amount", "Total Price", "DateTime"};
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(headerStyle);
                }

                int rowNum = 1;
                for (SaleDTO sale : sales) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(sale.uuidSale());
                    row.createCell(1).setCellValue(sale.nameProduct());
                    row.createCell(2).setCellValue(sale.quantity());
                    row.createCell(3).setCellValue(sale.totalPrice().doubleValue());
                    row.createCell(4).setCellValue(sale.saleDate());
                }

                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                workbook.write(outputStream);
                return outputStream.toByteArray();
            } catch (IOException e) {
                throw new ExcelReportGenerationException("Error generating the sales report", e);
            }
        });
    }
}
