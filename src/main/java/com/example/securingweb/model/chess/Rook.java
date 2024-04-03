package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public Rook(boolean isWhite, int row, int col) {
        super(isWhite, PieceType.ROOK, row, col, isWhite ? 'R' : 'r');
    }

    @Override
    public List<Move> getUnfilteredMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        int[][] directions = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } }; // Up, Down, Left, Right

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
                    break; // Stop moving in this direction after a capture or hitting a friendly piece
                }
            }
        }
        return moves;
    }

}
