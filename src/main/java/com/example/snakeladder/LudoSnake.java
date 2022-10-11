package com.example.snakeladder;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.snakeladder.Player.gameBoard;

public class LudoSnake extends Application {

    Group tileGroup = new Group();

    public static final int tileSize = 40;
    int height = 10;
    int width = 10;

    int yLine = 430;

    int diceValue=1;

    Label randResult;
    Button gameButton;

    Player playerOne;
    Player playerTwo;
    boolean gameStart = false;
    boolean playerOneTurn=true;
    boolean playerTwoTurn=false;
    public Pane createContent(){
        Pane root = new Pane();
        root.setPrefSize(width*tileSize,height*tileSize+80);
        root.getChildren().addAll(tileGroup);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Tile tile = new Tile(tileSize,tileSize);
                tile.setTranslateX(j*tileSize);
                tile.setTranslateY(i*tileSize);
                tileGroup.getChildren().addAll(tile);
            }

        }

        playerOne = new Player(tileSize, Color.CHOCOLATE);
        playerTwo = new Player(tileSize-10, Color.BLACK);


        randResult = new Label("Game not started");
        randResult.setTranslateX(150);
        randResult.setTranslateY(410);

        Button player1Button = new Button("Player One");
        player1Button.setTranslateX(10);
        player1Button.setTranslateY(yLine);
        player1Button.setOnAction(actionEvent -> {
            if(gameStart){
                if(playerOneTurn){
                    getDiceValue();
                    randResult.setText( "PlayerOne - " + diceValue);
                    //move the player
                    playerOne.movePlayer(diceValue);
                    playerOneTurn = false;
                    playerTwoTurn = true;

                    if(gameOver()){
                        settoinitailspos();
                    }

                }
            }

        });

        Button player2Button = new Button("Player Two");
        player2Button.setTranslateX(300);
        player2Button.setTranslateY(yLine);
        player2Button.setOnAction(actionEvent -> {
            if(gameStart){
                if(playerTwoTurn){
                    getDiceValue();
                    randResult.setText("PlayerTwo - " + diceValue);
                    //move the player
                    playerTwo.movePlayer(diceValue);
                    playerOneTurn = true;
                    playerTwoTurn = false;

                    if(gameOver()){
                        settoinitailspos();
                    }
                }
            }
        });

        gameButton = new Button("Start Game");
        gameButton.setTranslateX(150);
        gameButton.setTranslateY(yLine);
        gameButton.setOnAction(actionEvent -> {
            randResult.setText("Started");
            gameStart = !gameStart;
            if(gameStart)
            gameButton.setText("Game Going");
            else{
                gameButton.setText("Game stopped");
                settoinitailspos();
            }

        });



        Image img = new Image("D:\\LudoSnake\\src\\Image.png");
        ImageView boardImage = new ImageView();
        boardImage.setImage(img);
        boardImage.setFitHeight(tileSize*height);
        boardImage.setFitWidth(tileSize*width);


        tileGroup.getChildren().addAll(boardImage, randResult, playerOne.getGamePiece(), playerTwo.getGamePiece(), player1Button,player2Button,gameButton);

        return root;
    }

    boolean gameOver(){
        if(playerOne.getWinningStatus()){
            randResult.setText("Player One Won");
            gameButton.setText("Start Again");
            gameStart = false;
            return true;
        }
        else if(playerTwo.getWinningStatus()){
            randResult.setText("Player Two Won");
            gameButton.setText("Start Again");
            gameStart = false;
            return true;
        }
        return false;
    }

    private void settoinitailspos() {
        playerOne.currentPiecePosition = 0;
        playerOne.movePlayer(0);
        playerTwo.currentPiecePosition = 0;
        playerTwo.movePlayer(0);
        playerOneTurn = true;
        playerTwoTurn = false;
    }

    private void getDiceValue(){
        diceValue = (int)(Math.random()*6+1);
    }

    @Override
    public void start(Stage stage) throws IOException {

        Scene scene = new Scene(createContent());
        stage.setTitle("Ludo Snake");
        stage.setScene(scene);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                long currentTime =  System.currentTimeMillis();
                long dt = currentTime -  Player.lastMovementTime;


                if(dt>1e3){
                    Player.lastMovementTime = currentTime;

//
                    playerOne.playerAtSnakeOrLadder();
                    playerTwo.playerAtSnakeOrLadder();
                }
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch();
    }
}