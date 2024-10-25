package com.runto.domain.chat.api;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/group")
@RequiredArgsConstructor
public class GroupChatController {

    /**
     * 그룹 채팅 생성, 참가 api 를 따로 만들까?
     * !!따로 만들 경우
     * 명확한 책임의 분리가 가능함.
     * 클라이언트에게 두번 요청을 받아야함.
     * 채팅방의 동시성 제어를 따로 해줘야함.
     * 한번의 처리에 대한 데이터 처리량이 줄어든다.
     *
     * !!모임 서비스에서 한번에 처리할 경우
     * 엔티티에 대한 접근(쿼리)이 줄어든다.
     * 동시성처리는 모임 생성,참가에서 하면 채팅방도 동시성 해결
     * 한번에 처리에 대한 데이터 처리량이 증가함.
     * 채팅 생성 api 따로 없으므로 채팅 생성, 참가를 따로 하고싶을 경우의 기능의 확장성 없음
     */

    //그룹 채팅 생성 api

    //그룹 채팅 참가 api
}
