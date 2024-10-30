package com.runto.domain.chat.api;

import com.runto.domain.chat.application.DirectChatService;
import com.runto.domain.chat.dto.DirectChatInfoDTO;
import com.runto.domain.chat.dto.DirectChatRoomResponse;
import com.runto.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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
    public ResponseEntity<DirectChatInfoDTO> getDirectChatRoom(@RequestParam(name = "other_id") Long otherId,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails){
        DirectChatInfoDTO directChatInfoDTO = directChatService.createAndGetDirectChat(userDetails.getUserId(),otherId);
        //TODO 채팅방 정보를 통한 이전 채팅 메시지 보여주기,
        return ResponseEntity.ok(directChatInfoDTO);
    }

    //1:1 채팅방 목록 조회
    @GetMapping("/list")
    public ResponseEntity<Slice<DirectChatRoomResponse>> getDirectChatRoomList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                               @RequestParam(name = "page_num") int pageNum,
                                                                               @RequestParam(defaultValue = "7") int size){
        return ResponseEntity.ok(directChatService.getDirectChatRoomList(userDetails.getUserId(), pageNum,size));
    }

    //TODO 1:1 채팅방 목록에서 눌러서 채팅방 조회
    @GetMapping("/{room_id}")
    public ResponseEntity<Void> getDirectChatRoomFromList(@PathVariable(name = "room_id") Long roomId){
        //TODO 채팅방 정보를 통한 이전 채팅 메시지 보여주기
        return ResponseEntity.ok().build();
    }

}
