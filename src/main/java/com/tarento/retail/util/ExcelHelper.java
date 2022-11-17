package com.tarento.retail.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import com.tarento.retail.model.Institute;

public class ExcelHelper {
  public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  static String[] HEADERs = { "District Name", "Parent Tr. Center Code", "Parent Tr. Center Name", "Degree", "Course", "Applied Year", "Sector", "EmailId" };
  static String SHEET = "Institute Details";

  public static boolean hasExcelFormat(MultipartFile file) {

    if (!TYPE.equals(file.getContentType())) {
      return false;
    }

    return true;
  }

  public static ByteArrayInputStream institutesToExcel(List<Institute> institutes) {

    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
      Sheet sheet = workbook.createSheet(SHEET);

      // Header
      Row headerRow = sheet.createRow(0);

      for (int col = 0; col < HEADERs.length; col++) {
        Cell cell = headerRow.createCell(col);
        cell.setCellValue(HEADERs[col]);
      }
     
      int rowIdx = 1;
      for (Institute institute : institutes) {
        Row row = sheet.createRow(rowIdx++);

        row.createCell(0).setCellValue(institute.getDistrictName());
        row.createCell(1).setCellValue(institute.getParentTrCenterCode());
        row.createCell(2).setCellValue(institute.getParentTrCenterName());
        row.createCell(3).setCellValue(institute.getDegree());
        row.createCell(4).setCellValue(institute.getCourse());
        row.createCell(5).setCellValue(institute.getAppliedYear());
        row.createCell(6).setCellValue(institute.getSector());
        row.createCell(6).setCellValue(institute.getEmailId());
      }

      workbook.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
    }
  }

  public static List<Institute> excelToInstitutes(InputStream is) {
    try {
      Workbook workbook = new XSSFWorkbook(is);

      Sheet sheet = workbook.getSheet(SHEET);
      Iterator<Row> rows = sheet.iterator();

      List<Institute> institutes = new ArrayList<Institute>();

      int rowNumber = 0;
      while (rows.hasNext()) {
        Row currentRow = rows.next();

        // skip header
        if (rowNumber == 0) {
          rowNumber++;
          continue;
        }

        Iterator<Cell> cellsInRow = currentRow.iterator();

        Institute institute = new Institute();

        int cellIdx = 0;
        while (cellsInRow.hasNext()) {
          Cell currentCell = cellsInRow.next();

          switch (cellIdx) {
          case 0:
        	  institute.setDistrictName(currentCell.getStringCellValue());
            break;

          case 1:
        	  institute.setParentTrCenterCode(currentCell.getStringCellValue());
            break;

          case 2:
        	  institute.setParentTrCenterName(currentCell.getStringCellValue());
            break;

          case 3:
        	  institute.setDegree(currentCell.getStringCellValue());
            break;

          case 4:
        	  institute.setCourse(currentCell.getStringCellValue());
            break;

          case 5:
        	  institute.setAppliedYear(currentCell.getStringCellValue());
            break;
            
          case 6:
        	  institute.setSector(currentCell.getStringCellValue());
            break;
            
          case 7:
        	  institute.setEmailId(currentCell.getStringCellValue());
            break;
            
          
          default:
            break;
          }

          cellIdx++;
        }
        if(institute.getEmailId() != null && !institute.getEmailId().isEmpty()) {
        	institutes.add(institute);
        }
        
      }

      workbook.close();

      return institutes;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
    }
  }
}
