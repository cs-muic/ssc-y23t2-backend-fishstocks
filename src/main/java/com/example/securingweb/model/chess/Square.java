package com.example.securingweb.model.chess;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Square {
    private String name;
    private Piece piece;
    private final int row, col;
    private boolean isWhite;

    public Square(int row, int col, boolean isWhite) {
        this.row = row;
        this.col = col;
        this.piece = null;
        this.isWhite = isWhite;
        this.name = calcSquareName(row, col);
    }
    private String calcSquareName(int row, int col) {
        // Define letters for columns
        char[] columnLetters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        // Calculate square name based on row and column indices
        return String.valueOf(columnLetters[col]) + (8 - row);
    }

    public String getSquareName(){return name;}
    public boolean isOccupied() {
        return piece != null;
    }

    public void emptySquare(){ this.piece = null; }
    public boolean isWhite(){
        return isWhite;
    }

}
