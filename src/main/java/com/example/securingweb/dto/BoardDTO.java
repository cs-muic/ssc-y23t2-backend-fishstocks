package com.example.securingweb.dto;

import com.example.securingweb.model.chess.Square;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDTO {
    private PieceDTO[][] board = new PieceDTO[8][8];
    private KingDTO whiteKing;
    private KingDTO blackKing;

    public void setPiece(PieceDTO piece, int row, int col){
        board[row][col] = piece;
    }

    public void setNull(int row, int col) {
        board[row][col] = null;
    }
}


