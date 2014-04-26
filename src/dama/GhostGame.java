/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dama;

import java.awt.Color;

/**
 *
 * @author  Andrea Baggieri & Leonardo Zambaldo
 */
public class GhostGame extends Thread {

    /**
     * Pezzo di cui lo scopo trovare tutte le possibili mosse.
     */
    private final Pezzi pezzo;
    /**
     * Il campo su cui faremo un gioco fittizio.
     */
    private final Pezzi[][] campo = new Pezzi[8][8];
    /**
     * La direzzione del primo movimento.
     */
    private final int direction;
    /**
     * Dove andremo a restituire il risultato.
     */
    private final AIHard ai;
    /**
     * Numero di livelli del albero delle possibili mosse
     */
    private final int level;

    public GhostGame(Pezzi pezzo, Pezzi[][] campo, AIHard ai, int direction, int level) {
        this.pezzo = pezzo;
        copyCampo(campo);
        this.ai = ai;
        this.direction = direction;
        this.level = level;
        if (pezzo instanceof Pedina) {
            super.setName("Pedina (" + pezzo.getX() + "," + pezzo.getY() + ")");
        } else {
            super.setName("Damone (" + pezzo.getX() + "," + pezzo.getY() + ")");
        }
    }

    @Override
    public void run() {
        int cd;
        int priority;
        if ((cd = controlDirection(campo[pezzo.getY()][pezzo.getX()].posPosible(), direction, campo[pezzo.getY()][pezzo.getX()])) != -1) {
            priority = priorityTree(pezzo.getY(), pezzo.getX(), level, false, cloneCampo(campo), cd);
            ai.setBestPriority(pezzo.getY(), pezzo.getX(), priority, direction);
        }else {
            ai.setBestPriority(pezzo.getY(), pezzo.getX(), Integer.MIN_VALUE, direction);
        }

    }

