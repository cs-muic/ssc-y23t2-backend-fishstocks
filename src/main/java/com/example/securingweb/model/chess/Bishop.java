package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(String name, boolean isWhite, Square square) {
        super(name, isWhite, PieceType.BISHOP, square, isWhite ? 'B' : 'b');
    }

    @Override
    public List<Move> getUnfilteredMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        int[][] directions = { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } }; // Diagonal movements

        for (int[] dir : directions) {
            int newRow = square.getRow();
            int newCol = square.getCol();
            while (true) {
                newRow += dir[0];
                newCol += dir[1];
                if (!board.isPositionValid(newRow, newCol))
                    break;
                Square targetSquare = board.getSquare(newRow, newCol);
                if (!targetSquare.isOccupied()) {
                    moves.add(new Move(this.square, targetSquare, this, null, MoveType.REGULAR)); // No capture
                } else {
                    if (targetSquare.getPiece().isWhite() != this.isWhite()) {
                        moves.add(new Move(this.square, targetSquare, this, targetSquare.getPiece(), MoveType.REGULAR)); // Capture
                    }
                    break;
                }
            }
        }
        return moves;
    }

}