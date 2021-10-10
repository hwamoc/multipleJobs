package co.kr.talentx.openscout.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data   // getter, setter, toString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "income_raw")
public class Income {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 이름
    @NotBlank
    private String name;

    // 주민등록번호
    @NotBlank
    private String residentRegistNo;

    // 생년월일
    @NotBlank
    private String dateOfBirth;
//    LocalDate

    // 소득구분 ENUM not yet
    @NotBlank
    private String incomeClassification;

    // 총지급액
    @NotBlank
    private Integer totalPayment;

    // 소득세율
    @NotBlank
    private Double incomeTaxRate;

    // 소득세
    @NotBlank
    private String incomeTax;

    // 실지급액
    @NotBlank
    private String actualPayments;

    // 기업명
    @NotBlank
    private String companyName;

    // 사업자등록번호
    @NotBlank
    private String companyRegistNo;

    // 지급일
    @NotBlank
    private String paymentDate;
//    LocalDate

    // 소득내역
    @NotBlank
    private String incomeDetails;

    // 업무시작일
    @NotBlank
    private String  businessStartDate;
//    LocalDate

    // 업무종료일
    @NotBlank
    private String  businessEndDate;
//    LocalDate

    // 납기준수
    @NotBlank
    private Integer deliveryCompliance;

    // 업무품질
    @NotBlank
    private Integer workQuality;

    // 만족도
    @NotBlank
    private Integer satisfaction;

    // 전체평점
    @NotBlank
    private Double overallRating;

}