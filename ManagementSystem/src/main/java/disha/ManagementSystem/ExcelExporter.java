package disha.ManagementSystem;

import disha.ManagementSystem.entity.Enquiry;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

public class ExcelExporter {

    private final XSSFWorkbook workbook = new XSSFWorkbook();
    private Sheet sheet;
    private final List<Enquiry> enquiryList;

    public ExcelExporter(List<Enquiry> enquiryList) {
        this.enquiryList = enquiryList;
    }

    private void writeHeader() {
        sheet = workbook.createSheet("Enquiries");
        Row header = sheet.createRow(0);

        String[] headers = {
                "ID", "Name", "Email", "Class mode",
                "Course", "Status", "Urgency"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(headers[i]);
            sheet.autoSizeColumn(i);
        }
    }

    private void writeData() {
        int rowIndex = 1;

        for (Enquiry e : enquiryList) {
            Row row = sheet.createRow(rowIndex++);

            Object[] data = {
                    e.getId(),
                    e.getStudent_name(),
                    e.getEmail(),
                    e.getClass_mode(),
                    e.getSubject(),
                    e.getStatus(),
                    e.getUrgency()
            };

            for (int i = 0; i < data.length; i++) {
                row.createCell(i).setCellValue(String.valueOf(data[i]));
                sheet.autoSizeColumn(i);
            }
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeader();
        writeData();

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
