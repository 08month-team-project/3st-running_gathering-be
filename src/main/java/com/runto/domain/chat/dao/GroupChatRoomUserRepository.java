package com.runto.domain.chat.dao;

import com.runto.domain.chat.domain.GroupChatRoom;
import com.runto.domain.chat.domain.GroupChatRoomUser;
import com.runto.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupChatRoomUserRepository extends JpaRepository<GroupChatRoomUser,Long> {
    boolean existsByGroupChatRoomAndUser(GroupChatRoom groupChatRoom, User user);

}
