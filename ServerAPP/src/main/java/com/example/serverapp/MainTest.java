package com.example.serverapp;

import game.Game;

import java.util.ArrayList;

public class MainTest {
    public static void main(String[] args) {
//        System.out.println(Game.createGame("ahmed", "mohamedfarid"));
//        System.out.println(Game.updateGameStatus(3));
        System.out.println(Game.createGameMove(3, 3, "2","1","O"));
//          System.out.println();
        ArrayList<Game.GameMove> gameMoves = Game.getGameMovesById(3);
        System.out.println(gameMoves.size());
        for (Game.GameMove g:gameMoves) {
            System.out.println(g.toString());
        }
    }
}
