package com.runto.domain.chat.dao;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runto.domain.chat.domain.QDirectChatRoom;
import com.runto.domain.chat.dto.ChatRoomResponse;
import com.runto.domain.user.domain.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;


@RequiredArgsConstructor
@Repository
public class DirectChatRoomRepositoryCustomImpl implements DirectChatRoomRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<ChatRoomResponse> getChatRooms(Long userId, Pageable pageable) {
        QDirectChatRoom d = QDirectChatRoom.directChatRoom;
        QUser u = QUser.user;

        List<ChatRoomResponse> chatRoomList = jpaQueryFactory
                .select(Projections.constructor(ChatRoomResponse.class,
                        d.id,
                        JPAExpressions.select(u.nickname)
                                .from(u)
                                .where(isUserOpponent(userId,d,u)),
                        JPAExpressions.select(u.profileImageUrl)
                                .from(u)
                                .where(isUserOpponent(userId,d,u))
                        ))//select end
                .from(d)
                .where(d.user1.id.eq(userId).or(d.user2.id.eq(userId)))
                .orderBy(d.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(chatRoomList, pageable, hasNextPage(pageable, chatRoomList));
    }

    private boolean hasNextPage(Pageable pageable, List<ChatRoomResponse> chatRoomList){
        if (chatRoomList.size() > pageable.getPageSize()){
            chatRoomList.remove(chatRoomList.size() - 1);
            return true;
        }
        return false;
    }

    private BooleanExpression isUserOpponent(Long userId, QDirectChatRoom d,QUser u){
        return d.user1.id.eq(userId).and(d.user2.id.eq(u.id))
                .or(d.user2.id.eq(userId).and(d.user1.id.eq(u.id)));
    }
}
