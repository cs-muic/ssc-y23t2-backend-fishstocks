package com.example.securingweb.model.chess;

import java.util.List;

public class ChessDisplayUtil {
    public static void displayMovableSquares(Board board, ChessRules rules, Square selectedSquare) {
        Piece selectedPiece = selectedSquare.getPiece();
        if (selectedPiece == null) {
            System.out.println("No piece at the selected square.");
            return;
        }

        List<Move> possibleMoves = rules.getPossibleMoves(selectedPiece, board);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = board.getSquare(i, j);
                boolean isMoveTarget = possibleMoves.stream().anyMatch(move -> move.getEnd().equals(square));
                if (isMoveTarget) {
                    System.out.print("* ");
                } else if (square.getPiece() == null) {
                    System.out.print(". ");
                } else {
                    System.out.print(square.getPiece().getSymbol() + " ");
                }
            }
            System.out.println();
        }
    }
}
