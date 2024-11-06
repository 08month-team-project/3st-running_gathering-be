package com.runto.domain.chat.dao;

import com.runto.domain.chat.domain.GroupChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupChatRoomRepository extends JpaRepository<GroupChatRoom,Long> {

    @Query("select gc from GroupChatRoom gc " +
            "where gc.gathering.id = :gid")
    GroupChatRoom findByGatheringId(@Param("gid") Long gatheringId);

    @Query("select gcr " +
            "from GroupChatRoom gcr " +
            "join fetch GroupChatRoomUser gu on gu.groupChatRoom.id = gcr.id " +
            "join fetch Gathering g on gcr.gathering.id = g.id " +
            "where gu.user.id = :id ")
    Slice<GroupChatRoom> findGroupChatRoomById(Long id, Pageable pageable);
}
