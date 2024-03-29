package com.example.securingweb.model.chess;


public class Move {
    // Handles movement, en passant, castling, promotions
    private final Square start;
    private final Square end;
    private final Piece movedPiece;
    private final Piece capturedPiece;
    private final String notation;
    private final boolean isEnPassantCapture;
    private final boolean isCastlingKingSide;
    private final boolean isCastlingQueenSide;
    private final PieceType promotionType;

    public Move(Square start, Square end, Piece movedPiece, Piece capturedPiece, boolean isEnPassantCapture,
            boolean isCastlingKingSide, boolean isCastlingQueenSide, PieceType promotionType) {
        this.start = start;
        this.end = end;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.isEnPassantCapture = isEnPassantCapture;
        this.isCastlingKingSide = isCastlingKingSide;
        this.isCastlingQueenSide = isCastlingQueenSide;
        this.promotionType = promotionType;
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

    public String getNotation() {
        return notation;
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
        return isEnPassantCapture;
    }

    public boolean isCastlingKingSide() {
        return isCastlingKingSide;
    }

    public boolean isCastlingQueenSide() {
        return isCastlingQueenSide;
    }

    public PieceType getPromotionType() {return promotionType;}


}