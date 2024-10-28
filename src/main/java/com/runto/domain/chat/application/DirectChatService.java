package com.runto.domain.chat.application;

import com.runto.domain.chat.dao.DirectChatRoomRepository;
import com.runto.domain.chat.domain.DirectChatRoom;
import com.runto.domain.chat.exception.ChatException;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.excepction.UserException;
import com.runto.global.exception.ErrorCode;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DirectChatService {
    private final UserRepository userRepository;
    private final DirectChatRoomRepository directChatRoomRepository;

    @Transactional
    public void createDirectChat(String email, Long otherId){
        User me = userRepository.findByEmail(email)
                .orElseThrow(()->new UserException(ErrorCode.USER_NOT_FOUND));
        User otherUser = userRepository.findById(otherId)
                .orElseThrow(()->new UserException(ErrorCode.USER_NOT_FOUND));

        //동일한 아이디의 채팅방 생성 막기
        if (me.getId().equals(otherUser.getId())){
            throw new ChatException(ErrorCode.CHATROOM_CREATE_FAILED);
        }

        //A와 B의 채팅방이 이미 존재하는지 확인
        if (directChatRoomRepository.existsByUserPair(me,otherUser)){
            throw new ChatException(ErrorCode.CHATROOM_ALREADY_EXIST);
        }

        DirectChatRoom directChatRoom = DirectChatRoom.createRoom(me,otherUser);
        try {
            directChatRoomRepository.save(directChatRoom);
        }catch (OptimisticLockException | DataIntegrityViolationException e){
            throw new ChatException(ErrorCode.CHATROOM_ALREADY_EXIST);
        }
    }
}
