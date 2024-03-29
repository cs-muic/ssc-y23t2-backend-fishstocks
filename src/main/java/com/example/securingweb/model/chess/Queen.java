package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen(String name, boolean isWhite, Square square) {
        super(name, isWhite, PieceType.QUEEN, square, isWhite ? 'Q' : 'q');
    }

    @Override
    public List<Move> getUnfilteredMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        int[][] directions = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 }, { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } }; // Combining
                                                                                                                       // Rook
                                                                                                                       // and
                                                                                                                       // Bishop
                                                                                                                       // movements

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
                    moves.add(new Move(this.square, targetSquare, this, null, false, false, null)); // No capture
                } else {
                    if (targetSquare.getPiece().isWhite() != this.isWhite()) {
                        moves.add(new Move(this.square, targetSquare, this, targetSquare.getPiece(), false, false, null)); // Capture
                    }
                    break;
                }
            }
        }
        return moves;
    }

}
