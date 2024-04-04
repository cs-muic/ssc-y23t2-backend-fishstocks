package com.example.securingweb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MoveDTO {
    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;

}
