package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class ChessRules {
    private Board board;
    private GameHistory gameHistory;

    public ChessRules(Board board, GameHistory gameHistory) {
        this.board = board;
        this.gameHistory = gameHistory;
    }

    public boolean scanCheck(Square kingLocation, boolean kingIsWhite) {
        int[][] directions = {
                { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 },
                { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 }
        };

        for (int[] dir : directions) {
            if (scanForThreat(kingLocation.getRow(), kingLocation.getCol(), dir[0], dir[1], kingIsWhite)) {
                return true; // Threat found, king is in check
            }
        }
        // Check for knight threats
        return scanForKnightThreats(kingLocation.getRow(), kingLocation.getCol(), kingIsWhite);
    }

    // Scan in a direction from the king's position for a threat
    private boolean scanForThreat(int row, int col, int rowInc, int colInc, boolean kingIsWhite) {
        int currentRow = row + rowInc;
        int currentCol = col + colInc;
        while (currentRow >= 0 && currentRow < 8 && currentCol >= 0 && currentCol < 8) {
            Piece piece = board.getSquare(currentRow, currentCol).getPiece();
            if (piece != null) {
                if (piece.isWhite() != kingIsWhite) { // Encounter an enemy piece
                    // Check if the piece is a threat
                    if (isThreateningPiece(piece, rowInc, colInc, kingIsWhite)) {
                        return true;
                    }
                }
                // Stop scanning if a pawn is encountered, regardless of color
                if (piece.getType() == PieceType.PAWN) {
                    return false;
                }
                // Blocked by own piece, no threat from this direction
                return false;
            }
            currentRow += rowInc;
            currentCol += colInc;
        }
        return false; // No threats found in this direction
    }

    // Determine if a piece can attack the king based on direction and piece type
    private boolean isThreateningPiece(Piece piece, int rowInc, int colInc, boolean kingIsWhite) {
        PieceType type = piece.getType();
        boolean straight = rowInc == 0 || colInc == 0;
        boolean diagonal = Math.abs(rowInc) == Math.abs(colInc);

        if ((type == PieceType.ROOK && straight) ||
                (type == PieceType.BISHOP && diagonal) ||
                (type == PieceType.QUEEN && (straight || diagonal))) {
            return true;
        }

        // Add special case for pawns
        if (type == PieceType.PAWN && diagonal && Math.abs(rowInc) == 1 && Math.abs(colInc) == 1) {
            // White pawn attacking down
            if (kingIsWhite && rowInc == 1) { // Black pawn attacking up
                return true;
            } else return !kingIsWhite && rowInc == -1;
        }

        return false;
    }

    // Check for knight threats specifically
    private boolean scanForKnightThreats(int kingRow, int kingCol, boolean kingIsWhite) {
        int[][] knightMoves = {
                { -2, -1 }, { -2, 1 }, { -1, -2 }, { -1, 2 },
                { 1, -2 }, { 1, 2 }, { 2, -1 }, { 2, 1 }
        };

        for (int[] move : knightMoves) {
            int newRow = kingRow + move[0];
            int newCol = kingCol + move[1];
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                Piece piece = board.getSquare(newRow, newCol).getPiece();
                if (piece != null && piece.isWhite() != kingIsWhite && piece.getType() == PieceType.KNIGHT) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isMoveLegal(Move move, Player currentPlayer) {
        // These special types have already been checked
        if (move.isCastle()
                || move.isEnPassantCapture()
                || move.isPromotion()){
            return true;
        }

        // Try to make the move on the board
        board.movePiece(move.getStart(), move.getEnd());

        // Check if it puts your own king in check
        boolean inCheckAfterMove = scanCheck(board.getKingSquare(currentPlayer.isWhite()), currentPlayer.isWhite());
        board.undoMove(move);

        // The move is legal if it doesn't leave the king in check
        return !inCheckAfterMove;
    }

    private boolean canCastle(Board board, boolean kingSide, Piece king) {
        // Check if the squares between the king and the rook are empty
        int start = kingSide ? 5 : 1;
        int end = kingSide ? 6 : 3;

        // Check if the squares between king and rook are free
        for (int i = start; i <= end; i++) {
            if (board.getSquare(king.isWhite() ? 7 : 0, i).isOccupied()) {
                return false;
            }
        }
        // Check if the rook has moved
        Piece piece = board.getSquare(king.isWhite() ? 7 : 0, kingSide ? 7 : 0).getPiece();
        System.out.println("DEBUG: " + piece.getLocation().getRow() +", "+ piece.getLocation().getCol());
        if (piece == null || !(piece instanceof Rook) || piece.hasMoved()) {
            System.out.println("Rook has already moved");
            System.out.println("Rook location:"+piece.getLocation().getRow()+","+piece.getLocation().getCol());
            return false;
        }
        return true;
    }


    /**
     * This gets the raw moves from the piece class and filters out ones that don't abide to our chess rules
     * It also checks for the availability of special moves
     * @param piece
     * @param board
     * @return Returns list of legal moves
     */

    public List<Move> getPossibleMoves(Piece piece, Board board) {
        List<Move> possibleMoves = piece.getUnfilteredMoves(board);
        List<Move> legalMoves = new ArrayList<>();

        // If King piece, check for castling
        if (piece instanceof King) {
            addCastleMove(board, piece, true, legalMoves);
            addCastleMove(board, piece, false, legalMoves);
        }

        // If Pawn piece, check for en passant
        if (piece instanceof Pawn) {
            addEnPassantMove(board, piece, legalMoves);
        }

        for (Move move : possibleMoves) {
            // Try to make the move on the board
            board.movePiece(move.getStart(), move.getEnd());

            // Check if it puts your own king in check
            boolean inCheckAfterMove = scanCheck(board.getKingSquare(piece.isWhite()), piece.isWhite());
            board.undoMove(move);

            // The move is legal if it doesn't leave the king in check
            if (!inCheckAfterMove) {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    private void addEnPassantMove(Board board, Piece piece, List<Move> legalMoves) {
        if (!(piece instanceof Pawn)) {
            return;
        }

        int direction = piece.isWhite() ? -1 : 1;
        int startRow = piece.getLocation().getRow();
        int startCol = piece.getLocation().getCol();

        // Check the squares diagonally in front of the pawn
        for (int colOffset : new int[]{-1, 1}) {
            int targetCol = startCol + colOffset;
            if (targetCol < 0 || targetCol >= 8) {
                continue;
            }

            Piece targetPiece = board.getSquare(startRow, targetCol).getPiece();
            if (targetPiece != null && targetPiece.getType() == PieceType.PAWN && targetPiece.isWhite() != piece.isWhite()) {
                // Check if the opponent's pawn just moved two squares forward
                Move lastMove = gameHistory.getLastMove();
                if (lastMove != null && lastMove.getMovedPiece() == targetPiece && Math.abs(lastMove.getStart().getRow() - lastMove.getEnd().getRow()) == 2) {
                    // Add the en passant capture to the list of legal moves
                    Square startSquare = board.getSquare(startRow, startCol);
                    Square endSquare = board.getSquare(startRow + direction, targetCol);
                    legalMoves.add(new Move(startSquare, endSquare, piece, targetPiece, MoveType.EN_PASSANT));
                }
            }
        }
    }
    private void addCastleMove(Board board, Piece piece, boolean isKingSide, List<Move> legalMoves) {
        if (canCastle(board, isKingSide, piece)) {
            int row = piece.isWhite() ? 7 : 0;
            int kingColumn = isKingSide ? 6 : 2; // King moves to column 6 (G) if kingside, 2 (C) if queenside
            int rookColumn = isKingSide ? 7 : 0; // Rook is at column 7 (H) if kingside, 0 (A) if queenside
            Piece king = board.getKingSquare(piece.isWhite()).getPiece();
            Piece rook = board.getSquare(row, rookColumn).getPiece(); // Get the correct rook based on the castle side
            legalMoves.add(new Move(board.getKingSquare(piece.isWhite()), board.getSquare(row, kingColumn), king, rook, MoveType.CASTLE));
        }
    }

}