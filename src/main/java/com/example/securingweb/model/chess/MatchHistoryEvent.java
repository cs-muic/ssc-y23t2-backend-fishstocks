package com.example.securingweb.model.chess;

// MatchHistoryEvent
public class MatchHistoryEvent extends GameEvent {
    private final String historyDescription;

    public MatchHistoryEvent(String historyDescription) {
        this.historyDescription = historyDescription;
    }

    public String getHistoryDescription() {
        return historyDescription;
    }

    @Override
    public EventType getEventType() {
        return null;
    }
}
