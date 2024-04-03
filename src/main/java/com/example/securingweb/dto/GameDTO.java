package com.example.securingweb.dto;

import com.example.securingweb.model.chess.GameHistory;
import com.example.securingweb.model.chess.GameStatus;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GameDTO {
    private String gameId;
    private BoardDTO board;
    private PlayerDTO player1, player2;
    private GameHistory gameHistory;
    private GameStatus status;
}
