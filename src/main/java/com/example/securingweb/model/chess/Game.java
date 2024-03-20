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

    public Game() {
        board = new Board();
        playerWhite = new Player(true, board);
        playerBlack = new Player(false, board);
        currentPlayer = playerWhite;
        gameState = new GameState();

    }

    // Moves a piece from one position to another, if the move is valid.
    // NOTE: This will need to be mouse click later
    public boolean makeMove(Square start, Square end) {
        Piece startPiece = start.getPiece();

        // Check if there is a piece at the starting square
        if (startPiece == null) {
            System.out.println("There is no piece at the starting square.");
            return false;
        }

        // Check if the piece belongs to the current player
        if (startPiece.isWhite() != currentPlayer.isWhite()) {
            System.out.println("You can only move your own pieces.");
            return false;
        }

        // Check if the move is valid
        if (!startPiece.canMoveTo(end, board)) {
            System.out.println("Invalid move.");
            return false;
        }
        // Check if the move puts the king in check
        if (!canMoveWithoutCheck(start, end)) {
            System.out.println("This move would put your king in check.");
            return false;
        } else {
            // Can legally make the move
            Piece targetPiece = board.movePiece(start, end); // returns us a captured piece if any
            handleCapturedPiece(targetPiece);
            updateMoveHistory(start, end);
        }
        currentPlayer.updateSquares(start, end);

        return true;
    }

    private void handleCapturedPiece(Piece targetPiece) {
        if (targetPiece != null) {
            currentPlayer.addCaptured(targetPiece);
        }
    }

    private void updateMoveHistory(Square start, Square end) {
        currentPlayer.addMove(start + " " + end);
    }

    private void switchPlayers() {
        currentPlayer = (currentPlayer == playerWhite) ? playerBlack : playerWhite;
        gameState.setWhiteTurn(currentPlayer.isWhite());
        notifyObservers();
    }

    private void updateGameState() {
        if (scanCheck(board.getKingSquare(currentPlayer.isWhite()), currentPlayer.isWhite())) {
            gameState.setCheck(true);
            if (!hasLegalMoves()) {
                gameState.setCheckmate();
            }
        }
        else {
            gameState.setCheck(false);
            if (!hasLegalMoves()) {
                gameState.setStalemate();
            }
        }
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

    private boolean scanCheck(Square kingLocation, boolean kingIsWhite) {
        int[][] directions = {
                { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 },
                { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 }
        };

        for (int[] dir : directions) {
            if (scanForThreat(kingLocation.getRow(), kingLocation.getCol(), dir[0], dir[1], kingIsWhite)) {
                return true; // Threat found, king is in check
            }
        }
        // Check for knight threats
        return scanForKnightThreats(kingLocation.getRow(), kingLocation.getCol(), kingIsWhite);
    }

    // Scan in a direction from the king's position for a threat
    private boolean scanForThreat(int row, int col, int rowInc, int colInc, boolean kingIsWhite) {
        int currentRow = row + rowInc;
        int currentCol = col + colInc;
        while (currentRow >= 0 && currentRow < 8 && currentCol >= 0 && currentCol < 8) {
            Piece piece = board.getSquare(currentRow, currentCol).getPiece();
            if (piece != null) {
                if (piece.isWhite() != kingIsWhite) { // Encounter an enemy piece
                    // Check if the piece is a threat
                    return isThreateningPiece(piece, rowInc, colInc, kingIsWhite);
                } else {
                    return false; // Blocked by own piece, no threat from this direction
                }
            }
            currentRow += rowInc;
            currentCol += colInc;
        }
        return false; // No threats found in this direction
    }
    // Determine if a piece can attack the king based on direction and piece type
    private boolean isThreateningPiece(Piece piece, int rowInc, int colInc, boolean kingIsWhite) {
        // Simplify threat logic based on piece type and direction
        // Example: Rooks on straight lines, bishops on diagonals, queens on both
        PieceType type = piece.getType();
        boolean straight = rowInc == 0 || colInc == 0;
        boolean diagonal = Math.abs(rowInc) == Math.abs(colInc);

        if ((type == PieceType.ROOK && straight) ||
                (type == PieceType.BISHOP && diagonal) ||
                (type == PieceType.QUEEN && (straight || diagonal))) {
            return true;
        }

        // Add special case for pawns
        if (type == PieceType.PAWN && diagonal && Math.abs(rowInc) == 1 &&
                ((kingIsWhite && rowInc < 0) || (!kingIsWhite && rowInc > 0))) {
            return true; // Pawn can attack diagonally
        }

        return false;
    }

    // Check for knight threats specifically
    private boolean scanForKnightThreats(int kingRow, int kingCol, boolean kingIsWhite) {
        int[][] knightMoves = {
                { -2, -1 }, { -2, 1 }, { -1, -2 }, { -1, 2 },
                { 1, -2 }, { 1, 2 }, { 2, -1 }, { 2, 1 }
        };

        for (int[] move : knightMoves) {
            int newRow = kingRow + move[0];
            int newCol = kingCol + move[1];
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                Piece piece = board.getSquare(newRow, newCol).getPiece();
                if (piece != null && piece.isWhite() != kingIsWhite && piece.getType() == PieceType.KNIGHT) {
                    return true;
                }
            }
        }
        return false;
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

            // Display the possible moves for the selected piece
            board.displayWithMovableSquares(selectedSquare);

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
            System.out.println(scanCheck(board.getKingSquare(currentPlayer.isWhite()), currentPlayer.isWhite()));

            System.out.println(gameState.isCheck());
            System.out.println(gameState.isCheckmate());
            System.out.println(gameState.isStalemate());
        }
    }

    /**
     * Scans if player still has any legal moves
     * Stalemate - king has no moves but isn't in check.
     * Checkmate - king has no moves and is in check
     * 
     * @return boolean
     */
    private boolean hasLegalMoves() {
        // Must already know that gameState != inCheck
        // no legal moves for current player
        for (Square square : currentPlayer.getSquares()) {
            List<Square> possibleMoves = square.getPiece().getPossibleMoves(board);
            if (possibleMoves != null){
                for (Square possibleMove : possibleMoves) {
                    if (canMoveWithoutCheck(square, possibleMove)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean canMoveWithoutCheck(Square start, Square end) {
        Piece originalPiece = end.getPiece();
        board.movePiece(start, end);
        boolean inCheck = scanCheck(board.getKingSquare(currentPlayer.isWhite()), currentPlayer.isWhite());
        board.movePiece(end, start); // Revert the move
        end.setPiece(originalPiece); // Put back the original piece
        return !inCheck;
    }

}