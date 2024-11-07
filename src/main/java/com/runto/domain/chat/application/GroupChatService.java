package com.runto.domain.chat.application;

import com.runto.domain.chat.dao.GroupChatRoomRepository;
import com.runto.domain.chat.dao.GroupChatRoomUserRepository;
import com.runto.domain.chat.dao.GroupMessageRepository;
import com.runto.domain.chat.domain.GroupChatContent;
import com.runto.domain.chat.domain.GroupChatRoom;
import com.runto.domain.chat.domain.GroupChatRoomUser;
import com.runto.domain.chat.dto.ChatRoomResponse;
import com.runto.domain.chat.dto.MessageQueueDTO;
import com.runto.domain.chat.dto.MessageResponse;
import com.runto.domain.chat.exception.ChatException;
import com.runto.domain.gathering.dao.GatheringMemberRepository;
import com.runto.domain.gathering.dao.GatheringRepository;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.User;
import com.runto.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupChatService {
    private final GroupChatRoomUserRepository groupChatRoomUserRepository;
    private final GatheringRepository gatheringRepository;
    private final GatheringMemberRepository gatheringMemberRepository;
    private final GroupChatRoomRepository groupChatRoomRepository;
    private final UserRepository userRepository;
    private final GroupMessageRepository groupMessageRepository;

    // 그룹 채팅방 생성
    @Transactional
    public void createGroupChatRoom(Long gatheringId){
        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(()->new ChatException(ErrorCode.GATHERING_NOT_FOUND));

        GroupChatRoom groupChatRoom = GroupChatRoom.createRoom(gathering);

        groupChatRoomRepository.save(groupChatRoom);
    }

    // 그룹 채팅방 참여
    @Transactional
    public void joinGroupChatRoom(Long userId, Long gatheringId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ChatException(ErrorCode.USER_NOT_FOUND));

        GroupChatRoom groupChatRoom = groupChatRoomRepository.findByGatheringId(gatheringId);

        //모임에 참여 중인지 확인
        if (gatheringMemberRepository.existsGatheringMemberByGatheringIdAndUserId(gatheringId,userId)){
            throw new ChatException(ErrorCode.GATHERING_MEMBER_NOT_FOUND);
        }

        //내가 그 채팅방에 참여중인지 확인
        if (groupChatRoomUserRepository.existsByGroupChatRoomAndUser(groupChatRoom,user)){
            throw new ChatException(ErrorCode.CHATROOM_ALREADY_JOINED);
        }

        groupChatRoom.addGroupChatUser(GroupChatRoomUser.createGroupChatRoomUser(groupChatRoom,user));
    }

    // 그룹 채팅방 목록 조회
    @Transactional
    public Slice<ChatRoomResponse> getGroupChatRoomList(Long userId, Pageable pageable){
        Slice<GroupChatRoom> groupChatRooms = groupChatRoomRepository.findGroupChatRoomById(userId, pageable);
        System.out.println("groupChatRooms: " + groupChatRooms.getContent());
        List<ChatRoomResponse> chatRoomResponses = groupChatRooms.stream().map(ChatRoomResponse::fromGroupChatRoom).toList();
        System.out.println("chatRoomResponses: " + chatRoomResponses);
        return new SliceImpl<>(chatRoomResponses, pageable, groupChatRooms.hasNext());
    }


    // 그룹 채팅방 목록에서 조회하기
    public Slice<MessageResponse> getGroupChatRoom(Long roomId, Pageable pageable){
        LocalDateTime daysAgo = LocalDateTime.now().minusDays(2);

        Slice<GroupChatContent> groupChatContents = groupMessageRepository.findGroupChatContent(roomId,daysAgo,pageable);

        List<Long> senderIdList = groupChatContents.stream()
                .map(GroupChatContent::getSenderId).distinct().toList();

        List<User> users = userRepository.findAllById(senderIdList);

        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, u->u));

        List<MessageResponse> messageResponses = groupChatContents.stream().map(groupChatContent->{
            User user = userMap.get(groupChatContent.getSenderId());
            return MessageResponse.of(groupChatContent,user);

        }).toList();

        return new SliceImpl<>(messageResponses, pageable, groupChatContents.hasNext());
    }

    @Transactional
    public void saveGroupChatChatContent(MessageQueueDTO messageQueueDTO, boolean messageSent){
        GroupChatContent groupChatContent = GroupChatContent.of(messageQueueDTO);
        if (!messageSent){
            groupChatContent.changeStatusToFailed();
        }
        groupMessageRepository.save(groupChatContent);
    }
}
