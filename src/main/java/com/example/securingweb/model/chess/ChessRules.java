package com.example.securingweb.model.chess;

public class ChessRules {
    private Board board;
    public ChessRules(Board board) {
        this.board = board;
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
        // Simplify threat logic based on piece type and direction
        // Example: Rooks on straight lines, bishops on diagonals, queens on both
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
        // Try to make the move on the board
        Piece capturedPiece = move.getCapturedPiece();
        board.movePiece(move.getStart(), move.getEnd());

        // Check if it puts your own king in check
        boolean inCheckAfterMove = scanCheck(board.getKingSquare(currentPlayer.isWhite()), currentPlayer.isWhite());
        board.undoMove(move.getStart(), move.getEnd(), move.getMovedPiece(), capturedPiece);

        // The move is legal if it doesn't leave the king in check
        return !inCheckAfterMove;
    }
}