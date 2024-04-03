package com.example.securingweb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PieceDTO {
    String name;
    int row;
    int col;
}
