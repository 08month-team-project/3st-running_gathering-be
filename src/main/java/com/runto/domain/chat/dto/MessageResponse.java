package com.runto.domain.chat.dto;

import com.runto.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    //클라이언트에 응답하는 용도
    private Long senderId;
    private String senderName;
    private String senderProfileImageUrl;
    private String content;
    private String timestamp;

    public static MessageResponse of(MessageQueueDTO messageQueueDTO, User user){
        return MessageResponse.builder()
                .senderId(messageQueueDTO.getSenderId())
                .senderName(user.getName())
                .senderProfileImageUrl(user.getProfileImageUrl())
                .content(messageQueueDTO.getContent())
                .timestamp(messageQueueDTO.getTimestamp())
                .build();
    }
}
