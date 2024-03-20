package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class Player implements GameObserver{
    private boolean isWhite; // Player's color
    private List<Piece> capturedPieces; // Pieces this player has currently captured
    private List<Square> occupiedSquares;

    public Player(boolean isWhite, Board board) {
        this.isWhite = isWhite;
        this.capturedPieces = new ArrayList<>();
        this.occupiedSquares = new ArrayList<>();
        setupOccupiedSquares(isWhite, board);

    }

    private void setupOccupiedSquares(Boolean isWhite, Board board) {
        int start = isWhite ? 6 : 0;
        int end = isWhite ? 7 : 1;

        for(int row = start; row <= end; row++){
            for(int col = 0; col < 8; col ++){
                occupiedSquares.add(board.getSquare(row,col));
            }
        }
    }

    public void addCaptured(Piece piece){
        capturedPieces.add(piece);
    }

    public boolean isWhite() {
        return isWhite;
    }
    public List<Square> getSquares(){return occupiedSquares;}

    public void updateSquares(Square start, Square end) {
        occupiedSquares.remove(start);
        occupiedSquares.add(end);
    }

    @Override
    public void update(GameState state) {
        // Check if it's this player's turn
        if (state.isWhiteTurn() == this.isWhite) {
            System.out.println("It's your turn."); // change this to graphical version later
        }

        // React to check or checkmate status
        if (state.isCheck()) {
            System.out.println("You are in check.");
        }
        if (state.isCheckmate()) {
            System.out.println("Checkmate.");
        }
    }

    /**
     * Scans if player still has any legal moves
     * Stalemate - king has no moves but isn't in check.
     * Checkmate - king has no moves and is in check
     *
     * @return boolean
     */
    public boolean hasLegalMoves(Board board, ChessRules rules) {
        for (Square square : this.occupiedSquares) {
            Piece piece = square.getPiece();
            if (piece == null || piece.isWhite() != this.isWhite) continue;

            List<Square> possibleMoves = piece.getPossibleMoves(board);
            if(possibleMoves != null) {
                for (Square target : possibleMoves) {
                    Move tentativeMove = new Move(square, target, piece, target.getPiece(), false, false); // Example; adjust as needed
                    if (rules.isMoveLegal(tentativeMove, this)) {
                        return true; // Found a legal move
                    }
                }
            }
        }
        return false;
    }
}
