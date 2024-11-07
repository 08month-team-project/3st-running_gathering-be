//package com.runto.global.config;
//
//import com.runto.domain.user.dao.UserRepository;
//import com.runto.domain.user.domain.User;
//import com.runto.domain.user.type.UserRole;
//import com.runto.domain.user.type.UserStatus;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.test.JobLauncherTestUtils;
//import org.springframework.batch.test.context.SpringBatchTest;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import java.util.Collections;
//
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@SpringBatchTest
//public class DisabledUserJobConfigTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private JobRepository jobRepository;
//
//    @Mock
//    private PlatformTransactionManager transactionManager;
//
//    @InjectMocks
//    private DisabledUserJobConfig disabledUserJobConfig;
//
//    private JobLauncherTestUtils jobLauncherTestUtils;
//
//    @BeforeEach
//    public void setUp() {
//        jobLauncherTestUtils = new JobLauncherTestUtils();
//        jobLauncherTestUtils.setJob(disabledUserJobConfig.disabledUserJob(disabledUserStep()));
//        jobLauncherTestUtils.setJobLauncher(mock(JobLauncher.class));
//    }
//
//    @Test
//    public void testDisabledUserJob() throws Exception {
//        // Given: 필요한 Mock 설정
//        User user = User.builder()
//                .email("email@email.com")
//                .name("name")
//                .role(UserRole.USER)
//                .status(UserStatus.ACTIVE)
//                .build(); // 임의의 User 객체 생성
//        when(userRepository.findByModifiedAtBeforeAndStatusEquals(any(), any())).thenReturn(Collections.singletonList(user));
//        doNothing().when(userRepository).saveAll(any());
//
//        // When: Job 실행
//        jobLauncherTestUtils.launchJob();
//
//        // Then: userRepository의 saveAll 메서드가 호출되었는지 확인
//        verify(userRepository, times(1)).saveAll(any());
//    }
//
//    private Step disabledUserStep() {
//        ItemReader<User> reader = disabledUserJobConfig.disabledUserReader();
//        ItemProcessor<User, User> processor = disabledUserJobConfig.disabledUserProcessor();
//        ItemWriter<User> writer = disabledUserJobConfig.disabledUserWriter();
//        return disabledUserJobConfig.disabledUserStep(reader, processor, writer);
//    }
//}