package com.study.yaroslavambrozyak.messenger.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="user_acc")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id",updatable = false,unique = true)
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String surName;
    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "usersInRoom")
    private Set<ChatRoom> userChatRooms = new HashSet<>();

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
}
