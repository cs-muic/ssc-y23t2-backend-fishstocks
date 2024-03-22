package com.example.securingweb.model.chess;

public class GameHistory {
    private StringBuilder gameHistory;
    private StringBuilder playerWhiteHistory;
    private StringBuilder playerBlackHistory;
    private int moveNumber;

    GameHistory() {
        gameHistory = new StringBuilder();
        playerWhiteHistory = new StringBuilder();
        playerBlackHistory = new StringBuilder();
        moveNumber = 1;
    }

    public String getGameHistory() {
        return gameHistory.toString();
    }

    public String getPlayerWhiteHistory() {
        return playerWhiteHistory.toString();
    }

    public String getPlayerBlackHistory() {
        return playerBlackHistory.toString();
    }

    private StringBuilder getPlayerHistory(Player currentPlayer) {
        return currentPlayer.isWhite() == true ? playerWhiteHistory : playerBlackHistory;
    }

    private void incrementMoveNumber() {
        moveNumber++;
    }

    public void recordMove(Move move) {
        gameHistory.append(move.getNotation());
    }

}
