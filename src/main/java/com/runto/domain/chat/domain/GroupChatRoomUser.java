package com.runto.domain.chat.domain;

import com.runto.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"room_id","user_id"}))
@Entity
public class GroupChatRoomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private GroupChatRoom groupChatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static GroupChatRoomUser createGroupChatRoomUser(GroupChatRoom room,User user){
        GroupChatRoomUser groupChatRoomUser = new GroupChatRoomUser();
        groupChatRoomUser.groupChatRoom = room;
        groupChatRoomUser.user = user;
        return groupChatRoomUser;
    }

    public void injectGroupChatRoom(GroupChatRoom groupChatRoom) {
        this.groupChatRoom = groupChatRoom;
    }

}
