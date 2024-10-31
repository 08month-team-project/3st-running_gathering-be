package com.runto.domain.chat.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runto.domain.chat.dto.MessageQueueDTO;
import com.runto.domain.chat.dto.MessageResponse;
import com.runto.domain.chat.exception.ChatException;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.User;
import com.runto.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final SimpMessagingTemplate simpleMessagingTemplate;
    private final UserRepository userRepository;
    private final DirectChatService directChatService;
    private final RetryTemplate retryTemplate;

    @RabbitListener(queues = "${rabbit.direct.queue}",concurrency = "5")
    public void receiveDirectMessage(String message){
        try {
            log.info("received direct message = {}",message);
            ObjectMapper objectMapper = new ObjectMapper();
            MessageQueueDTO messageQueueDTO = objectMapper.readValue(message, MessageQueueDTO.class);

            User user = userRepository.findById(messageQueueDTO.getSenderId())
                    .orElseThrow(()->new ChatException(ErrorCode.USER_NOT_FOUND));

            MessageResponse messageResponse = MessageResponse.of(messageQueueDTO,user);

            boolean messageSent = retryTemplate.execute(new RetryCallback<Boolean, Exception>() {
                @Override
                public Boolean doWithRetry(RetryContext context) throws Exception {
                    try {
                        simpleMessagingTemplate.convertAndSend("/topic/direct/" + messageQueueDTO.getRoomId(), messageResponse);
                        return true;
                    }catch (MessagingException e){
                        log.error("Send message Failed = {}",messageResponse,e);
                        throw e;
                    }
                }
            }, new RecoveryCallback<Boolean>() {
                @Override
                public Boolean recover(RetryContext context) throws Exception {
                    //모든 재시도 실패했을때의 처리
                    log.error("Send Message to client and retry this. message = {}", messageResponse);
                    return false;
                }
            });

            directChatService.saveDirectChatContent(messageQueueDTO, messageSent);

        }catch (JsonProcessingException e){
            log.error("Message parsing error to directMessage = {}",e.getMessage());

        }catch (Exception e){
            log.error("UnExpected error = {}",e.getMessage());

        }
    }
    @RabbitListener(queues = "${rabbit.dl.queue}")
    public void receiveDeadLetterMessage(String message){
        //데드레터 로그찍기 -> 저장은 나중에 생각해보기
        log.error("Receive deadLetterMessage in queue = {}",message);
    }

}
