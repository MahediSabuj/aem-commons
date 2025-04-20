package com.aem.commons.core.services;

import org.osgi.annotation.versioning.ConsumerType;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@ConsumerType
public interface ExcelService {
    /**
     * Reads an Excel file and returns a list of unused assets.
     *
     * @param inputStream the InputStream of the Excel file
     * @return a list of unused asset paths
     * @throws IOException if an error occurs while reading the file
     */
    List<String> readExcelFile(InputStream inputStream) throws IOException;
}
