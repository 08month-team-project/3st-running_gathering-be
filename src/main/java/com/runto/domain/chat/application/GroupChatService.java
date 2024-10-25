package com.runto.domain.chat.application;

import com.runto.domain.chat.dao.GroupChatRoomRepository;
import com.runto.domain.chat.dao.GroupChatRoomUserRepository;
import com.runto.domain.chat.domain.GroupChatRoom;
import com.runto.domain.chat.domain.GroupChatRoomUser;
import com.runto.domain.chat.exception.ChatException;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.user.domain.User;
import com.runto.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupChatService {
    private final GroupChatRoomUserRepository groupChatRoomUserRepository;

    // 그룹 채팅방 참여
    @Transactional
    public void joinGroupChatRoom(User user, GroupChatRoom groupChatRoom){

        //내가 그 채팅방에 참여중인지 확인
        if (groupChatRoomUserRepository.existsByGroupChatRoomAndUser(groupChatRoom,user)){
            throw new ChatException(ErrorCode.CHATROOM_ALREADY_JOINED);
        }

        groupChatRoom.addGroupChatUser(GroupChatRoomUser.createGroupChatRoomUser(groupChatRoom,user));
    }
}
