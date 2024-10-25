package com.runto.domain.chat.domain;

import com.runto.domain.chat.exception.ChatException;
import com.runto.domain.common.BaseTimeEntity;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Getter
@Entity
public class GroupChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_chat_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id",unique = true)
    private Gathering gathering;

    @OneToMany(mappedBy = "groupChatRoom",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<GroupChatRoomUser> roomUserList = new ArrayList<>();

    //최신 메시지 타임스탬프?
    //private LocalDateTime lastMessageTimestamp;

    public static GroupChatRoom createRoom(Gathering gathering){
        GroupChatRoom groupChatRoom = new GroupChatRoom();
        groupChatRoom.gathering = gathering;
        return groupChatRoom;
    }

    public void addGroupChatUser(GroupChatRoomUser groupChatRoomUser){
        if (roomUserList.size() >= gathering.getMaxNumber()){
            throw new ChatException(ErrorCode.CHATROOM_FULL);
        }

        groupChatRoomUser.injectGroupChatRoom(this);
        roomUserList.add(groupChatRoomUser);
    }

}
