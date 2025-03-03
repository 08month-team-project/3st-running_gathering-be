package com.runto.domain.chat.dto;

import com.runto.domain.chat.domain.DirectChatContent;
import com.runto.domain.chat.domain.GroupChatContent;
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
    private Long senderId;
    private String senderName;
    private String senderProfileImageUrl;
    private String content;
    private String timestamp;

    public static MessageResponse of(MessageQueueDTO messageQueueDTO, User user){
        return MessageResponse.builder()
                .senderId(messageQueueDTO.getSenderId())
                .senderName(user.getNickname())
                .senderProfileImageUrl(user.getProfileImageUrl())
                .content(messageQueueDTO.getContent())
                .timestamp(messageQueueDTO.getTimestamp())
                .build();
    }

    public static MessageResponse of(DirectChatContent directChatContent, User user){
        return MessageResponse.builder()
                .senderId(directChatContent.getSenderId())
                .senderName(user.getNickname())
                .senderProfileImageUrl(user.getProfileImageUrl())
                .content(directChatContent.getContent())
                .timestamp(directChatContent.getTimestamp().toString())
                .build();
    }

    public static MessageResponse of(GroupChatContent groupChatContent, User user){
        return MessageResponse.builder()
                .senderId(groupChatContent.getSenderId())
                .senderName(user.getNickname())
                .senderProfileImageUrl(user.getProfileImageUrl())
                .content(groupChatContent.getContent())
                .timestamp(groupChatContent.getTimestamp().toString())
                .build();
    }
}
