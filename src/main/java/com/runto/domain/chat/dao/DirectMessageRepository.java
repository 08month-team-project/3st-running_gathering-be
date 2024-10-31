package com.runto.domain.chat.dao;

import com.runto.domain.chat.domain.DirectChatContent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DirectMessageRepository extends MongoRepository<DirectChatContent,Long> {
}
