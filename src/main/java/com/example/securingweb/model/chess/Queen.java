package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen(boolean isWhite, Square square) {
        super(isWhite, PieceType.QUEEN, square, isWhite ? 'Q' : 'q');
    }

    @Override
    public List<Square> getPossibleMoves(Board board) {
        List<Square> moves = new ArrayList<>();
        // Combines directions from the rook and bishop
        int[][] directions = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 }, { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };

        for (int[] dir : directions) {
            int newRow = square.getRow(), newCol = square.getCol();
            while (true) {
                newRow += dir[0];
                newCol += dir[1];
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
        return moves;
    }

}
