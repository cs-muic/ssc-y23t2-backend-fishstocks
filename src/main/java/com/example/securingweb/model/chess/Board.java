package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private final Square[][] board;
    private final Map<PieceType, HashMap<String, Piece>> pieceMap; // Hashmap for easy access to our pieces

    /**
     * Initial board setup: Squares + Piece
     * Pieces are also put into a hashmap for easy access
     */
    public Board() {
        // Fill board with squares
        pieceMap = new HashMap<>();
        for (PieceType type : PieceType.values()) {
            pieceMap.put(type, new HashMap<>());
        }
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
            placePiece("pawn_b_" + (col + 1), PieceType.PAWN, false, 1, col);
            placePiece("pawn_w_" + (col + 1), PieceType.PAWN, true, 6, col);
        }
        // Place black pieces
        placePiece("rook_b_1", PieceType.ROOK, false, 0, 0);
        placePiece("rook_b_2", PieceType.ROOK, false, 0, 7);
        placePiece("rook_w_1", PieceType.ROOK, true, 7, 0);
        placePiece("rook_w_2", PieceType.ROOK, true, 7, 7);

        placePiece("knight_b_1", PieceType.KNIGHT, false, 0, 1);
        placePiece("knight_b_2", PieceType.KNIGHT, false, 0, 6);
        placePiece("knight_w_1", PieceType.KNIGHT, true, 7, 1);
        placePiece("knight_w_2", PieceType.KNIGHT, true, 7, 6);

        placePiece("bishop_b_1", PieceType.BISHOP, false, 0, 2);
        placePiece("bishop_b_2", PieceType.BISHOP, false, 0, 5);
        placePiece("bishop_w_1", PieceType.BISHOP, true, 7, 2);
        placePiece("bishop_w_2", PieceType.BISHOP, true, 7, 5);

        placePiece("queen_b_1", PieceType.QUEEN, false, 0, 3);
        placePiece("queen_w_1", PieceType.QUEEN, true, 7, 3);

        placePiece("king_b_1", PieceType.KING, false, 0, 4);
        placePiece("king_w_1", PieceType.KING, true, 7, 4);
    }

    private void placePiece(String name, PieceType type, boolean isWhite, int row, int col) {
        Piece piece;
        switch (type) {
            case PAWN:
                piece = new Pawn(name, isWhite, board[row][col]);
                break;
            case ROOK:
                piece = new Rook(name, isWhite, board[row][col]);
                break;
            case KNIGHT:
                piece = new Knight(name, isWhite, board[row][col]);
                break;
            case BISHOP:
                piece = new Bishop(name, isWhite, board[row][col]);
                break;
            case KING:
                piece = new King(name, isWhite, board[row][col]);
                break;
            case QUEEN:
                piece = new Queen(name, isWhite, board[row][col]);
                break;

            default:
                throw new IllegalArgumentException("Invalid piece type: " + type);
        }
        board[row][col].setPiece(piece);
        pieceMap.get(type).put(name, piece);

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
        HashMap<String, Piece> kingMap = pieceMap.get(PieceType.KING);
        if(isWhite){
            return kingMap.get("king_w_1").getLocation();
        }else{
            return kingMap.get("king_b_1").getLocation();
        }
    }

    public void doEnPassant(Piece attackingPawn, Piece targetPawn) {
        // Kills the target pawn so hashmap needs to be updated
        updateMap(false, targetPawn);
        // Remove from the board
        Square location = targetPawn.getLocation();
        location.emptySquare();
        // Move the attacking pawn to the target pawn's square
        attackingPawn.updateSquare(location);
        location.setPiece(attackingPawn);
    }

    public void doCastle(Piece king, Piece rook) {
        // Get the original locations
        Square kingStart = king.getLocation();
        Square rookStart = rook.getLocation();

        // Determine the new locations
        Square rookEnd = (rookStart.getCol() < 4) ? board[rookStart.getRow()][3] : board[rookStart.getRow()][5];
        Square kingEnd = (kingStart.getCol() < 4) ? board[kingStart.getRow()][2] : board[kingStart.getRow()][6];

        // Move the pieces
        movePiece(rookStart, rookEnd);
        movePiece(kingStart, kingEnd);
    }

    public void doPromotions(Piece pawnPiece, Piece promotedPiece) {
        // Remove the pawn from the board and the pieceMap
        updateMap(false, pawnPiece);
        Square loc = pawnPiece.getLocation();
        loc.emptySquare();

        // Add the promoted piece to the board and the pieceMap
        promotedPiece.updateSquare(loc);
        loc.setPiece(promotedPiece);
        updateMap(true, promotedPiece);
    }

    /**
     * Adds or removes the piece from the hashmap
     * @param addPiece - if true we want to add, else we want to remove
     * @param piece
     */
    public void updateMap(boolean addPiece, Piece piece){
        HashMap<String, Piece> pieceMap = this.pieceMap.get(piece.getType());
        if (pieceMap != null) {
            if (addPiece){
                pieceMap.put(piece.getName(), piece);
            }else{
                pieceMap.remove(piece.getName());
            }
        }
    }

}
