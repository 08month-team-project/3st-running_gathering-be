package com.runto.domain.chat.dao;

import com.runto.domain.chat.domain.DirectChatContent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;

public interface DirectMessageRepository extends MongoRepository<DirectChatContent,Long> {
    @Query("{'room_id' : ?0, 'status': 'SENT', 'timestamp' :  {$gte : ?1} }")
    Slice<DirectChatContent> findDirectChatContent(Long roomId, LocalDateTime daysAgo, Pageable pageable);
}
