package com.runto.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DirectChatRoomResponse {

    private Long roomId;
    private String otherName;
    private String otherProfileImage;

}

