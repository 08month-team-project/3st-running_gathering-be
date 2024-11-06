package com.runto.domain.chat.dao;

import com.runto.domain.chat.dto.ChatRoomResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectChatRoomRepositoryCustom {

    Slice<ChatRoomResponse> getChatRooms(Long userId, Pageable pageable);
}
