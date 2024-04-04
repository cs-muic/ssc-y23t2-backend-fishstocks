package com.example.securingweb.model.chess;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public abstract class Piece {
    protected int row; // Position on the board
    protected int col;
    protected boolean isWhite;
    protected PieceType type;
    // Currently is symbol for console but will need to be switched to png
    protected char symbol;
    protected int moveCount;

    public Piece(boolean isWhite, PieceType type, int row, int col, char symbol) {
        this.isWhite = isWhite;
        this.type = type;
        this.row = row;
        this.col = col;
        this.symbol = symbol;
        this.moveCount = 0;
    }

    public abstract List<Move> getUnfilteredMoves(Board board);

    public void incrementMoveCount() {
        moveCount++;
    }

    public void decrementMoveCount() {
        if (moveCount > 0) {
            moveCount--;
        }
    }

    public boolean hasMoved() {
        return moveCount > 0;
    }

    public void updateLocation(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
