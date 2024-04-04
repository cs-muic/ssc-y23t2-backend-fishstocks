package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(boolean isWhite, int row, int col) {
        super(isWhite, PieceType.KING, row, col, isWhite ? 'K' : 'k');
    }

    @Override
    public List<Move> getUnfilteredMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        int[][] directions = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (board.isPositionValid(newRow, newCol)) {
                Square targetSquare = board.getSquare(newRow, newCol);
                if (targetSquare != null
                        && (!targetSquare.isOccupied() || targetSquare.getPiece().isWhite() != this.isWhite())) {
                    moves.add(new Move(board.getSquare(row, col), targetSquare, this, targetSquare.getPiece(), MoveType.REGULAR));
                }
            }
        }

        return moves;
    }


}
