package com.example.securingweb.model.chess;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameState{
    private boolean check;
    private boolean checkmate;
    private boolean stalemate;
    // other state variables e.g. game history, captured pieces, etc.

    public GameState() {
        this.check = false;
        this.checkmate = false;
        this.stalemate = false;
    }


}
