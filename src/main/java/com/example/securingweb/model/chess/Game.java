package com.example.securingweb.model.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Manages game state
// Game rule enforcement
// Interact with users input + check for valid moves and observers

public class Game implements GameSubject {
    private List<GameObserver> observers = new ArrayList<>();
    private GameState gameState;
    public Board board;
    private Player playerWhite, playerBlack;
    private GameHistory gameHistory;
    private ChessRules rules;

    public Game() {
        board = new Board();
        playerWhite = new Player(true, board);
        playerBlack = new Player(false, board);
        gameState = new GameState(playerWhite);
        gameHistory = new GameHistory();
        rules = new ChessRules(board, gameHistory);

    }

    public boolean makeMove(Square start, Square end) {
        Piece startPiece = start.getPiece();
        if (startPiece == null || !startPiece.isWhite() == gameState.getCurrentPlayer().isWhite()) {
            System.out.println("Invalid move.");
            return false;
        }

        List<Move> possibleMoves = rules.getPossibleMoves(startPiece, board);
        Move chosenMove = possibleMoves.stream()
                .filter(move -> move.getEnd().equals(end))
                .findFirst()
                .orElse(null);

        if (chosenMove == null || !rules.isMoveLegal(chosenMove, gameState.getCurrentPlayer())) {
            System.out.println("This move is not legal.");
            return false;
        }

        executeMove(chosenMove);
        gameState.getCurrentPlayer().updateSquares(start, end);
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
        }else if (move.isEnPassantCapture()) {
            board.doEnPassant(move.getMovedPiece(), move.getCapturedPiece());
        } else if (move.getPromotionType() != null) {
            // Handle pawn promotion
        } else {
            // Standard move
            Piece capturedPiece = move.getCapturedPiece();
            if (capturedPiece != null){
                gameState.getCurrentPlayer().addCaptured(capturedPiece);
                board.updateMap(false, capturedPiece);
            }
            board.movePiece(move.getStart(), move.getEnd());
        }
    }

    private void switchPlayers() {
        gameState.setCurrentPlayer((gameState.getCurrentPlayer() == playerWhite) ? playerBlack : playerWhite);
    }

    private void updateGameState() {
        if (rules.scanCheck(board.getKingSquare(gameState.getCurrentPlayer().isWhite()),
                gameState.getCurrentPlayer().isWhite())) {
            gameState.setCheck(true);
            if (!gameState.getCurrentPlayer().hasLegalMoves(board, rules)) {
                gameState.setCheckmate();
            }
        } else {
            gameState.setCheck(false);
            if (!gameState.getCurrentPlayer().hasLegalMoves(board, rules)) {
                gameState.setStalemate();
            }
        }
        notifyObservers(new GameStateChangeEvent(gameState));
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
    public void notifyObservers(GameEvent event) {
    }

    /**
     * Play loop for console
     * 
     */
    public void play() {
        Scanner scanner = new Scanner(System.in);

        while (!gameState.isCheckmate() && !gameState.isStalemate()) {

            // Display the board
            board.printBoard();

            // Ask the player to select a square
            System.out.println((gameState.getCurrentPlayer().isWhite() ? "White's" : "Black's") + " move: ");
            System.out.println("Select a square: ");
            String squareInput = scanner.nextLine();
            String[] squareParts = squareInput.split(",");
            int squareRow = Integer.parseInt(squareParts[0]);
            int squareCol = Integer.parseInt(squareParts[1]);
            Square selectedSquare = board.getSquare(squareRow, squareCol);

            if (selectedSquare.getPiece().isWhite() != gameState.getCurrentPlayer().isWhite()) {
                System.out.println("You can only move your own pieces.");
                continue;
            }

            // Display the possible moves for the selected piece
            board.displayMovableSquares(rules, selectedSquare);

            // Ask the player to select a move
            System.out.println("Select a move: ");
            String moveInput = scanner.nextLine();
            String[] moveParts = moveInput.split(",");
            int moveRow = Integer.parseInt(moveParts[0]);
            int moveCol = Integer.parseInt(moveParts[1]);
            Square targetSquare = board.getSquare(moveRow, moveCol);

            // Make the move
            if (!makeMove(selectedSquare, targetSquare)) {
                System.out.println("Invalid move. Please try again.");
            }

            // Update the game state after the move
            switchPlayers();
            updateGameState();

        }
        System.out.println(gameHistory.getGameHistory());
    }

}