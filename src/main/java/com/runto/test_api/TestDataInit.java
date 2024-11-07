package com.runto.test_api;

import com.runto.domain.gathering.dao.GatheringMemberCountRepository;
import com.runto.domain.gathering.dao.GatheringRepository;
import com.runto.domain.gathering.domain.*;
import com.runto.domain.gathering.type.GatheringType;
import com.runto.domain.gathering.type.GoalDistance;
import com.runto.domain.gathering.type.RunningConcept;
import com.runto.domain.user.dao.LocalAccountRepository;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.LocalAccount;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.type.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.runto.domain.gathering.type.GatheringMemberRole.ORGANIZER;
import static com.runto.domain.gathering.type.GatheringMemberRole.PARTICIPANT;
import static com.runto.domain.gathering.type.GatheringStatus.NORMAL;
import static com.runto.domain.user.type.Gender.MAN;
import static com.runto.domain.user.type.Gender.WOMAN;

@RequiredArgsConstructor
@Component
public class TestDataInit {

    private final UserRepository userRepository;
    private final LocalAccountRepository localAccountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final GatheringRepository gatheringRepository;
    private final GatheringMemberCountRepository gatheringMemberCountRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {

        String password = bCryptPasswordEncoder.encode("123456");

        List<User> users = new ArrayList<>();
        List<Gathering> gatherings = new ArrayList<>();


        // 회원 세팅
        for (int i = 1; i <= 10; i++) {

            LocalAccount localAccount = LocalAccount.builder()
                    .password(password)
                    .build();

            User user = User.builder()
                    .email("runto" + i + "@gmail.com")
                    .name("테스트유저이름" + i)
                    .nickname("테스트유저닉네임" + i)
                    .gender(MAN)
                    //.status()
                    .localAccount(localAccount)
                    .role(UserRole.USER)
                    .profileImageUrl(i+ "번유저 썸네일 url")
                    .build();

            users.add(user);
            userRepository.saveAll(users);
        }


        Location location = Location
                .of(new AddressName("address_name",
                                "1depth_name",
                                "2depth_name",
                                "3depth_name"),
                        new RegionCode(0, 0),
                        new Coordinates(new BigDecimal("0.0"), new BigDecimal("0.0")));


        for (int i = 0; i < 10; i++) {
            Gathering gathering = Gathering.builder()
                    .organizerId(users.get(i).getId())
                    .title("(기한 남음) 우리 모두 달립시다 - runto" + (i+1) + "유저")
                    .description("재밌게 달려봅시다.")
                    .appointedAt(LocalDateTime.now().plusDays(i + 2))
                    .deadline(LocalDateTime.now().plusDays(i + 1))
                    .concept(RunningConcept.HEALTH)
                    .goalDistance(GoalDistance.FREE)
                    .thumbnailUrl(i + "번 모임 썸네일")
                    //.hits(0)
                    .location(location)
                    .status(NORMAL)
                    .maxNumber(6)
                    //.currentNumber(0)
                    .gatheringType(GatheringType.GENERAL)
                    .build();
            gathering.addMember(users.get(i), ORGANIZER);

            for (int j = 0; j < 10; j++) {
                if (j == i) continue;

                if ((i % 2 == 0 && j % 2 == 0) || (i % 2 != 0 && j % 2 != 0)) {
                    gathering.addMember(users.get(j), PARTICIPANT);
                }
            }

            gatherings.add(gathering);
        }

//        for (int i = 0; i < 10; i++) {
//            Gathering gathering = Gathering.builder()
//                    .organizerId(users.get(i).getId())
//                    .title("(기한 만료) 우리 모두 달립시다 - runto" + (i+1) + "유저")
//                    .description("재밌게 달려봅시다." + i)
//                    .appointedAt(LocalDateTime.now().minusDays(i + 2))
//                    .deadline(LocalDateTime.now().minusDays(i + 1))
//                    .concept(RunningConcept.HEALTH)
//                    .goalDistance(GoalDistance.FREE)
//                    .thumbnailUrl(i + "번 모임 썸네일")
//                    //.hits(0)
//                    .location(location)
//                    .status(NORMAL)
//                    .maxNumber(10)
//                    //.currentNumber(0)
//                    .gatheringType(GatheringType.EVENT)
//                    .build();
//            gathering.applyForEvent();
//
//            gathering.addMember(users.get(i), ORGANIZER);
//
//            for (int j = 0; j < 10; j++) {
//                if (j == i) continue;
//
//                if ((i % 2 == 0 && j % 2 == 0) || (i % 2 != 0 && j % 2 != 0)) {
//                    gathering.addMember(users.get(j), PARTICIPANT);
//                }
//            }
//
//            gatherings.add(gathering);
//        }

        gatheringRepository.saveAll(gatherings);


        List<GatheringMemberCount> gatheringMemberCounts = new ArrayList<>();
        gatherings.forEach(gathering ->
                gatheringMemberCounts.add(GatheringMemberCount.from(gathering)));

        gatheringMemberCountRepository.saveAll(gatheringMemberCounts);

    }
}
