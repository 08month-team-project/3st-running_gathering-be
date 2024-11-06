package com.runto.domain.chat.api;


import com.runto.domain.chat.application.GroupChatService;
import com.runto.domain.chat.dto.ChatResponse;
import com.runto.domain.chat.dto.ChatRoomResponse;
import com.runto.domain.chat.dto.MessageResponse;
import com.runto.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat/group")
@RequiredArgsConstructor
public class GroupChatController {

    private final GroupChatService groupChatService;

    //그룹 채팅 생성 api -> 생성자는 자동으로 참가되야함
    @PostMapping
    public void createGroupChat(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @RequestParam("id") Long gatheringId){
        groupChatService.createGroupChatRoom(gatheringId);
        groupChatService.joinGroupChatRoom(userDetails.getUserId(), gatheringId);
    }

    //그룹 채팅 참가 api
    @PostMapping("/join")
    public void joinGroupChat(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @RequestParam("id") Long gatheringId){
        groupChatService.joinGroupChatRoom(userDetails.getUserId(), gatheringId);
    }
    //그룹 채팅방 목록 조회
    @GetMapping("/list")
    public ResponseEntity<Slice<ChatRoomResponse>> getGroupChatList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                    @RequestParam(name = "page_num") int pageNum,
                                                                    @RequestParam(defaultValue = "7") int size){
        return ResponseEntity.ok(groupChatService.getGroupChatRoomList(userDetails.getUserId(),pageNum ,size));
    }
    //그룹 채팅방 목록에서 조회
    @GetMapping("/{room_id}")
    public ResponseEntity<ChatResponse> getGroupChatRoom(@PathVariable("room_id") Long roomId,
                                                         @RequestParam(name = "page_num") int pageNum,
                                                         @RequestParam(defaultValue = "7") int size){
        Slice<MessageResponse> messageResponses = groupChatService.getGroupChatRoom(roomId,pageNum,size);
        ChatResponse chatResponse = ChatResponse.builder()
                .roomId(roomId)
                .messages(messageResponses).build();

        return ResponseEntity.ok(chatResponse);
    }
}
