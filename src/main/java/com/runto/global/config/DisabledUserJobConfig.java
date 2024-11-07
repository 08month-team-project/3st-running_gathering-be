package com.runto.global.config;

import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.type.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DisabledUserJobConfig {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    // Job 생성
    @Bean("disabledUserJob")
    public Job disabledUserJob(Step disabledUserStep) {
        // JobBuilder를 사용하여 Job 생성
        return new JobBuilder("disabledUserJob", jobRepository)
                .preventRestart() // Job 재실행 방지
                .start(disabledUserStep) // Step 시작
                .build();
    }

    // Step 생성
    @JobScope
    @Bean("disabledUserStep")
    public Step disabledUserStep(ItemReader<User> itemReader,
                                 ItemProcessor<User, User> itemProcessor,
                                 ItemWriter<User> itemWriter) {

        // StepBuilder를 사용하여 Step 생성
        return new StepBuilder("disabledUserStep", jobRepository)
                .<User, User>chunk(10, transactionManager) // Chunk 사이즈 설정
                .reader(itemReader) // Reader 설정
                .processor(itemProcessor) // Processor 설정
                .writer(itemWriter) // Writer 설정
                .build();
    }

    //추후 최근로그인 기준으로
    @Bean
    @StepScope
    public ListItemReader<User> disabledUserReader() {
        // 1년 이상 수정되지 않은 활성 사용자 조회
        List<User> oldUsers = userRepository.findByModifiedAtBeforeAndStatusEquals(
                LocalDateTime.now().minusYears(1), UserStatus.ACTIVE);
        return new ListItemReader<>(oldUsers); // ListItemReader로 반환
    }

    // Processor 정의: User 상태를 비활성화로 설정
    @Bean
    public ItemProcessor<User, User> disabledUserProcessor() {
        return user -> {
            // 상태를 비활성화로 설정하는 메서드 호출
            user.disabledUser();
            return user;
        };
    }

    // Writer 정의: 사용자 저장
    @Bean
    public ItemWriter<User> disabledUserWriter() {
        return userRepository::saveAll; // 사용자 저장 메서드 호출
    }
}