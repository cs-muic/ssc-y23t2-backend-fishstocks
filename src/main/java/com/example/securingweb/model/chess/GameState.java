package com.example.securingweb.model.chess;

public class GameState {
    private boolean whiteTurn;
    private boolean check;
    private boolean checkmate;
    private boolean stalemate;
    // other state variables e.g. game history, captured pieces, etc.

    public GameState() {
        this.whiteTurn = true; // White starts by default
        this.check = false;
        this.checkmate = false;
        this.stalemate = false;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }
    public void setWhiteTurn(boolean white) {
        whiteTurn = white;
    }


    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isCheckmate() {
        return checkmate;
    }

    public void setCheckmate(boolean checkmate) {
        this.checkmate = checkmate;
    }


    public void setStalemate() { this.stalemate = true; }

    public boolean isStalemate() {return stalemate;}

}
