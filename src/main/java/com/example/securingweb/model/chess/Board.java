package com.example.securingweb.model.chess;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;


@Getter
@Setter

public class Board {
    private final Square[][] board;

    private HashMap<Boolean, Square> kingMap = new HashMap<>();


    /**
     * Initial board setup: Squares + Piece
     * Pieces are also put into a hashmap for easy access
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
            placePiece(PieceType.PAWN, false, 1, col);
            placePiece(PieceType.PAWN, true, 6, col);
        }
        // Place black pieces
        placePiece(PieceType.ROOK, false, 0, 0);
        placePiece(PieceType.ROOK, false, 0, 7);
        placePiece(PieceType.ROOK, true, 7, 0);
        placePiece(PieceType.ROOK, true, 7, 7);

        placePiece(PieceType.KNIGHT, false, 0, 1);
        placePiece(PieceType.KNIGHT, false, 0, 6);
        placePiece(PieceType.KNIGHT, true, 7, 1);
        placePiece(PieceType.KNIGHT, true, 7, 6);

        placePiece(PieceType.BISHOP, false, 0, 2);
        placePiece(PieceType.BISHOP, false, 0, 5);
        placePiece(PieceType.BISHOP, true, 7, 2);
        placePiece(PieceType.BISHOP, true, 7, 5);

        placePiece(PieceType.QUEEN, false, 0, 3);
        placePiece(PieceType.QUEEN, true, 7, 3);

        placePiece(PieceType.KING, false, 0, 4);
        placePiece(PieceType.KING, true, 7, 4);

    }

    private void placePiece(PieceType type, boolean isWhite, int row, int col) {
        Piece piece = PieceFactory.createPiece(this, type, isWhite, row, col);
        board[row][col].setPiece(piece);
        if(type == PieceType.KING){
            kingMap.put(isWhite, this.getSquare(row, col));
        }
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
                    displayChar = currSquare.isWhite() ? '.' : '-'; // Using '.' for white, ':' for black for
                    // visibility
                }

                System.out.print(displayChar + " ");
            }
            System.out.println(); // New line at the end of each row
        }
    }


    public void movePiece(Square start, Square end) {
        Piece piece = start.getPiece();
        if (piece != null) {
            Piece capturedPiece = end.getPiece();
            if (capturedPiece != null) {
//                updateMap(false, capturedPiece); // Remove the captured piece from the map
            }
            piece.updateLocation(end.getRow(), end.getCol());
            end.setPiece(piece);
            start.emptySquare();

            piece.incrementMoveCount();
        } else {
            throw new IllegalStateException("No piece at the start square");
        }
    }

    public void undoMove(Move move) {
        // Move the piece back to its original position
        Piece piece = move.getEnd().getPiece();
        if (piece != null) {
            piece.updateLocation(move.getStart().getRow(), move.getStart().getCol());
            move.getStart().setPiece(piece);
            if(move.getCapturedPiece() == null){
                move.getEnd().emptySquare();
            }else{
                move.getEnd().setPiece(move.getCapturedPiece());
            }
            piece.decrementMoveCount();
        } else {
            throw new IllegalStateException("No piece at the end square");
        }

    }

    public Square getKingSquare(boolean isWhite) {
        return kingMap.get(isWhite);
    }

    public void doEnPassant(Piece attackingPawn, Piece targetPawn) {
        // Get the location of the target pawn
        Square targetLocation = getSquare(targetPawn.getRow(), targetPawn.getCol());

        // Determine the location where the attacking pawn should end up
        Square newLocation;
        if (attackingPawn.isWhite()) {
            newLocation = getSquare(targetLocation.getRow() - 1, targetLocation.getCol());
        } else {
            newLocation = getSquare(targetLocation.getRow() + 1, targetLocation.getCol());
        }

        // Remove the target pawn from the board
        targetLocation.emptySquare();
        // Remove the attacking pawn from its old location
        getSquare(attackingPawn.getRow(), attackingPawn.getCol()).emptySquare();

        // Move the attacking pawn to the new location
        attackingPawn.updateLocation(newLocation.getRow(), newLocation.getCol());
        newLocation.setPiece(attackingPawn);
    }

    public void doCastle(Piece king, Piece rook) {
        // Get the original locations
        Square kingStart = getSquare(king.getRow(), king.getCol());
        Square rookStart = getSquare(rook.getRow(), rook.getCol());

        // Determine the new locations
        Square rookEnd, kingEnd;
        if (rookStart.getCol() > kingStart.getCol()) { // Kingside castle
            rookEnd = board[rookStart.getRow()][5];
            kingEnd = board[kingStart.getRow()][6];
        } else { // Queenside castle
            rookEnd = board[rookStart.getRow()][3];
            kingEnd = board[kingStart.getRow()][2];
        }

        // Move the pieces
        movePiece(rookStart, rookEnd);
        movePiece(kingStart, kingEnd);
    }

    public void promotePawn(Move move) {
        // Get the location where the pawn should be promoted
        Square location = move.getEnd();

        // Store the pawn's original location
        Square originalLocation = getSquare(move.getMovedPiece().getRow(), move.getMovedPiece().getCol());


        if(move.getCapturedPiece() != null){
            // need this for when the pawn captures AND promotes at the same time
            getSquare(move.getCapturedPiece().getRow(), move.getCapturedPiece().getCol()).emptySquare(); // empty the square with wtv piece we just captured
        }

        // Empty the square where the pawn was
        originalLocation.emptySquare();

        // Empty the square where the pawn should be promoted
        location.emptySquare();

        // Create promoted piece
        Piece promotedPiece = PieceFactory.createPromotedPiece(this, move.getMovedPiece().isWhite(), location.getRow(), location.getCol());

        // Update the square
        location.setPiece(promotedPiece);
    }


    public void handleCapturedPiece(GameState gameState, Piece capturedPiece) {
        gameState.getCurrentPlayer().addCaptured(capturedPiece);
    }
}
