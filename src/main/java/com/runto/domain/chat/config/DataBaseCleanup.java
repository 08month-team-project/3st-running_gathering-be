package com.runto.domain.chat.config;

import com.runto.domain.chat.dao.DirectMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataBaseCleanup implements CommandLineRunner {
    private final DirectMessageRepository directMessageRepository;

    @Override
    public void run(String... args) throws Exception {
        //앱 재실행시 1:1채팅방 컬렉션 모두 삭제 초기화
        //개발시 테스트 원활하게 하기 위한 용도입니다. 삭제예정
        directMessageRepository.deleteAll();
    }
}
