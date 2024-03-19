package com.example.securingweb.model.chess;
public class Square {
    private Piece piece;
    private final int row, col;
    private boolean isWhite;

    public Square(int row, int col, boolean isWhite) {
        this.row = row;
        this.col = col;
        this.piece = null;
        this.isWhite = isWhite;
    }

    public boolean isOccupied() {
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void removePiece() {
        this.piece = null;
    }
    public int getRow(){
        return this.row;
    }
    public int getCol(){
        return this.col;
    }
    public boolean getIsWhite(){
        return isWhite;
    }

}
