package game;

import java.time.Instant;
import java.util.ArrayList;

import component.*;

public class Game {
    private ArrayList<TriPair<Integer, Integer, Long>> players;//id, last mov, last bomb
    private final ArrayList<TriPair<Integer, Integer, Integer>> bombs;//x, y, time --> Coordinate e tempo rimanente all'esplosione
    private final Cell[][] board;
    public boolean start;

    public Game() {
        players = new ArrayList<TriPair<Integer, Integer, Long>>(4);
        board = new Cell[11][15];   //y dispari per forza
        bombs = new ArrayList<TriPair<Integer, Integer, Integer>>();
        start = false;
        init();
    }

    public void start() {
        start = true;
    }

    public void init() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = new Cell(-1);
            }
        }

        //muri a righe alternate
        for (int i = 1; i < board.length - 1; i += 2) {
            for (int j = 1; j < board[0].length; j += 2) {
                board[i][j].setWall();
            }
        }
        //genera random ostacoli
        for (int i = 0; i < 15; i++) {
            genObs();
        }
    }

    public int addPlayer() {
        if (players.size() == 4 || start) return -1;
        players.add(new TriPair<>(players.size() + 1, 0, (long) -1));
        switch (players.size()) {
            case 1:
                board[0][0].setPlayer(players.getLast().f);
                break;
            case 2:
                board[board.length - 1][board[0].length - 1].setPlayer(players.getLast().f);
                break;
            case 3:
                board[0][board[0].length - 1].setPlayer(players.getLast().f);
                break;
            case 4:
                board[board.length - 1][0].setPlayer(players.getLast().f);
                break;
        }
        return players.getLast().f;
    }

    //mosse 0-->up, 1-->down, 2-->dx, 3-->sx
    public void goUP(int id) {//0-->up
        Pair<Integer, Integer> pos = findPlayer(id);
        if (!start || pos == null || pos.s == 0) return;
        if (board[pos.s - 1][pos.f].isEmpty()) {
            board[pos.s - 1][pos.f].setPlayer(id);
            board[pos.s][pos.f].setEmpty();
            players.get(indexPlayer(id)).s = 0;
        }
    }

    public void goDW(int id) {//1-->down
        Pair<Integer, Integer> pos = findPlayer(id);
        if (!start || pos == null || pos.s + 1 == board.length) return;
        if (board[pos.s + 1][pos.f].isEmpty()) {
            board[pos.s + 1][pos.f].setPlayer(id);
            board[pos.s][pos.f].setEmpty();
            players.get(indexPlayer(id)).s = 1;
        }
    }

    public void goDX(int id) {//2-->dx
        Pair<Integer, Integer> pos = findPlayer(id);
        if (!start || pos == null || pos.f + 1 == board[0].length) return;
        if (board[pos.s][pos.f + 1].isEmpty()) {
            board[pos.s][pos.f + 1].setPlayer(id);
            board[pos.s][pos.f].setEmpty();
            players.get(indexPlayer(id)).s = 2;
        }
    }

    public void goSX(int id) {//3-->sx
        Pair<Integer, Integer> pos = findPlayer(id);
        if (!start || pos == null || pos.f == 0) return;
        if (board[pos.s][pos.f - 1].isEmpty()) {
            board[pos.s][pos.f - 1].setPlayer(id);
            board[pos.s][pos.f].setEmpty();
            players.get(indexPlayer(id)).s = 3;
        }
    }

    public TriPair<Integer, Integer, Integer> dropBomb(int id) {
        Pair<Integer, Integer> pos = findPlayer(id);
        if (!start || pos == null) return null;
        int index = indexPlayer(id);
        switch (players.get(index).s) {
            case 0 -> pos.s++;
            case 1 -> pos.s--;
            case 2 -> pos.f--;
            case 3 -> pos.f++;
        }
        if (players.get(index).t + 5 > Instant.now().getEpochSecond()) return null;
        if (pos.s < 0 || pos.s >= board.length || pos.f < 0 || pos.f >= board[0].length) return null;
        if (board[pos.s][pos.f].isEmpty()) {
            bombs.add(new TriPair<>(pos, 3));
            board[pos.s][pos.f].setBomb();
            players.get(index).t = Instant.now().getEpochSecond();
            return bombs.getLast();
        }
        return null;
    }

    private int indexPlayer(int id) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).f == id) {
                return i;
            }
        }
        return -1;
    }

    private Pair<Integer, Integer> findPlayer(int id) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].player && board[i][j].getId() == id) {
                    return new Pair<Integer, Integer>(j, i);
                }
            }
        }
        return null;
    }

    private void genObs() {
        int wy, wx;
        do {
            wy = (int) (Math.random() * board.length);
            wx = (int) (Math.random() * board[0].length);
        } while (!board[wy][wx].isEmpty() || wy == 0 || wx == 0 || wy == board.length - 1 || wx == board[0].length - 1);

        if (Math.random() < 0.5)
            board[wy][wx].setBush();
        else
            board[wy][wx].setCrate();
    }

    public void killPlayer(int id) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).f == id) {
                players.remove(i);
                break;
            }
        }
    }

    public boolean isOver() {
        return players.size() <= 1 && start;
    }

    public void reloadMatrix(byte[] buf, int len) {

        ArrayList<TriPair<Integer, Integer, Long>> pl = new ArrayList<>(4);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (buf[i * board[i].length + j + 1] == 10)
                    board[i][j].setCrate();
                else if (buf[i * board[i].length + j + 1] == 11)
                    board[i][j].setBush();
                else if (buf[i * board[i].length + j + 1] == 12)
                    board[i][j].setWall();
                else if (buf[i * board[i].length + j + 1] == 13) {
                    board[i][j].setBomb();
                } else if (buf[i * board[i].length + j + 1] == 14)
                    board[i][j].setEmpty();
                else{
                    board[i][j].setPlayer(buf[i * board[i].length + j + 1]);
                    pl.add(new TriPair<>((int)buf[i * board[i].length + j + 1], 0, (long) -1));
                }

            }
        }
        start = true;
        /*while (players.size() != buf[11 * 15 + 1]) {
            if (players.size() < buf[11 * 15 + 1]) {
                players.add(new TriPair<>(players.size() + 1, 0, (long) -1));
            } else {
                players.removeLast();
            }
        }*/
        players = pl;
        bombs.clear();
        for (int i = 11 * 15 + 1; i + 3 < len; i += 3) {
            bombs.add(new TriPair<>((int) buf[i + 1], (int) buf[i + 2], (int) buf[i + 3]));
        }
    }

    public byte[] createPacket() {
        byte[] buf = new byte[300];
        buf[0] = (byte) 4;//0

        //1 --> 165
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) { //player-->id, obs_crate--> 10, obs_bush-->11, wall-->12, bomb-->13, empty-->14
                buf[i * board[i].length + j + 1] = (byte) (
                        board[i][j].player ? board[i][j].id :
                                board[i][j].obs_crate ? 10 :
                                        board[i][j].obs_bush ? 11 :
                                                board[i][j].wall ? 12 :
                                                        board[i][j].bomb ? 13 : 14
                );
            }
        }

        buf[11 * 15 + 1] = (byte) players.size();//

        int last = 11 * 15 + 1;
        for (int i = 0; i / 3 < bombs.size(); i += 3) {
            buf[11 * 15 + 1 + (i + 1)] = (byte) (int) bombs.get(i / 3).f;
            buf[11 * 15 + 1 + (i + 2)] = (byte) (int) bombs.get(i / 3).s;
            buf[11 * 15 + 1 + (i + 3)] = (byte) (int) bombs.get(i / 3).t;
            last = 11 * 15 + 1 + (i + 3);
        }

        for (int i = last + 1; i < buf.length; i++) {
            buf[i] = 99;
        }
        return buf;
    }

    public ArrayList<TriPair<Integer, Integer, Long>> getPlayers() {
        return players;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public ArrayList<TriPair<Integer, Integer, Integer>> getBombs() {
        return bombs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Cell[] cells : board) {
            for (Cell cell : cells) {
                sb.append(cell);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getWin(){
        if (!players.isEmpty()){
            return "Player " + players.getFirst().f;
        }
        return "Draw!";
    }
}
