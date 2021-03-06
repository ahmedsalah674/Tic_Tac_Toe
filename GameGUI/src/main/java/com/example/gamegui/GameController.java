package com.example.gamegui;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import java.net.URL;
import java.util.*;

public class GameController implements Initializable {

    @FXML private Button button1;
    @FXML private Button button2;
    @FXML private Button button3;
    @FXML private Button button4;
    @FXML private Button button5;
    @FXML private Button button6;
    @FXML private Button button7;
    @FXML private Button button8;
    @FXML private Button button9;
    @FXML private Button resetbtn;
    @FXML private Label txt;
    @FXML private ImageView backImage;
    @FXML private AnchorPane rootPane;
    @FXML private Line line1;
    @FXML private Line line2;
    @FXML private Line line3;
    @FXML private Line line4;
    public static int difficulty = 0;
    private int [][]board ;
    private int movesLeft = 9;
    private  boolean gameOver = false;
    ArrayList<Button> buttons;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        System.out.println(difficulty);
        board = new int[3][3];
        BackgroundImage myBI= new BackgroundImage(new Image("https://cdn.pixabay.com/photo/2015/11/15/18/42/white-1044659_960_720.jpg",600,400,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        rootPane.setBackground(new Background(myBI));
        ArrayList<Line> lines = new ArrayList<>(Arrays.asList(line1, line2, line3, line4));
        lines.forEach(line -> line.setStyle("-fx-stroke: #a41f1f"));
        buttons = new ArrayList<>(Arrays.asList(button1,button2,button3,button4,button5,button6,button7,button8,button9));
        buttons.forEach(button ->{
            button.setStyle("-fx-background-color: transparent;");
            setupCursor(button);
            setupButton(button);
            button.setFocusTraversable(false);
        });
        backImage.setCursor(Cursor.HAND);
        resetbtn.setCursor(Cursor.HAND);
//        if(difficulty==1)
//            AImove();
    }
    @FXML
    void reset() {
        BackgroundImage myBI= new BackgroundImage(new Image("https://cdn.pixabay.com/photo/2015/11/15/18/42/white-1044659_960_720.jpg",600,400,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        rootPane.setBackground(new Background(myBI));
        movesLeft = 9;
        gameOver = false;
        for (int[] row: board)
            Arrays.fill(row, 0);
        buttons.forEach(this::resetButton);
        txt.setText("");
//        if(difficulty==1)
//            AImove();
    }
    public static void setDifficulty(int difficulty) {
        GameController.difficulty = difficulty;
    }
    public void back(){
        Main.changeSceneName("ChooseGameGui.fxml");
    }
    public void resetButton(Button button){
        button.setDisable(false);
        button.setText("");
    }
    private void setupCursor(Button button){button.setOnMouseEntered(mouseEvent -> button.setCursor(Cursor.HAND));}
    private void setupButton(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
            button.setDisable(true);
            button.setStyle("-fx-text-fill: black");
            button.setText("O");
            updateBoard(button);
            movesLeft--;//8//6//4//2//
            checkIfGameIsOver();
            if(movesLeft >= 1 && !gameOver) {
                AImove();
//                movesLeft--;//7//5//3//1
                checkIfGameIsOver();
            }
        });
    }

    private void AImove(){
        if(difficulty == 0)
            easyAI();
        else
            hardAI();
        movesLeft--;
    }
    private void easyAI(){
        int x;
        int y;
        Random rand = new Random();
        do{
            x = rand.nextInt(3);
            y = rand.nextInt(3);
        }while(board[x][y] != 0);
        board[x][y] = 2;
        displayAiMove(x,y);
    }
    private void hardAI() {
        int[]aiMove = findBestMove(board);
//        System.out.println(aiMove.toString());
        displayAiMove(aiMove[0],aiMove[1]);
    }
    public void displayAiMove(int x, int y){
        if(x == 0 && y == 0)
        {
            button1.setStyle("-fx-text-fill: red; -fx-background-color: transparent;");
            button1.setText("X");
            button1.setDisable(true);
        }
        else if(x == 0 && y == 1){
            button2.setStyle("-fx-text-fill: red; -fx-background-color: transparent;");
            button2.setText("X");
            button2.setDisable(true);
        }
        else if(x == 0 && y == 2){
            button3.setStyle("-fx-text-fill: red; -fx-background-color: transparent;");
            button3.setText("X");
            button3.setDisable(true);
        }
        else if(x == 1 && y == 0){
            button4.setStyle("-fx-text-fill: red; -fx-background-color: transparent;");
            button4.setText("X");
            button4.setDisable(true);
        }
        else if(x == 1 && y == 1){
            button5.setStyle("-fx-text-fill: red; -fx-background-color: transparent;");
            button5.setText("X");
            button5.setDisable(true);
        }
        else if(x == 1 && y == 2){
            button6.setStyle("-fx-text-fill: red; -fx-background-color: transparent;");
            button6.setText("X");
            button6.setDisable(true);
        }
        else if(x == 2 && y == 0){
            button7.setStyle("-fx-text-fill: red; -fx-background-color: transparent;");
            button7.setText("X");
            button7.setDisable(true);
        }
        else if(x == 2 && y == 1){
            button8.setStyle("-fx-text-fill: red; -fx-background-color: transparent;");
            button8.setText("X");
            button8.setDisable(true);
        }
        else if(x == 2 && y == 2){
            button9.setStyle("-fx-text-fill: red; -fx-background-color: transparent;");
            button9.setText("X");
            button9.setDisable(true);
        }
    }
    private void updateBoard(Button b){
        switch (b.getId()){
            case "button1"->board[0][0] = 1;
            case "button2"->board[0][1] = 1;
            case "button3"->board[0][2] = 1;
            case "button4"->board[1][0] = 1;
            case "button5"->board[1][1] = 1;
            case "button6"->board[1][2] = 1;
            case "button7"->board[2][0] = 1;
            case "button8"->board[2][1] = 1;
            case "button9"->board[2][2] = 1;
        }
    }
    public void checkIfGameIsOver(){
        for (int a = 0; a < 8; a++) {
            String line = switch (a) {
                case 0 -> button1.getText() + button2.getText() + button3.getText();
                case 1 -> button4.getText() + button5.getText() + button6.getText();
                case 2 -> button7.getText() + button8.getText() + button9.getText();
                case 3 -> button1.getText() + button5.getText() + button9.getText();
                case 4 -> button3.getText() + button5.getText() + button7.getText();
                case 5 -> button1.getText() + button4.getText() + button7.getText();
                case 6 -> button2.getText() + button5.getText() + button8.getText();
                case 7 -> button3.getText() + button6.getText() + button9.getText();
                default -> null;
            };
            //X winner
            if (line.equals("XXX")) {
//                txt.setText("X won!");
                buttons.forEach(button -> button.setDisable(true));
                gameOver = true;
                Main.changeSceneName("loser.fxml");
            }
            //O winner
            else if (line.equals("OOO")) {
//                txt.setText("O won!");
                buttons.forEach(button -> button.setDisable(true));
                gameOver = true;
                Main.changeSceneName("winner.fxml");
            }
            //Tie
            if(movesLeft <=0 && !gameOver)
            {
                gameOver = true;
                Main.changeSceneName("Tie.fxml");
//                txt.setText("Oh NO!! .... Its A Tie!");
            }
        }
    }
    // to check if there is any cell empty
    static Boolean isMovesLeft(int [][]board)
    {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == 0)
                    return true;
        return false;
    }
    // to evaluate the value of the board
    static int evaluate(int [][]b)
    {
        // Checking for Rows for X or O
        for (int row = 0; row < 3; row++)
        {
            if (b[row][0] == b[row][1] &&
                    b[row][1] == b[row][2])
            {
                if (b[row][0] == 2)
                    return +10; // any  postive number
                else if (b[row][0] == 1)
                    return -10;  // any negative number
            }
        }

        // Checking for Columns for X or O
        for (int col = 0; col < 3; col++)
        {
            if (b[0][col] == b[1][col] &&
                    b[1][col] == b[2][col])
            {
                if (b[0][col] == 2)
                    return +10;

                else if (b[0][col] == 1)
                    return -10;
            }
        }

        // Checking for Diagonals for X or O
        if (b[0][0] == b[1][1] && b[1][1] == b[2][2])
        {
            if (b[0][0] == 2)
                return +10;
            else if (b[0][0] == 1)
                return -10;
        }

        if (b[0][2] == b[1][1] && b[1][1] == b[2][0])
        {
            if (b[0][2] == 2)
                return +10;
            else if (b[0][2] == 1)
                return -10;
        }
        // Else if none of them have won then return 0
        return 0;
    }
    // This is the minimax function. It considers all
    // the possible ways the game can go and returns
    // the value of the board
    static int minimax(int [][]board,int depth, Boolean isMax)
    {
        int score = evaluate(board);
        // If Maximizer has won the game
        // return his/her evaluated score
        if (score == 10)
            return score;
        // If Minimizer has won the game
        // return his/her evaluated score
        if (score == -10)
            return score;
        // If there are no more moves and
        // no winner then it is a tie
        if (!isMovesLeft(board))
            return 0;
        // If this maximizer's move
        if (isMax)
        {
            int best = -1000;

            // Traverse all cells
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    // Check if cell is empty
                    if (board[i][j]==0)
                    {
                        // Make the move
                        board[i][j] = 2;

                        // Call minimax recursively and choose
                        // the maximum value
                        best = Math.max(best, minimax(board,
                                depth + 1, false));

                        // Undo the move
                        board[i][j] = 0;
                    }
                }
            }
            return (best-depth);
        }

        // If this minimizer's move
        else
        {
            int best = 1000;
            // Traverse all cells
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    // Check if cell is empty
                    if (board[i][j] == 0)
                    {
                        // Make the move
                        board[i][j] = 1;
                        // Call minimax recursively and choose
                        // the minimum value
                        best = Math.min(best, minimax(board,
                                depth + 1, true));
                        // Undo the move
                        board[i][j] = 0;
                    }
                }
            }
            return (best+depth);
        }
    }
    public int[] findBestMove(int [][]board)
    {
        int moveVal;
        int bestVal = -1000;
        int x = -1;
        int y = -1;
        // Traverse all cells, evaluate minimax function
        // for all empty cells. And return the cell
        // with optimal value.
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                // Check if cell is empty
                if (board[i][j] == 0)
                {
                    // Make the move
                    board[i][j] = 2;

                    // compute evaluation function for this
                    // move.
                    moveVal = minimax(board, 0, false);

                    // Undo the move
                    board[i][j] = 0;

                    // If the value of the current move is
                    // more than the best value, then update
                    // best/
                    if (moveVal > bestVal)
                    {
                        x = i;
                        y = j;
                        bestVal = moveVal;
                    }
                }
            }
        }
        board[x][y] = 2;
        return new int[]{x,y};
    }
