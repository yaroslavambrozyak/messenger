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
    @ManyToMany
    @JoinTable(name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_s_id"))
    private Set<User> friends = new HashSet<>();
    @ManyToMany(mappedBy = "friends")
    private Set<User> befriended = new HashSet<>();
    @ManyToMany
    @JoinTable(name = "user_friends_req",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private Set<User> friendsReq = new HashSet<>();
    @ManyToMany(mappedBy = "friendsReq")
    private Set<User> befriendedReq = new HashSet<>();

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

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public Set<User> getBefriended() {
        return befriended;
    }

    public void setBefriended(Set<User> befriended) {
        this.befriended = befriended;
    }

    public Set<User> getFriendsReq() {
        return friendsReq;
    }

    public void setFriendsReq(Set<User> friendsReq) {
        this.friendsReq = friendsReq;
    }

    public Set<User> getBefriendedReq() {
        return befriendedReq;
    }

    public void setBefriendedReq(Set<User> befriendedReq) {
        this.befriendedReq = befriendedReq;
    }
}
