package com.example.securingweb.model.chess;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GamePlay {
    private String gameId;
    private Square start;
    private Square end;

    public GamePlay(String gameId, Square start, Square end) {
        this.gameId = gameId;
        this.start = start;
        this.end = end;
    }

    public String getGameId() {
        return gameId;
    }

    public Square getStart() {
        return start;
    }

    public Square getEnd() {
        return end;
    }
}