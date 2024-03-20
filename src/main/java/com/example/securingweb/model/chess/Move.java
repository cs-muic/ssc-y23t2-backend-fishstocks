package com.example.securingweb.model.chess;

public class Move {
    // Handles movement, en passant, castling, promotions
    private final Square start;
    private final Square end;
    private final Piece movedPiece;
    private final Piece capturedPiece;
    private final boolean isCheck;
    private final boolean isCheckmate;
    private final String notation;

    public Move(Square start, Square end, Piece movedPiece, Piece capturedPiece, boolean isCheck, boolean isCheckmate) {
        this.start = start;
        this.end = end;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.isCheck = isCheck;
        this.isCheckmate = isCheckmate;
        this.notation = makeNotation();
    }

    public Square getStart() {
        return start;
    }

    public Square getEnd() {
        return end;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public boolean isCheckmate() {
        return isCheckmate;
    }

    public String getNotation() {
        return notation;
    }

    private String makeNotation(){
        StringBuilder notation = new StringBuilder();
        char symbol = start.getPiece().getSymbol();
        String hist = end.getSquareName();
        if (symbol == 'p' || symbol == 'P') {
            notation.append(hist);
        } else {
            notation.append(symbol + hist);
        }
        notation.append(" ");
        return notation.toString();
    }


}