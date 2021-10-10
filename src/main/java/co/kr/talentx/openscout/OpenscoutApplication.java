package co.kr.talentx.openscout;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class OpenscoutApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenscoutApplication.class, args);
	}

}
