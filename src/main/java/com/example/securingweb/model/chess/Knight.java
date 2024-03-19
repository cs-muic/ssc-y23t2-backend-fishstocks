package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(boolean isWhite, Square square) {
        super(isWhite, PieceType.KNIGHT, square, isWhite ? 'N' : 'n');
    }

    @Override
    public List<Square> getPossibleMoves(Board board) {
        int[][] offsets = {
                { -2, -1 }, { -1, -2 }, { 1, -2 }, { 2, -1 },
                { 2, 1 }, { 1, 2 }, { -1, 2 }, { -2, 1 }
        };
        List<Square> moves = new ArrayList<>();

        for (int[] offset : offsets) {
            int newRow = square.getRow() + offset[0];
            int newCol = square.getCol() + offset[1];

            if (board.isPositionValid(newRow, newCol)) {
                Square targetSquare = board.getSquare(newRow, newCol);
                if (!targetSquare.isOccupied() || targetSquare.getPiece().isWhite() != this.isWhite()) {
                    moves.add(targetSquare);
                }
            }
        }
        return moves;
    }

}
