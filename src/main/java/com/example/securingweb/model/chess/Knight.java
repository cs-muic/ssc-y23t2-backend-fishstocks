package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(String name, boolean isWhite, Square square) {
        super(name, isWhite, PieceType.KNIGHT, square, isWhite ? 'N' : 'n');
    }

    @Override
    public List<Move> getUnfilteredMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        int[][] offsets = { { -2, -1 }, { -2, 1 }, { -1, -2 }, { -1, 2 }, { 1, -2 }, { 1, 2 }, { 2, -1 }, { 2, 1 } }; // L-shapes

        for (int[] offset : offsets) {
            int newRow = square.getRow() + offset[0];
            int newCol = square.getCol() + offset[1];
            if (board.isPositionValid(newRow, newCol)) {
                Square targetSquare = board.getSquare(newRow, newCol);
                if (!targetSquare.isOccupied() || targetSquare.getPiece().isWhite() != this.isWhite()) {
                    moves.add(new Move(this.square, targetSquare, this, targetSquare.getPiece(), false, false,
                            false)); // Possible capture
                }
            }
        }
        return moves;
    }

}
