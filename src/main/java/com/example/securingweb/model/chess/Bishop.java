package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(boolean isWhite, int row, int col) {
        super(isWhite, PieceType.BISHOP, row, col, isWhite ? 'B' : 'b');
    }

    @Override
    public List<Move> getUnfilteredMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        int[][] directions = { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } }; // Diagonal movements

        for (int[] dir : directions) {
            int newRow = row;
            int newCol = col;
            while (true) {
                newRow += dir[0];
                newCol += dir[1];
                if (!board.isPositionValid(newRow, newCol))
                    break;
                Square targetSquare = board.getSquare(newRow, newCol);
                if (!targetSquare.isOccupied()) {
                    moves.add(new Move(board.getSquare(row, col), targetSquare, this, null, MoveType.REGULAR)); // No capture
                } else {
                    if (targetSquare.getPiece().isWhite() != this.isWhite()) {
                        moves.add(new Move(board.getSquare(row, col), targetSquare, this, targetSquare.getPiece(), MoveType.REGULAR)); // Capture
                    }
                    break;
                }
            }
        }
        return moves;
    }

}