package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(boolean isWhite, int row, int col) {
        super(isWhite, PieceType.PAWN, row, col, isWhite ? 'P' : 'p');
    }

    @Override
    public List<Move> getUnfilteredMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        int direction = this.isWhite() ? -1 : 1;
        int startRow = this.isWhite() ? 6 : 1;

        // Single square forward
        int newRow = row + direction;
        if (board.isPositionValid(newRow, col) && !board.getSquare(newRow, col).isOccupied()) {
            boolean isPromotion = newRow == 0 || newRow == 7;
            moves.add(new Move(board.getSquare(row, col), board.getSquare(newRow, col), this, null, isPromotion? MoveType.PROMOTION:MoveType.REGULAR));

            // Double square forward from start
            if (row == startRow) {
                int doubleForwardRow = newRow + direction;
                if (board.isPositionValid(doubleForwardRow, col)
                        && !board.getSquare(doubleForwardRow, col).isOccupied()) {
                    moves.add(new Move(board.getSquare(row,col), board.getSquare(doubleForwardRow, col), this, null, MoveType.REGULAR));
                }
            }
        }

        // Capturing moves
        int[] captureCols = { col - 1, col + 1 };
        for (int captureCol : captureCols) {
            if (board.isPositionValid(newRow, captureCol)) {
                Square targetSquare = board.getSquare(newRow, captureCol);
                if (targetSquare.isOccupied() && targetSquare.getPiece().isWhite() != this.isWhite()) {
                    boolean isPromotion = newRow == 0 || newRow == 7;
                    moves.add(new Move(board.getSquare(row, col), targetSquare, this, targetSquare.getPiece(),
                            isPromotion? MoveType.PROMOTION:MoveType.REGULAR));
                }
            }
        }

        return moves;
    }


}
