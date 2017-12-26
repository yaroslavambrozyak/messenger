package com.study.yaroslavambrozyak.messenger.repository;

import com.study.yaroslavambrozyak.messenger.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,Long> {
}
