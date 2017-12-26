package com.study.yaroslavambrozyak.messenger.repository;

import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
}
