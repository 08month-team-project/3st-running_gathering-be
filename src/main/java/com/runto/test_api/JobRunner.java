package com.runto.test_api;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//앱이 실행될 때 배치로 일괄처리
@Component
@RequiredArgsConstructor
public class JobRunner implements CommandLineRunner {

    final private JobLauncher jobLauncher;

    final private Job disabledUserJob;

    @Override
    public void run(String... args) throws Exception {
        jobLauncher.run(disabledUserJob, new JobParameters());
    }
}
