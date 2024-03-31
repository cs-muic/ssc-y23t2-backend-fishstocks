package com.example.securingweb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MoveDTO {
    private String start;
    private String end;
    private boolean isSpecialMove;

}
