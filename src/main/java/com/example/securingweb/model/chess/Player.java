package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class Player implements GameObserver{
    private boolean isWhite; // Player's color
    private StringBuilder moveHistory; // Stores the moves in algebraic notation, e.g., "e2 e4"
    private List<Piece> capturedPieces; // Pieces this player has currently captured
    private List<Square> occupiedSquares;

    public Player(boolean isWhite) {
        this.isWhite = isWhite;
        this.moveHistory = new StringBuilder();
        this.capturedPieces = new ArrayList<>();
        this.occupiedSquares = new ArrayList<>();
    }

    public void addMove(String move) {
        moveHistory.append(move);
    }
    public void addCaptured(Piece piece){
        capturedPieces.add(piece);
    }

    public String getMoveHistory() {return moveHistory.toString();}

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
     * TODO undo function?
     */
}
