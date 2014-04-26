
package dama;

import java.awt.Color;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author  Andrea Baggieri & Leonardo Zambaldo
 */
public class AIHard extends AI {

    /**
     * array dove verranno inseriti i valori calcolati gostgame
     * che indicano la priorita della pedina.
     */
    private final int[][] priorityDirection;
    /**
     * numero di livelli da calcolare in gostgame
     * indica anche la previsione di numero di mosse.
     */
    private final int level;
  
    /**
     * per sapere se la mossa precedente era una mangiara e indica anche quale 
     * e' la pedina.
     */
    private static final int[] sequence = {-1, -1};

    public AIHard(Controllore control, int level) {
        super(control);
        this.priorityDirection = new int[8][8];
        this.level = level;
    }

    @Override
    protected void givePriority() {

        //setto i campi ai valori nulli
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                priorityCampo[i][j] = Integer.MIN_VALUE;
                priorityDirection[i][j] = -1;
            }
        }
        //cerco se ci sono pedine che sono obbligate a mangiare
        LinkedList<Pezzi> canEat = posCanEat();
        if (!canEat.isEmpty()) {//se ce almeno una pedina che deve mangare
            if (canEat.size() == 1) {//ce solo una pedina che deve mangiare
                if (canEat.getFirst().posPosible()[2] != null) {
                    //faccio partire il ghostgame per vedere quele direzione è piu convegniente
                    startGhostGame(canEat.getFirst());
                } else {
                    // mettera MAX_VALUE nelle cordinate del pezzo e tutto il resto a 0;
                    priorityCampo[canEat.getFirst().getY()][canEat.getFirst().getX()] = Integer.MAX_VALUE;
                    priorityDirection[canEat.getFirst().getY()][canEat.getFirst().getX()] = 9;// caso un cui ce un unica direzione e la prendo da posPssibol()
                }
            } else {
                //per ogni pezzo che puo mangiare faccio partire il ghostgame
                for (Pezzi pezzi : canEat) {
                    startGhostGame(pezzi);
                }
            }
        } else {
            // se non c'è nessuna pedina che deve mangiare passo alla gestione del movimento
            // faccio partire il ghostgame per ogni pedina che puo muoversi
            for (Pezzi[] pezziVet : copyCampo) {
                for (Pezzi pezzi : pezziVet) {
                    if (pezzi != null && pezzi.canMove() && pezzi.getColore() == Color.BLACK) {
                        startGhostGame(pezzi);
                    }
                }
            }
        }
    }

    @Override
    protected void moveAI() {
        Pezzi pezzo = null;// salvo quello che ha priorita maggiore
        int pri = Integer.MIN_VALUE; // prendo il valore minimo da confrontare

        //cerco quello con priorita maggiore
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (priorityCampo[i][j] >= pri) {
                    pezzo = copyCampo[i][j];
                    pri = priorityCampo[i][j];
                }
            }
        }
        //se ce una mangiata consecutiva setto la pedina precedente
        if (sequence[0] != -1 && copyCampo[sequence[0]][sequence[1]] != null && copyCampo[sequence[0]][sequence[1]].getColore() == Color.BLACK && copyCampo[sequence[0]][sequence[1]].canEat()) {
            pezzo = copyCampo[sequence[0]][sequence[1]];
        }
        //faccio il moviemento con la pedina con priorita maggiore
        // cerco la direzione del movimento
        switch (priorityDirection[pezzo.getY()][pezzo.getX()]) {
            case 0: {
                if (!pezzo.canEat()) {
                    control.nextPosition(pezzo.getY(), pezzo.getX(), (1 + pezzo.getY()), (pezzo.getX() - 1), this);
                    sequence[0] = -1;
                    sequence[1] = -1;
                } else {
                    control.nextPosition(pezzo.getY(), pezzo.getX(), (pezzo.getY() + 2), (pezzo.getX() - 2), this);
                    sequence[0] = pezzo.getY() + 2;
                    sequence[1] = pezzo.getX() - 2;
                }
                break;
            }
            case 1: {
                if (!pezzo.canEat()) {
                    sequence[0] = -1;
                    sequence[1] = -1;
                    control.nextPosition(pezzo.getY(), pezzo.getX(), pezzo.getY() + 1, pezzo.getX() + 1, this);
                } else {
                    control.nextPosition(pezzo.getY(), pezzo.getX(), (pezzo.getY() + 2), (2 + pezzo.getX()), this);
                    sequence[0] = pezzo.getY() + 2;
                    sequence[1] = pezzo.getX() + 2;
                }
                break;
            }
            case 2: {
                if (!pezzo.canEat()) {
                    sequence[0] = -1;
                    sequence[1] = -1;
                    control.nextPosition(pezzo.getY(), pezzo.getX(), pezzo.getY() - 1, pezzo.getX() - 1, this);
                } else {
                    control.nextPosition(pezzo.getY(), pezzo.getX(), pezzo.getY() - 2, pezzo.getX() - 2, this);
                    sequence[0] = pezzo.getY() - 2;
                    sequence[1] = pezzo.getX() - 2;
                }
                break;
            }
            case 3: {
                if (!pezzo.canEat()) {
                    sequence[0] = -1;
                    sequence[1] = -1;
                    control.nextPosition(pezzo.getY(), pezzo.getX(), pezzo.getY() - 1, pezzo.getX() + 1, this);
                } else {
                    control.nextPosition(pezzo.getY(), pezzo.getX(), pezzo.getY() - 2, pezzo.getX() + 2, this);
                    sequence[0] = pezzo.getY() - 2;
                    sequence[1] = pezzo.getX() + 2;
                }
                break;
            }
            case 9: {
                Integer[] pos = pezzo.posPosible();
                control.nextPosition(pezzo.getY(), pezzo.getX(), pos[0], pos[1], this);
                sequence[0] = pos[0];
                sequence[1] = pos[1];
                break;
            }
            default: {
                System.out.println("Error! Invalid direction!");
            }

        }

    }

    /**
     * controlla tutto il campo e restituisce una linked di oggetti pezzi che
     * devono mangiare.
     *
     * @return linkedlist di pezzi che possono mangiare
     */
    private LinkedList<Pezzi> posCanEat() {
        LinkedList<Pezzi> pieceCanEat = new LinkedList<>();
        for (Pezzi[] pezziVet : copyCampo) {
            for (Pezzi pezzi : pezziVet) {
                if (pezzi != null && pezzi.canEat() && pezzi.getColore() == Color.BLACK) {
                    pieceCanEat.add(pezzi);
                }
            }
        }
        return pieceCanEat;
    }

    /**
     * FA PARTIRE UN THREAD NELLE 4 DIREZIONI POSSIBILI A DISCAPITO DEL FATTO
     * CHE SIA UNA PEDINA O UN DAMONE, SARA IL THREAD CHE SE NE OCCUPERA'
     *
     * 0 - IN BASSO A SINISTRA 1 - IN BASSO A DESTRA 2 - IN ALTO A SINISTRA 3 -
     * IN ALTO A DESTRA.
     *
     * @param pezzo
     */
    private void startGhostGame(Pezzi pezzo) {
        if (pezzo instanceof Pedina) {
            try {
                Thread t1 = new GhostGame(pezzo, copyCampo, this, 0, level);
                Thread t2 = new GhostGame(pezzo, copyCampo, this, 1, level);
                t1.start();
                t2.start();
                t1.join(8000);
                t2.join(8000);
            } catch (InterruptedException ex) {
                Logger.getLogger(AIHard.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                Thread t1 = new GhostGame(pezzo, copyCampo, this, 0, level);
                Thread t2 = new GhostGame(pezzo, copyCampo, this, 1, level);
                Thread t3 = new GhostGame(pezzo, copyCampo, this, 2, level);
                Thread t4 = new GhostGame(pezzo, copyCampo, this, 3, level);
                t1.start();
                t2.start();
                t3.start();
                t4.start();
                t1.join(8000);
                t2.join(8000);
                t3.join(8000);
                t4.join(8000);
            } catch (InterruptedException ex) {
                Logger.getLogger(AIHard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Setto il valore nel campo delle direzionio.
     * @param y coordinata
     * @param x coordinata
     * @param direction valore che indica la direzione
     */
    private void setPriorityDirection(int y, int x, int direction) {
        this.priorityDirection[y][x] = direction;
    }

    /**
     * Setto il valore nel campo delle direzionio.
     * @param y coordinata
     * @param x coordinata
     * @param value valore della priorita
     */
    private void setPriorityCampo(int y, int x, int value) {
        this.priorityCampo[y][x] = value;
    }

    /**
     * Metodo che viene utilizzato da GostGame per settare le priorità nel campo dopo
     * averle calcolate, controlla che il valore che e' gia inserito è mirore di quello arrivato.
     * @param y coordinata
     * @param x coordinata
     * @param priority prorità calcolata
     * @param direction direzione da prendere
     */
    public synchronized void setBestPriority(int y, int x, int priority, int direction) {
        if (priorityCampo[y][x] <= priority) {
            setPriorityCampo(y, x, priority);
            setPriorityDirection(y, x, direction);
        }
    }

    private String stampaDirection() {
        String campoS = "- 0 1 2 3 4 5 6 7 X\n -----------------\n";
        int i = 0;
        for (int[] ria : priorityDirection) {
            campoS += i++;
            for (int j : ria) {
                campoS += "|" + (j == -1 ? "_" : j);
            }

            campoS += "|\n -----------------\n";
        }
        return campoS;
    }

}
