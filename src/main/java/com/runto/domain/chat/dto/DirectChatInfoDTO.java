package com.runto.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectChatInfoDTO {
    private Long roomId;
    private Long myId;
    private Long otherId;

    public static DirectChatInfoDTO of(Long roomId, Long myId, Long otherId){
        return DirectChatInfoDTO.builder()
                .roomId(roomId)
                .myId(myId)
                .otherId(otherId).build();
    }
}
