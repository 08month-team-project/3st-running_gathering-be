package com.runto.domain.chat.application;

import com.runto.domain.chat.dao.GroupChatRoomRepository;
import com.runto.domain.chat.dao.GroupChatRoomUserRepository;
import com.runto.domain.chat.domain.GroupChatRoom;
import com.runto.domain.chat.domain.GroupChatRoomUser;
import com.runto.domain.gathering.domain.*;
import com.runto.domain.gathering.type.GoalDistance;
import com.runto.domain.gathering.type.RunningConcept;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.type.Gender;
import com.runto.domain.user.type.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GroupChatServiceTest {

    @Mock
    private GroupChatRoomRepository groupChatRoomRepository;

    @Mock
    private GroupChatRoomUserRepository groupChatRoomUserRepository;

    @InjectMocks
    private GroupChatService groupChatService;

    private Gathering gathering;

    @BeforeEach
    void setUp() {
        User user1 = User.builder()
                .id(1L)
                .email("email@gmail.com")
                .name("홍길동")
                .gender(Gender.MAN)
                .nickname("길동이")
                .profileImageUrl("xxx.png")
                .role(UserRole.USER)
                .build();


        gathering = Gathering.builder()
                .id(1L)
                .title("개모임")
                .description("개들의 모임")
                .appointedAt(LocalDateTime.now())
                .deadline(LocalDateTime.MAX)
                .concept(RunningConcept.MARATHON)
                .goalDistance(GoalDistance.HALF_MARATHON)
                .thumbnailUrl("testImage.png")
                .location(Location
                        .of(new AddressName("address_name",
                                        "1depth_name",
                                        "2depth_name",
                                        "3depth_name"),
                                new RegionCode(0, 0),
                                new Coordinates(0.0, 0.0)))
                .maxNumber(10)
                .build();
    }


    @DisplayName("그룹 채팅방 참여 성공 테스트")
    @Test
    void joinGroupChatRoom_success() {
        //given
        User user2 = User.builder()
                .id(2L)
                .email("email2@gmail.com")
                .name("장보고")
                .gender(Gender.MAN)
                .nickname("해신")
                .profileImageUrl("xxx123.png")
                .role(UserRole.USER)
                .build();
        Long roomId = 1L;

        GroupChatRoom groupChatRoom = GroupChatRoom.createRoom(gathering);
        GroupChatRoomUser groupChatRoomUser = GroupChatRoomUser.createGroupChatRoomUser(groupChatRoom, user2);

        //when
        when(groupChatRoomRepository.findById(1L)).thenReturn(Optional.of(groupChatRoom));
        when(groupChatRoomUserRepository.existsByGroupChatRoomAndUser(any(GroupChatRoom.class), any(User.class))).thenReturn(false);
        when(groupChatRoomUserRepository.findById(any(Long.class))).thenReturn(Optional.of(groupChatRoomUser));

        groupChatService.joinGroupChatRoom(user2, groupChatRoom);

        //then
        verify(groupChatRoomRepository).findById(any(Long.class));
        verify(groupChatRoomUserRepository).existsByGroupChatRoomAndUser(any(GroupChatRoom.class), any(User.class));
        Optional<GroupChatRoomUser> savedGroupChatUser = groupChatRoomUserRepository.findById(user2.getId());
        assertTrue(savedGroupChatUser.isPresent());
        assertEquals(groupChatRoomUser.getId(), savedGroupChatUser.get().getId());
    }

    //존재 x , 이미 참여중인 채팅방, 채팅방 인원 초과
    @DisplayName("그룹 채팅방 참여 실패 테스트 - 존재하지 않는 채팅방")
    @Test
    void joinGroupChatRoom_fail_notExist() {
        //TODO
    }

    @DisplayName("그룹 채팅방 참여 실패 테스트 - 이미 참여중인 채팅방")
    @Test
    void joinGroupChatRoom_fail_alreadyJoin() {
        //TODO
    }

    @DisplayName("그룹 채팅방 참여 실패 테스트 - 채팅방 인원 초과")
    @Test
    void joinGroupChatRoom_overUsers() {
        //TODO
    }
}