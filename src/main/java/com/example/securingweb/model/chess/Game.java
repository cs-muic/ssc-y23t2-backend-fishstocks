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
    private Player playerWhite, playerBlack, currentPlayer;
    private GameHistory gameHistory;
    private ChessRules rules;

    public Game() {
        board = new Board();
        playerWhite = new Player(true, board);
        playerBlack = new Player(false, board);
        currentPlayer = playerWhite;
        gameState = new GameState();
        gameHistory = new GameHistory();
        rules = new ChessRules(board);

    }

    // Moves a piece from one position to another, if the move is valid.
    // NOTE: This will need to be mouse click later
    public boolean makeMove(Square start, Square end) {
        Piece startPiece = start.getPiece();
        // || startPiece.isWhite() != currentPlayer.isWhite() ||
        if (startPiece == null || !startPiece.canMoveTo(end, board)) {
            System.out.println("Invalid move.");
            return false;
        }
        Piece capturedPiece = end.getPiece(); // This might be null if no piece is captured
        Move move = new Move(start, end, startPiece, capturedPiece, false, false);
        if (!rules.isMoveLegal(move, currentPlayer)) {
            System.out.println("This move would put your king in check.");
            return false;
        }
        // Execute move
        board.movePiece(start, end);
        currentPlayer.updateSquares(start, end); // Make sure this method now properly handles the move
        gameHistory.recordMove(move); // Assuming GameHistory now has a method to record moves
        return true;
    }

    private void handleCapturedPiece(Piece targetPiece) {
        if (targetPiece != null) {
            currentPlayer.addCaptured(targetPiece);
        }
    }

    private void switchPlayers() {
        currentPlayer = (currentPlayer == playerWhite) ? playerBlack : playerWhite;
        gameState.setWhiteTurn(currentPlayer.isWhite());
        notifyObservers();
    }

    private void updateGameState() {
        if (rules.scanCheck(board.getKingSquare(currentPlayer.isWhite()), currentPlayer.isWhite())) {
            gameState.setCheck(true);
            if (!currentPlayer.hasLegalMoves(board, rules)) {
                gameState.setCheckmate();
            }
        }
        else {
            gameState.setCheck(false);
            if (!currentPlayer.hasLegalMoves(board, rules)) {
                gameState.setStalemate();
            }
        }
        notifyObservers();
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
    public void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.update(gameState);
        }
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
            System.out.println((currentPlayer.isWhite() ? "White's" : "Black's") + " move: ");
            System.out.println("Select a square: ");
            String squareInput = scanner.nextLine();
            String[] squareParts = squareInput.split(",");
            int squareRow = Integer.parseInt(squareParts[0]);
            int squareCol = Integer.parseInt(squareParts[1]);
            Square selectedSquare = board.getSquare(squareRow, squareCol);

            if(selectedSquare.getPiece().isWhite() != currentPlayer.isWhite()){
                System.out.println("You can only move your own pieces.");
                continue;
            }

            // Display the possible moves for the selected piece
            board.displayMovableSquares(selectedSquare);

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
                continue;
            }
            // Switch players
            switchPlayers();
            updateGameState();

//            System.out.println(gameState.isCheck());
//            System.out.println(gameState.isCheckmate());
//            System.out.println(gameState.isStalemate());
        }
        System.out.println(gameHistory.getGameHistory());

    }

    private boolean canMoveWithoutCheck(Square start, Square end) {
        Piece originalPiece = end.getPiece();
        board.movePiece(start, end);
        boolean inCheck = rules.scanCheck(board.getKingSquare(currentPlayer.isWhite()), currentPlayer.isWhite());
        board.movePiece(end, start); // Revert the move
        end.setPiece(originalPiece); // Put back the original piece
        return !inCheck;
    }


}