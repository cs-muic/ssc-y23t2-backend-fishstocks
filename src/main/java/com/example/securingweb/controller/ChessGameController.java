package com.example.securingweb.controller;

import com.example.securingweb.model.chess.Board;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChessGameController {
    @MessageMapping("/move")
    @SendTo("/topic/moves")
    public Board processMove(Board board) {
        // Process the move, update the game state, etc.
        return board;
    }
}
