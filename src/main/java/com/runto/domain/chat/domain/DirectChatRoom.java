package com.runto.domain.chat.domain;

import com.runto.domain.common.BaseTimeEntity;
import com.runto.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "direct_chat_room", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"user1_id","user2_id"})})
public class DirectChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "direct_chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    //채팅방의 상태? -> 그룹채팅도 마찬가지로 넣을지 말지 생각하기

    //Optimistic Lock -> 읽기의 비율이 높음
    @Version
    private Long version;

    public static DirectChatRoom createRoom(User user1, User user2){
        DirectChatRoom directChatRoom = new DirectChatRoom();
        directChatRoom.user1 = user1;
        directChatRoom.user2 = user2;
        return  directChatRoom;
    }
}
