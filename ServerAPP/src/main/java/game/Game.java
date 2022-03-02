package game;

import database.GameDatabase;
import database.GameMoveDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Game {
    private int id;
    private String playerXUserName;
    private String playerOUserName;
    private String lastPlayer;
    private ArrayList<GameMove> moves;
    private static final GameDatabase Db =new GameDatabase();
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public ArrayList<GameMove> getMoves() {return moves;}
    public String getPlayerXUserName() {return playerXUserName;}
    public String getPlayerOUserName() {return playerOUserName;}
    public void setMoves(ArrayList<GameMove> moves) {this.moves = moves;}
    public void setPlayerXUserName(String playerXUserName) {this.playerXUserName = playerXUserName;}
    public void setPlayerOUserName(String playerOYUserName) {this.playerOUserName = playerOYUserName;}
    public boolean isEmpty(){return moves == null || moves.isEmpty();}
    public static class GameMove{
        private int moveNumber;
        private int gameID;
        private int xCoordinate;
        private int yCoordinate;
        private char moveShape;
        private static final GameMoveDatabase Db =new GameMoveDatabase();
        public int getMoveNumber() {return moveNumber;}
        public void setMoveNumber(int moveNumber) {this.moveNumber = moveNumber;}
        public int getGameID() {return gameID;}
        public void setGameID(int gameID) {this.gameID = gameID;}
        public int getXCoordinate() {return xCoordinate;}
        public void setXCoordinate(int xCoordinate) {this.xCoordinate = xCoordinate;}
        public int getYCoordinate() {return yCoordinate;}
        public void setYCoordinate(int yCoordinate) {this.yCoordinate = yCoordinate;}
        public char getMoveShape() {return moveShape;}
        public void setGameShape(String gameShape) {this.moveShape = gameShape.charAt(0);}
        public boolean createMove(){return Db.createGameMove(gameID,moveNumber,xCoordinate,yCoordinate, String.valueOf(moveShape));}
        public ArrayList<GameMove> getGameMovesById(){//
            GameMove gameMove;
            ArrayList<GameMove> gameMovesArrayList=new ArrayList<>();
            ResultSet gameMovesResultSet =Db.getGameMoves(gameID);
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
                System.out.println("inside Game-GameMove-getGameMovesById function: "+e);
            }
            return gameMovesArrayList;
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
                System.out.println("inside Game-createGameMoveObject function: "+e);
            }
            return gameMove;
        }
        public boolean removeMoves(int gameID){return Db.removeGameMoves(gameID);}
        @Override
        public String toString() {
            return moveNumber +
                    "-" + gameID +
                    "-" + xCoordinate +
                    "-" + yCoordinate +
                    "-" + moveShape;
        }
        public GameMove(String move){
//            [1-0-0-O, 2-0-1-X, 3-1-0-O]
            String[] moveData = move.split("-");
            moveNumber=Integer.parseInt(moveData[0]);
            xCoordinate=Integer.parseInt(moveData[1]);
            yCoordinate=Integer.parseInt(moveData[2]);
            moveShape=moveData[3].charAt(0);
        }
        public GameMove(){}

    }
    public Game(){}
    public Game(String playerXUserName, String playerOUserName){
        this.playerOUserName=playerOUserName;
        this.playerXUserName=playerXUserName;
    }
    public Game(String playerXUserName, String playerOUserName,String lastPlayer ,String moves) {
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

    @Override
    public String toString() {
        if(moves==null)
            moves=new ArrayList<>();
        return id +
                ":" + playerXUserName +
                ":" + playerOUserName +
                ":" + lastPlayer +
                ":" + moves;
    }
    public boolean createGame(){
//        ahmed:ahmeds:ahmed:[1-0-0-O, 2-0-1-X, 3-1-0-O]
//        Game thisGame =new Game(playerXUserName,playerOUserName,lastPlayer,Moves);
        if( !isGame() && Db.createGame(playerXUserName, playerOUserName, lastPlayer)){
            id=getGameID(playerXUserName, playerOUserName, lastPlayer);
            for (GameMove Move:moves){
                Move.setGameID(id);
                if(!Move.createMove()) {
//                    removeGame();
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    public int getGameID(String playerXUserName,String playerOUserName,String lastPlayer){
        ResultSet GameIDResultSet = Db.getGameId(playerXUserName,playerOUserName,lastPlayer);
        if(GameIDResultSet!=null){
            try {
                if (GameIDResultSet.first()) {
                    return GameIDResultSet.getInt(1);
                }
            } catch (SQLException e) {
                System.out.println("Inside Game-getGameID function: " + e);
            }
        }
        return -1;
    }
    public boolean isGame()  {
        ResultSet gameData = Db.getGameData(id);
        try {
            if (gameData != null)
                return gameData.first();
        } catch (SQLException e) {
            System.out.println("inside the isGame function " + e);
        }
        return false;
    }

    public Game getGameByPlayersName(){
        Game gameData =new Game();
        GameMove templateMove = new GameMove();
        ResultSet gameResultSet=Db.getGameByPlayersName(this.playerOUserName, this.playerXUserName);
        try {
            if(gameResultSet!=null&&gameResultSet.first()) {
                gameData.id = gameResultSet.getInt(1);
                gameData.playerXUserName = gameResultSet.getString(2);
                gameData.playerOUserName = gameResultSet.getString(3);
                gameData.lastPlayer = gameResultSet.getString(4);
                templateMove.setGameID(gameData.id);
                gameData.setMoves(templateMove.getGameMovesById());
            }
        } catch (SQLException e) {
            System.out.println("inside getGameByPlayersName function: "+e);
        }
        return gameData;
    }
    public static boolean removeGame(int gameID){ return  new GameMove().removeMoves(gameID)&&Db.removeGame(gameID);}


//    public static ArrayList<GameMove> getGameMovesById(int gameID){//
//        ResultSet gameMovesResultSet = null;GameMove gameMove;
//        ArrayList<GameMove> gameMovesArrayList=new ArrayList<>();
//        if(Db.isGame(gameID))
//            gameMovesResultSet=Db.getGameMoves(gameID);
//        try {
//            if(gameMovesResultSet!=null &&gameMovesResultSet.first())
//            {
//                gameMove = createGameMoveObject(gameMovesResultSet);
//                gameMovesArrayList.add(gameMove);
//                while (gameMovesResultSet.next()) {
//                    gameMove = createGameMoveObject(gameMovesResultSet);
//                    gameMovesArrayList.add(gameMove);
//                }
//            }
//        } catch (SQLException e) {
//            System.out.println("inside getGameMovesById function: "+e);
//        }
//        return gameMovesArrayList;
//    }
//    public static boolean updateGameStatus(int gameID){return  Db.isGame(gameID) && Db.updateGameStatus(gameID);}
//    public static boolean removeGame(String player1,String player2){return removeGame(getGameByPlayersName(player1, player2).getId());}
//    public static boolean removeGame(String player1,String player2){return removeGame(getGameByPlayersName(player1, player2).getId());}
//    public static boolean removeGameMoves(int gameID){return  Db.isGame(gameID) && Db.removeGameMoves(gameID);}
}