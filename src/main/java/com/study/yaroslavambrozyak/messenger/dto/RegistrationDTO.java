package com.study.yaroslavambrozyak.messenger.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

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
    @Length(min=4)
    private String password;
}
