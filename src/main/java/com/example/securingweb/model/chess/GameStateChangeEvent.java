package com.example.securingweb.model.chess;

// GameStateChangeEvent
public class GameStateChangeEvent extends GameEvent {
    private final GameState state;

    public GameStateChangeEvent(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    @Override
    public EventType getEventType() {
        return null;
    }
}
