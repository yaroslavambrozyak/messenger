package com.study.yaroslavambrozyak.messenger.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chat_id", updatable = false, unique = true)
    private long id;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_room",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> usersInRoom = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "chatRoom")
    private Set<Message> messages = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsersInRoom() {
        return usersInRoom;
    }

    public void setUsersInRoom(Set<User> usersInRoom) {
        this.usersInRoom = usersInRoom;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }
}
