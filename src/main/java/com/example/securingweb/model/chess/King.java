package com.example.securingweb.model.chess;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(String name, boolean isWhite, Square square) {
        super(name, isWhite, PieceType.KING, square, isWhite ? 'K' : 'k');
    }

    @Override
    public List<Move> getUnfilteredMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        int[][] directions = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };

        for (int[] dir : directions) {
            int newRow = this.square.getRow() + dir[0];
            int newCol = this.square.getCol() + dir[1];
            if (board.isPositionValid(newRow, newCol)) {
                Square targetSquare = board.getSquare(newRow, newCol);
                if (targetSquare != null
                        && (!targetSquare.isOccupied() || targetSquare.getPiece().isWhite() != this.isWhite())) {
                    moves.add(new Move(this.square, targetSquare, this, targetSquare.getPiece(), MoveType.REGULAR));
                }
            }
        }

        return moves;
    }


}
