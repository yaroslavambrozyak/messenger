package com.study.yaroslavambrozyak.messenger.dto;

import com.study.yaroslavambrozyak.messenger.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {

    @NotBlank
    private String name;
    @NotBlank
    private String surName;
    @Email
    private String email;
    @NotBlank
    @Length(min = 4)
    private String password;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date birthday;
    private Gender gender;
}
