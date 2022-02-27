package database;

import game.Game;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.ResultSet.TYPE_SCROLL_SENSITIVE;

public class GameDatabase extends Database{
    public GameDatabase() {}
    public boolean isGame(int id)  {
        ResultSet gameData=getGameData(id);
        try {
            if(gameData!=null)
                return gameData.first();
        }catch (SQLException e) {
            System.out.println("inside the isGame function " + e);
        }
        return false;
    }
    public boolean createGame(String playerX,String playerY){
        if (isDbConnected()){
            try {
                PreparedStatement ps=con.prepareStatement("insert into games (playerX,playerY) values(?,?)"
                        , TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setString(1, playerX);
                ps.setString(2, playerY);
                int newGamesNumber =ps.executeUpdate();
                return newGamesNumber>0;
            } catch (SQLException e) {
                System.out.println("inside createGame function : " + e);
            }
        }
        return false;
    }
    public ResultSet getGameData (int id) {
        if(isDbConnected()) {
            try {
                PreparedStatement ps = con.prepareStatement("select * from games where id =? ;", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setInt(1, id);
                ResultSet gameData = ps.executeQuery();
                return gameData;
            } catch (SQLException e) {
                System.out.println("inside the getGameData function " + e);
            }
        }
        return null;
    }
    public boolean updateGameStatus(int id){
        if(isDbConnected()){
            try {
                PreparedStatement ps = con.prepareStatement("update games set status=1 where id =? ;", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ps.setInt(1, id);
                int gameUpdated = ps.executeUpdate();
                return gameUpdated>0;
            } catch (SQLException e) {
                System.out.println("inside the updateGameStatus function " + e);
            }
        }
        return false;
    }
    public int getGameStatus(int id){
        try {
            ResultSet gameData=getGameData(id);
            if(gameData!=null&&gameData.first())
                return gameData.getInt(4);
        } catch (SQLException e) {
            System.out.println("inside getGameStatus function: " + e);
        }
        return 1;
    }
    public boolean createGameMove(int id ,int moveNumber,int xCoordinate,int yCoordinate,String moveShape){
        if(isDbConnected() && isGame(id) && getGameStatus(id)!=1){
            try {
                PreparedStatement ps = con.prepareStatement("insert into gamesMoves (gameID,moveNumber,xCoordinate,yCoordinate,moveShape)" +
                        " values(?,?,?,?,?);", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ps.setInt(1, id);
                ps.setInt(2, moveNumber);
                ps.setInt(3, xCoordinate);
                ps.setInt(4, yCoordinate);
                ps.setString(5,moveShape);
//                System.out.println(ps.toString());
                int gameUpdated = ps.executeUpdate();
                return gameUpdated>0;
            } catch (SQLException e) {
                System.out.println("inside the createGameMove function " + e);
            }
        }
        return false;
    }
    public ResultSet getGameMoves(int id){
        if(isDbConnected()){
            try {
                PreparedStatement ps = con.prepareStatement("select * from gamesMoves where gameID=? limit 9;", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setInt(1, id);
                ResultSet gameMoves = ps.executeQuery();
                return gameMoves;
            }catch (SQLException e){
                System.out.println("inside getGameMoves function: "+e);
            }
        }
        return null;
    }
    public ResultSet getGameByPlayersName(String player1,String player2){
        if(isDbConnected()){
            try {
                PreparedStatement ps = con.prepareStatement("select * from games where((playerX=? and playerY=?) or (playerX=? and playerY=?)) order by gameID desc"
                , TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setString(1, player1);
                ps.setString(2, player2);
                ps.setString(3, player2);
                ps.setString(4, player1);
                ResultSet gameData = ps.executeQuery();
                if(gameData!=null)
                return gameData;
            }catch (SQLException e){
                System.out.println("inside getGameMoves function: "+e);
            }
        }
        return null;
    }

    public boolean removeGame(int gameID){
        if(isDbConnected()){
            try {
                PreparedStatement ps = con.prepareStatement("delete from games where gameID=?", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ps.setInt(1, gameID);
                int gameDeleted=ps.executeUpdate();
                return gameDeleted>0;
            } catch (SQLException e) {
                System.out.println("inside the removeGame function:" + e);
            }
        }
        return false;
    }
    public boolean removeGame(String player1,String player2){
        ResultSet gameData = getGameByPlayersName(player1, player2);
        boolean result;
        try {
            if (gameData != null && gameData.first()) {
                removeGame(gameData.getInt(1));
                while (gameData.next()){
                    removeGame(gameData.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("inside removeGame With strings function: "+e);
        }
        return false;
    }

    public boolean removeGameMoves(int gameID){
        if(isDbConnected()){
            try {
                PreparedStatement ps = con.prepareStatement("delete from gamesMoves where gameID =? ;", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ps.setInt(1, gameID);
                int gameUpdated = ps.executeUpdate();
                return gameUpdated>=0;
            } catch (SQLException e) {
                System.out.println("inside the  removeGameMoves function " + e);
            }
        }
        return false;
    }
//    public ResultSet getGameByPlayersName(String player1,String player2){
//        if(isDbConnected()){
//            try {
//                PreparedStatement ps = con.prepareStatement("select * from games where((playerX=? and playerY=?) or (playerX=? and playerY=?))"
//                        , TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
//                ps.setString(1, player1);
//                ps.setString(2, player2);
//                ps.setString(3, player2);
//                ps.setString(4, player1);
//                ResultSet gameData = ps.executeQuery();
//                if(gameData!=null)
//                    return gameData;
//            }catch (SQLException e){
//                System.out.println("inside getGameMoves function: "+e);
//            }
//        }
//        return null;
//    }

}
