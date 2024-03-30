package com.example.securingweb.service;

import com.example.securingweb.dto.SimplifiedMove;
import com.example.securingweb.exception.InvalidGameException;
import com.example.securingweb.exception.InvalidParamException;
import com.example.securingweb.exception.NotFoundException;
import com.example.securingweb.model.chess.*;

import com.example.securingweb.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GameService {
    public Game createGame(Player player1){
        Game game = new Game();
        String gameId = UUID.randomUUID().toString();
        game.setGameId(gameId);

        GameHistory gameHistory = new GameHistory();
        gameHistory.setGameID(gameId);
        game.setPlayer1(player1);
        game.setGameState(new GameState(player1));


        Board board = new Board();
        game.setBoard(board);

        game.getPlayer1().setWhite(true);
        game.getPlayer1().setupOccupiedSquares(player1.isWhite(), board);


        game.setGameState(new GameState(game.getPlayer1()));
        game.setGameHistory(gameHistory);
        game.setRules(new ChessRules(board, gameHistory));
        game.setStatus(GameStatus.NEW);
        GameStorage.getInstance().setGame(game);

        return game;
    }

    public Game connectToGame(Player player2, String gameId) throws InvalidParamException, InvalidGameException {
        if(GameStorage.getInstance().getGames().containsKey(gameId)){
            throw new InvalidParamException("Game with provided id doesnt' exist");
        }
        Game game = GameStorage.getInstance().getGames().get(gameId);
        if(game.getPlayer2() != null) {
            throw new InvalidGameException("Game is not valid anymore");
        }
        game.setPlayer2(player2);
        game.setStatus(GameStatus.IN_PROGRESS);
        GameStorage.getInstance().setGame(game);
        return game;

    }

    public Game connectToRandomGame(Player player2) throws NotFoundException{
        Game game = GameStorage.getInstance().getGames().values().stream()
                .filter(it->it.getStatus().equals(GameStatus.NEW))
                .findFirst().orElseThrow(()-> new NotFoundException("Game not found"));
        game.setPlayer2(player2);
        game.setStatus(GameStatus.IN_PROGRESS);
        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game gamePlay(GamePlay gamePlay) throws NotFoundException, InvalidGameException {
        if (!GameStorage.getInstance().getGames().containsKey(gamePlay.getGameId())) {
            throw new NotFoundException("Game not found");
        }

        Game game = GameStorage.getInstance().getGames().get(gamePlay.getGameId());
        if (game.getStatus().equals(GameStatus.FINISHED)) {
            throw new InvalidGameException("Game is already finished");
        }

        Board board = game.getBoard();
        Square start = board.getSquare(gamePlay.getStart().getRow(), gamePlay.getStart().getCol());
        Square end = board.getSquare(gamePlay.getEnd().getRow(), gamePlay.getEnd().getCol());

        Piece movedPiece = start.getPiece();
        List<Move> possibleMoves = game.getRules().getPossibleMoves(movedPiece, game.getBoard());

        Move userMove = possibleMoves.stream()
                .filter(move -> move.getStart().equals(start) && move.getEnd().equals(end))
                .findFirst()
                .orElseThrow(() -> new InvalidGameException("Invalid move"));

        if (!game.makeMove(userMove)) {
            throw new InvalidGameException("Invalid move");
        }

        GameStorage.getInstance().setGame(game);
        return game;
    }

    public List<SimplifiedMove> getPossibleMoves(String gameId, int row, int col) throws NotFoundException {
        Game game = GameStorage.getInstance().getGames().get(gameId);
        if (game == null) {
            throw new NotFoundException("Game not found");
        }

        Square square = game.getBoard().getSquare(row, col);
        Piece piece = square.getPiece();
        List<Move> validMoves = game.getRules().getPossibleMoves(piece, game.getBoard());

        return validMoves.stream().map(move -> new SimplifiedMove(
                move.getStart().getSquareName(),
                move.getEnd().getSquareName(),
                move.isCastle() || move.isEnPassantCapture() || move.isPromotion()
        )).collect(Collectors.toList());
    }


}


