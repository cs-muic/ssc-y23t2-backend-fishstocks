package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class GameHistory {
    private List<Move> gameHistory;
    private List<Move> playerWhiteHistory;
    private List<Move> playerBlackHistory;

    GameHistory() {
        gameHistory = new ArrayList<>();
        playerWhiteHistory = new ArrayList<>();
        playerBlackHistory = new ArrayList<>();
    }

    public List<Move> getGameHistory() {
        return gameHistory;
    }

    private List<Move> getPlayerHistory(Boolean playerIsWhite) {
        return playerIsWhite ? playerWhiteHistory : playerBlackHistory;
    }

    public void recordMove(Move move) {
        gameHistory.add(move);
        getPlayerHistory(move.getMovedPiece().isWhite()).add(move);
    }

    public Move getLastMove() {
        if (gameHistory.isEmpty()) {
            return null;
        }
        return gameHistory.get(gameHistory.size() - 1);
    }

    public String getHisotryString() {
        StringBuilder history = new StringBuilder();
        int moveNumber = 1;
        for (int i = 0; i < gameHistory.size(); i += 2) {
            history.append(moveNumber).append(". ");
            history.append(gameHistory.get(i).getNotation());
            if (i + 1 < gameHistory.size()) {
                history.append(" ").append(gameHistory.get(i + 1).getNotation());
            }
            history.append("\n");
            moveNumber++;
        }
        return history.toString();
    }
}