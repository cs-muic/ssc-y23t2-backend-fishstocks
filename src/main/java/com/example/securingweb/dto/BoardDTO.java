package com.example.securingweb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDTO {
    private PieceDTO[][] board = new PieceDTO[8][8];
    private KingDTO whiteKing;
    private KingDTO blackKing;

}



