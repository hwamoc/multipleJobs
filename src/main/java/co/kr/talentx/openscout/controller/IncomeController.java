package co.kr.talentx.openscout.controller;

import co.kr.talentx.openscout.entity.Income;
import co.kr.talentx.openscout.helper.CSVHelper;
import co.kr.talentx.openscout.service.IncomeService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
public class IncomeController {

  @Autowired
  IncomeService incomeRawService;

  @Operation(summary = "오픈 스카우트 과제 요구사항 3 검증", description = "N잡러들의 소득과 평판 정보 파일을 전처리한 데이터들을 확인합니다.")
  @GetMapping("/admin/csvdatas")
  public List<Income> main() { // 1
    return incomeRawService.getAllIncomeRaws();
  }

  @PostMapping("/excel/read")
  public String readExcel(@RequestParam("file") MultipartFile file, Model model)
          throws IOException { // 2
    String message = "";
    List<Income> dataList = new ArrayList<Income>();

    String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3

    if (!extension.equals("csv") && !extension.equals("xlsx") && !extension.equals("xls")) {
      throw new IOException("엑셀파일만 업로드 해주세요.");
    }

    // 윈도우에서 구현해야함
//    if (extension.equals("xlsx")) {
//      Workbook workbook = null;
//
//      if (extension.equals("xlsx")) {
//        workbook = new XSSFWorkbook(file.getInputStream());
//      } else if (extension.equals("xls")) {
//        workbook = new HSSFWorkbook(file.getInputStream());
//      }
//
//      Sheet worksheet = workbook.getSheetAt(0);
//      System.out.println("worksheet" + worksheet);
//
//      for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) { // 4
//
//        Row row = worksheet.getRow(i);
//
//        IncomeRaw data = new IncomeRaw();
//        data.setId((long) row.getCell(0).getNumericCellValue());
//        data.setName(row.getCell(1).getStringCellValue());
//        data.setResidentRegistNo(row.getCell(2).getStringCellValue());
//        data.setDateOfBirth(row.getCell(3).getStringCellValue());
//        data.setIncomeClassification(row.getCell(4).getStringCellValue());
//        data.setTotalPayment(row.getCell(5).getStringCellValue());
//        data.setIncomeTaxRate(row.getCell(6).getStringCellValue());
//        data.setIncomeTax(row.getCell(7).getStringCellValue());
//        data.setActualPayments(row.getCell(8).getStringCellValue());
//        data.setCompanyName(row.getCell(9).getStringCellValue());
//        data.setCompanyRegistNo(row.getCell(10).getStringCellValue());
//        data.setPaymentDate(row.getCell(11).getStringCellValue());
//        data.setIncomeDetails(row.getCell(12).getStringCellValue());
//        data.setBusinessStartDate(row.getCell(13).getStringCellValue());
//        data.setBusinessEndDate(row.getCell(14).getStringCellValue());
//        data.setDeliveryCompliance(row.getCell(15).getStringCellValue());
//        data.setWorkQuality(row.getCell(16).getStringCellValue());
//        data.setSatisfaction(row.getCell(17).getStringCellValue());
//        data.setOverallRating(row.getCell(18).getStringCellValue());
//
//        dataList.add(data);
//      }
//
//    } else
      if (extension.equals("csv")) {
      if (CSVHelper.hasCSVFormat(file)) {
        try {
          incomeRawService.save(file);
          dataList = incomeRawService.getAllIncomeRaws();
          message = "Uploaded the file successfully: " + file.getOriginalFilename();
//          return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
          message = "Could not upload the file: " + file.getOriginalFilename() + "!";
//          return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
      }
      message = "Please upload a csv file!";
    }
    model.addAttribute("datas", dataList); // 5

    return "excelList";
  }



}
