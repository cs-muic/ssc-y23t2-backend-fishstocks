package com.example.securingweb.model.chess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private final Square[][] board;
    private final Map<Boolean, Square> kingPositions = new HashMap<>();

    /**
     * Initial board setup: Squares + Pieces
     */
    public Board() {
        // Fill board with squares
        board = new Square[8][8];
        boolean isWhite = true;
        for (int row = 0; row < 8; row++) {
            if (row % 2 == 1) {
                isWhite = false; // alternate black and white squares
            }
            for (int col = 0; col < 8; col++) {
                board[row][col] = new Square(row, col, isWhite);
                isWhite = !isWhite;
            }
        }

        // Fill starting squares with right pieces
        for (int col = 0; col < 8; col++) {
            board[1][col].setPiece(new Pawn(false, board[1][col]));
            board[6][col].setPiece(new Pawn(true, board[6][col]));
        }
        // Place black pieces
        board[0][0].setPiece(new Rook(false, board[0][0]));
        board[0][7].setPiece(new Rook(false, board[0][7]));
        board[0][1].setPiece(new Knight(false, board[0][1]));
        board[0][6].setPiece(new Knight(false, board[0][6]));
        board[0][2].setPiece(new Bishop(false, board[0][2]));
        board[0][5].setPiece(new Bishop(false, board[0][5]));
        board[0][3].setPiece(new Queen(false, board[0][3]));
        board[0][4].setPiece(new King(false, board[0][4]));

        // Place white pieces
        board[7][0].setPiece(new Rook(true, board[7][0]));
        board[7][7].setPiece(new Rook(true, board[7][7]));
        board[7][1].setPiece(new Knight(true, board[7][1]));
        board[7][6].setPiece(new Knight(true, board[7][6]));
        board[7][2].setPiece(new Bishop(true, board[7][2]));
        board[7][5].setPiece(new Bishop(true, board[7][5]));
        board[7][3].setPiece(new Queen(true, board[7][3]));
        board[7][4].setPiece(new King(true, board[7][4]));

        // Keep track of king positions for easy access for checkmate scan
        kingPositions.put(true, board[7][4]); // White King's initial position
        kingPositions.put(false, board[0][4]); // Black King's initial position
    }

    /**
     * Checks that the row and col is within the board's bounds
     * 
     * @param row
     * @param col
     * @return true if within board
     */
    public boolean isPositionValid(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    /**
     * Returns the square on that board coordinate
     * 
     * @param row
     * @param col
     * @return
     */
    public Square getSquare(int row, int col) {
        if (isPositionValid(row, col)) {
            return board[row][col];
        }
        return null;
    }

    // For testing on console
    public void printBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square currSquare = board[row][col];
                Piece currPiece = currSquare.getPiece();
                char displayChar;

                if (currPiece != null) {
                    displayChar = currPiece.getSymbol();
                } else {
                    // Differentiate color for empty squares more visibly
                    displayChar = currSquare.getIsWhite() ? '.' : '-'; // Using '.' for white, ':' for black for
                    // visibility
                }

                System.out.print(displayChar + " ");
            }
            System.out.println(); // New line at the end of each row
        }
    }

    /**
     * Will display the squares that a piece can move to
     * 
     * @param selectedSquare
     */
    public void displayWithMovableSquares(Square selectedSquare) {
        Piece selectedPiece = selectedSquare.getPiece();
        if (selectedPiece == null) {
            System.out.println("No piece at the selected square.");
            return;
        }

        List<Square> movableSquares = selectedPiece.getPossibleMoves(this);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = getSquare(i, j);
                if (movableSquares.contains(square)) {
                    System.out.print("* ");
                } else if (square.getPiece() == null) {
                    System.out.print(". ");
                } else {
                    System.out.print(square.getPiece().getSymbol() + " ");
                }
            }
            System.out.println();
        }
    }

    public Piece movePiece(Square start, Square end) { // returns the piece that was captured (if any)
        Piece startPiece = start.getPiece();
        start.setPiece(null);
        Piece endPiece = end.getPiece();
        end.setPiece(startPiece);
        return endPiece;
    }

    public Square getKingSquare(boolean isWhite) {
        return kingPositions.get(isWhite);
    }

}
