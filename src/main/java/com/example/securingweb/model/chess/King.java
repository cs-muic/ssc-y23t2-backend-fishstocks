package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    private boolean hasMoved;
    private boolean canCastleQS;
    private boolean canCastleKS;

    public King(boolean isWhite, Square square) {
        super(isWhite, PieceType.KING, square, isWhite ? 'K' : 'k');
        hasMoved = false;
        canCastleKS = false; // will need to check in game rules
        canCastleQS = false;
    }

    @Override
    public List<Move> getUnfilteredMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        int[][] directions = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };

        for (int[] dir : directions) {
            int newRow = this.square.getRow() + dir[0];
            int newCol = this.square.getCol() + dir[1];
            if (board.isPositionValid(newRow, newCol)) {
                Square targetSquare = board.getSquare(newRow, newCol);
                if (targetSquare != null
                        && (!targetSquare.isOccupied() || targetSquare.getPiece().isWhite() != this.isWhite())) {
                    moves.add(new Move(this.square, targetSquare, this, targetSquare.getPiece(), false, false, false,
                            null));
                }
            }
        }

        // Castling moves
        if (this.canCastleKS) {
            moves.add(new Move(this.square, board.getSquare(this.isWhite() ? 7 : 0, 6), this, null, false, true, false,
                    null));
        }
        if (this.canCastleQS) {
            moves.add(new Move(this.square, board.getSquare(this.isWhite() ? 7 : 0, 2), this, null, false, false, true,
                    null));
        }

        return moves;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public void setCanCastleQS(boolean canCastle) {
        this.canCastleQS = canCastle;
    }

    public void setCanCastleKS(boolean canCastle) {
        this.canCastleKS = canCastle;
    }

}
