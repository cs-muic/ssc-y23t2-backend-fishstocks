package com.example.securingweb.controller;
import com.example.securingweb.dto.ConnectRequest;
import com.example.securingweb.dto.SimplifiedMove;
import com.example.securingweb.exception.InvalidGameException;
import com.example.securingweb.exception.InvalidParamException;
import com.example.securingweb.exception.NotFoundException;
import com.example.securingweb.model.chess.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.example.securingweb.service.GameService;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;


@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @PostMapping("/start")
    public ResponseEntity<Game> start(Player player){
        log.info("start game request: {}");
        return ResponseEntity.ok(gameService.createGame(player));
    }

    @PostMapping("/connect")
    public ResponseEntity<Game> connect(@RequestBody ConnectRequest request) throws InvalidGameException, InvalidParamException {
        log.info("connect request: {}", request);
        return ResponseEntity.ok(gameService.connectToGame(request.getPlayer(), request.getGameId()));
    }

    @PostMapping("/connect/random")
    public ResponseEntity<Game> connectRandom(@RequestBody Player player) throws NotFoundException {
        log.info("connect random {}", player);
        return ResponseEntity.ok(gameService.connectToRandomGame(player));
    }

    @PostMapping("/gameplay")
    public ResponseEntity<Game> gamePlay(@RequestBody GamePlay request) throws NotFoundException, InvalidGameException{
        log.info("gameplay: {}", request);
        Game game = gameService.gamePlay(request);
        simpMessagingTemplate.convertAndSend("/topic/game-progress" + game.getGameId(), game); // use websocket to notify the other player of our move
        return ResponseEntity.ok(game);
    }
    @PostMapping("/{gameId}/getPossibleMoves")
    public ResponseEntity<List<SimplifiedMove>> validMoves(@PathVariable String gameId, @RequestParam("row") int row, @RequestParam("col") int col) {
        log.info("Valid moves request for game {}: square at row {}, col {}", gameId, row, col);
        try {
            List<SimplifiedMove> validMoves = gameService.getPossibleMoves(gameId, row, col);
            return ResponseEntity.ok(validMoves);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}
