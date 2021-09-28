package co.kr.talentx.openscout.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Builder // builder를 사용할수 있게 합니다.
//@Entity // jpa entity임을 알립니다.
@Getter
@Setter
//@ToString
@NoArgsConstructor // 인자없는 생성자를 자동으로 생성합니다.
@AllArgsConstructor // 인자를 모두 갖춘 생성자를 자동으로 생성합니다.
//@Table(name = "user") // 'user' 테이블과 매핑됨을 명시
public class IncomeExcelData {
    private long seq;
    private String name;                    // 이름
    private String residentRegistNo;        // 주민등록번호
    private String dateOfBirth;             // 생년월일
    private String incomeClassification;    // 소득구분
    private String totalPayment;            // 총지급액
    private String incomeTaxRate;           // 소득세율
    private String incomeTax;               // 소득세
    private String actualPayments;          // 실지급액
    private String companyName;             // 기업명
    private String companyRegistNo;         // 사업자등록번호
    private String paymentDate;             // 지급일
    private String incomeDetails;           // 소득내역
    private String businessStartDate;       // 업무시작일
    private String businessEndDate;         // 업무종료일
    private String deliveryCompliance;      // 납기준수
    private String workQuality;             // 업무품질
    private String satisfaction;            // 만족도
    private String overallRating;           // 전체평점

    // 수정시 데이터 처리
//    public User setUpdate(String name, String phoneNo, String dateOfBirth, String gender) {
//        this.name = name;
//        this.phoneNo = phoneNo;
//        this.dateOfBirth = dateOfBirth;
//        this.gender = gender;
//        return this;
//    }
}