package com.runto.test_api;

import com.runto.domain.gathering.dao.GatheringRepository;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.domain.Location;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.runto.domain.gathering.type.GatheringMemberRole.ORGANIZER;
import static com.runto.domain.gathering.type.GatheringMemberRole.PARTICIPANT;
import static com.runto.domain.user.type.Gender.WOMAN;

@RequiredArgsConstructor
@Component
public class TestDataInit {

    private final UserRepository userRepository;
    private final LocalAccountRepository localAccountRepository;

    private final GatheringRepository gatheringRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {

        List<User> users = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            LocalAccount localAccount = LocalAccount.builder()
                    .password("123456")
                    .build();

            users.add(User.builder()
                    .email("runto@gmail.com")
                    .name("테스트유저이름" + i + 1)
                    .nickname("테스트유저닉네임" + i + 1)
                    .gender(WOMAN)
                    //.status()
                    .localAccount(localAccount)
                    .role(UserRole.USER)
                    .profileImageUrl(i + 1 + "번유저 썸네일 url")
                    .build());
        }
        users = userRepository.saveAll(users);


        List<Gathering> gatherings = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Gathering gathering = Gathering.builder()
                    .organizerId(users.get(i).getId())
                    .title("(기한 남음) 우리 모두 달립시다 " + i)
                    .description("재밌게 달려봅시다." + i)
                    .appointedAt(LocalDateTime.now().plusDays(i + 2))
                    .deadline(LocalDateTime.now().plusDays(i + 1))
                    .concept(RunningConcept.HEALTH)
                    .goalDistance(GoalDistance.FREE)
                    .thumbnailUrl(i + "번 모임 썸네일")
                    //.hits(0)
                    .location(Location.of("우리집 앞", 0.0, 0.0))
                    //.status(NORMAL)
                    .maxNumber(10)
                    .currentNumber(1)
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

        for (int i = 0; i < 10; i++) {
            Gathering gathering = Gathering.builder()
                    .organizerId(users.get(i).getId())
                    .title("(기한 만료) 우리 모두 달립시다 " + i)
                    .description("재밌게 달려봅시다." + i)
                    .appointedAt(LocalDateTime.now().minusDays(i + 2))
                    .deadline(LocalDateTime.now().minusDays(i + 1))
                    .concept(RunningConcept.HEALTH)
                    .goalDistance(GoalDistance.FREE)
                    .thumbnailUrl(i + "번 모임 썸네일")
                    //.hits(0)
                    .location(Location.of("우리집 앞", 0.0, 0.0))
                    //.status(NORMAL)
                    .maxNumber(10)
                    .currentNumber(1)
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



        gatherings = gatheringRepository.saveAll(gatherings);


    }
}
