package client;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import database.*;

//import static java.sql.ResultSet.TYPE_SCROLL_SENSITIVE;

public class Player {
    private String userName;
    private String password;
    private int status;
    private int score;
    private final PlayerDatabase Db=new PlayerDatabase();
    public Player(){}
    public Player(String playerName){
        setUserName(playerName);
        getPlayerData();
    }
    public Player(String playerName, String playerPassword){
        setUserName(playerName);
        setPassword(playerPassword);
        getPlayerData();
    }
//    Player(String playerName, String playerPassword, int playerStatus){
//        setUserName(playerName);
//        setPassword(playerPassword);
//        setStatus(playerStatus);
//        getPlayerData();
//    }
//    Player(String playerName , String playerPassword, int playerStatus, int playerScore){
//        setUserName(playerName);
//        setPassword(playerPassword);
//        setScore(playerScore);
//        getPlayerData();
//    }
//    Player(Player copyPlayer){
//        setUserName(copyPlayer.getUserName());
//        setPassword(copyPlayer.getPassword());
//        setStatus(copyPlayer.getStatus());
//        setScore(copyPlayer.getScore());
//    }
    private  Player(ResultSet playerData) {
        try {
            setUserName(playerData.getString(1));
            setStatus(playerData.getInt(2));
            setScore(playerData.getInt(3));
        }catch (SQLException e){
            System.out.println("inside Player(ResultSet playerData) constructor: "+e);
        }
    }
    private void getPlayerData(){
        if(isPlayer()){
            try {
                ResultSet playerData = Db.getPlayer(userName);
                if(playerData.first()) {
                    setStatus(playerData.getInt(3));
                    setScore(playerData.getInt(4));
                }
            } catch (SQLException e) {
                System.out.println("inside Player.getPlayerData function: " + e);
            }
        }
    }
    public void setUserName(String playerName){
        if(playerName != null && ! playerName.trim().equals(""))
            userName=playerName;
    }
    public void setPassword(String playerPass){
        if(playerPass != null && !playerPass.equals(""))
            password=playerPass;
    }
    public void setStatus(int playerStatus){
        if(playerStatus >= 0 && playerStatus <= 2 )
            status=playerStatus;
        else
            status=0;
    }
    public void setScore(int playerScore){
        score = Math.max(playerScore, 0);
    }
    public String getUserName(){
        return userName;
    }
//    public String getPassword(){
//        return password;
//    }
    public int getStatus(){
        return status;
    }
    public int getScore(){
        return score;
    }
    public boolean isPlayer() { return Db.isDbConnected() && Db.isPlayer(userName); }

    public static String hashPassword(String password) {
        String passwordHashed = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            passwordHashed = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("inside the hashPassword function " + e);
        }
        return passwordHashed;
    }

    public boolean checkPassword() {
        return Db.isDbConnected() && hashPassword(password).equals(Db.getPlayerHashPassword(this.userName));
    }
    public boolean signUp(){
        if (password == null || password.equals(""))
            return false;
        return !isPlayer() && Db.signUp(userName,hashPassword(password));
    }
    public boolean isLogin(){
        return status > 0;
    }
    public boolean login()
    {
        if(isPlayer() && !isLogin() && checkPassword() && Db.login(userName)) {
            status =1;
            return true;
        }
        return false;
    }

    public boolean logout()
    {
        if(isPlayer() && isLogin() && Db.logout(userName)) {
            status =0;
            return true;
        }
        return false;
    }

//    public boolean addFriend(String friendName){
//        Player friend = new Player(friendName);
//        return isPlayer() && isLogin() && friend.isPlayer() && !checkFriendShip(friendName)  && Db.addFriend(userName,friendName);
//    }

//    public boolean checkFriendShip(String friendName){
//        Player friend = new Player(friendName);
//        return isPlayer() && friend.isPlayer() && Db.checkFriendShip(userName , friendName);
//    }
    public ArrayList<Player> getFriends(){
        ArrayList<Player> friends= new ArrayList<>();
        if (isPlayer()) {
            try{
                ResultSet friendsData = Db.getFriends(userName);
                if(friendsData.first()) {
                    Player friend = new Player(friendsData);
                    friends.add(friend);
                    while (friendsData.next()) {
                        friend = new Player(friendsData);
                        friends.add(friend);
                    }
                }
            }
            catch (SQLException e){
                System.out.println("inside player.getFriends function: " + e);
            }
        }
        return friends;
    }

    public ArrayList<Player> getPlayers(){
        ArrayList<Player> players= new ArrayList<>();
        try{
            ResultSet playersData = Db.getPlayers();
            if(playersData!=null&&playersData.first()) {
                Player onePlayer;
                if(userName!=null&&userName.equals(playersData.getString(1))) ;
                else {
                    onePlayer = new Player(playersData);
                    players.add(onePlayer);
                }
                while (playersData.next()) {
                    if(userName!=null&&userName.equals(playersData.getString(1)))
                        continue;
                    onePlayer = new Player(playersData);
                    players.add(onePlayer);
                }
            }
        }
        catch (SQLException e){
            System.out.println("inside player.getFriends function: " + e);
        }
        return players;
    }

//    public ArrayList<Player> getOnlinePlayers(){
//        ArrayList<Player> players= new ArrayList<>();
//        try{
//            ResultSet playersData = Db.getOnlinePlayers(userName);
//            if(playersData.first()) {
//                Player onePlayer = new Player(playersData);
//                players.add(onePlayer);
//                while (playersData.next()) {
//                    onePlayer = new Player(playersData);
//                    players.add(onePlayer);
//                }
//            }
//        }
//        catch (SQLException e){
//            System.out.println("inside player.getFriends function: " + e);
//        }
//        return players;
//    }
    public boolean addScore(int points){
        return isPlayer()&&isLogin()&& Db.updateScore(points, userName);
    }

//    public boolean inGame(){return isLogin()&&Db.inGame(userName);}
    public boolean leaveGame(){return isLogin()&&Db.leaveGame(userName);}
    public boolean setInGame() {return isLogin()&&Db.setInGame(userName);}
}
