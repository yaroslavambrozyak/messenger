package com.study.yaroslavambrozyak.messenger.repository;

import com.study.yaroslavambrozyak.messenger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User,Long> {
    User getByName(String name);
    User getByEmail(String email);
    @Query(value = "SELECT u.id FROM User u WHERE u.name=?1")
    Long getUserId(String name);
}
