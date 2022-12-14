package com.example.demo;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class Main {





//    public static void main(String[] args) throws Throwable {
//        // keep 100 rows in memory, exceeding rows will be flushed to disk
//        SXSSFWorkbook wb = new SXSSFWorkbook(100);
//        Sheet sh = wb.createSheet();
//        for(int rownum = 0; rownum < 1000; rownum++){
//            Row row = sh.createRow(rownum);
//            for(int cellnum = 0; cellnum < 10; cellnum++){
//                Cell cell = row.createCell(cellnum);
//                String address = new CellReference(cell).formatAsString();
//                cell.setCellValue(address);
//            }
//        }
//
//        // Rows with rownum < 900 are flushed and not accessible
//        for(int rownum = 0; rownum < 900; rownum++){
//            Assert.assertNull(sh.getRow(rownum));
//        }
//
//        // ther last 100 rows are still in memory
//        for(int rownum = 900; rownum < 1000; rownum++){
//            Assert.assertNotNull(sh.getRow(rownum));
//        }
//
//        FileOutputStream out = new FileOutputStream("sxssf.xlsx");
//        wb.write(out);
//        out.close();
//
//        // dispose of temporary files backing this workbook on disk
//        wb.dispose();
//    }



//    public static void main(String[] args) throws Throwable {
//        FileInputStream inputStream = new FileInputStream("mytemplate.xlsx");
//        XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
//        inputStream.close();
//
//        SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
//        wb.setCompressTempFiles(true);
//
//        SXSSFSheet sh = wb.getSheetAt(0);
//        sh.setRandomAccessWindowSize(100);// keep 100 rows in memory, exceeding rows will be flushed to disk
//        for(int rownum = 4; rownum < 100000; rownum++){
//            Row row = sh.createRow(rownum);
//            for(int cellnum = 0; cellnum < 10; cellnum++){
//                Cell cell = row.createCell(cellnum);
//                String address = new CellReference(cell).formatAsString();
//                cell.setCellValue(address);
//            }
//        }
//
//        FileOutputStream out = new FileOutputStream("tempsxssf.xlsx");
//        wb.write(out);
//        out.close();
//    }

//    // Main driver method
//    public static void main(String[] args) {
//        // Blank workbook
//        XSSFWorkbook workbook = new XSSFWorkbook();
//
//        // Creating a blank Excel sheet
//        XSSFSheet sheet = workbook.createSheet("student Details");
//
//        // Creating an empty TreeMap of string and Object][]
//        // type
//        Map<String, Object[]> data = new TreeMap<>();
//
//        // Writing data to Object[]
//        // using put() method
//        data.put("1", new Object[]{"ID", "NAME", "LASTNAME"});
//        data.put("2", new Object[]{1, "Pankaj", "Kumar"});
//        data.put("3", new Object[]{2, "Prakashni", "Yadav"});
//        data.put("4", new Object[]{3, "Ayan", "Mondal"});
//        data.put("5", new Object[]{4, "Virat", "kohli"});
//
//        // Iterating over data and writing it to sheet
//        Set<String> keyset = data.keySet();
//
//        int rownum = 0;
//
//        for (String key : keyset) {
//            // Creating a new row in the sheet
//            Row row = sheet.createRow(rownum++);
//            Object[] objArr = data.get(key);
//
//            int cellnum = 0;
//
//            for (Object obj : objArr) {
//                // This line creates a cell in the next
//                //  column of that row
//                Cell cell = row.createCell(cellnum++);
//                if (obj instanceof String)
//                    cell.setCellValue((String) obj);
//                else if (obj instanceof Integer)
//                    cell.setCellValue((Integer) obj);
//            }
//        }
//
//        // Try block to check for exceptions
//        try {
//            // Writing the workbook
//            FileOutputStream out = new FileOutputStream(new File("gfgcontribute.xlsx"));
//            workbook.write(out);
//
//            // Closing file output connections
//            out.close();
//
//            // Console message for successful execution of
//            // program
//            System.out.println("gfgcontribute.xlsx written successfully on disk.");
//        }
//
//        // Catch block to handle exceptions
//        catch (Exception e) {
//            // Display exceptions along with line number
//            // using printStackTrace() method
//            e.printStackTrace();
//        }
//    }

//    public static void main(String[] args) throws IOException {
//        Workbook workbook = new XSSFWorkbook();
//
//        Sheet sheet = workbook.createSheet("Persons");
//        sheet.setColumnWidth(0, 6000);
//        sheet.setColumnWidth(1, 4000);
//
//        Row header = sheet.createRow(0);
//
//        CellStyle headerStyle = workbook.createCellStyle();
//        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
//        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
//        font.setFontName("Arial");
//        font.setFontHeightInPoints((short) 16);
//        font.setBold(true);
//        headerStyle.setFont(font);
//
//        Cell headerCell = header.createCell(0);
//        headerCell.setCellValue("Name");
//        headerCell.setCellStyle(headerStyle);
//
//        headerCell = header.createCell(1);
//        headerCell.setCellValue("Age");
//        headerCell.setCellStyle(headerStyle);
//
//
//
//        CellStyle style = workbook.createCellStyle();
//        style.setWrapText(true);
//
//        Row row = sheet.createRow(2);
//        Cell cell = row.createCell(0);
//        cell.setCellValue("John Smith");
//        cell.setCellStyle(style);
//
//        cell = row.createCell(1);
//        cell.setCellValue(20);
//        cell.setCellStyle(style);
//
//
//
//        File currDir = new File(".");
//        String path = currDir.getAbsolutePath();
//        String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";
//
//        FileOutputStream outputStream = new FileOutputStream(fileLocation);
//        workbook.write(outputStream);
//        workbook.close();
//    }
}
