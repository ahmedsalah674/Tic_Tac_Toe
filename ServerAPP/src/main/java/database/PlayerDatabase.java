package database;

//import javafx.scene.Parent;
//
//import java.io.File;
//import java.io.FileNotFoundException;
import java.sql.*;
//import java.util.Hashtable;
//import java.util.Scanner;
import static java.sql.ResultSet.TYPE_SCROLL_SENSITIVE;

public class PlayerDatabase extends Database{
    public PlayerDatabase() {}
    public boolean isPlayer(String userName) {
        try {
            PreparedStatement ps = con.prepareStatement("select * from players where userName =? ;", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, userName);
            ResultSet userData = ps.executeQuery();
            return userData.first();
        } catch (SQLException e) {
            System.out.println("inside the isPlayer function " + e);
        }
        return false;
    }

    public ResultSet getPlayer(String userName) {
        ResultSet userData = null;
        try {
            PreparedStatement ps = con.prepareStatement("select * from players where userName =? ;", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, userName);
            userData = ps.executeQuery();
            userData.first();
        } catch (SQLException e) {
            System.out.println("inside the signUp function " + e);
        }
        return userData;
    }

    public boolean signUp(String userName, String password) {// return -1 if disconnect 0 is username already found in DB 1 if he signed up successfully
        try {
            PreparedStatement ps = con.prepareStatement("insert into players (userName, password) values (?,?)");
            ps.setString(1, userName);
            ps.setString(2, password);
            int i = ps.executeUpdate();
            System.out.println("you insert " + i + " player in signUp function");
            reconnect();
            return true;
        } catch (SQLException e) {
            System.out.println("inside the signUp function " + e);
        }
        return false;
    }

//    public boolean isLogin(String userName){
//        try {
//            PreparedStatement ps = con.prepareStatement("select status from players where userName =? ;", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            ps.setString(1, userName);
//            ResultSet userData = ps.executeQuery();
//            userData.first();
//            if (userData.getInt(1)==1)
//                return true;
//        } catch (SQLException e) {
//            System.out.println("inside the signUp function " + e);
//        }
//        return false;
//    }

    public boolean login(String userName) {
        try {
            PreparedStatement ps = con.prepareStatement("update players set status =1 where userName =? ;", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, userName);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("inside the signUp function " + e);
        }
        reconnect();
        return false;
    }

    public String getPlayerHashPassword(String userName) {
        try {
            ResultSet playerData = getPlayer(userName);
            if (playerData.first())
                return playerData.getString(2);
        } catch (SQLException e) {
            System.out.println("inside getPlayerPassword function: " + e);
        }
        return null;
    }
    public boolean logout(String userName) {
        try {
            PreparedStatement ps = con.prepareStatement("update players set status =0 where userName =? ;", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, userName);
            if (ps.executeUpdate() > 0) {
                System.out.println("logout  : " + ps.executeUpdate());
                return true;
            }
        } catch (SQLException e) {
            System.out.println("inside the logout function " + e);
        }
        return false;
    }
//    public boolean addFriend(String userName, String friendName) {
//        try {
//            PreparedStatement ps = con.prepareStatement("insert into friends values ( ? , ? ) ;");
//            ps.setString(1, userName);
//            ps.setString(2, friendName);
//            int i = ps.executeUpdate();
//            System.out.println("you add in friends table : " + i);
//            if (i > 0)
//                return true;
//        } catch (SQLException e) {
//            System.out.println("inside the addFriends : " + e);
//        }
//        return false;
//    }
    public boolean checkFriendShip(String playerName, String friendName) {
        try {
            PreparedStatement ps = con.prepareStatement("select * from friends " +
                    "where (playerName = ? and friendName =?)" +
                    " or ( friendName = ? and playerName =?) ;", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, playerName);
            ps.setString(2, friendName);
            ResultSet friendShip = ps.executeQuery();
            return friendShip.first();
        } catch (SQLException e) {
            System.out.println("inside the checkFriendShip : " + e);
        }
        return false;
    }
    public ResultSet getFriends(String userName) {
        ResultSet friendsData = null;
        try {
            PreparedStatement ps = con.prepareStatement(
            "select userName,status,score from players " +
                "where userName =any(select playerName from friends where friendName =?" +
                "union " +
                "select friendName from friends where playerName =?)" +
                " order by score desc;"
                , TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            for (int i = 1; i <= 2; i++)
                ps.setString(i, userName);
            friendsData = ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("inside the getFriends" + e);
        }
        return friendsData;
    }
    public ResultSet getPlayers() { //return null if DB not connect and list of players name ,status and score ordered by sore
//        System.out.println("isDbConnected()->" + isDbConnected());
        ResultSet playersData = null;
        if (isDbConnected()) {
            try {
                PreparedStatement ps = con.prepareStatement("select userName,status,score from players order by status desc ,score  desc;", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                playersData = ps.executeQuery();
            } catch (SQLException e) {
                System.out.println("inside the getPlayers" + e);
            }
        }
        return playersData;
    }
//    public ResultSet getOnlinePlayers(String userName) { //return null if DB not connect and list of players name ,status and score ordered by sore
//        System.out.println("isDbConnected()->" + isDbConnected());
//        ResultSet playersData = null;
//        if (isDbConnected()) {
//            try {
//                PreparedStatement ps = con.prepareStatement("select userName,status,score from players where status=1 and userName != ? order by score desc;", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
//                ps.setString(1, userName);
//                playersData = ps.executeQuery();
//            } catch (SQLException e) {
//                System.out.println("inside the getPlayers" + e);
//            }
//        }
//        return playersData;
//    }
    public boolean updateScore(int points ,String userName) {
//        System.out.println("isDbConnected()->" + isDbConnected());
        if (isDbConnected()) {
            try {
                PreparedStatement ps = con.prepareStatement("update players set score=score+? where userName = ? ;", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setInt(1, points);
                ps.setString(2, userName);
                int playersUpdated =ps.executeUpdate();
                if(playersUpdated>=1)
                    return true;
            } catch (SQLException e) {
                System.out.println("inside the updateScore database" + e);
            }
        }
        return false;
    }


//    public boolean inGame(String userName){
//        try {
//            PreparedStatement ps = con.prepareStatement("select status from players where userName =? ;", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            ps.setString(1, userName);
//            ResultSet userData = ps.executeQuery();
//            userData.first();
//            if (userData.getInt(1)==2)
//                return true;
//        } catch (SQLException e) {
//            System.out.println("inside the signUp function " + e);
//        }
//        return false;
//    }
    public boolean leaveGame(String userName){
        try {
            PreparedStatement ps = con.prepareStatement("update players set status = 1 where userName =? ;", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, userName);
            int userData = ps.executeUpdate();
            if (userData>0)
                return true;
        } catch (SQLException e) {
            System.out.println("inside the signUp function " + e);
        }
        return false;
    }

    public boolean setInGame(String userName){
        try {
            PreparedStatement ps = con.prepareStatement("update players set status =2 where userName=? ;", TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, userName);
            int userData = ps.executeUpdate();
            if (userData>0)
                return true;
        } catch (SQLException e) {
            System.out.println("inside the signUp function " + e);
        }
        return false;
    }


}
