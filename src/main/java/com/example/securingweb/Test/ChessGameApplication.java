package com.example.securingweb.Test;

import com.example.securingweb.dto.MoveDTO;
import com.example.securingweb.dto.PieceDTO;
import com.example.securingweb.model.chess.Game;
import com.example.securingweb.model.chess.Player;
import com.example.securingweb.service.GameService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.securingweb.Test", "com.example.securingweb.service"})
public class ChessGameApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChessGameApplication.class, args);
    }

    @Bean
    public CommandLineRunner runGame(GameService gameService) {
        return (args) -> {
            // Initialize a new game, potentially with default players

//            Player player = new Player("root");
//            //Game game = gameService.createGame(player);
//            PieceDTO piece = new PieceDTO("K", 0, 1);
//            // MoveDTO move = new MoveDTO(0, 1, 2, 2, "no");
//
//            // Example moves
//            gameService.makeMove(game.getGameId(), piece, move);
//
//            System.out.println("Final Game State: ");
        };
    }
}