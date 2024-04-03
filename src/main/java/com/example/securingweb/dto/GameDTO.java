package com.example.securingweb.dto;

import com.example.securingweb.model.chess.GameHistory;
import com.example.securingweb.model.chess.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class GameDTO {
    private String gameId;
    private BoardDTO board;
    private PlayerDTO player1, player2;
    private GameHistory gameHistory;
    private GameStatus status;

    public GameDTO(){
        gameId = null;
        board = null;
        player1 = null;
        player2 = null;
        gameHistory = null;
        status = null;
    }
}
