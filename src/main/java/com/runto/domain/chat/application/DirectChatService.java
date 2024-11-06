package com.runto.domain.chat.application;

import com.runto.domain.chat.dao.DirectChatRoomRepository;
import com.runto.domain.chat.dao.DirectMessageRepository;
import com.runto.domain.chat.domain.DirectChatContent;
import com.runto.domain.chat.domain.DirectChatRoom;
import com.runto.domain.chat.dto.ChatRoomResponse;
import com.runto.domain.chat.dto.DirectChatInfoDTO;
import com.runto.domain.chat.dto.MessageQueueDTO;
import com.runto.domain.chat.dto.MessageResponse;
import com.runto.domain.chat.exception.ChatException;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.excepction.UserException;
import com.runto.global.exception.ErrorCode;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectChatService {
    private final UserRepository userRepository;
    private final DirectChatRoomRepository directChatRoomRepository;
    private final DirectMessageRepository directMessageRepository;

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

    public Slice<ChatRoomResponse> getDirectChatRoomList(Long userId, Pageable pageable){
        User me = userRepository.findById(userId)
                .orElseThrow(()->new UserException(ErrorCode.USER_NOT_FOUND));
        return directChatRoomRepository.getChatRooms(me.getId(), pageable);
    }

    public Slice<MessageResponse> getDirectChatMessages(Long roomId, Pageable pageable){
        LocalDateTime daysAgo = LocalDateTime.now().minusDays(2);
        Slice<DirectChatContent> directChatContents = directMessageRepository.findDirectChatContent(roomId, daysAgo, pageable);

        List<Long> senderIdList = directChatContents.stream()
                .map(DirectChatContent::getSenderId).distinct().toList();

        List<User> users = userRepository.findAllById(senderIdList);

        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId,u->u));

        List<MessageResponse> messageResponses = directChatContents.stream().map(directChatContent->{
                User user = userMap.get(directChatContent.getSenderId());
                return MessageResponse.of(directChatContent,user);

                }).toList();
        return new SliceImpl<>(messageResponses, pageable, directChatContents.hasNext());
    }

    @Transactional
    public void saveDirectChatContent(MessageQueueDTO messageQueueDTO, boolean messageSent){
        DirectChatContent directChatContent = DirectChatContent.of(messageQueueDTO);
        if (!messageSent){
            directChatContent.changeStatusToFailed();
        }
        directMessageRepository.save(directChatContent);
    }
}
