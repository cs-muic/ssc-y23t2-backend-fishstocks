package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Pawn extends Piece {
    protected boolean hasMoved;

    public Pawn(String name, boolean isWhite, Square square) {
        super(name, isWhite, PieceType.PAWN, square, isWhite ? 'P' : 'p');
        hasMoved = false;
    }

    @Override
    public List<Move> getUnfilteredMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        int direction = this.isWhite() ? -1 : 1;
        int startRow = this.isWhite() ? 6 : 1;

        // Single square forward
        int newRow = square.getRow() + direction;
        if (board.isPositionValid(newRow, square.getCol()) && !board.getSquare(newRow, square.getCol()).isOccupied()) {
            boolean isPromotion = newRow == 0 || newRow == 7;
            moves.add(new Move(this.square, board.getSquare(newRow, square.getCol()), this, null, isPromotion? MoveType.PROMOTION:MoveType.REGULAR));

            // Double square forward from start
            if (square.getRow() == startRow) {
                int doubleForwardRow = newRow + direction;
                if (board.isPositionValid(doubleForwardRow, square.getCol())
                        && !board.getSquare(doubleForwardRow, square.getCol()).isOccupied()) {
                    moves.add(new Move(this.square, board.getSquare(doubleForwardRow, square.getCol()), this, null, MoveType.REGULAR));
                }
            }
        }

        // Capturing moves
        int[] captureCols = { square.getCol() - 1, square.getCol() + 1 };
        for (int captureCol : captureCols) {
            if (board.isPositionValid(newRow, captureCol)) {
                Square targetSquare = board.getSquare(newRow, captureCol);
                if (targetSquare.isOccupied() && targetSquare.getPiece().isWhite() != this.isWhite()) {
                    boolean isPromotion = newRow == 0 || newRow == 7;
                    moves.add(new Move(this.square, targetSquare, this, targetSquare.getPiece(),
                            isPromotion? MoveType.PROMOTION:MoveType.REGULAR));
                }
            }
        }

        return moves;
    }


}
