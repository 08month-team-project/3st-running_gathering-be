package com.runto.domain.chat.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runto.domain.chat.dao.DirectChatRoomRepository;
import com.runto.domain.chat.dto.MessageDTO;
import com.runto.domain.chat.dto.MessageQueueDTO;
import com.runto.domain.chat.exception.ChatException;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.excepction.UserException;
import com.runto.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerService {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DirectChatRoomRepository directChatRoomRepository;
    private final UserRepository userRepository;

    @Value("${rabbit.exchange}")
    private String exchange;

    @Value("${rabbit.direct.routing}")
    private String directRoutingKey;

    public void sendDirectMessage(MessageDTO messageDTO, Long senderId){
        if (!userRepository.existsById(senderId)){
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }
        if (!directChatRoomRepository.existsById(messageDTO.getRoomId())){
            throw new ChatException(ErrorCode.CHATROOM_NOT_FOUND);
        }
        log.info("Producer Service send Message = {}",messageDTO.getContent());
        MessageQueueDTO messageQueueDTO = MessageQueueDTO.fromMessageDTO(messageDTO,senderId);

        try {
            String objectToJSON = objectMapper.writeValueAsString(messageQueueDTO);
            rabbitTemplate.convertAndSend(exchange,
                    directRoutingKey, objectToJSON);
            log.info("Producer Service send Message to RabbitMQ = {}",messageQueueDTO.getContent());
        }catch (JsonProcessingException e){
            log.info("message parsing error to sendDirectMessage");
        }
    }
}
