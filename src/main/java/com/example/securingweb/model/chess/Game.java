package com.example.securingweb.model.chess;

import com.example.securingweb.exception.InvalidMoveException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// Manages game state
// Game rule enforcement
// Interact with users input + check for valid moves and observers

@Setter
@Getter
public class Game {
    private String gameId;
    private List<GameObserver> observers = new ArrayList<>();
    private GameState gameState;
    private Board board;
    private Player player1, player2;
    private GameHistory gameHistory;
    private ChessRules rules;
    private GameStatus status;
    private boolean currentPlayerIsWhite;

    public boolean makeMove(Move move) {
        Square start = move.getStart();
        Square end = move.getEnd();
        Piece startPiece = start.getPiece();
        if (startPiece == null || !(startPiece.isWhite() == currentPlayerIsWhite)) {
            assert startPiece != null;
            System.out.println("Wrong player currentplayer: "+ player1.isWhite()+ startPiece.isWhite);
            return false;
        }

        List<Move> possibleMoves = rules.getPossibleMoves(startPiece, board);
        Move chosenMove = possibleMoves.stream()
                .filter(m -> m.getEnd().equals(end))
                .findFirst()
                .orElse(null);

        if (chosenMove == null || !rules.isMoveLegal(chosenMove, getCurrentPlayer())) {
            return false;
        }

        executeMove(chosenMove);
        System.out.println("In game class: " + getCurrentPlayer());
        getCurrentPlayer().updateSquares(start, end);
        gameHistory.recordMove(chosenMove);

        return true;
    }

    /**
     * TODO: implement these if else
     *
     * @param move
     */
    private void executeMove(Move move) {

        // Handle special moves -> castling, en passant, and promotion
        if (move.isCastle()) {
            board.doCastle(move.getMovedPiece(), move.getCapturedPiece());
        } else if (move.isEnPassantCapture()) {
            board.doEnPassant(move.getMovedPiece(), move.getCapturedPiece());
        } else if (move.isPromotion()) {
            // Handle pawn promotion
            board.promotePawn(move);
        } else {
            // Standard move
            Piece capturedPiece = move.getCapturedPiece();
            if (capturedPiece != null) {
                getCurrentPlayer().addCaptured(capturedPiece);

            }
            board.movePiece(move.getStart(), move.getEnd());
        }
        switchPlayers();

    }

    private void switchPlayers() {
        currentPlayerIsWhite = (!currentPlayerIsWhite);
    }

    public void updateGameState() {
        if (rules.scanCheck(board.getKingSquare(currentPlayerIsWhite),
                currentPlayerIsWhite)) {
            gameState.setCheck(true);
            if (!((currentPlayerIsWhite?player1:player2).hasLegalMoves(board, rules))) {
                gameState.setCheckmate(true);
            }
        } else {
            gameState.setCheck(false);
            if (!((currentPlayerIsWhite?player1:player2).hasLegalMoves(board, rules))) {
                gameState.setStalemate(true);
            }
        }
    }
    public Player getCurrentPlayer(){
        return (currentPlayerIsWhite? player1: player2);
    }

}