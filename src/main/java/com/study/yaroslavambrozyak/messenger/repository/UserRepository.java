package com.study.yaroslavambrozyak.messenger.repository;

import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);

    User findByEmail(String email);

    @Query(value = "SELECT u.id FROM User u WHERE u.email=?1")
    Long getUserId(String email);

    @Query("SELECT c FROM ChatRoom c JOIN c.usersInRoom u WHERE u.id=?1")
    Page<ChatRoom> getChatRoom(long id, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.befriended uf WHERE uf.id=?1")
    Page<User> getUserFriends(long id, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.befriendedReq ureq WHERE ureq.id=?1")
    Page<User> getUserFriendRequest(long id, Pageable pageable);
}
