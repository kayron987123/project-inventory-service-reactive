package org.gad.inventory_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static org.gad.inventory_service.utils.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelReportServiceImpl implements ExcelReportService {

    @Override
    public Mono<byte[]> generateSalesReport(List<SaleDTO> sales) {
        return Mono.fromCallable(() -> generateExcelBytes(sales))
                .doOnError(e -> log.error(FAILED_GENERATE_REPORT, e))
                .onErrorMap(e -> e instanceof ExcelReportGenerationException ? e :
                        new ExcelReportGenerationException(ERROR_GENERATING_REPORT, e));
    }

    private byte[] generateExcelBytes(List<SaleDTO> sales) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(SHEET_NAME);
            createHeaderRow(workbook, sheet);
            populateDataRows(sheet, sales);
            autoSizeColumns(sheet);

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void createHeaderRow(Workbook workbook, Sheet sheet) {
        CellStyle headerStyle = createHeaderStyle(workbook);
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < REPORT_HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(REPORT_HEADERS[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private void populateDataRows(Sheet sheet, List<SaleDTO> sales) {
        int rowNum = 1;
        for (SaleDTO sale : sales) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(sale.idSale());
            row.createCell(1).setCellValue(sale.nameProduct());
            row.createCell(2).setCellValue(sale.quantity());
            row.createCell(3).setCellValue(sale.totalPrice().doubleValue());
            row.createCell(4).setCellValue(sale.saleDate());
        }
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < REPORT_HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
