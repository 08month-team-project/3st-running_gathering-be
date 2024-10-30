package com.runto.domain.chat.application;

import com.runto.domain.chat.dao.DirectChatRoomRepository;
import com.runto.domain.chat.domain.DirectChatRoom;
import com.runto.domain.chat.dto.DirectChatInfoDTO;
import com.runto.domain.chat.dto.DirectChatRoomResponse;
import com.runto.domain.chat.exception.ChatException;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.excepction.UserException;
import com.runto.global.exception.ErrorCode;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectChatService {
    private final UserRepository userRepository;
    private final DirectChatRoomRepository directChatRoomRepository;

    @Transactional
    public DirectChatInfoDTO createAndGetDirectChat(Long userId,Long otherId){
        createDirectChat(userId, otherId);
        return getDirectChatInfo(userId, otherId);
    }

    @Transactional
    public void createDirectChat(Long userId, Long otherId){
        User me = userRepository.findById(userId)
                .orElseThrow(()->new UserException(ErrorCode.USER_NOT_FOUND));
        User otherUser = userRepository.findById(otherId)
                .orElseThrow(()->new UserException(ErrorCode.USER_NOT_FOUND));

        //A와 B의 채팅방이 이미 존재하는지 확인
        if (directChatRoomRepository.existsByUserPair(me,otherUser)){
            return;
        }

        //동일한 아이디의 채팅방 생성 막기
        if (me.getId().equals(otherUser.getId())){
            throw new ChatException(ErrorCode.CHATROOM_CREATE_FAILED_OWN);
        }

        DirectChatRoom directChatRoom = DirectChatRoom.createRoom(me,otherUser);
        try {
            directChatRoomRepository.save(directChatRoom);
        }catch (OptimisticLockException | DataIntegrityViolationException e){
            return;
        }
    }

    @Transactional
    public DirectChatInfoDTO getDirectChatInfo(Long userId, Long otherId){
        User me = userRepository.findById(userId)
                .orElseThrow(()->new UserException(ErrorCode.USER_NOT_FOUND));
        User otherUser = userRepository.findById(otherId)
                .orElseThrow(()->new UserException(ErrorCode.USER_NOT_FOUND));

        DirectChatRoom directChatRoom = directChatRoomRepository.findByUserPair(me,otherUser)
                .orElseThrow(()->new ChatException(ErrorCode.CHATROOM_NOT_FOUND));

        return DirectChatInfoDTO.of(directChatRoom.getId(),me.getId(),otherUser.getId());
    }

    public Slice<DirectChatRoomResponse> getDirectChatRoomList(Long userId,int pageNum,int size){
        User me = userRepository.findById(userId)
                .orElseThrow(()->new UserException(ErrorCode.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(pageNum,size);

        return directChatRoomRepository.getChatRooms(me.getId(), pageable);
    }

}
