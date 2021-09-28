package co.kr.talentx.openscout.controller;

import co.kr.talentx.openscout.entity.IncomeExcelData;
import co.kr.talentx.openscout.helper.CSVHelper;
import co.kr.talentx.openscout.service.CSVService;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ExcelController {

//  @Autowired
//  CSVService fileService;

  @GetMapping("/excel")
  public String main() { // 1
    return "excel";
  }

  @PostMapping("/excel/read")
  public String readExcel(@RequestParam("file") MultipartFile file, Model model)
          throws IOException { // 2

    List<IncomeExcelData> dataList = new ArrayList<IncomeExcelData>();

    String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3

    if (!extension.equals("csv") && !extension.equals("xlsx") && !extension.equals("xls")) {
      throw new IOException("엑셀파일만 업로드 해주세요.");
    }

    if (extension.equals("xlsx")) {
      Workbook workbook = null;

      if (extension.equals("xlsx")) {
        workbook = new XSSFWorkbook(file.getInputStream());
      } else if (extension.equals("xls")) {
        workbook = new HSSFWorkbook(file.getInputStream());
      }

      Sheet worksheet = workbook.getSheetAt(0);
      System.out.println("worksheet" + worksheet);

      for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) { // 4

        Row row = worksheet.getRow(i);

        IncomeExcelData data = new IncomeExcelData();
        data.setSeq((long) row.getCell(0).getNumericCellValue());
        data.setName(row.getCell(1).getStringCellValue());
        data.setResidentRegistNo(row.getCell(2).getStringCellValue());
        data.setDateOfBirth(row.getCell(3).getStringCellValue());
        data.setIncomeClassification(row.getCell(4).getStringCellValue());
        data.setTotalPayment(row.getCell(5).getStringCellValue());
        data.setIncomeTaxRate(row.getCell(6).getStringCellValue());
        data.setIncomeTax(row.getCell(7).getStringCellValue());
        data.setActualPayments(row.getCell(8).getStringCellValue());
        data.setCompanyName(row.getCell(9).getStringCellValue());
        data.setCompanyRegistNo(row.getCell(10).getStringCellValue());
        data.setPaymentDate(row.getCell(11).getStringCellValue());
        data.setIncomeDetails(row.getCell(12).getStringCellValue());
        data.setBusinessStartDate(row.getCell(13).getStringCellValue());
        data.setBusinessEndDate(row.getCell(14).getStringCellValue());
        data.setDeliveryCompliance(row.getCell(15).getStringCellValue());
        data.setWorkQuality(row.getCell(16).getStringCellValue());
        data.setSatisfaction(row.getCell(17).getStringCellValue());
        data.setOverallRating(row.getCell(18).getStringCellValue());

        dataList.add(data);
      }

    } else if (extension.equals("csv")) {
      if (CSVHelper.hasCSVFormat(file)) {
        try {
//          dataList = fileService.save(file);
          dataList = CSVHelper.csvToIncomeData(file.getInputStream());

//          message = "Uploaded the file successfully: " + file.getOriginalFilename();
//          return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
          String message = "Could not upload the file: " + file.getOriginalFilename() + "!";
          return message;
//          return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
      }

      String message = "Please upload a csv file!";
    }



    model.addAttribute("datas", dataList); // 5

    return "excelList";

  }



}
