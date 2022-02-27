package game;

import client.Player;
import database.GameDatabase;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Game {
    private int id;
    private String playerXUserName;
    private String playerYUserName;
    private int status;
    private static final GameDatabase Db =new GameDatabase();
    private ArrayList<GameMove> moves;
    public ArrayList<GameMove> getMoves() {return moves;}
    public void setMoves(ArrayList<GameMove> moves) {this.moves = moves;}
    public void setId(int id) {this.id = id;}
    public void setPlayerXUserName(String playerXUserName) {this.playerXUserName = playerXUserName;}
    public void setPlayerYUserName(String playerYUserName) {this.playerYUserName = playerYUserName;}
    public void setStatus(int status) {this.status = status;}
    public String getPlayerXUserName() {return playerXUserName;}
    public String getPlayerYUserName() {return playerYUserName;}
    public int getStatus() {return status;}
    public int getId() {return id;}
    public static class GameMove{
        private int moveNumber;
        private int gameID;
        private int xCoordinate;
        private int yCoordinate;
        private char gameShape;
        public int getMoveNumber() {return moveNumber;}
        public void setMoveNumber(int moveNumber) {this.moveNumber = moveNumber;}
        public int getGameID() {return gameID;}
        public void setGameID(int gameID) {this.gameID = gameID;}
        public int getXCoordinate() {return xCoordinate;}
        public void setXCoordinate(int xCoordinate) {this.xCoordinate = xCoordinate;}
        public int getYCoordinate() {return yCoordinate;}
        public void setYCoordinate(int yCoordinate) {this.yCoordinate = yCoordinate;}
        public char getGameShape() {return gameShape;}
        public void setGameShape(String gameShape) {this.gameShape = gameShape.charAt(0);}
        @Override
        public String toString() {
            return "GameMove{" +
                    "moveNumber=" + moveNumber +
                    ", gameID=" + gameID +
                    ", xCoordinate=" + xCoordinate +
                    ", yCoordinate=" + yCoordinate +
                    ", gameShape=" + gameShape +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", playerXUserName='" + playerXUserName + '\'' +
                ", playerYUserName='" + playerYUserName + '\'' +
                ", status=" + status +
                ", moves=" + moves.size() +
                '}';
    }

    public static boolean createGame(String playerX, String playerY){
        Player playerXObject=new Player(playerX);
        Player playerYObject=new Player(playerY);
        return playerXObject.isPlayer()&& playerYObject.isPlayer()&&Db.createGame(playerX,playerY);
    }
    public static boolean createGameMove(int id,int moveNumber,String xCoordinate,String yCoordinate,String moveShape){
        return Db.isGame(id)&&Db.getGameStatus(id)!=1&&Db.createGameMove(id,moveNumber,Integer.valueOf(xCoordinate),Integer.parseInt(yCoordinate),moveShape);
    }

    public static Game getGameByPlayersName(String player1,String player2){
        Game gameData =new Game();ResultSet gameResultSet = null;
        Player player1Object=new Player(player1);Player player2Object=new Player(player2);

        if(player1Object.isLogin()&&player2Object.isLogin()&&player1Object.isPlayer()&&player2Object.isPlayer()) {
            gameResultSet=Db.getGameByPlayersName(player1, player2);
        }
        try {
            if(gameResultSet!=null&&gameResultSet.first()) {
                gameData.id = gameResultSet.getInt(1);
                gameData.playerXUserName = gameResultSet.getString(2);
                gameData.playerYUserName = gameResultSet.getString(3);
                gameData.status = gameResultSet.getInt(4);
                gameData.setMoves(getGameMovesById(gameData.id));
            }
        } catch (SQLException e) {
            System.out.println("inside getGameByPlayersName function: "+e);
        }
        return gameData;
    }

    private static GameMove createGameMoveObject(ResultSet gameMovesResultSet){
        GameMove gameMove = new GameMove();
        try {
            gameMove.setMoveNumber(gameMovesResultSet.getInt(1));
            gameMove.setGameID(gameMovesResultSet.getInt(2));
            gameMove.setXCoordinate(gameMovesResultSet.getInt(3));
            gameMove.setYCoordinate(gameMovesResultSet.getInt(4));
            gameMove.setGameShape(gameMovesResultSet.getString(5));
        } catch (SQLException e) {
            System.out.println("inside createGameMoveObject function: "+e);
        }
        return gameMove;
    }


    public static ArrayList<GameMove> getGameMovesById(int gameID){
        ResultSet gameMovesResultSet = null;GameMove gameMove;
        ArrayList<GameMove> gameMovesArrayList=new ArrayList<>();
        if(Db.isGame(gameID))
            gameMovesResultSet=Db.getGameMoves(gameID);
        try {
            if(gameMovesResultSet!=null &&gameMovesResultSet.first())
            {
                gameMove = createGameMoveObject(gameMovesResultSet);
                gameMovesArrayList.add(gameMove);
                while (gameMovesResultSet.next()) {
                    gameMove = createGameMoveObject(gameMovesResultSet);
                    gameMovesArrayList.add(gameMove);
                }
            }
        } catch (SQLException e) {
            System.out.println("inside getGameMovesById function: "+e);
        }
        return gameMovesArrayList;
    }
    public static boolean updateGameStatus(int gameID){
        return  Db.isGame(gameID) && Db.updateGameStatus(gameID);
    }
    public static boolean removeGame(int gameID){return Db.isGame(gameID) && Db.getGameStatus(gameID)==1 && removeGameMoves(gameID)&& Db.removeGame(gameID);}
    public static boolean removeGameMoves(int gameID){
        return  Db.isGame(gameID) && Db.removeGameMoves(gameID);
    }
}
