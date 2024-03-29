package com.example.securingweb.model.chess;

public class PieceFactory {

    public static Piece createPiece(String name, PieceType pieceType, boolean isWhite, Square location) {
        switch (pieceType) {
            case PAWN:
                return new Pawn(name, isWhite, location);
            case ROOK:
                return new Rook(name, isWhite, location);
            case KNIGHT:
                return new Knight(name, isWhite, location);
            case BISHOP:
                return new Bishop(name, isWhite, location);
            case QUEEN:
                return new Queen(name, isWhite, location);
            case KING:
                return new King(name, isWhite, location);
            default:
                throw new IllegalArgumentException("Invalid piece type: " + pieceType);
        }
    }
    public static PieceType getPieceTypeFromInput(String input) {
        switch (input.toLowerCase()) {
            case "p":
            case "pawn":
                return PieceType.PAWN;
            case "r":
            case "rook":
                return PieceType.ROOK;
            case "n":
            case "knight":
                return PieceType.KNIGHT;
            case "b":
            case "bishop":
                return PieceType.BISHOP;
            case "q":
            case "queen":
                return PieceType.QUEEN;
            case "k":
            case "king":
                return PieceType.KING;
            default:
                throw new IllegalArgumentException("Invalid piece type: " + input);
        }
    }

}