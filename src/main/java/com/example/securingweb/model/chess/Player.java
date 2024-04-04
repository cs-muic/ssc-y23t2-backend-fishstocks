package com.example.securingweb.model.chess;

import com.example.securingweb.dto.PlayerDTO;
import com.example.securingweb.storage.GameStorage;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class Player {
    private String login;
    private boolean isWhite; // Player's color
    @JsonManagedReference
    private List<Piece> capturedPieces; // Pieces this player has currently captured
    @JsonManagedReference
    private List<Square> occupiedSquares;
    public Player(boolean isWhite, PlayerDTO playerDTO, String gameID){
        this.login = playerDTO.getLogin();
        capturedPieces = playerDTO.getCapturedPieces();
        this.isWhite = isWhite;
        this.occupiedSquares = new ArrayList<>();
        setupOccupiedSquares(GameStorage.getInstance().getGames().get(gameID).getBoard());
    }

    public Player(String login) {
        this.login = login;
        this.capturedPieces = new ArrayList<>();
        this.occupiedSquares = new ArrayList<>();

    }

    public void setupOccupiedSquares(Board board) {

        int start = isWhite ? 6 : 0;
        int end = isWhite ? 7 : 1;

        for (int row = start; row <= end; row++) {
            for (int col = 0; col < 8; col++) {
                occupiedSquares.add(board.getSquare(row, col));
            }
        }
    }

    public void addCaptured(Piece piece) {
        capturedPieces.add(piece);
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void updateSquares(Square start, Square end) {
        occupiedSquares.remove(start);
        occupiedSquares.add(end);
    }

    /**
     * Scans if player still has any legal moves
     *
     * @return boolean
     */
    public boolean hasLegalMoves(Board board, ChessRules rules) {
        for (Square square : this.occupiedSquares) {
            Piece piece = square.getPiece();
            if (piece == null || piece.isWhite() != this.isWhite)
                continue;

            List<Move> possibleMoves = rules.getPossibleMoves(piece, board);
            for (Move tentativeMove : possibleMoves) {
                if (rules.isMoveLegal(tentativeMove, this)) {
                    return true; // Found a legal move
                }
            }
        }
        return false;
    }



}
