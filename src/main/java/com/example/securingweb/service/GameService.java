package com.example.securingweb.service;

import com.example.securingweb.dto.*;
import com.example.securingweb.exception.InvalidGameException;
import com.example.securingweb.exception.InvalidParamException;
import com.example.securingweb.exception.NotFoundException;
import com.example.securingweb.model.chess.*;
import com.example.securingweb.repo.UserRepository;
import com.example.securingweb.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GameService {

    private UserRepository userRepository;
    private Game createGame(Player player1){
        Game game = new Game();
        String gameId = UUID.randomUUID().toString();
        game.setGameId(gameId);
        GameStorage.getInstance().setGame(game);



        GameHistory gameHistory = new GameHistory();
        game.setPlayer1(player1);
        game.setPlayer2(new Player("kk2")); //major fix
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

    public GameStatus findState(String gameId){
        Map<String, Game> gamesSet = GameStorage.getInstance().getGames();

        if (gamesSet.containsKey(gameId)){
            Game game = gamesSet.get(gameId);
            GameState gameState = game.getGameState();
            if(game.getPlayer2() == null){
                return GameStatus.NEW;
            }
            else if(!gameState.isCheckmate() || gameState.isStalemate()){
                return GameStatus.IN_PROGRESS;
            }
            else{
                return GameStatus.FINISHED;
            }

        }
        else{
            throw new RuntimeException("This gameId does not exist");
        }
    }

    public PlayerDTO toplayerDTO(Player player){
        List<PieceDTO> l = player.getCapturedPieces().stream().map(
                (piece) -> new PieceDTO(piece.getType().toString(), piece.getRow(), piece.getCol())).toList();
        return new PlayerDTO(player.getLogin(), l);
    }

    public GameDTO createGameDTO(Player player1){

        Game game = createGame(player1);

        GameDTO gameDTO = new GameDTO();
        PlayerDTO playerDTO = toplayerDTO(player1);

        gameDTO.setGameId(game.getGameId());
        gameDTO.setGameHistory(game.getGameHistory());
        gameDTO.setBoard(makeBoardDTO(game.getBoard()));
        gameDTO.setPlayer1(playerDTO);

        return gameDTO;
    }


    public BoardDTO makeBoardDTO(Board board) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setWhiteKing(new KingDTO(true, board.getKingSquare(true).getRow(), board.getKingSquare(true).getCol()));
        boardDTO.setBlackKing(new KingDTO(false, board.getKingSquare(false).getRow(), board.getKingSquare(false).getCol()));
        PieceDTO[][] b = new PieceDTO[8][8];
        for(int row = 0; row < 8; row ++){
            for (int col = 0; col < 8; col++){
                Piece piece = board.getSquare(row, col).getPiece();
                if (piece != null) {
                    b[row][col] = new PieceDTO(makePieceName(piece), row, col);
                }else{
                    b[row][col] = new PieceDTO("", row, col);
                }
            }
        }
        boardDTO.setBoard(b);
        return boardDTO;
    }

    private String makePieceName(Piece piece) {
        if (piece == null) return "";
        switch(piece.getSymbol()) {
            case 'b':
                return "bishop-black";
            case 'B':
                return "bishop-white";
            case 'k':
                return "king-black";
            case 'K':
                return "king-white";
            case 'q':
                return "queen-black";
            case 'Q':
                return "queen-white";
            case 'n':
                return "knight-black";
            case 'N':
                return "knight-white";
            case 'r':
                return "rook-black";
            case 'R':
                return "rook-white";
            case 'p':
                return "pawn-black";
            case 'P':
                return "pawn-white";
            default:
                return "unknown";
        }
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

    public Game gamePlay(String gameId, MoveDTO moveDTO) throws NotFoundException, InvalidGameException {
        Game game;
        if (!GameStorage.getInstance().getGames().containsKey(gameId)) {
//            game = createGame(new Player(playerDTO.getLogin()));
            game = createGame(new Player(moveDTO.getName()));
//            throw new NotFoundException("Game not found");
        }else{
            game = GameStorage.getInstance().getGames().get(gameId);
        }

        if (game.getStatus().equals(GameStatus.FINISHED)) {
            throw new InvalidGameException("Game is already finished");
        }

        Board board = game.getBoard();
        Square start = board.getSquare(moveDTO.getStartRow(), moveDTO.getStartCol());
        Square end = board.getSquare(moveDTO.getEndRow(), moveDTO.getEndCol());

        Piece movedPiece = start.getPiece();
        List<Move> possibleMoves = game.getRules().getPossibleMoves(movedPiece, game.getBoard());

        Move userMove = possibleMoves.stream()
                .filter(move -> move.getStart().equals(start) && move.getEnd().equals(end))
                .findFirst()
                .orElseThrow(() -> new InvalidGameException("Invalid move"));
        if (!game.makeMove(userMove)) {
            throw new InvalidGameException("Invalid move");
        }
        game.updateGameState();
        GameStorage.getInstance().setGame(game);
        return game;
    }

    public List<MoveDTO> getPossibleMoves(String gameId, int row, int col) throws NotFoundException {
        Game game = GameStorage.getInstance().getGames().get(gameId);
        if (game == null) {
            throw new NotFoundException("Game not found");
        }

        Square square = game.getBoard().getSquare(row, col);
        Piece piece = square.getPiece();
        List<Move> validMoves = game.getRules().getPossibleMoves(piece, game.getBoard());

        return validMoves.stream().map(move -> new MoveDTO(
                move.getStart().getRow(),
                move.getStart().getCol(),
                move.getEnd().getRow(),
                move.getEnd().getCol(),
                move.moveTypetoString(),game.getPlayer1().toString()
        )).collect(Collectors.toList());
    }

    public BoardDTO makeMove(String gameId, PieceDTO pieceDTO, MoveDTO moveDTO) throws NotFoundException {
        Game game = GameStorage.getInstance().getGames().get(gameId);
        if (game == null) {
            throw new NotFoundException("Game not found");
        }
        int s_row = moveDTO.getStartRow();
        int s_col = moveDTO.getStartCol();
        int e_row = moveDTO.getEndRow();
        int e_col = moveDTO.getEndCol();
        Square start = game.getBoard().getSquare(s_row,s_col);
        Square end = game.getBoard().getSquare(e_row,e_col);
        Piece movePiece = game.getBoard().getSquare(s_row,s_col).getPiece();
        Piece capturedPiece = game.getBoard().getSquare(e_row,e_col).getPiece();

        if (moveDTO.getSpecialMove().equals( "CASTLE")){
            game.getBoard().doCastle(movePiece,capturedPiece);
        } else if (moveDTO.getSpecialMove().equals("ENPASSANT")) {
            game.getBoard().doEnPassant(movePiece,capturedPiece);
        }else if (moveDTO.getSpecialMove().equals("PROMOTION")) {
            promotePawn(game, pieceDTO, moveDTO);
        }else{
            if (capturedPiece != null){
                game.getBoard().handleCapturedPiece(game.getGameState(),capturedPiece);
            }
            game.getBoard().movePiece(start,end);

        }
        game.switchPlayers();
        return makeBoardDTO(game.getBoard());
    }



    /*
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
                board.handleCapturedPiece(gameState, capturedPiece);

            }
            board.movePiece(move.getStart(), move.getEnd());
        }
        switchPlayers();

    }
     */


        public void promotePawn(Game game, PieceDTO pieceDTO, MoveDTO moveDTO) {
        // Get the location where the pawn should be promoted

        int s_row = moveDTO.getStartRow();
        int s_col = moveDTO.getStartCol();
        int e_row = moveDTO.getEndRow();
        int e_col = moveDTO.getEndCol();
        Square start = game.getBoard().getSquare(s_row,s_col);
        Square end = game.getBoard().getSquare(e_row,e_col);
        Piece movePiece = game.getBoard().getSquare(s_row,s_col).getPiece();
        Piece capturedPiece = game.getBoard().getSquare(e_row,e_col).getPiece();
        Square location = end;

        // Store the pawn's original location
        Square originalLocation = game.getBoard().getSquare(movePiece.getRow(), movePiece.getCol());


        if(capturedPiece != null){
            // need this for when the pawn captures AND promotes at the same time
            game.getBoard().getSquare(capturedPiece.getRow(), capturedPiece.getCol()).emptySquare(); // empty the square with wtv piece we just captured
        }

        // Empty the square where the pawn was
        originalLocation.emptySquare();

        // Empty the square where the pawn should be promoted
        location.emptySquare();

        // Create promoted piece
        Piece promotedPiece = PieceFactory.createPromotedPiece(game.getBoard(), movePiece.isWhite(), location.getRow(), location.getCol());

        // Update the square
        location.setPiece(promotedPiece);
    }



}


