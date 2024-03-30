package com.example.securingweb.model.chess;

import java.util.Scanner;

public class PieceFactory {


    public static Piece createPiece(Board board, PieceType pieceType, boolean isWhite, Square location) {
        String name = generatePieceName(board, pieceType, isWhite);
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
    private static String generatePieceName(Board board, PieceType type, boolean isWhite) {
        String color = isWhite ? "w" : "b";
        int count = board.getPieceMap().get(type).get(isWhite).size() + 1;
        return type.toString().toLowerCase() + "_" + color + "_" + count;
    }

    private static PieceType getPromotionPieceType() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What piece would you like? [q,r,b,n] ");
        String input = scanner.nextLine();
        return PieceFactory.getPieceTypeFromInput(input);
    }

    public static Piece createPromotedPiece(Board board, boolean isWhite, Square location) {
        PieceType promotionType = getPromotionPieceType();
        return createPiece(board, promotionType, isWhite, location);
    }

}