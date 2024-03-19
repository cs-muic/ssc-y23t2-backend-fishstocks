package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;

public class Game {
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
}
