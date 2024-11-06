package com.runto.domain.chat.dto;

import com.runto.domain.chat.domain.GroupChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponse {

    private Long roomId;
    private String name;
    private String profileImage;

}

