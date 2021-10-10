package co.kr.talentx.openscout.controller;


import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobLauncherController {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job uploadCSVJob;

    @Operation(summary = "오픈 스카우트 과제 요구사항 3", description = "csv 파일 기준으로 구현되었습니다.")
    @PostMapping("/admin/upload-csv")
    public String handle(@Parameter(description = "csv 파일 이름 (프로젝트 디렉토리에 해당 파일을 복사해주세요.)", required = true, example = "data.csv") @RequestParam("fileName") String fileName) throws Exception{
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("fileName", fileName)
                .toJobParameters();
        jobLauncher.run(uploadCSVJob, jobParameters);
        return "success";
    }
}