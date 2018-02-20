package com.study.yaroslavambrozyak.messenger.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue
    @Column(name = "chat_id", updatable = false, unique = true)
    private long id;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_room",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> usersInRoom = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chatRoom")
    private Set<Message> messages = new HashSet<>();
    private LocalDateTime lastActivity;
}
