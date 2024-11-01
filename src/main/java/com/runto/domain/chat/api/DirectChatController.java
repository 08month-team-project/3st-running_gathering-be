package com.runto.domain.chat.api;

import com.runto.domain.chat.application.DirectChatService;
import com.runto.domain.chat.dto.DirectChatInfoDTO;
import com.runto.domain.chat.dto.DirectChatRoomResponse;
import com.runto.domain.chat.dto.MessageResponse;
import com.runto.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/chat/direct")
@RequiredArgsConstructor
public class DirectChatController {
    private final DirectChatService directChatService;

    //1:1 채팅방 1:1채팅하기로 조회(생성과 같이 있음)
    @GetMapping
    public ResponseEntity<Slice<MessageResponse>> getDirectChatRoom(@RequestParam(name = "other_id") Long otherId,
                                                                    @RequestParam(name = "page_num") int pageNum,
                                                                    @RequestParam(defaultValue = "7") int size,
                                                                    @AuthenticationPrincipal CustomUserDetails userDetails){
        DirectChatInfoDTO directChatInfoDTO = directChatService.createAndGetDirectChat(userDetails.getUserId(),otherId);
        Slice<MessageResponse> messageResponses = directChatService.getDirectChatMessages(directChatInfoDTO.getRoomId(),pageNum,size);
        return ResponseEntity.ok(messageResponses);
    }

    //1:1 채팅방 목록 조회
    @GetMapping("/list")
    public ResponseEntity<Slice<DirectChatRoomResponse>> getDirectChatRoomList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                               @RequestParam(name = "page_num") int pageNum,
                                                                               @RequestParam(defaultValue = "7") int size){
        return ResponseEntity.ok(directChatService.getDirectChatRoomList(userDetails.getUserId(), pageNum,size));
    }

    //1:1 채팅방 조회 (목록에서)
    @GetMapping("/{room_id}")
    public ResponseEntity<Slice<MessageResponse>> getDirectChatRoomFromList(@PathVariable(name = "room_id") Long roomId,
                                                          @RequestParam(name = "page_num") int pageNum,
                                                          @RequestParam(defaultValue = "7") int size){
        Slice<MessageResponse> messageResponses = directChatService.getDirectChatMessages(roomId,pageNum,size);
        return ResponseEntity.ok(messageResponses);
    }

}
