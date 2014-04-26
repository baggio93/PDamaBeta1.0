/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dama;

import java.util.LinkedList;
import javax.swing.JOptionPane;

/**
 *
 * @author  Andrea Baggieri & Leonardo Zambaldo
 */
public class MoveList {

    private static LinkedList<Move> lista = new LinkedList<>();
    private Move move;

    public void addDamoneList(int y, int x, boolean turn) {
        this.lista.addFirst(new Move(y, x, turn));
    }

    public void addMoveList(int y, int x, int nextY, int nextX, boolean turn) {
        this.lista.addFirst(new Move(y, x, nextY, nextX, turn));
    }

    public void addMoveListWithEat(int y, int x, int nextY, int nextX, int eatY, int eatX, boolean turn, int piece) {
        this.lista.addFirst(new Move(y, x, nextY, nextX, eatY, eatX, turn, piece));
    }

    public void removeLastMove() {
        lista.removeFirst();
    }

    public static String tostring() {
        String s = "";
        int i=lista.size();
        for (Move move : lista) {
            s +=(i--)+ ": "+ move.toString() + "\n";
        }
        return s;
    }
    
    public void resetList(){
        lista = new LinkedList<>();
    }

    public boolean getTurn() {
        return lista.getFirst().getTurn();
    }

    public int[] getLastMove() {
        int[] lastMove = null;
        try {
            move = lista.getFirst();
            lastMove = new int[7];

            lastMove[0] = move.getY();
            lastMove[1] = move.getX();
            lastMove[2] = move.getNextY();
            lastMove[3] = move.getNextX();
            lastMove[4] = move.getEatY();
            lastMove[5] = move.getEatX();
            lastMove[6] = move.getPiece();
        } catch (java.util.NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "Non ci sono mosse a ritroso!");
        }
        return lastMove;
    }

}

class Move {

    int y, x, nextY, nextX;
    int eatY = -1;
    int eatX = -1;
    boolean turn;
    int piece;

    public Move(int y, int x, boolean turn) {
        this.y = y;
        this.x = x;
        this.turn = turn;
        this.nextY = -1;
        this.nextX = -1;
    }

    public Move(int y, int x, int nextY, int nextX, boolean turn) {
        this.y = y;
        this.x = x;
        this.nextY = nextY;
        this.nextX = nextX;
        this.turn = turn;
    }

    public Move(int y, int x, int nextY, int nextX, int eatY, int eatX, boolean turn, int piece) {
        this.y = y;
        this.x = x;
        this.nextY = nextY;
        this.nextX = nextX;
        this.eatY = eatY;
        this.eatX = eatX;
        this.turn = turn;
        this.piece = piece;
    }

    public boolean getTurn() {
        return turn;
    }

    @Override
    public String toString() {
        if (eatY == -1 && nextY == -1) {
            return (!turn ? "USER:" : "PC:") + " ( " + x + "," + y + " ) -> Damone";
        } else if (eatY == -1) {
            return (!turn ? "USER:" : "PC:") + " ( " + x + "," + y + " ) -> ( " + nextX + "," + nextY + " )";
        } else {
            return (turn ? "USER:" : "PC:") + " ( " + x + "," + y + " ) -> ( " + nextX + "," + nextY + " ); ( " + eatX + "," + eatY + " ) -> eat";
        }
    }

    public int getPiece() {
        return piece;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getNextY() {
        return nextY;
    }

    public int getNextX() {
        return nextX;
    }

    public int getEatY() {
        return eatY;
    }

    public int getEatX() {
        return eatX;
    }

}
