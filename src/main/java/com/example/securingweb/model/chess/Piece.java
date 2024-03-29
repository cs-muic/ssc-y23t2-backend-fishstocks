package com.example.securingweb.model.chess;

import java.util.List;

public abstract class Piece {
    protected String pieceName;
    protected Square square; // Position on the board
    protected boolean isWhite;
    protected PieceType type;
    protected char symbol;
    protected int moveCount;

    public char getSymbol() {
        return symbol; // Currently is symbol for console but will need to be switched to png
    }

    public Piece(String pieceName, boolean isWhite, PieceType type, Square square, char symbol) {
        this.isWhite = isWhite;
        this.type = type;
        this.square = square;
        this.symbol = symbol;
        this.moveCount = 0;
    }

    public abstract List<Move> getUnfilteredMoves(Board board);

    public boolean isWhite() {
        return isWhite;
    }

    public PieceType getType() {
        return this.type;
    }

    public Square getLocation(){
        return this.square;
    }

    public void updateSquare(Square square) {
        this.square = square;
    }

    public String getName() {
        return this.pieceName;
    }    public void incrementMoveCount() {
        moveCount++;
    }

    public void decrementMoveCount() {
        if (moveCount > 0) {
            moveCount--;
        }
    }

    public int getMoveCount(){return moveCount;}

    public boolean hasMoved() {
        return moveCount > 0;
    }

}
