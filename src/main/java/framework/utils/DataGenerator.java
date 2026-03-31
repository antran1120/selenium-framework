package framework.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataGenerator {
    public static void main(String[] args) {
        Workbook workbook = new XSSFWorkbook();

        // 1. SmokeCases
        Sheet smokeSheet = workbook.createSheet("SmokeCases");
        Row smokeHeader = smokeSheet.createRow(0);
        smokeHeader.createCell(0).setCellValue("username");
        smokeHeader.createCell(1).setCellValue("password");
        smokeHeader.createCell(2).setCellValue("expected_url");
        smokeHeader.createCell(3).setCellValue("description");

        Object[][] smokeData = {
            {"standard_user", "secret_sauce", "inventory.html", "Valid user"},
            {"problem_user", "secret_sauce", "inventory.html", "Problem user"},
            {"performance_glitch_user", "secret_sauce", "inventory.html", "Glitch user"}
        };
        for (int i = 0; i < smokeData.length; i++) {
            Row row = smokeSheet.createRow(i + 1);
            for (int j = 0; j < smokeData[i].length; j++) {
                row.createCell(j).setCellValue(smokeData[i][j].toString());
            }
        }

        // 2. NegativeCases
        Sheet negativeSheet = workbook.createSheet("NegativeCases");
        Row negativeHeader = negativeSheet.createRow(0);
        negativeHeader.createCell(0).setCellValue("username");
        negativeHeader.createCell(1).setCellValue("password");
        negativeHeader.createCell(2).setCellValue("expected_error");
        negativeHeader.createCell(3).setCellValue("description");

        Object[][] negativeData = {
            {"invalid_user", "secret_sauce", "Epic sadface: Username and password do not match any user in this service", "Invalid username"},
            {"standard_user", "invalid_pass", "Epic sadface: Username and password do not match any user in this service", "Invalid password"},
            {"locked_out_user", "secret_sauce", "Epic sadface: Sorry, this user has been locked out.", "Locked out user"},
            {"", "secret_sauce", "Epic sadface: Username is required", "Empty username"},
            {"standard_user", "", "Epic sadface: Password is required", "Empty password"}
        };
        for (int i = 0; i < negativeData.length; i++) {
            Row row = negativeSheet.createRow(i + 1);
            for (int j = 0; j < negativeData[i].length; j++) {
                row.createCell(j).setCellValue(negativeData[i][j].toString());
            }
        }

        // 3. BoundaryCases
        Sheet boundarySheet = workbook.createSheet("BoundaryCases");
        Row boundaryHeader = boundarySheet.createRow(0);
        boundaryHeader.createCell(0).setCellValue("username");
        boundaryHeader.createCell(1).setCellValue("password");
        boundaryHeader.createCell(2).setCellValue("expected_error");
        boundaryHeader.createCell(3).setCellValue("description");

        Object[][] boundaryData = {
            {"standard_user".repeat(10), "secret_sauce", "Epic sadface: Username and password do not match any user in this service", "Very long username"},
            {"standard_user", "secret_sauce".repeat(10), "Epic sadface: Username and password do not match any user in this service", "Very long password"},
            {"<script>alert(1)</script>", "secret_sauce", "Epic sadface: Username and password do not match any user in this service", "XSS in username"},
            {"' OR '1'='1", "secret_sauce", "Epic sadface: Username and password do not match any user in this service", "SQL Injection in username"}
        };
        for (int i = 0; i < boundaryData.length; i++) {
            Row row = boundarySheet.createRow(i + 1);
            for (int j = 0; j < boundaryData[i].length; j++) {
                row.createCell(j).setCellValue(boundaryData[i][j].toString());
            }
        }

        File folder = new File("src/test/resources/testdata");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (FileOutputStream outputStream = new FileOutputStream("src/test/resources/testdata/login_data.xlsx")) {
            workbook.write(outputStream);
            System.out.println("Excel file successfully created");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
