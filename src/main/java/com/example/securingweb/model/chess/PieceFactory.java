package com.example.securingweb.model.chess;

import java.util.Scanner;

public class PieceFactory {


    public static Piece createPiece(Board board, PieceType pieceType, boolean isWhite, int row, int col) {
        switch (pieceType) {
            case PAWN:
                return new Pawn(isWhite, row, col);
            case ROOK:
                return new Rook(isWhite, row, col);
            case KNIGHT:
                return new Knight(isWhite, row, col);
            case BISHOP:
                return new Bishop(isWhite, row, col);
            case QUEEN:
                return new Queen(isWhite, row, col);
            case KING:
                return new King(isWhite, row, col);
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

    private static PieceType getPromotionPieceType() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What piece would you like? [q,r,b,n] ");
        String input = scanner.nextLine();
        return PieceFactory.getPieceTypeFromInput(input);
    }

    public static Piece createPromotedPiece(Board board, boolean isWhite, int row, int col) {
        PieceType promotionType = getPromotionPieceType();
        return createPiece(board, promotionType, isWhite, row, col);
    }

}