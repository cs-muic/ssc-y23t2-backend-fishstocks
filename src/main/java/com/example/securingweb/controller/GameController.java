package com.example.securingweb.controller;

import com.example.securingweb.dto.*;
import com.example.securingweb.exception.InvalidGameException;
import com.example.securingweb.exception.InvalidParamException;
import com.example.securingweb.exception.NotFoundException;
import com.example.securingweb.model.chess.Game;
import com.example.securingweb.model.chess.Player;
import com.example.securingweb.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @PostMapping("/start")
    public ResponseEntity<GameDTO> start(@RequestBody PlayerDTO playerDTO){
        log.info("start game request: {}");
        return ResponseEntity.ok(gameService.createGameDTO(gameService.createGame(playerDTO)));
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

    @PostMapping("/{gameId}/gameplay")
    public ResponseEntity<GameDTO> gamePlay(@PathVariable String gameId, @RequestBody MoveDTO request) throws NotFoundException, InvalidGameException{
        log.info("gameplay: {}", request);
        Game game = gameService.gamePlay(gameId,request);
        GameDTO gameDTO = gameService.createGameDTO(game);
        simpMessagingTemplate.convertAndSend("/topic/game-progress" + game.getGameId(), game); // use websocket to notify the other player of our move
        return ResponseEntity.ok(gameDTO);
    }

    @PostMapping("/{gameId}/makeMove")
    public ResponseEntity<BoardDTO> makeMove(@PathVariable String gameId, @RequestBody PieceDTO pieceDTO, @RequestBody MoveDTO moveDTO){
    try{
        BoardDTO board = gameService.makeMove(gameId, pieceDTO, moveDTO);
        return ResponseEntity.ok(board);
    } catch (NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    }
    @PostMapping("/{gameId}/getPossibleMoves")
    public ResponseEntity<List<MoveDTO>> validMoves(@PathVariable String gameId, @RequestBody PieceDTO piece) {
        log.info("Valid moves request for game {}: square at row {}, col {}", gameId, piece.getRow(), piece.getCol());
        try {
            List<MoveDTO> validMoves = gameService.getPossibleMoves(gameId, piece.getRow(), piece.getCol());
            return ResponseEntity.ok(validMoves);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}
