package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.ResultSet.TYPE_SCROLL_SENSITIVE;

public class GameMoveDatabase extends Database{
    public boolean createGameMove(int id ,int moveNumber,int xCoordinate,int yCoordinate,String moveShape){
//        System.err.println(moveShape);
        if(isDbConnected()){
            try {
                PreparedStatement ps = con.prepareStatement("insert into gamesMoves (gameID,moveNumber,xCoordinate,yCoordinate,moveShape)" +
                        " values(?,?,?,?,?);", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setInt(1, id);
                ps.setInt(2, moveNumber);
                ps.setInt(3, xCoordinate);
                ps.setInt(4, yCoordinate);
                ps.setString(5,moveShape);
                int gameUpdated = ps.executeUpdate();
                return gameUpdated>0;
            } catch (SQLException e) {
//                e.printStackTrace();
                System.out.println("inside the GameMoveDatabase-createGameMove function " + e);
            }
        }
        return false;
    }
    public ResultSet getGameMoves(int id){
        if(isDbConnected()){
            try {
                PreparedStatement ps = con.prepareStatement("select * from gamesMoves where gameID=? limit 9;", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setInt(1, id);
                return ps.executeQuery();
            }catch (SQLException e){
                System.out.println("inside GameMoveDatabase-getGameMoves function: "+e);
            }
        }
        return null;
    }

    public boolean removeGameMoves(int gameID){
        if(isDbConnected()){
            try {
                PreparedStatement ps = con.prepareStatement("delete from gamesMoves where gameID =? ;", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setInt(1, gameID);
                int gameUpdated = ps.executeUpdate();
                return gameUpdated>=0;
            } catch (SQLException e) {
                System.out.println("inside GameMoveDatabase-removeGameMoves function " + e);
            }
        }
        return false;
    }
}
