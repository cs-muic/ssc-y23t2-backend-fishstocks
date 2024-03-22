package com.example.securingweb.model.chess;

import java.util.List;

public abstract class Piece {
    protected Square square; // Position on the board
    protected boolean isWhite;
    protected PieceType type;
    protected char symbol;

    public char getSymbol() {
        return symbol; // Currently is symbol for console but will need to be switched to png
    }

    public Piece(boolean isWhite, PieceType type, Square square, char symbol) {
        this.isWhite = isWhite;
        this.type = type;
        this.square = square;
        this.symbol = symbol;
    }

    public abstract List<Move> getUnfilteredMoves(Board board);

    public boolean isWhite() {
        return isWhite;
    }

    public PieceType getType() {
        return this.type;
    }

    public void updateSquare(Square square) {
        this.square = square;
    }
}
