package com.study.yaroslavambrozyak.messenger.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NotFound;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user_acc")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id", updatable = false, unique = true)
    private long id;
    private String name;
    private String surName;
    @ManyToMany(mappedBy = "usersInRoom")
    private Set<ChatRoom> userChatRooms = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    private Set<Message> messages = new HashSet<>();
    @Column(unique = true)
    private String email;
    private String password;

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

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Set<ChatRoom> getUserChatRooms() {
        return userChatRooms;
    }

    public void setUserChatRooms(Set<ChatRoom> userChatRooms) {
        this.userChatRooms = userChatRooms;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
