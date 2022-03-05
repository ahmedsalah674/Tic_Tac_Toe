package handler;

import com.example.gamegui.Main;
import com.example.gamegui.OnlineGameController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.Optional;

public class GameHandler {
    private static class Game {
        public int id;
        public String playerXUserName;
        public String playerOUserName;
        public String lastPlayer;
        public ArrayList<GameMove> moves;

        public boolean isEmpty() {
            return moves==null || moves.isEmpty();
        }
        @Override
        public String toString() {
            if(moves!=null)
                return
                        playerXUserName +
                                ":"+playerOUserName +
                                ":"+lastPlayer +
                                ":"+moves;
            else
                return id +
                        ":"+playerXUserName +
                        ":"+playerOUserName +
                        ":[]";}

        static class GameMove {
            public int moveNumber;
            public int xCoordinate;
            public int yCoordinate;
            public char moveShape;
            @Override
            public String toString() {
                return    moveNumber  + "-"
                        + xCoordinate + "-"
                        + yCoordinate + "-"
                        + moveShape;}
            public GameMove(int moveNumber, int xCoordinate, int yCoordinate, char gameShape) {
                this.moveNumber  = moveNumber;
                this.xCoordinate = xCoordinate;
                this.yCoordinate = yCoordinate;
                this.moveShape   = gameShape;
            }
            public GameMove(String move){
//            [1-13-0-0-O, 2-13-0-1-X, 3-13-1-0-O]
                String[] moveData = move.split("-");
                moveNumber=Integer.parseInt(moveData[0]);
                xCoordinate=Integer.parseInt(moveData[2]);
                yCoordinate=Integer.parseInt(moveData[3]);
                moveShape=moveData[4].charAt(0);
            }
        }

        public Game(String playerX, String playerO, String lastPlayer, char[][] bord) {
//            ahmed:ahmeds:[1-0-0-O, 2-0-1-X, 3-1-0-O]
            this.moves= new ArrayList<>();
            this.playerXUserName = playerX;
            this.playerOUserName = playerO;
            int moveNumber = 0;
            for (int i = 0; i < bord.length; i++) {
                for (int j = 0; j < bord[i].length; j++) {
                    if (bord[i][j] == 'X' || bord[i][j] == 'O') {
                        moveNumber++;
                        GameMove thisMove = new GameMove(moveNumber, i, j, bord[i][j]);
                        this.moves.add(thisMove);
                    }
                }
            }
            this.lastPlayer = lastPlayer;
        }
        public Game(String id,String playerXUserName, String playerOUserName,String lastPlayer ,String moves) {
            this.id= Integer.parseInt(id);
            this.playerOUserName=playerOUserName;
            this.playerXUserName=playerXUserName;
            this.lastPlayer=lastPlayer;
            this.moves=new ArrayList<>();
//        [1-0-0-O, 2-0-1-X, 3-1-0-O]
            String[] movesData = moves.replace("[", "").replace("]", "").split(",");
//        1-0-0-O, 2-0-1-X, 3-1-0-O
            for (String moveData:movesData) {
                this.moves.add(new GameMove(moveData.trim()));
            }
        }
    }
    public static void handleResponse(String[] responseParts) {
//        System.out.println("GameHandler-handleResponse() response-> " + responseParts[0]);
        switch (responseParts[0]) {
            case "createGameRequest"   -> handleCreateGameRequest();
            case "saveThisGameResponse"->handleSaveThisGameResponse(responseParts);
            case "canselSaveResponse"  -> handleCanselSaveResponse();
            case "getGameResponse"     -> handleGetGameResponse(responseParts);
//            case "removeGameResponse" -> handleRemoveGame(responseParts);
            case "loadGameResponse" -> handleLoadGameResponse(responseParts);
            default -> System.out.println("Unexpected value for request: " + responseParts[0]);
        }
    }

