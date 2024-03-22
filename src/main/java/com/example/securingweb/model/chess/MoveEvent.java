package com.example.securingweb.model.chess;

public class MoveEvent extends GameEvent {
    private final Move move;

    public MoveEvent(Move move) {
        this.move = move;
    }

    public Move getMove() {
        return move;
    }

    @Override
    public EventType getEventType() {
        return null;
    }
}
