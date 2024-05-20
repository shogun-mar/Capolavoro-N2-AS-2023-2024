package game;

import audio.Audio;
import component.*;
import java.util.ArrayList;
import java.io.File;

public class Bomb implements Runnable {
    private final int x;
    private final int y;
    private final TriPair<Integer, Integer, Integer> t;
    private final Game game;
    private final Controller controller;

    private final Audio explosionSound = new Audio("audio"+File.separator+"explosion.wav");

    public Bomb(TriPair<Integer, Integer, Integer> t, Game game, Controller controller){
        this.t = t;
        x = t.f;
        y = t.s;
        this.game = game;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            for (int i = 3; i > 0; i--) {
                Thread.sleep(750);
                t.t--;
                controller.sendData();
                controller.reload();
            }
            Thread.sleep(750);
            explosion(); //Uccide giocatori in un raggio di 3 caselle

            ArrayList<TriPair<Integer, Integer, Integer>> bombs = game.getBombs();
            //rimuove bomba
            for (int i = 0; i < bombs.size(); i++) {
                if(bombs.get(i).f ==x && bombs.get(i).s ==y){
                    bombs.remove(i);
                    game.getBoard()[y][x].setEmpty();
                    break;
                }
            }
            controller.sendData();
            controller.reload();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void explosion(){
        explosionSound.play();
        System.out.println("BOOM");
        Cell[][] board = game.getBoard();
        boolean[] check = {false, false, false, false}; //sotto, sopra, destra, sinistra

        for (int i = 0; i < 3; i++) {
            //sotto
            if(y+i<board.length && !board[y+i][x].bomb){
                if(!check[0]){
                    if(board[y+i][x].player){
                        game.killPlayer(board[y+i][x].getId());
                        board[y+i][x].setEmpty();
                    }else if(!board[y+i][x].isEmpty()){
                        if(board[y+i][x].wall) check[0]=true;
                        else board[y+i][x].setEmpty();
                    }
                }
            }

            //sopra
            if(y-i>=0 && !board[y-i][x].bomb){
                if(!check[1]) {
                    if(board[y-i][x].player){
                        game.killPlayer(board[y-i][x].getId());
                        board[y-i][x].setEmpty();
                    }else if(!board[y-i][x].isEmpty()){
                        if(board[y-i][x].wall) check[1]=true;
                        else board[y-i][x].setEmpty();
                    }
                }
            }

            //destra
            if(x+i<board[0].length && !board[y][x+i].bomb){
                if(!check[2]) {
                    if(board[y][x+i].player){
                        game.killPlayer(board[y][x+i].getId());
                        board[y][x+i].setEmpty();
                    }else if(!board[y][x+i].isEmpty()){
                        if(board[y][x+i].wall) check[2]=true;
                        else board[y][x+i].setEmpty();
                    }
                }
            }

            //sinistra
            if(x-i>=0 && !board[y][x-i].bomb){
                if(!check[3]) {
                    if(board[y][x-i].player){
                        game.killPlayer(board[y][x-i].getId());
                        board[y][x-i].setEmpty();
                    }else if(!board[y][x-i].isEmpty()){
                        if(board[y][x-i].wall) check[3]=true;
                        else board[y][x-i].setEmpty();
                    }
                }
            }
        }
    }
}