    /**
     * Crea una copia esatta del campo attuale passandoglia ovviamente il campo
     * da copiare
     *
     * @param campo
     */
    private void copyCampo(Pezzi[][] campo) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (campo[i][j] != null) {
                    this.campo[i][j] = campo[i][j].createClone(this.campo);
                } else {
                    this.campo[i][j] = null;
                }

            }
        }
    }

    /**
     * Fa il movimento nel campo fittizzio e valuta in base ai nostri valori se
     * è stata una buona mossa oppure una brutta mossa, dopo di che inverte il
     * campo e restituisce la sua falutazione
     *
     * @param y coordinate inizili
     * @param x coordinate inizili
     * @param nextY coordinate finali
     * @param nextX coordinate finali
     * @param invert indica se sta giocando come PC(false) o utene(true)
     * @param campo su quale campo sta giocando.
     * @return calutazione della giocata
     */
    private int nextPositionGhost(int y, int x, int nextY, int nextX, boolean invert, Pezzi[][] campo) {
        if (!invert && (x + y) % 2 != 0 || invert && (x + y) % 2 == 0) {
            System.err.println("error in y" + y + " e x " + x);
        }
        if (!invert && (nextY + nextX) % 2 != 0 || invert && (nextY + nextX) % 2 == 0) {
            System.err.println("error in nexty e nextx");
        }
        int r = 0;
        int gravity = 1;

        if (campo[y][x].canEat()) { // stiamo muovendo per mangiare
            do {
                campo[nextY][nextX] = campo[y][x]; // copia l'oggetto nella nuova pos
                removePieceEatGhost(y, x, nextY, nextX, campo); // rimuove l'oggetto mangiato
                campo[y][x] = null; // rimuove l'oggetto nella vecchia posizione
                campo[nextY][nextX].setPos(nextY, nextX);
                if (!invert) {//turno pc
                    if (campo[nextY][nextX] instanceof Pedina) {
                        r += 5;
                    } else {
                        r += 10;
                    }
                    if (becomeDamone(nextY, nextX, campo)) {
                        r += 5;
                    }
                } else {//nostro turno
                    if (campo[nextY][nextX] instanceof Pedina) {
                        r -= 5;
                    } else {
                        r -= 10;
                    }
                    if (becomeDamone(nextY, nextX, campo)) {
                        r -= 5;
                    }
                }

                // preparo nel caso di una mangiata consecutiva
                y = nextY;
                x = nextX;
                try {
                    nextY = campo[y][x].posPosible()[0];
                    nextX = campo[y][x].posPosible()[1];
                } catch (NullPointerException e) {
                    break;
                }
                gravity++;
            } while (campo[y][x].canEat() && gravity < 5); // controlla nelle nuove pos se è possibile mangiare, se vero MANGIA NUOVAMENTE

        } else { // se non posso mangiare posso comunque muovere

            campo[nextY][nextX] = campo[y][x]; // copio oggetto nella nuova pos
            campo[y][x] = null; // cancello oggetto vecchia pos 
            campo[nextY][nextX].setPos(nextY, nextX);
            if (!invert) {//turno pc
                if (becomeDamone(nextY, nextX, campo)) {
                    r += 5;
                }
            } else {
                if (becomeDamone(nextY, nextX, campo)) {
                    r -= 5;
                }
            }
        }
        invertCampo(campo); // questo metodo inverte il campo alla fine della valutazione
        return r;
    }

    /**
     * Rimuove le pedine che sono state mangiate nel campo fittizzio.
     *
     * @param y coordinate iniziale
     * @param x coordinate iniziale
     * @param nextY coordinate finale
     * @param nextX coordinate finale
     */
    private void removePieceEatGhost(int y, int x, int nextY, int nextX, Pezzi[][] campo) {
        if (nextX >= 0 && nextX <= 7 && nextY >= 0 && nextY <= 7 && x <= 7 && y <= 7 && x >= 0 && y >= 0) {
            if (y > nextY) {// sta salendo
                if (x < nextX) {//sta andando verso destra
                    campo[y - 1][ x + 1] = null;
                } else {
                    campo[y - 1][ x - 1] = null;
                }//questo per il giocatore se è una pedina
            } else {
                if (x < nextX) {//sta andando verso destra per il giocatore
                    campo[y + 1][ x + 1] = null;
                } else {
                    campo[y + 1][ x - 1] = null;
                }//per IA se è una pedina 
            }
        } else {
            System.err.println("errore cancellazione  y= " + y + " x= " + x);
            System.out.println(stampaCampo(campo));
        }
    }

    /**
     * Inverte le pedine del giocatore con quelle del pc.
     */
    private void invertCampo(Pezzi[][] campo) {

        Pezzi moment;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                moment = campo[i][j];
                campo[i][j] = campo[7 - i][j];

                if (campo[i][j] != null) {
                    campo[i][j].setPos(i, j);
                    campo[i][j].invertColor();
                }
                campo[7 - i][j] = moment;

                if (campo[7 - i][j] != null) {
                    campo[7 - i][j].setPos(7 - i, j);
                    campo[7 - i][j].invertColor();
                }

            }

        }

    }

    /**
     * Fa l'albero di tutte le possibili giocate partendo da una indicata, il
     * medoto è ricorsivo e fa il prossimo movimento richiamdo se stesso, si
     * ferma quando il level arriva ad 0.
     *
     * @param y coordinate iniziale
     * @param x coordinate iniziale
     * @param level livello raggiunto
     * @param turn indica se sta giocando come PC(false) o utene(true)
     * @param campo su quale campo sta giocando.
     * @return valutazione di tutte le giocate
     */
    private int priorityTree(int y, int x, int level, boolean turn, Pezzi[][] campo, int direction) {

        //se la direzione è -1 la pedina non può muoversi, di conseguenza il thread terminerà con 0
        // se invece ha un valore diverso compreso tra 0 e 3 il calcolo delle possibilità inizierà
        if (direction == -1) {
            return 0;
        }

        //inizio valutazione mossa
        int n = 0;
        if (level > 0) {

            Integer[] posPossible = campo[y][x].posPosible();
            if (posPossible[direction * 2] != null) {
                //controllare in che direzzione sto andando 
                n += nextPositionGhost(y, x, posPossible[direction * 2], posPossible[direction * 2 + 1], turn, campo);
            }
            //zona ciclica dove si iniziera il gioco virtuale

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (campo[i][j] != null && campo[i][j].getColore() == Color.BLACK) {

                        int cd;
                        int cal = 0;
                        if (campo[i][j] instanceof Pedina) {
                            cd = controlDirection(campo[i][j].posPosible(), 0, campo[i][j]); // controllo nelle posizioni dei sottoalberi, se non sono possibili altre mosse in quella direzione il Thread si killa
                            cal = priorityTree(i, j, level - 1, !turn, cloneCampo(campo), cd);
                            cd = controlDirection(campo[i][j].posPosible(), 1, campo[i][j]);
                            cal += priorityTree(i, j, level - 1, !turn, cloneCampo(campo), cd);
                        } else {
                            cd = controlDirection(campo[i][j].posPosible(), 0, campo[i][j]);
                            cal = priorityTree(i, j, level - 1, !turn, cloneCampo(campo), cd);
                            cd = controlDirection(campo[i][j].posPosible(), 1, campo[i][j]);
                            cal += priorityTree(i, j, level - 1, !turn, cloneCampo(campo), cd);
                            cd = controlDirection(campo[i][j].posPosible(), 2, campo[i][j]);
                            cal += priorityTree(i, j, level - 1, !turn, cloneCampo(campo), cd);
                            cd = controlDirection(campo[i][j].posPosible(), 3, campo[i][j]);
                            cal += priorityTree(i, j, level - 1, !turn, cloneCampo(campo), cd);
                        }
                        n += cal;
                    }
                }
            }
        }
        return n;

    }

    /**
     * Clona il campo e restituisce il nuovo campo
     *
     * @param campo campo da copiare
     * @return nuovo campo identico
     */
    private Pezzi[][] cloneCampo(Pezzi[][] campo) {

        Pezzi[][] cloneCampo = new Pezzi[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (campo[i][j] != null) {
                    cloneCampo[i][j] = campo[i][j].createClone(cloneCampo);
                } else {
                    cloneCampo[i][j] = null;
                }

            }
        }
        return cloneCampo;

    }

    /**
     * Questo metodo effettua un controllo per una caso specifico di
     * posPossible, infatti se la posizione è una sola il metodo posPossible la
     * infilerà tra le prime caselle e questo è un problema perchè ghostGame
     * potrebbe vederla come una mossa effettuata nella direzione sbagliata, di
     * conseguenza se verifichiamo che la direzione è DIVERSA da quella
     * dichiarata allo start del Thread questultimo ma fermato restituiendo
     * subito il minimo valore.
     *
     * @param posPossible tutte le possibili direzioni che puo prendere la
     * pedina
     * @param direction la direzione da cercare in posPossible
     * @param pezzo per confrontare sulla posizione attuale della pedina
     * @return la posizione nel array posPossible
     */
    private int controlDirection(Integer[] posPossible, int direction, Pezzi pezzo) {
        
        int ris = -1;
        switch (direction) {
            case 0: {
                if (posPossible.length > 4 && posPossible[6] != null && posPossible[7] < pezzo.getX() && posPossible[6] > pezzo.getY()) {
                    ris = 3;
                } else if (posPossible.length > 4 && posPossible[4] != null && posPossible[5] < pezzo.getX() && posPossible[5] > pezzo.getY()) {
                    ris = 2;
                } else if (posPossible[2] != null && posPossible[3] < pezzo.getX() && posPossible[2] > pezzo.getY()) {
                    ris = 1;
                } else if (posPossible[0] != null && posPossible[1] < pezzo.getX() && posPossible[0] > pezzo.getY()) {
                    ris = 0;
                }
                break;
            }
            case 1: {
                if (posPossible.length > 4 && posPossible[6] != null && posPossible[7] > pezzo.getX() && posPossible[6] > pezzo.getY()) {
                    ris = 3;
                } else if (posPossible.length > 4 && posPossible[4] != null && posPossible[5] > pezzo.getX() && posPossible[5] > pezzo.getY()) {
                    ris = 2;
                } else if (posPossible[2] != null && posPossible[3] > pezzo.getX() && posPossible[2] > pezzo.getY()) {
                    ris = 1;
                } else if (posPossible[0] != null && posPossible[1] > pezzo.getX() && posPossible[0] > pezzo.getY()) {
                    ris = 0;
                }
                break;
            }
            case 2: {
                if (posPossible[6] != null && posPossible[7] < pezzo.getX() && posPossible[6] < pezzo.getY()) {
                    ris = 3;
                } else if (posPossible[4] != null && posPossible[5] < pezzo.getX() && posPossible[5] < pezzo.getY()) {
                    ris = 2;
                } else if (posPossible[2] != null && posPossible[3] < pezzo.getX() && posPossible[2] < pezzo.getY()) {
                    ris = 1;
                } else if (posPossible[0] != null && posPossible[1] < pezzo.getX() && posPossible[0] < pezzo.getY()) {
                    ris = 0;
                }
                break;
            }
            case 3: {
                if (posPossible[6] != null && posPossible[7] > pezzo.getX() && posPossible[6] < pezzo.getY()) {
                    ris = 3;
                } else if (posPossible[4] != null && posPossible[5] > pezzo.getX() && posPossible[5] < pezzo.getY()) {
                    ris = 2;
                } else if (posPossible[2] != null && posPossible[3] > pezzo.getX() && posPossible[2] < pezzo.getY()) {
                    ris = 1;
                } else if (posPossible[0] != null && posPossible[1] > pezzo.getX() && posPossible[0] < pezzo.getY()) {
                    ris = 0;
                }
                break;
            }
            default: {
            }
        }
        return ris;
    }

    /**
     *
     *
     * @param camp
     * @return campo
     */
    private String stampaCampo(Pezzi[][] camp) {
        String campoS = "- 0 1 2 3 4 5 6 7 X\n -----------------\n";
        int i = 0;
        for (Pezzi[] riga : camp) {
            campoS += i++;
            for (Pezzi pezzo : riga) {
                campoS += "|" + (pezzo == null ? "_" : pezzo.getColore() == Color.WHITE ? (pezzo instanceof Damone ? "c" : "0") : (pezzo instanceof Damone ? "z" : "X"));
            }
            campoS += "|\n -----------------\n";
        }
        return campoS + "Y";
    }

    /**
     * Prende le coordinate x y inserite prende il pezzo che si trova, se la
     * pedina e' nel lato opposot da cui e partita inserisce un damone nella
     * posizione
     *
     * @param nextY Coordinata Y da verificare
     * @param nextX Coordinata X da verificare
     */
    private boolean becomeDamone(int nextY, int nextX, Pezzi[][] campo) {
        if (campo[nextY][nextX].getColore() == Color.BLACK && nextY == 7 && campo[nextY][nextX] instanceof Pedina) {
            campo[nextY][nextX] = new Damone(campo[nextY][nextX].getColore(), nextY, nextX, campo);
            return true;
        } else if (campo[nextY][nextX].getColore() == Color.WHITE && nextY == 7 && campo[nextY][nextX] instanceof Pedina) {
            campo[nextY][nextX] = new Damone(campo[nextY][nextX].getColore(), nextY, nextX, campo);
            return true;
        }
        return false;
    }

    private String StampaPos(Integer[] i) {
        String s = "";

        for (Integer integer : i) {
            s += integer==null?"null":integer;
        }

        return s;
    }
}
