package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(boolean isWhite, int row, int col) {
        super(isWhite, PieceType.KNIGHT, row, col, isWhite ? 'N' : 'n');
    }

    @Override
    public List<Move> getUnfilteredMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        int[][] offsets = { { -2, -1 }, { -2, 1 }, { -1, -2 }, { -1, 2 }, { 1, -2 }, { 1, 2 }, { 2, -1 }, { 2, 1 } }; // L-shapes

        for (int[] offset : offsets) {
            int newRow = row + offset[0];
            int newCol = col + offset[1];
            if (board.isPositionValid(newRow, newCol)) {
                Square targetSquare = board.getSquare(newRow, newCol);
                if (!targetSquare.isOccupied() || targetSquare.getPiece().isWhite() != this.isWhite()) {
                    moves.add(new Move(board.getSquare(row, col), targetSquare, this, targetSquare.getPiece(), MoveType.REGULAR)); // Possible capture
                }
            }
        }
        return moves;
    }

}