//    public void printArray(int arr[][]){
//        for(int i=0; i<arr.length; i++)
//        {
//            for(int j=0; j<arr[i].length; j++)
//                System.out.println(arr[i][j]);
//        }
//    }
//    public void showVideoAlert(){
//        StackPane secondaryLayout2 = new StackPane();
//////        Media media = new Media(new File("src/main/resources/com/example/gamegui/videoplayback.flv").toURI().toString());
////        Media media = new Media("https://doc-00-6g-docs.googleusercontent.com/docs/securesc/9prknvmcj9gud36ias9pvl2lhiqtn8uf/q5uprmpeji571dq7p6dbl4986fol3tm7/1646232900000/07243282101955004259/07243282101955004259/1SJ93esbbc_USofGV4CclGaOrI14-561c?e=download&ax=ACxEAsa4Mce8nTVX9tFsuDnvc5NfG1utyxYT-X-mgTGf_avCGwp7F9HKNMrrDDfDNPtO8aAGJ3L8stroFgeMUPMFMWUfXznGK2t2-0cEBomXF0o3jjmhAN2KgmWf6Z8QW-cZsDIq2nkvCdKJDf71jUYEzm3ajPnCUE1u2yjkmWzCYgKj_H87l8PhEl_Hk_xHHaQidO7FRV8tT31qmzJJyKg81QPKER8htxuACamyWyvNqLdg0I3sW_aWK7V2r6eJPqUeL2nqe0S3mPw9s-BuzLQDng0IlUdiUnIYJ0eLzWdBQxx-o-gxCQK2LRSGpd2_3-BDDfvXpKbtsu4F9t7s63oAOeTKIYddHIgqhXDGcGHLs4sMY0ggtQ-lbVrYLH8RmAJy8RixeL2-4tREWyfnBAWortS1yPNF5zl2K2VwmnBHVs88A7MoAyEWXYP0bATywD5O-9Be0wLRtUC5MXl3sffEHXvu4bnHkPJER7AYd0ZSjslJzL11WarD_6zbW1pR7bYihVkFLI0WEMI-lZm2lA75c8VhMZd0zm_9TrZZpOIwn-IG5_C5tnO7bV5A9N1-9bUktq19Bbaj2_HCFFcwO5Bw8n4svvzZJlEFj2eWg6tQXycOmVMbN38PlQ8kZrV_9SprVOgjGJsP7qWKrvTv4TOARDrKmGb0aKuFYX_JLgly&authuser=0&nonce=9if50m0mqgg3u&user=07243282101955004259&hash=e391mlep2huf97v0rri876jm4k6437vo");
////        MediaPlayer videoForWinner = new MediaPlayer(media);
//        MediaPlayer videoForWinner = new MediaPlayer(new Media(getClass().getResource("loser.mp4").toExternalForm()));
//        MediaView mediaView2 = new MediaView(videoForWinner);
//        secondaryLayout2.getChildren().addAll(mediaView2);
//        Scene secondScene2 = new Scene(secondaryLayout2, 420, 400);
//        Stage secondStage2 = new Stage();
//        secondStage2.setResizable(false);
//        secondStage2.setScene(secondScene2);
//        secondStage2.show();
//        videoForWinner.play();
//        PauseTransition delay = new PauseTransition(Duration.seconds(10));
//        delay.setOnFinished( event -> secondStage2.close() );
//        delay.play();
//    }
//        String path = "/home/ahmedsalah/-----------------------/lastUpdatedVersion/GameGUI/playAgain.mp4";
//        MediaPlayer videoForWinner = new MediaPlayer(new Media(getClass().getResource(path).toExternalForm()));
////        Media media = new Media(new File(path).toURI().toString());
////        MediaPlayer videoForLoser = new MediaPlayer(media);
//////        videoForLoser.setAutoPlay(true);
//        MediaView mediaView = new MediaView(videoForWinner);
////        Label label = new Label("Do you really think your time is correct?");
////        VBox content = new VBox(10, label, mediaView);
//        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
//        Label label = new Label("Do you really think your time is correct?");
//        VBox content = new VBox(10, label, mediaView);
//        content.setAlignment(Pos.CENTER);
//        alert.getDialogPane().setContent(content);
//        Optional<ButtonType> result = alert.showAndWait();
//        result.
////        alert.setGraphic(new MediaView(new Media("")));
//    }
}