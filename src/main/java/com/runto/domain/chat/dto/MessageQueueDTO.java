package com.runto.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageQueueDTO {
    //큐에 메시지 전송 혹은 큐에서 메시지를 받는 용도
    private Long senderId;
    private Long roomId;
    private String content;
    private String timestamp;

    public static MessageQueueDTO fromMessageDTO(MessageDTO messageDTO,Long senderId){
        return MessageQueueDTO.builder()
                .senderId(senderId)
                .roomId(messageDTO.getRoomId())
                .content(messageDTO.getContent())
                .timestamp(messageDTO.getTimestamp().toString()).build();
    }
}
