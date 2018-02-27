package com.study.yaroslavambrozyak.messenger.dto;

import com.study.yaroslavambrozyak.messenger.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    private String name;
    private String surName;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date birthday;
    private Gender gender;
}
