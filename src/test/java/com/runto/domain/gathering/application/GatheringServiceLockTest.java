package com.runto.domain.gathering.application;

import com.runto.domain.gathering.application.GatheringService;
import com.runto.domain.gathering.dao.GatheringMemberCountRepository;
import com.runto.domain.gathering.dao.GatheringMemberRepository;
import com.runto.domain.gathering.dao.GatheringRepository;
import com.runto.domain.gathering.dao.GatheringViewRecordRepository;
import com.runto.domain.gathering.domain.*;
import com.runto.domain.gathering.exception.GatheringException;
import com.runto.domain.gathering.type.GoalDistance;
import com.runto.domain.gathering.type.RunningConcept;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.LocalAccount;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.type.UserRole;
import com.runto.global.config.QueryDSLConfig;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.runto.domain.gathering.type.GatheringMemberRole.ORGANIZER;
import static com.runto.domain.gathering.type.GatheringStatus.NORMAL;
import static com.runto.domain.gathering.type.GatheringType.GENERAL;
import static com.runto.domain.user.type.Gender.MAN;
import static com.runto.domain.user.type.UserStatus.ACTIVE;
import static com.runto.global.exception.ErrorCode.GATHERING_MEMBER_COUNT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@Rollback(value = false)
@Slf4j
@Import(QueryDSLConfig.class)
@ActiveProfiles("local") // test 로 잡으면 빈 생성 문제 있음
@SpringBootTest
class GatheringServiceLockTest {

//    @Autowired
//    private GatheringViewCountService gatheringViewCountService;

    @Autowired
    private GatheringService gatheringService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GatheringRepository gatheringRepository;

    @Autowired
    private GatheringViewRecordRepository gatheringViewRecordRepository;

    @Autowired
    private GatheringMemberRepository gatheringMemberRepository;

    @Autowired
    private GatheringMemberCountRepository gatheringMemberCountRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private List<User> users;
    private Gathering gathering;
    private GatheringMemberCount gatheringMemberCount;

    @BeforeEach
    public void init() {
        users = new ArrayList<>();


        for (int i = 1; i <= 100; i++) {

            String password = bCryptPasswordEncoder.encode("123456");

            LocalAccount localAccount = LocalAccount.builder()
                    .password(password)
                    .build();

            User user = User.builder()
                    .email("view_count_test" + i + "@gmail.com")
                    .name("테스트유저이름" + i)
                    .nickname("view_test" + i)
                    .gender(MAN)
                    .status(ACTIVE)
                    .localAccount(localAccount)
                    .role(UserRole.USER)
                    .profileImageUrl(i + "번유저 썸네일 url")
                    .build();

            users.add(user);
        }
        users = userRepository.saveAll(users);

        Location location = Location
                .of(new AddressName("address_name",
                                "1depth_name",
                                "2depth_name",
                                "3depth_name"),
                        new RegionCode(0, 0),
                        new Coordinates(new BigDecimal("0.0"), new BigDecimal("0.0")));


        gathering = Gathering.builder()
                .organizerId(users.get(0).getId())
                .title("조회수 동시성 테스트")
                .description("재밌게 달려봅시다.")
                .appointedAt(LocalDateTime.now().plusDays(2))
                .deadline(LocalDateTime.now().plusDays(1))
                .concept(RunningConcept.HEALTH)
                .goalDistance(GoalDistance.FREE)
                .thumbnailUrl("모임 썸네일")
                .hits(0L)
                .location(location)
                .status(NORMAL)
                .maxNumber(100)
                .currentNumber(0)
                .gatheringType(GENERAL)
                .build();

        gathering.addMember(users.get(0), ORGANIZER);
        gathering.updateCurrentNumber(1);

        gathering = gatheringRepository.save(gathering);

        gatheringMemberCount = gatheringMemberCountRepository
                .save(GatheringMemberCount.from(gathering));
    }

    // Gathering 하나
    // 서로 다른 유저 100명

    @DisplayName("100개 요청")
    @Test
    void addGatheringViewRecord() throws InterruptedException {
        int threadCount = users.size();
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int userIdx = i; // 자바의 람다와 익명 클래스 내부에서 사용되는 변수는 final 또는 effectively final 이어야 함

            executorService.submit(() -> {
                try {
                    gatheringService.hitGathering(users.get(userIdx).getId(), gathering.getId());

                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Gathering findGathering = gatheringRepository
                .findById(gathering.getId()).orElseThrow();

        int hitCount = gatheringViewRecordRepository
                .countByGatheringId(findGathering.getId());

        assertEquals(users.size(), findGathering.getHits());
        assertEquals(users.size(), hitCount);
    }

    @DisplayName("참가신청 99개 요청 (주최자 포함 100개)")
    @Test
    void addGatheringMember() throws InterruptedException {
        int threadCount = users.size();
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int userIdx = i; // 자바의 람다와 익명 클래스 내부에서 사용되는 변수는 final 또는 effectively final 이어야 함

            executorService.submit(() -> {
                try {
                    gatheringService.participateGathering(users.get(userIdx).getId(), gathering.getId());

                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Gathering findGathering = gatheringRepository
                .findById(gathering.getId()).orElseThrow();

        int memberCount = gatheringMemberRepository
                .countByGatheringId(findGathering.getId());

        /**
         * Gathering 의 조회수 기능은 낙관적 락을 적용해볼려고, Gathering 에  @Version 를 적용했었는데,
         * 이 때, 참가 기능이 제대로 동작하지 않는다.
         * 비관적 락을 적용해서 가져온 GatheringMemberCount 도 동시성 제어 자체가 안되고있다. (이건 다른 엔티티인데, 왜인지..?)
         * @Version 을 적용하지않으면 의도한 대로 동작한다.
         */
        GatheringMemberCount findGatheringMemberCount = gatheringMemberCountRepository.findByGatheringIdWithPessimisticLock(findGathering.getId())
                .orElseThrow(() -> new GatheringException(GATHERING_MEMBER_COUNT_NOT_FOUND));


        assertEquals(users.size(), findGatheringMemberCount.getCurrentNumber());
        assertEquals(users.size(), findGathering.getCurrentNumber());
        assertEquals(users.size(), memberCount);
    }

}