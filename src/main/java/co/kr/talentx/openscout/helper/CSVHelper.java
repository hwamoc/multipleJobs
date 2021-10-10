package co.kr.talentx.openscout.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;
import co.kr.talentx.openscout.entity.Income;

public class CSVHelper {
    public static String TYPE = "text/csv";
    static String[] HEADERs = { "이름", "주민등록번호", "생년월일", "소득구분", "총지급액", "소득세율", "소득세", "실지급액", "기업명", "사업자등록번호", "지급일", "소득내역", "업무시작일", "업무종료일", "납기준수", "업무품질", "만족도", "전체평점" };

    public static boolean hasCSVFormat(MultipartFile file) {
        System.out.println("what csv" + file.getContentType());
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<Income> csvToIncomeData(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Income> dataList = new ArrayList<Income>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            loop:
            for (CSVRecord csvRecord : csvRecords) {
                try {
                    String values[] = {csvRecord.get(0), csvRecord.get(1), csvRecord.get(2), csvRecord.get(3), csvRecord.get(4), csvRecord.get(5),
                            csvRecord.get(6), csvRecord.get(7), csvRecord.get(8), csvRecord.get(9), csvRecord.get(10), csvRecord.get(11), csvRecord.get(12),
                            csvRecord.get(13), csvRecord.get(14), csvRecord.get(15), csvRecord.get(16), csvRecord.get(17), csvRecord.get(18)};

                    Double overallRatingTemp = (Double.parseDouble(values[15]) + Double.parseDouble(values[16]) + Double.parseDouble(values[17])) / 3; // 전체평점 = (납기준수 + 업무품질 + 만족도) / 3
                    values[18] = String.valueOf(Math.round(overallRatingTemp * 100) / 100.0);

                    Double incomeTaxTemp = Double.parseDouble(values[5]) * Double.parseDouble(values[6]);  // 소득세 = 총지급액 * 소득세율
                    values[7] = String.valueOf(Math.round(incomeTaxTemp * 100) / 100.0);

                    Double actualPaymentsTemp = Double.parseDouble(values[5]) - Double.parseDouble(values[7]);  // 실지급액 = 총지급액 - 소득세
                    values[8] = String.valueOf(Math.round(actualPaymentsTemp * 100) / 100.0);

                    boolean checked = false;
                    for (int i = 0; i <= values.length-1; i++) {
                        if (values[i] == null) {
                            break loop;
                        } else if (i == values.length-1) {
                            checked = true;
                        }
                    }
                    if (checked) {
                        DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        Income data = new Income(
                                Long.parseLong(values[0]),
                                values[1],//"이름"
                                values[2],//"주민등록번호"
                                values[3],
//                                LocalDate.parse(values[3], DATEFORMATTER),//"생년월일"
                                values[4],//"소득구분"
                                Integer.valueOf(values[5]),//"총지급액"
                                Double.parseDouble(values[6]),//"소득세율"
                                values[7],//"소득세"
                                values[8],//"실지급액"
                                values[9],//"기업명"
                                values[10],//"사업자등록번호"
                                values[11],
//                                LocalDate.parse(values[11], DATEFORMATTER),//"지급일"
                                values[12],//"소득내역"
                                values[13],
//                                LocalDate.parse(values[13], DATEFORMATTER),//"업무시작일"
                                values[14],
//                                LocalDate.parse(values[14], DATEFORMATTER),//"업무종료일"
                                Integer.valueOf(values[15]),//"납기준수"
                                Integer.valueOf(values[16]),//"업무품질"
                                Integer.valueOf(values[17]),//"만족도"
                                Double.parseDouble(values[18])//"전체평점"
                        );
//                    System.out.println("datadata: " + data);
                        dataList.add(data);
                    }
                } catch (Exception e) {
                    System.out.println("구멍난 데이터" + e);
                }
            }
//            System.out.println("dataList: " + dataList);
            return dataList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}

