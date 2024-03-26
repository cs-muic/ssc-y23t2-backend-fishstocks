package com.example.securingweb.model.chess;

public class GameState implements GameObserver {
    private Player currentPlayer;
    private boolean check;
    private boolean checkmate;
    private boolean stalemate;
    // other state variables e.g. game history, captured pieces, etc.

    public GameState(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.check = false;
        this.checkmate = false;
        this.stalemate = false;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isCheckmate() {
        return checkmate;
    }

    public boolean isCheck() {
        return check;
    }

    public boolean isStalemate() {
        return stalemate;
    }

    public void setCheckmate() {
        this.checkmate = true;
    }

    public void setStalemate() {
        this.stalemate = true;
    }

    @Override
    public void update(GameEvent event) {

    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
