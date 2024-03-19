package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class Game implements GameSubject{
    private List<GameObserver> observers = new ArrayList<>();

    private GameState gameState;
    public Board board;
    private Player playerWhite, playerBlack, currentPlayer;

    public Game() {
        board = new Board();
        gameState = new GameState();
        playerWhite = new Player(true);
        playerBlack = new Player(false);
        currentPlayer = playerWhite;

    }
    private void switchPlayer() {
        currentPlayer = (currentPlayer == playerWhite) ? playerBlack : playerWhite;
        gameState.setWhiteTurn(currentPlayer.isWhite());
        notifyObservers();
    }

    @Override
    public void addObserver(GameObserver observer) {
        observers.add(observer);

    }

    @Override
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);

    }

    @Override
    public void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.update(gameState);
        }
    }
}
