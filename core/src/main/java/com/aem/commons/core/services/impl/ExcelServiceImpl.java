package com.aem.commons.core.services.impl;

import com.aem.commons.core.services.ExcelService;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component(service = { ExcelService.class },
  property = {
    Constants.SERVICE_DESCRIPTION + "=Excel Service"
})
public class ExcelServiceImpl implements ExcelService {
    /**
     * Reads an Excel file and returns a list of unused assets.
     *
     * @param inputStream the InputStream of the Excel file
     * @return a list of unused asset paths
     * @throws IOException if an error occurs while reading the file
     */
    @Override
    public List<String> readExcelFile(InputStream inputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<String> unusedAssets = new ArrayList<>();

        for (Row row : sheet) {
            Cell source = row.getCell(0); // Asset Path
            Cell reference = row.getCell(4); // Reference Count

            String assetPath = getCellValue(source);
            String referenceCount = getCellValue(reference);

            if (NumberUtils.toInt(referenceCount, -1) == 0) {
                unusedAssets.add(assetPath);
            }
        }

        return unusedAssets;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }
}
