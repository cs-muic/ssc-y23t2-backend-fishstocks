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

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GameService {

    private UserRepository userRepository;
    public Game createGame(PlayerDTO player1DTO){
        Game game = new Game();
        String gameId = UUID.randomUUID().toString();
        game.setGameId(gameId);
        game.setBoard(new Board());
        GameStorage.getInstance().setGame(game);
        boolean rngPlayerColor = Math.random() % 2 == 1;
        game.setPlayer1(new Player(rngPlayerColor, player1DTO, gameId));

//        game.setPlayer2(new Player("kk2")); //major fix
        game.setGameState(new GameState());
        game.setGameHistory(new GameHistory());
        game.setRules(new ChessRules(game.getBoard(), game.getGameHistory()));
        game.setStatus(GameStatus.NEW);
        game.setCurrentPlayerIsWhite(true);
//        game.setPlayer2(new Player(false, new PlayerDTO("p2login", false,  new ArrayList<>()), gameId));
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
                return GameStatus.FINISHED;
            }
            else{
                return GameStatus.IN_PROGRESS;
            }

        }
        else{
            throw new RuntimeException("This gameId does not exist");
        }
    }

//    public PlayerDTO toplayerDTO(Player player){
//        List<PieceDTO> l = player.getCapturedPieces().stream().map(
//                (piece) -> new PieceDTO(piece.getType().toString(), piece.getRow(), piece.getCol())).toList();
//        return new PlayerDTO(player.getLogin(), l);
//    }

    public GameDTO createGameDTO(Game game){
        GameDTO gameDTO = new GameDTO();
        gameDTO.setGameId(game.getGameId());
        gameDTO.setGameHistory(game.getGameHistory());
        gameDTO.setBoard(makeBoardDTO(game.getBoard()));

        gameDTO.setPlayer1(new PlayerDTO(game.getPlayer1().getLogin(), game.getPlayer1().isWhite(), game.getPlayer1().getCapturedPieces()));
        if(game.getPlayer2()!=null){
            gameDTO.setPlayer2(new PlayerDTO(game.getPlayer2().getLogin(), game.getPlayer2().isWhite(), game.getPlayer2().getCapturedPieces()));
        }
        gameDTO.setStatus(game.getStatus());

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


    public Game connectToGame(PlayerDTO player2DTO, String gameId) throws InvalidParamException, InvalidGameException {
        if(!GameStorage.getInstance().getGames().containsKey(gameId)){
            throw new InvalidParamException("Game with provided id doesn't exist");
        }
        Game game = GameStorage.getInstance().getGames().get(gameId);
        if(game.getPlayer2() != null) {
            throw new InvalidGameException("Game is not valid anymore");
        }
        game.setPlayer2(new Player (false, player2DTO, gameId));
        game.setStatus(GameStatus.IN_PROGRESS);
        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game connectToRandomGame(PlayerDTO player2DTO) {
        Optional<Game> optionalGame = GameStorage.getInstance().getGames().values().stream()
                .filter(it -> it.getStatus().equals(GameStatus.NEW))
                .findFirst();

        Game game;
        if (optionalGame.isPresent()) {
            game = optionalGame.get();
            game.setPlayer2(new Player(!game.getPlayer1().isWhite(), player2DTO, game.getGameId()));
            game.setStatus(GameStatus.IN_PROGRESS);
            GameStorage.getInstance().setGame(game);
        } else {
            game = createGame(player2DTO);
        }

        return game;
    }

    public Game gamePlay(String gameId, MoveDTO moveDTO) throws NotFoundException, InvalidGameException {
        Game game;
        if (!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new NotFoundException("Game not found");
        }else{
            game = GameStorage.getInstance().getGames().get(gameId);
        }

        if (game.getStatus().equals(GameStatus.FINISHED)) {
            throw new InvalidGameException("Game is already finished");
        }

        Board board = game.getBoard();
        Square start = board.getSquare(moveDTO.getStartRow(), moveDTO.getStartCol());
        Square end = board.getSquare(moveDTO.getEndRow(), moveDTO.getEndCol());
        List<Move> possibleMoves = game.getRules().getPossibleMoves(start.getPiece(), board);
        Move chosenMove = possibleMoves.stream()
                .filter(m -> m.getEnd().equals(end))
                .findFirst()
                .orElse(null);

        assert chosenMove != null;
        if (!game.makeMove(chosenMove)){
            throw new InvalidGameException("Invalid move");
        }
        game.updateGameState();
        game.setStatus(findState(gameId));
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
                move.getEnd().getCol()
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

//        if (moveDTO.getSpecialMove().equals( "CASTLE")){
//            game.getBoard().doCastle(movePiece,capturedPiece);
//        } else if (moveDTO.getSpecialMove().equals("ENPASSANT")) {
//            game.getBoard().doEnPassant(movePiece,capturedPiece);
//        }else if (moveDTO.getSpecialMove().equals("PROMOTION")) {
//            promotePawn(game, pieceDTO, moveDTO);
//        }else{
//            if (capturedPiece != null){
//                game.getBoard().handleCapturedPiece(game.getGameState(),capturedPiece);
//            }
//            game.getBoard().movePiece(start,end);
//
//        }
//        game.switchPlayers();
        return makeBoardDTO(game.getBoard());
    }


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


