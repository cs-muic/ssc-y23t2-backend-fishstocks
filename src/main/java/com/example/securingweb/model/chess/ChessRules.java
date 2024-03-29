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
                    return isThreateningPiece(piece, rowInc, colInc, kingIsWhite);
                } else {
                    return false; // Blocked by own piece, no threat from this direction
                }
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
        if (type == PieceType.PAWN && diagonal && Math.abs(rowInc) == 1 &&
                ((kingIsWhite && rowInc < 0) || (!kingIsWhite && rowInc > 0))) {
            return true; // Pawn can attack diagonally
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
                || move.getPromotionType() != null){
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
        for (int i = start; i <= end; i++) {
            if (board.getSquare(king.isWhite() ? 7 : 0, i).isOccupied()) {
                return false;
            }
        }
        // Check if the rook has moved
        Piece piece = board.getSquare(king.isWhite() ? 7 : 0, kingSide ? 7 : 0).getPiece();
        if (piece == null || !(piece instanceof Rook) || ((Rook) piece).hasMoved()) {
            return false;
        }
        return true;
    }

    private boolean canEnPassant(Board board, Square start, Square end) {
        Piece piece = start.getPiece();
        if (piece == null || piece.getType() != PieceType.PAWN) {
            return false;
        }
        // Check if the move is a valid en passant move
        int direction = piece.isWhite() ? -1 : 1;
        if (end.getRow() == start.getRow() + direction && Math.abs(end.getCol() - start.getCol()) == 1) {
            // Check if the adjacent square in the direction of the move contains an opponent's pawn
            Piece adjacentPiece = board.getSquare(start.getRow(), end.getCol()).getPiece();
            if (adjacentPiece != null && adjacentPiece.getType() == PieceType.PAWN && adjacentPiece.isWhite() != piece.isWhite()) {
                // Check if the opponent's pawn just moved two squares forward
                Move lastMove = gameHistory.getLastMove();
                if (lastMove.getStart().getRow() == start.getRow() && lastMove.getEnd().getRow() == start.getRow() + 2 * direction && lastMove.getEnd().getCol() == end.getCol()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if piece can promote
     * @param piece
     * @return
     */
    private boolean canPromote(Piece piece) throws IllegalArgumentException {
        if (piece.getType() != PieceType.PAWN) {
            throw new IllegalArgumentException("Only pawns can be promoted");
        }
        return (piece.isWhite() && piece.getLocation().getRow() == 0) || (!piece.isWhite() && piece.getLocation().getRow() == 7);
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
            addCastleMove(board, piece, true, 6, legalMoves);
            addCastleMove(board, piece, false, 2, legalMoves);
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
    private void addCastleMove(Board board, Piece piece, boolean isKingSide, int rookColumn, List<Move> legalMoves) {
        if (canCastle(board, isKingSide, piece)) {
            int row = piece.isWhite() ? 7 : 0;
            Piece rook = board.getSquare(row, rookColumn).getPiece();
            legalMoves.add(new Move(board.getKingSquare(piece.isWhite()), board.getSquare(row, rookColumn), piece, rook, false, true, null));
        }
    }

    public boolean canMoveTo(Piece currPiece, Square targetSquare, Board board) {
        List<Move> possibleMoves = currPiece.getUnfilteredMoves(board);
        return possibleMoves.contains(targetSquare);
    }
}