    private static void handleLoadGameResponse(String[] responseParts) {
        Game savedGame=getSavedGame(responseParts);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Continue Game!!");
            alert.setHeaderText("you already have game saved");
            alert.setResizable(false);
            alert.showAndWait();
            loadGame(savedGame);
        });
        Main.sendMessage("removeGameRequest:"+savedGame.id,"Game");
    }

    private static Game getSavedGame(String[] responseParts) {
        Game savedGame = new Game(responseParts[1],responseParts[2], responseParts[3], responseParts[4], responseParts[5]);
        char  playerShape;
        if(savedGame.playerXUserName.equals(Main.playerUserName))
            playerShape='X';
        else
            playerShape='O';
        OnlineGameController.setMyTurn(!savedGame.lastPlayer.equals(Main.playerUserName));
        OnlineGameController.setPlayerShape(playerShape);
        return savedGame;
    }

    private static void loadGame(Game savedGame) {
        for (Game.GameMove move : savedGame.moves) {
            OnlineGameController.setMove(move.xCoordinate + "_" + move.yCoordinate, move.moveShape);
            OnlineGameController.board[move.xCoordinate][move.yCoordinate] = move.moveShape;
        }
    }

    private static void handleGetGameResponse(String[] responseParts) {
        //getGameResponse:13:ahmed:ahmeds:ahmed:[1-13-0-0-O, 2-13-0-1-X, 3-13-0-2-O, 4-13-1-1-X]
        if(!responseParts[1].equals("null")){
            Game savedGame = getSavedGame(responseParts);
            if(OnlineGameController.getMyTurn()) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Continue Game!!");
                    alert.setHeaderText("you already have one saved, do you want to continue it ?");
                    alert.setResizable(false);
                    Optional<ButtonType> result = alert.showAndWait();
                    ButtonType button = result.orElse(ButtonType.CANCEL);
                    if (button == ButtonType.OK) {
                        Main.sendMessage("reloadGameRequest:"+Main.playerUserName+":"+Main.otherPlayerUserName, "Game");
                        loadGame(savedGame);
                    }else
                        Main.sendMessage("removeGameRequest:"+savedGame.id,"Game");
                });
            }
//            Main.sendMessage("removeGameRequest:"+savedGame.id,"Game");
        }
    }

    private static void handleCanselSaveResponse() {
        OnlineGameController.gameSaved=false;
        Platform.runLater(()->{
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Can't Save");
            alert.setHeaderText("Your opponent want to continue this game you can leave it but " +
                    "you will lose it :\"");
            alert.setResizable(false);
            alert.showAndWait();
        });
    }

    private static void handleSaveThisGameResponse(String[] responseParts) {
        if(responseParts[1].equals("true")) {
            OnlineGameController.gameSaved=true;
            Main.leaveGame();
        }
    }

    private static Game createGameWithAllMoves(){
        String playerX;String playerO;String lastPlayer;
        if(OnlineGameController.playerShape=='X') {
            playerX = Main.playerUserName;
            playerO = Main.otherPlayerUserName;
        }else {
            playerX = Main.otherPlayerUserName;
            playerO = Main.playerUserName;
        }
        if(OnlineGameController.getMyTurn())
            lastPlayer=Main.otherPlayerUserName;
        else
            lastPlayer=Main.playerUserName;
        return new Game(playerX,playerO,lastPlayer,OnlineGameController.board);
    }
    private static void handleCreateGameRequest() {
        Platform.runLater(()->{
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Save Game");
            alert.setHeaderText("Your opponent want to save this game are you ok with that?!");
            alert.setResizable(false);
            Optional<ButtonType> result = alert.showAndWait();
            ButtonType button = result.orElse(ButtonType.CANCEL);
            if (button == ButtonType.OK) {
                Game thisGame = createGameWithAllMoves();
                if(!thisGame.isEmpty()){
//                    System.err.println(thisGame);
                    Main.sendMessage("saveThisGameRequest:"+thisGame, "Game");
                }else {
                    OnlineGameController.gameSaved=true;
                    Main.leaveGame();
                }
            }else {
                Main.sendMessage("canselSaveRequest:"+Main.otherPlayerUserName,"Game" );
            }
        });
    }



}
