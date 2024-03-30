package com.example.securingweb.model.chess;

import lombok.Getter;

public class Move {
    // Handles movement, en passant, castling, promotions
    @Getter
    private final Square start;
    @Getter
    private final Square end;
    @Getter
    private final Piece movedPiece;
    @Getter
    private final Piece capturedPiece;
    @Getter
    private final String notation;
    private final MoveType moveType;


    public Move(Square start, Square end, Piece movedPiece, Piece capturedPiece, MoveType moveType) {
        this.start = start;
        this.end = end;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.moveType = moveType;
        this.notation = makeNotation();
    }

    private String makeNotation() {
        StringBuilder notation = new StringBuilder();
        char symbol = movedPiece.getSymbol();
        String hist = end.getSquareName();
        if (symbol == 'p' || symbol == 'P') {
            notation.append(hist);
        } else {
            notation.append(symbol + hist);
        }
        notation.append(" ");
        return notation.toString();
    }

    public boolean isEnPassantCapture() {
        return moveType == MoveType.EN_PASSANT;
    }

    public boolean isCastle() {
        return moveType == MoveType.CASTLE;
    }


    public boolean isPromotion() {return moveType == MoveType.PROMOTION;}


}