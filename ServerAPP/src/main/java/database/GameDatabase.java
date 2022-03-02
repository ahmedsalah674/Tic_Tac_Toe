package database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.ResultSet.TYPE_SCROLL_SENSITIVE;

public class GameDatabase extends Database{
    public GameDatabase() {}
    public boolean createGame(String playerX,String playerO,String lastPlayer){
        if (isDbConnected()){
            try {
                PreparedStatement ps=con.prepareStatement("insert into games (playerX,playerO,lastPlayer) values(?,?,?)"
                        , TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setString(1, playerX);
                ps.setString(2, playerO);
                ps.setString(3, lastPlayer);
                int newGamesNumber =ps.executeUpdate();
                return newGamesNumber>0;
            } catch (SQLException e) {
                System.out.println("inside GameDatabase-createGame function : " + e);
            }
        }

        return false;
    }
    public ResultSet getGameId (String playerX,String playerO,String lastPlayer) {
        if(isDbConnected()) {
            try {
                PreparedStatement ps = con.prepareStatement(
                        "select id from games where playerX =? " +
                            "and playerO=? and lastPlayer=? order by id desc limit 1 "
                        , TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setString(1, playerX);
                ps.setString(2, playerO);
                ps.setString(3, lastPlayer);
                return ps.executeQuery();
            } catch (SQLException e) {
                System.out.println("inside the getGameId function " + e);
            }
        }
        return null;
    }
    public ResultSet getGameData (int id) {
        if(isDbConnected()) {
            try {
                PreparedStatement ps = con.prepareStatement("select * from games where id =? ;", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setInt(1, id);
                return ps.executeQuery();
            } catch (SQLException e) {
                System.out.println("inside the getGameData function " + e);
            }
        }
        return null;
    }
    public ResultSet getGameByPlayersName(String player1,String player2){
        if(isDbConnected()){
            try {
                PreparedStatement ps = con.prepareStatement("select * from games where((playerX=? and playerO=?) or (playerX=? and playerO=?)) order by id desc"
                , TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setString(1, player1);
                ps.setString(2, player2);
                ps.setString(3, player2);
                ps.setString(4, player1);
                ResultSet gameData = ps.executeQuery();
                if(gameData!=null)
                return gameData;
            }catch (SQLException e){
                System.out.println("inside GameDatabase-getGameByPlayersName function: "+e);
            }
        }
        return null;
    }

    public boolean removeGame(int gameID){
        if(isDbConnected()){
            try {
                PreparedStatement ps = con.prepareStatement("delete from games where id=?", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setInt(1, gameID);
                int gameDeleted=ps.executeUpdate();
                return gameDeleted>0;
            } catch (SQLException e) {
                System.out.println("inside the removeGame function:" + e);
            }
        }
        return false;
    }
}
