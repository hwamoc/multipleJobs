package co.kr.talentx.openscout.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor    // 생성자 DI를 위한 어노테이션
public class JobConfig {
    private static final String NAME = "uploadCSVJob";
    private final JobBuilderFactory jobBuilderFactory;
    private final Step uploadCSVStep;

    @Bean
    public Job updateProductPodJob() {
        return jobBuilderFactory.get(NAME)
                .start(uploadCSVStep)
                .build();
    }
}
