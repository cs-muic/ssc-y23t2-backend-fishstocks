package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

@Data
public class Player implements GameObserver {
    private String login;
    private boolean isWhite; // Player's color
    @JsonManagedReference
    private List<Piece> capturedPieces; // Pieces this player has currently captured
    @JsonManagedReference
    private List<Square> occupiedSquares;

    public Player(String login) {
        this.login = login;
        this.capturedPieces = new ArrayList<>();
        this.occupiedSquares = new ArrayList<>();
    }

    public void setupOccupiedSquares(boolean isWhite, Board board) {

        int start = isWhite ? 6 : 0;
        int end = isWhite ? 7 : 1;

        for (int row = start; row <= end; row++) {
            for (int col = 0; col < 8; col++) {
                occupiedSquares.add(board.getSquare(row, col));
            }
        }
    }

    public void addCaptured(Piece piece) {
        capturedPieces.add(piece);
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void updateSquares(Square start, Square end) {
        occupiedSquares.remove(start);
        occupiedSquares.add(end);
    }

    /**
     * Scans if player still has any legal moves
     *
     * @return boolean
     */
    public boolean hasLegalMoves(Board board, ChessRules rules) {
        for (Square square : this.occupiedSquares) {
            Piece piece = square.getPiece();
            if (piece == null || piece.isWhite() != this.isWhite)
                continue;

            List<Move> possibleMoves = rules.getPossibleMoves(piece, board);
            for (Move tentativeMove : possibleMoves) {
                if (rules.isMoveLegal(tentativeMove, this)) {
                    return true; // Found a legal move
                }
            }
        }
        return false;
    }

    @Override
    public void update(GameEvent event) {
        if (event instanceof GameStateChangeEvent) {
            GameStateChangeEvent gameStateChangeEvent = (GameStateChangeEvent) event;
            GameState state = gameStateChangeEvent.getState();

            // Check if it's this player's turn
            if (state.getCurrentPlayer().isWhite == this.isWhite) {
                System.out.println("It's your turn.");
            }

            // React to check or checkmate status
            if (state.isCheck()) {
                System.out.println("You are in check.");
            }
            if (state.isCheckmate()) {
                System.out.println("Checkmate.");
            }
        }
    }
}
