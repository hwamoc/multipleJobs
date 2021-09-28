package co.kr.talentx.openscout.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;
import co.kr.talentx.openscout.entity.IncomeExcelData;

public class CSVHelper {
    public static String TYPE = "text/csv";
    static String[] HEADERs = { "이름", "주민등록번호", "생년월일", "소득구분", "총지급액", "소득세율", "소득세", "실지급액", "기업명", "사업자등록번호", "지급일", "소득내역", "업무시작일", "업무종료일", "납기준수", "업무품질", "만족도", "전체평점" };


     public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<IncomeExcelData> csvToIncomeData(InputStream is) {
        System.out.println("csvParser 1 ");
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            System.out.println("csvParser" + csvParser);

            List<IncomeExcelData> dataList = new ArrayList<IncomeExcelData>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                IncomeExcelData data = new IncomeExcelData(
                        Long.parseLong(csvRecord.get("")),
                        csvRecord.get("이름"),
                        csvRecord.get("주민등록번호"),
                        csvRecord.get("생년월일"),
                        csvRecord.get("소득구분"),
                        csvRecord.get("총지급액"),
                        csvRecord.get("소득세율"),
                        csvRecord.get("소득세"),
                        csvRecord.get("실지급액"),
                        csvRecord.get("기업명"),
                        csvRecord.get("사업자등록번호"),
                        csvRecord.get("지급일"),
                        csvRecord.get("소득내역"),
                        csvRecord.get("업무시작일"),
                        csvRecord.get("업무종료일"),
                        csvRecord.get("납기준수"),
                        csvRecord.get("업무품질"),
                        csvRecord.get("만족도"),
                        csvRecord.get("전체평점")
                );

                System.out.println(data);
                dataList.add(data);
            }

            return dataList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

}

