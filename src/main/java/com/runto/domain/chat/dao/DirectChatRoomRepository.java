package com.runto.domain.chat.dao;

import com.runto.domain.chat.domain.DirectChatRoom;
import com.runto.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DirectChatRoomRepository extends JpaRepository<DirectChatRoom,Long> {

    @Query("select count(dcr) > 0 from DirectChatRoom dcr " +
            "where (dcr.user1 = :user1 and dcr.user2 = :user2) " +
            "or (dcr.user1 = :user2 and  dcr.user2 = :user1)")
    boolean existsByUserPair(@Param("user1")User user1, @Param("user2")User user2);

}
