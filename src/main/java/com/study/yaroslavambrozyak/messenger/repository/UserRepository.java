package com.study.yaroslavambrozyak.messenger.repository;

import com.study.yaroslavambrozyak.messenger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User,Long> {
    User getByName(String name);
}
