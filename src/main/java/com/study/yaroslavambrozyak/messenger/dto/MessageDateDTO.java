package com.study.yaroslavambrozyak.messenger.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDateDTO {

    private String text;
    private long userId;
    private LocalDateTime localDateTime;
}
