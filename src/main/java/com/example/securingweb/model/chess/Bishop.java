package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    private static final int[][] DIRECTIONS = { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };

    public Bishop(boolean isWhite, Square square) {
        super(isWhite, PieceType.BISHOP, square, isWhite ? 'B' : 'b');
    }

    @Override
    public List<Square> getPossibleMoves(Board board) {
        List<Square> moves = new ArrayList<>();

        for (int[] direction : DIRECTIONS) {
            addMovesInDirection(board, moves, direction);
        }

        return moves;
    }

    private void addMovesInDirection(Board board, List<Square> moves, int[] direction) {
        int newRow = this.square.getRow();
        int newCol = this.square.getCol();

        while (true) {
            newRow += direction[0];
            newCol += direction[1];

            if (!board.isPositionValid(newRow, newCol))
                break;

            Square targetSquare = board.getSquare(newRow, newCol);

            if (!targetSquare.isOccupied()) {
                moves.add(targetSquare);
            } else {
                if (targetSquare.getPiece().isWhite() != this.isWhite())
                    moves.add(targetSquare);
                break;
            }
        }
    }
}