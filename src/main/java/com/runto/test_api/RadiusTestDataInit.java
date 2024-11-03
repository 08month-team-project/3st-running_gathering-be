package com.runto.test_api;

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
import org.springframework.data.util.Pair;
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
import static com.runto.domain.user.type.Gender.WOMAN;

@RequiredArgsConstructor
@Component
public class RadiusTestDataInit {

    private final UserRepository userRepository;
    private final LocalAccountRepository localAccountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final GatheringRepository gatheringRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {

        String password = bCryptPasswordEncoder.encode("123456");

        List<User> users = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            LocalAccount localAccount = LocalAccount.builder()
                    .password(password)
                    .build();

            users.add(User.builder()
                    .email("runto" + i + "@gmail.com")
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


//        이순신 장군 동상 [서울 종로구 세종대로 172]
//        Longitude : 126.9769419
//        Latitude : 37.5710015

//        총 반경 1.0 km

        List<Pair<String, Coordinates>> locations = new ArrayList<>();

        // 실제 네이버지도에서 중심좌표, 반경거리 설정하고 구한 값들 (노션 참고)
        locations.add(Pair.of("IBK기업은행 인사동(in)", new Coordinates(new BigDecimal("126.9853853"), new BigDecimal("37.572062"))));
        locations.add(Pair.of("덕수궁 중화전(in)", new Coordinates(new BigDecimal("126.9748032"), new BigDecimal("37.5657571"))));
        locations.add(Pair.of("서울경찰청(in)", new Coordinates(new BigDecimal("126.971979688446"), new BigDecimal("37.5749462708923"))));
        locations.add(Pair.of("알라딘커뮤니케이션 전기차충전소(in)", new Coordinates(new BigDecimal("126.971254012533"), new BigDecimal("37.5638725916722"))));
        locations.add(Pair.of("세종예술아카데미(in)", new Coordinates(new BigDecimal("126.976168275947"), new BigDecimal("37.5718478584908"))));
        locations.add(Pair.of("서울특별시청(in)", new Coordinates(new BigDecimal("126.977829174031"), new BigDecimal("37.5663174209601"))));
        locations.add(Pair.of("경복궁 경회루(in 경계 근접)", new Coordinates(new BigDecimal("126.97598934173584"), new BigDecimal("37.579710107249326"))));
        locations.add(Pair.of("충호안보연(in 경계 근접)", new Coordinates(new BigDecimal("126.971487366174"), new BigDecimal("37.5630763476503"))));
        locations.add(Pair.of("서울원각사지십층석탑(in 경계 근접)", new Coordinates(new BigDecimal("126.988185"), new BigDecimal("37.5714876"))));
        locations.add(Pair.of("지지배배 스튜디오(in 경계 근접)", new Coordinates(new BigDecimal("126.971113948247"), new BigDecimal("37.5786647362008"))));
        locations.add(Pair.of("탑골공원팔각정(in 경계 근접)", new Coordinates(new BigDecimal("126.9882631"), new BigDecimal("37.57130000000001"))));
        locations.add(Pair.of("수모텔(in 경계 근접)", new Coordinates(new BigDecimal("126.968308231806"), new BigDecimal("37.5766942627651"))));
        locations.add(Pair.of("무악천경고교(in 경계 근접)", new Coordinates(new BigDecimal("126.967202493985"), new BigDecimal("37.5667630123693"))));
        locations.add(Pair.of("테이스트메이드(out 경계 근접)", new Coordinates(new BigDecimal("126.968959973229"), new BigDecimal("37.5774706425237"))));
        locations.add(Pair.of("화포식당 시청(out 경계 근접)", new Coordinates(new BigDecimal("126.9776044"), new BigDecimal("37.5617465"))));
        locations.add(Pair.of("탑골공원관리사무소(out 경계 근접)", new Coordinates(new BigDecimal("126.9883295"), new BigDecimal("37.5711455"))));
        locations.add(Pair.of("우리은행 명동금융센터(out 경계 근접)", new Coordinates(new BigDecimal("126.9847304"), new BigDecimal("37.5636741"))));
        locations.add(Pair.of("올리브영 종로YBM점(out 경계 근접)", new Coordinates(new BigDecimal("126.9889372"), new BigDecimal("37.569921"))));
        locations.add(Pair.of("매머드익스프레스 덕수궁롯데캐슬점(out 경계 근접)", new Coordinates(new BigDecimal("126.970042986983"), new BigDecimal("37.5637084894676"))));
        locations.add(Pair.of("사직동 그 가게(out 경계 근접)", new Coordinates(new BigDecimal("126.967779389331"), new BigDecimal("37.577095242281"))));
        locations.add(Pair.of("사직터널 그 집(out)", new Coordinates(new BigDecimal("126.964909548683"), new BigDecimal("37.5736166752108"))));


        List<Gathering> gatherings = new ArrayList<>();


        int i = 0;
        for (Pair<String, Coordinates> pair : locations) {

            // 주소공유하면 안돼서 계속 새로 만들기
            Location location = Location
                    .of(new AddressName(pair.getFirst(),
                                    "1depth_name",
                                    "2depth_name",
                                    "3depth_name"),
                            new RegionCode(0, 0),
                            new Coordinates(pair.getSecond().getX(), pair.getSecond().getY()));

            Gathering gathering = Gathering.builder()
                    .organizerId(users.get(0).getId())
                    .title(pair.getFirst())
                    .description("재밌게 달려봅시다.")
                    .appointedAt(LocalDateTime.now().plusDays(i + 2))
                    .deadline(LocalDateTime.now().plusDays(i + 1))
                    .concept(RunningConcept.HEALTH)
                    .goalDistance(GoalDistance.FREE)
                    .thumbnailUrl((i++) + "번 모임 썸네일")
                    .hits(0L)
                    .location(location)
                    .status(NORMAL)
                    .maxNumber(10)
                    .currentNumber(1)
                    .gatheringType(GatheringType.GENERAL)
                    .build();
            gathering.addMember(users.get(0), ORGANIZER);
            gathering.applyForEvent();

            // 0번 유저는 주최자
            for (int j = 1; j < users.size(); j++) {
                gathering.addMember(users.get(j), PARTICIPANT);
            }


            gatherings.add(gathering);
        }

        gatheringRepository.saveAll(gatherings);

    }


}
