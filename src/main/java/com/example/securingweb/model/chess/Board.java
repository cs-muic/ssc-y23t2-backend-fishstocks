package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private final Square[][] board;
    private final Map<PieceType, List<Piece>> pieceMap; // Hashmap for easy access to our pieces

    /**
     * Initial board setup: Squares + Piece
     * Pieces are also put into a hashmap for easy access
     */
    public Board() {
        // Fill board with squares
        pieceMap = new HashMap<>();
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
        Piece piece;
        switch (type) {
            case PAWN:
                piece = new Pawn(isWhite, board[row][col]);
                break;
            case ROOK:
                piece = new Rook(isWhite, board[row][col]);
                break;
            case KNIGHT:
                piece = new Knight(isWhite, board[row][col]);
                break;
            case BISHOP:
                piece = new Bishop(isWhite, board[row][col]);
                break;
            case KING:
                piece = new King(isWhite, board[row][col]);
                break;
            case QUEEN:
                piece = new Queen(isWhite, board[row][col]);
                break;

            default:
                throw new IllegalArgumentException("Invalid piece type: " + type);
        }
        board[row][col].setPiece(piece);
        pieceMap.computeIfAbsent(type, k -> new ArrayList<>()).add(piece);
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
    public void displayMovableSquares(ChessRules rules, Square selectedSquare) {
        Piece selectedPiece = selectedSquare.getPiece();
        if (selectedPiece == null) {
            System.out.println("No piece at the selected square.");
            return;
        }

        List<Move> possibleMoves = rules.getPossibleMoves(selectedPiece, this);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = getSquare(i, j);
                boolean isMoveTarget = possibleMoves.stream().anyMatch(move -> move.getEnd().equals(square));
                if (isMoveTarget) {
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

    public void movePiece(Square start, Square end) {
        Piece piece = start.getPiece();
        if (piece != null) {
            // If the piece being moved is a king or rook set it to flag that it has moved.
            if (piece instanceof King) {
                ((King) piece).setHasMoved(true);
            }
            if (piece instanceof Rook) {
                ((Rook) piece).setHasMoved(true);
            }
            Piece capturedPiece = end.getPiece();
            if (capturedPiece != null) {
                updateMap(false, capturedPiece); // Remove the captured piece from the map
            }
            piece.updateSquare(end);
            end.setPiece(piece);
            start.emptySquare();
            updateMap(true, piece); // Add the moved piece to the map
        } else {
            throw new IllegalStateException("No piece at the start square");
        }
    }

    public void undoMove(Move move) {
        // Move the piece back to its original position
        movePiece(move.getEnd(), move.getStart());
        if (move.getCapturedPiece() != null) {
            // If a piece was captured, add it back to the board and the pieceMap
            move.getEnd().setPiece(move.getCapturedPiece());
            updateMap(true, move.getCapturedPiece());
        }
        updateMap(true, move.getStart().getPiece()); // Add the moved piece back to the map
    }

    public Square getKingSquare(boolean isWhite) {
        List<Piece> kingList = pieceMap.get(PieceType.KING);
        for (Piece king : kingList) {
            if (king.isWhite() == isWhite) {
                return king.getLocation();
            }
        }
        return null; // Return null if no King of the specified color is found
    }


    /**
     * TODO: implement the actual movements on the board.
     */
    private void doEnPassant(Piece attackingPawn, Piece targetPawn) {
        // Kills the target pawn so hashmap needs to be updated
        updateMap(false, targetPawn);
        // Remove from the board
        Square loc = targetPawn.getLocation();
        loc.emptySquare();
    }

    private void doCastle(Piece Rook, Piece King) {

    }

    private void doPromotions(Piece pawnPiece, Piece promotedPiece) {

    }

    /**
     * Adds or removes the piece from the hashmap
     * @param addPiece - if true we want to add, else we want to remove
     * @param piece
     */
    public void updateMap(boolean addPiece, Piece piece){
        List<Piece> pieceList = this.pieceMap.get(piece.getType());
        if (pieceList != null) {
            if (addPiece){
                pieceList.add(piece);
            }else{
                pieceList.remove(piece);
            }
        }
    }
}
