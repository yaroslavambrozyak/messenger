package com.study.yaroslavambrozyak.messenger.repository;

import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.entity.Message;
import com.study.yaroslavambrozyak.messenger.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT m FROM Message m WHERE m.chatRoom.id = ?1")
    Page<Message> getChatRoomMessages(long id, Pageable pageable);
    @Query("SELECT u FROM User u JOIN u.userChatRooms c WHERE c.id=?1")
    Page<User> getUsersInChatRoom(long id, Pageable pageable);

}
