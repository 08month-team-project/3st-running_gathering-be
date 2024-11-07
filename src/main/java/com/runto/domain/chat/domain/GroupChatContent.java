package com.runto.domain.chat.domain;

import com.runto.domain.chat.dto.MessageQueueDTO;
import com.runto.domain.chat.type.MessageStatus;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "group_chat")
public class GroupChatContent {
    @Id
    private String id;

    @Field("room_id")
    @Indexed
    private Long roomId;

    @Field("sender_id")
    private Long senderId;

    private String content;

    private LocalDateTime timestamp;

    @Field("status")
    private MessageStatus messageStatus;

    public static GroupChatContent of(MessageQueueDTO messageQueueDTO){
        LocalDateTime timestamp = LocalDateTime.parse(messageQueueDTO.getTimestamp(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return GroupChatContent.builder()
                .roomId(messageQueueDTO.getRoomId())
                .senderId(messageQueueDTO.getSenderId())
                .content(messageQueueDTO.getContent())
                .timestamp(timestamp)
                .messageStatus(MessageStatus.SENT).build();
    }

    public void changeStatusToFailed(){
        this.messageStatus = MessageStatus.FAILED;
    }
}
