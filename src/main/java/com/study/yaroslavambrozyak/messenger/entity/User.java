package com.study.yaroslavambrozyak.messenger.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_acc")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "email")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id", updatable = false, unique = true)
    private long id;
    private String name;
    private String surName;
    @Column(unique = true)
    private String email;
    private String password;
    private boolean gender;
    private Date birthday;
    private String imagePath = "default.jpg";

    @ManyToMany(mappedBy = "usersInRoom")
    private Set<ChatRoom> userChatRooms = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    private Set<Message> messages = new HashSet<>();
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
}
