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

    public void updateMoveHistory(Square start, Square end, Player currentPlayer) {
        char symbol = start.getPiece().getSymbol();
        String hist = end.getSquareName();
        if (currentPlayer.isWhite()) {
            gameHistory.append(moveNumber).append(".");
            incrementMoveNumber();
        }

        if (symbol == 'p' || symbol == 'P') {
            getPlayerHistory(currentPlayer).append(hist);
            gameHistory.append(hist);
        } else {
            getPlayerHistory(currentPlayer).append(symbol + hist);
            gameHistory.append(symbol + hist);
        }
        getPlayerHistory(currentPlayer).append(" ");
        gameHistory.append(" ");
    }

}
