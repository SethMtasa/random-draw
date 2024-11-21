package com.netone.random_draw.service;

import com.netone.random_draw.exception.ResourceNotFoundException;
import com.netone.random_draw.exception.ValidationException;
import com.netone.random_draw.model.RandomDraw;
import com.netone.random_draw.repository.RandomDrawRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class RandomDrawService {

    @Autowired
    private RandomDrawRepository randomDrawRepository;

    public List<RandomDraw> saveRegistersFromExcel(MultipartFile file) throws IOException {

        List<RandomDraw> phoneNumbers = readExcelData(file);


        if (!phoneNumbers.isEmpty()) {

            randomDrawRepository.deleteAll();


            return randomDrawRepository.saveAll(phoneNumbers);
        } else {
            throw new ValidationException("No valid data found in the Excel file.");
        }
    }

    private List<RandomDraw> readExcelData(MultipartFile file) throws IOException {
        List<RandomDraw> registers = new ArrayList<>();
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                RandomDraw phoneNumbers = new RandomDraw();
                phoneNumbers.setMsisdn(getLongCellValue(row.getCell(1)));
                registers.add(phoneNumbers);
            }
        }
        return registers;
    }

    private Long getLongCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;
        if (cell.getCellType() == CellType.NUMERIC) return (long) cell.getNumericCellValue();
        try {
            return Long.parseLong(cell.getStringCellValue().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String getStringCellValue(Cell cell) {
        return cell == null || cell.getCellType() == CellType.BLANK ? null : cell.getStringCellValue().trim();
    }

    private LocalDate getLocalDateCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }

        switch (cell.getCellType()) {
            case NUMERIC:

                return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            case STRING:
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy", Locale.ENGLISH);
                    return LocalDate.parse(cell.getStringCellValue().trim(), formatter);
                } catch (DateTimeParseException e) {

                    return null;
                }
            default:
                return null;
        }
    }

    private BigDecimal getBigDecimalCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;

        switch (cell.getCellType()) {
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING:
                try {
                    return new BigDecimal(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {

                    System.err.println("Invalid number format in cell: " + cell.getStringCellValue());
                    return null;
                }
            case FORMULA:
                try {
                    if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                        return BigDecimal.valueOf(cell.getNumericCellValue());
                    } else {
                        System.err.println("Unsupported formula type in cell");
                        return null;
                    }
                } catch (IllegalStateException e) {
                    System.err.println("Error evaluating formula: " + e.getMessage());
                    return null;
                }
            default:
                return null;
        }
    }

    public List<RandomDraw> getAllRegisters() {
        return randomDrawRepository.findAll();
    }

    public RandomDraw getRegisterById(Long id) {
        return randomDrawRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Register not found for ID: " + id));
    }

    public RandomDraw updateRegister(Long id, RandomDraw updatedRegister) {
        RandomDraw existingRegister = randomDrawRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Register not found for ID: " + id));

        existingRegister.setMsisdn(updatedRegister.getMsisdn());


        return randomDrawRepository.save(existingRegister);
    }

}