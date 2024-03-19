package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(boolean isWhite, Square square) {
        super(isWhite, PieceType.KING, square, isWhite ? 'K' : 'k');
    }

    public List<Square> getPossibleMoves(Square currSquare, Board board) {
        List<Square> moves = new ArrayList<>();
        int[][] offsets = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };

        for (int[] offset : offsets) {
            int newRow = currSquare.getRow() + offset[0];
            int newCol = currSquare.getCol() + offset[1];
            if (board.isPositionValid(newRow, newCol)) {
                Square targetSquare = board.getSquare(newRow, newCol);
                if (!targetSquare.isOccupied() || targetSquare.getPiece().isWhite() != this.isWhite()) {
                    moves.add(targetSquare);
                }
            }
        }
        return moves;
    }

    @Override
    public List<Square> getPossibleMoves(Board board) {
        return null;
    }
}
