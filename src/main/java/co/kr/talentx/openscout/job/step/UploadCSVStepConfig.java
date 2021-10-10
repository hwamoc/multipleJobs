package co.kr.talentx.openscout.job.step;

import co.kr.talentx.openscout.entity.Income;
import co.kr.talentx.openscout.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class UploadCSVStepConfig {

    private static final String NAME = "uploadCSVJobStep";
    private final IncomeRepository incomeRepository;
    private final StepBuilderFactory stepBuilderFactory;
    @Value("${traffic_upper_limit:10000}")
    private int trafficUpperLimit;

    @Value("${chunk_size:1000}")
    private int chunkSize;

    @JobScope
    @Bean(NAME + "Step")
    public Step step() {
        return stepBuilderFactory.get(NAME + "Step")
                .<Income, Income>chunk(chunkSize)
                .reader(reader(null))
                .writer(writer())
                .build();
    }

    @StepScope
    @Bean(NAME + "Reader")
    public FlatFileItemReader<Income> reader(
            @Value("#{jobParameters[fileName]}") String fileName) {

        //Create reader instance
        FlatFileItemReader<Income> reader = new FlatFileItemReader<Income>();

        //Set input file location
        reader.setResource(new FileSystemResource(fileName));

        //Set number of lines to skips. Use it if file has header rows.
        reader.setLinesToSkip(1);

        reader.setLineMapper(new DefaultLineMapper() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "id", "name", "residentRegistNo", "dateOfBirth", "incomeClassification", "totalPayment", "incomeTaxRate", "incomeTax", "actualPayments",
                                "companyName", "companyRegistNo", "paymentDate", "incomeDetails", "businessStartDate", "businessEndDate", "deliveryCompliance", "workQuality", "satisfaction", "overallRating" });

                    }
                });
                //Set values in Employee class
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Income>() {
                    {
                        setTargetType(Income.class);
                    }
                });
            }
        });
        return reader;
    }



    @StepScope
    @Bean(NAME + "Writer")
    public ItemWriter<Income> writer() {
        return incomeRaws -> incomeRaws.forEach(this::setIncomeRaws);
    }

    private void setIncomeRaws(Income p) {
        try {
            Double overallRatingTemp = Double.valueOf((p.getDeliveryCompliance() + p.getWorkQuality() + p.getSatisfaction()) / 3); // 전체평점 = (납기준수 + 업무품질 + 만족도) / 3
            p.setOverallRating(Math.round(overallRatingTemp * 100) / 100.0);

            if (p.getActualPayments() != null && !p.getActualPayments().trim().isEmpty()) {
                Double incomeTaxTemp = Double.parseDouble(p.getActualPayments()) * p.getIncomeTaxRate();  // 소득세 = 총지급액 * 소득세율
                p.setIncomeTax(String.valueOf(Math.round(incomeTaxTemp * 100) / 100.0));

                Double actualPaymentsTemp = Double.parseDouble(p.getActualPayments()) - Double.parseDouble(p.getIncomeTax());  // 실지급액 = 총지급액 - 소득세
                p.setActualPayments(String.valueOf(Math.round(actualPaymentsTemp * 100) / 100.0));
            }

        Object[] values = { p.getName(), p.getResidentRegistNo(), p.getDateOfBirth(), p.getIncomeClassification(), p.getTotalPayment(), p.getIncomeTaxRate(), p.getIncomeTax(), p.getActualPayments(),
        p.getCompanyName(), p.getCompanyRegistNo(), p.getPaymentDate(), p.getIncomeDetails(), p.getBusinessStartDate(), p.getBusinessEndDate(), p.getDeliveryCompliance(), p.getWorkQuality(), p.getSatisfaction(), p.getOverallRating() };
        boolean containsEmpty = Arrays.stream(values).anyMatch(""::equals);

        if (!containsEmpty) {
            System.out.println("IncomeRaws" + p.toString());
            incomeRepository.save(p);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


