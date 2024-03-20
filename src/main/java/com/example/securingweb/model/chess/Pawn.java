package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    protected boolean hasMoved;

    public Pawn(boolean isWhite, Square square) {
        super(isWhite, PieceType.PAWN, square, isWhite ? 'P' : 'p');
        hasMoved = false;
    }

    @Override
    public List<Square> getPossibleMoves(Board board) {
        List<Square> moves = new ArrayList<>();
        int direction = this.isWhite() ? -1 : 1;
        int startRow = this.isWhite() ? 6 : 1;

        // Single square forward
        int newRow = square.getRow() + direction;
        if (board.isPositionValid(newRow, square.getCol()) && !board.getSquare(newRow, square.getCol()).isOccupied()) {
            moves.add(board.getSquare(newRow, square.getCol()));

            // Double square forward from start
            if (square.getRow() == startRow) {
                int doubleForwardRow = newRow + direction;
                if (board.isPositionValid(doubleForwardRow, square.getCol())
                        && !board.getSquare(doubleForwardRow, square.getCol()).isOccupied()) {
                    moves.add(board.getSquare(doubleForwardRow, square.getCol()));
                }
            }
        }

        // Capturing moves
        int[] captureCols = { square.getCol() - 1, square.getCol() + 1 }; // Check left and right
        for (int captureCol : captureCols) {
            newRow = square.getRow() + direction; // Calculate newRow for each captureCol
            if (board.isPositionValid(newRow, captureCol)) {
                Square targetSquare = board.getSquare(newRow, captureCol);
                if (targetSquare.isOccupied() && targetSquare.getPiece().isWhite() != this.isWhite()) {
                    moves.add(targetSquare);
                }
            }
        }

        return moves;
    }

}
