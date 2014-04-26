package dama;

import java.awt.Color;
import javax.swing.JOptionPane;
import graphics.EventWindow;
import graphics.moveListGraphics;
import graphics.Sound;

/**
 *
 * @author  Andrea Baggieri & Leonardo Zambaldo
 */
public class Controllore {

    static Controllore control;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        control = new Controllore();
    }

    /**
     * Identifica il campo il primo valore indica Y e il secondo indica X.
     */
    private static final Pezzi campo[][] = new Pezzi[8][8];
    /**
     * Variabile per vedere se la partita e conclusa
     */
    private static boolean victory = false;
    /**
     * Indica se è il turno del utente.
     */
    private static boolean userTurn;

    /**
     * Indica se si deve mangiare in connesecuzione, salva anche quale il pezzo.
     */
    private Pezzi eatSequence;
    /**
     * Interfaccia grafica.
     */
    private static EventWindow window;
    /**
     * Intefaccia audio.
     */
    private static final Sound sound = new Sound();
    /**
     * Variabile che setta la difficolta indicata dell'utente.
     */
    private static int difficult;
    /**
     * Classe dove si inseriscono le mosse effetuate.
     */
    private static final MoveList moveList = new MoveList();

    /**
     * Crea e prepara il campo per il gioco.
     */
    public Controllore() {

        createAllPiece();
        userTurn = true;
        eatSequence = null;
        setDificult();
        window = new EventWindow(this);
        window.start();
        if (difficult == 3) {
            sound.chuckNorris();
        }
    }

    /**
     * verifica se uno dei due giocatori ha vinto.
     */
    public static void victory() {
        int pieceBlack = 0;
        int pieceWhite = 0;
        boolean possBlack = false;  // controlla usando canEat e canMove se le pedine nere
        boolean possWhite = false;  // o bianche hanno ancora mosse da eseguire... se non c'è ne sono è partita persa

        // controllo tutte le pedine rimaste, le conto e controllo ci sia almeno una che si puo muovere
        for (Pezzi[] pezzis : campo) {
            for (Pezzi pezzi : pezzis) {
                if (pezzi != null && pezzi.getColore() == Color.BLACK) {
                    pieceBlack++;//conto le nere 
                    possBlack = possBlack || pezzi.canMove() || pezzi.canEat();
                    //controllo che ci sia una pedina che puo muoversi o mangiare nera
                } else if (pezzi != null && pezzi.getColore() == Color.WHITE) {
                    pieceWhite++;//conto le bianche
                    possWhite = possWhite || pezzi.canMove() || pezzi.canEat();
                    //controllo che ci sia una pedina che puo muoversi o mangiuserare bianca
                }
            }
        }

        if (pieceBlack == 0 || !possBlack) {
            //utente ha vinto
            victory = true;
            sound.winSTARWARS();
            JOptionPane.showMessageDialog(null, "HAI VINTO!!!\n");
        } else if (pieceWhite == 0 || !possWhite) {
            //PC a vinto
            victory = true;
            sound.looseSTARWARS();
            JOptionPane.showMessageDialog(null, "HAI PERSO... =(\n");
        } else {
            //la partita non è finita
            victory = false;
        }
    }

    /**
     * Stampa il campo nella modalita terminale, '0' per le pedine bianche, 'X'
     * per le pedine nere, 'c' damone binaco, 'z' damone nero.
     *
     * @return il campo stampato
     */
    @SuppressWarnings("empty-statement")
    public String stampaCampo() {
        String campoS = "- 0 1 2 3 4 5 6 7 X\n -----------------\n";
        int i = 0;
        for (Pezzi[] riga : Controllore.campo) {
            campoS += i++;
            for (Pezzi pezzo : riga) {
                campoS += "|" + (pezzo == null ? "_" : pezzo.getColore() == Color.WHITE ? (pezzo instanceof Damone ? "c" : "0") : (pezzo instanceof Damone ? "z" : "X"));
            }
            campoS += "|\n -----------------\n";
        }
        return campoS + "Y";
    }

    /**
     * Crea e posiziona le pedene nel posizione giusta.
     */
    private static void createAllPiece() {
//         posizionamento delle pedine nere
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 8; x += 2) {
                // si trova nella riga 1 viene scitato a dentra
                if (y != 1) {
                    setPezzo(y, x, new Pedina(Color.BLACK, y, x, campo));
                } else {
                    setPezzo(y, x + 1, new Pedina(Color.BLACK, y, x + 1, campo));
                }
            }
        }
        // posizionamento delle pedine bianche
        for (int y = 7; y > 4; y--) {
            for (int x = 7; x > 0; x -= 2) {
                // si trova nella riga 1 viene scitato a sinistra
                if (y != 6) {
                    setPezzo(y, x, new Pedina(Color.WHITE, y, x, campo));
                } else {
                    setPezzo(y, x - 1, new Pedina(Color.WHITE, y, x - 1, campo));
                }
            }
        }
    }

    /**
     * Prende in ingresso la posizione della pedina e la nuova posizione e chi
     * sta giocando, verifica se i dati sono corretti, poi effetua il movimento
     * (movimento o mangiata); controlla se la pedina puo mangiare di nuovo e
     * lascia il turno per mangiare di nuovo.
     *
     * @param y Coordinata Y della pezzo selezionato.
     * @param x Coordinata X della pezzo selezionato.
     * @param nextY Coordinata Y della nuova posizione.
     * @param nextX Coordinata X della nuova posizione.
     * @param ob Oggeto di chi sta giociocando.
     * @return se coretto true altrimenti false
     */
    public synchronized boolean nextPosition(int y, int x, int nextY, int nextX, Object ob) {
        System.out.println("(" + x + "," + y + ")->(" + nextX + "," + nextY + ")");
        if (x >= 0 && x < 8 && y < 8 && y >= 0 && nextX >= 0 && nextX < 8 && nextY < 8 && nextY >= 0 && !victory) {//i valori sono nel campo
            if (getPezzo(y, x) != null && isYourTurn(ob) && isYourPiece(y, x, ob)) { //la casella selezionata non è vuota ed è il mio turno per giocare inoltre controllo che la pedina sia mia
                if (getPezzo(nextY, nextX) == null) { //la nuova posizione è VUOTA // bisognia inserire il controllo se stai usando la tua pedina
                    if (getPezzo(y, x).canMove(nextY, nextX) && eatSequence == null && !somePieceCanEat(ob)) { // caso movimento (nessuna mangiata disponibile)
                        setPezzo(nextY, nextX, getPezzo(y, x));
                        removePiece(y, x);
                        userTurn = !userTurn;
                        sound.playSoundMove();
                        becomeDamone(nextY, nextX);
                        moveList.addMoveList(y, x, nextY, nextX, userTurn);
                        victory();
                        if (!userTurn) {
                            startIA(difficult);
                        } else {
                            window.upgradeIcon();
                        }
                    } else if (getPezzo(y, x).canEat(nextY, nextX) && eatSequence == null || eatSequence == campo[y][x]) { // caso mangiata
                        setPezzo(nextY, nextX, getPezzo(y, x));
                        int[] posEatPiece = removePedinaMangiata(y, x, nextY, nextX);
                        moveList.addMoveListWithEat(y, x, nextY, nextX, posEatPiece[0], posEatPiece[1], userTurn, posEatPiece[2]);
                        removePiece(y, x);
                        becomeDamone(nextY, nextX);
                        sound.playSoundMove();
                        if (getPezzo(nextY, nextX).canEat()) {//controllo se puo mangiare di nuovo
                            eatSequence = getPezzo(nextY, nextX);
                            if (!userTurn && !victory) {
                                startIA(difficult);
                                window.upgradeIcon();
                            } else {
                                window.upgradeIcon();
                            }
                        } else {
                            userTurn = !userTurn;
                            eatSequence = null;
                            if (!userTurn && !victory) {
                                startIA(difficult);
                            } else {
                                window.upgradeIcon();
                            }
                        }
                        victory();
                    } else {
                        System.out.println("posizione non valida (" + x + "," + y + ")->(" + nextX + "," + nextY + ")");
                        if (!userTurn && !victory) { // sistemazione temporanea!
                            startIA(difficult);
                        } else {
                            window.upgradeIcon();
                        }
                    }
                } else {
                    System.out.println("posizione occupata");
                    return false;
                }
            } else {
                System.out.println("la casella selezionata e vuota o non è il tuo turno");
                return false;
            }
        } else {
            System.out.println("coordinate fuori dal campo");
            return false;
        }
        moveListGraphics.updateMoveList();
        return true;
    }

    /**
     * Piu avanti si mettere acome paratmetro il valore per indicare quale IA si
     * scegliera di startare e con uno swith si fara partire
     *
     * @param i numero della difficolta
     */
    private void startIA(int i) {
        switch (i) {
            case 0: {
                new AIHard(this, 2).start();
                break;
            }
            case 1: {
                new AIHard(this, 4).start();
                break;
            }
            case 2: {
                new AIHard(this, 5).start();
                break;
            }
            case 3: {
                new ChuckNorrisAI(this, window).start();
                break;
            }
            default: {
                System.exit(0);
            }
        }
    }

    /**
     * Chiede alla pedina/damone che movimenti puo fare e li restituisce se i
     * dati inseriti sono incoretti o la posizone inserita non ce una pedina
     * restituisce null.
     *
     * @param y Coordinata Y della pezzo selezionato.
     * @param x Coordinata X della pezzo selezionato.
     * @return Restituisce un arrey Integer o null.
     */
    public Integer[] posPosible(int y, int x) {
        if (x >= 0 && x < 8 && y < 8 && y >= 0 && getPezzo(y, x) != null) {
            return getPezzo(y, x).posPosible();
        } else {
            return null;
        }
    }

    /**
     * In base al nome della classe del oggetto in ingresso indica se e' il suo
     * turno di giocare oppure no.
     *
     * @param ob oggetto
     * @return true se e' il turno del oggetto che la richiamto altrimenti false
     */
    private boolean isYourTurn(Object ob) {
        if (userTurn && ob.getClass() == window.getClass()) {
            return true;
        } else if (!userTurn && ob.getClass() != window.getClass()) {
            return true;
        }
        return false;
    }

    /**
     * Inserisce una pedina nella posizione indicata DA METTERE PRIVATO.
     *
     * @param y coordinata y del campo.
     * @param x coordinata x del campo.
     * @param p pedina da inserire nel posizione x y.
     */
    public static void setPezzo(int y, int x, Pezzi p) {
        p.setPos(y, x);
        campo[y][x] = p;
    }

    /**
     * Restituise la pedina nella pizione indicata.
     *
     * @param y coordinata y del campo.
     * @param x coordinata x del campo.
     * @return pedina selezionata dalle coordinate.
     */
    public Pezzi getPezzo(int y, int x) {
        return campo[y][x];
    }

    /**
     * Nella coordinate indicate inserisce null
     *
     * @param y coordinata y del campo.
     * @param x coordinata x del campo.
     */
    private static void removePiece(int y, int x) {
        campo[y][x] = null;
    }

    /**
     * Rimuove la pedina mangiata, funziona sia per le pedine che per i damoni.
     *
     * @param y posizione y della pedina
     * @param x posizione x della pedina
     * @param nextY futura posizione della pedina
     * @param nextX futura posizione della pedina
     */
    private int[] removePedinaMangiata(int y, int x, int nextY, int nextX) {
        int[] posPieceEat = new int[3];

        if (y > nextY) {// sta salendo
            if (x < nextX) {//sta andando verso destra
                if (campo[y - 1][x + 1] instanceof Pedina) {
                    posPieceEat[2] = 0;
                } else {
                    posPieceEat[2] = 1;
                }
                removePiece(y - 1, x + 1);
                posPieceEat[0] = y - 1;
                posPieceEat[1] = x + 1;
            } else {
                if (campo[y - 1][x - 1] instanceof Pedina) {
                    posPieceEat[2] = 0;
                } else {
                    posPieceEat[2] = 1;
                }
                removePiece(y - 1, x - 1);
                posPieceEat[0] = y - 1;
                posPieceEat[1] = x - 1;
            }//questo per il giocatore se è una pedina
        } else {
            if (x < nextX) {//sta andando verso destra per il giocatore
                if (campo[y + 1][x + 1] instanceof Pedina) {
                    posPieceEat[2] = 0;
                } else {
                    posPieceEat[2] = 1;
                }
                removePiece(y + 1, x + 1);
                posPieceEat[0] = y + 1;
                posPieceEat[1] = x + 1;
            } else {
                if (campo[y + 1][x - 1] instanceof Pedina) {
                    posPieceEat[2] = 0;
                } else {
                    posPieceEat[2] = 1;
                }
                removePiece(y + 1, x - 1);
                posPieceEat[0] = y + 1;
                posPieceEat[1] = x - 1;
            }//per IA se è una pedina 
        }
        return posPieceEat;
    }

    /**
     * Prende le coordinate x y inserite prende il pezzo che si trova, se la
     * pedina e' nel lato opposot da cui e partita inserisce un damone nella
     * posizione.
     *
     * @param nextY Coordinata Y da verificare
     * @param nextX Coordinata X da verificare
     */
    private void becomeDamone(int nextY, int nextX) {
        if (getPezzo(nextY, nextX).getColore() == Color.BLACK && nextY == 7 && getPezzo(nextY, nextX) instanceof Pedina) {
            setPezzo(nextY, nextX, new Damone(getPezzo(nextY, nextX).getColore(), nextY, nextX, campo));
            sound.playSoundDamoneDARTHVADER();
            window.upgradeIcon();
            moveList.addDamoneList(nextY, nextX, true);
        } else if (getPezzo(nextY, nextX).getColore() == Color.WHITE && nextY == 0 && getPezzo(nextY, nextX) instanceof Pedina) {
            setPezzo(nextY, nextX, new Damone(getPezzo(nextY, nextX).getColore(), nextY, nextX, campo));
            sound.playSoundDamoneLUKESTARWARS();
            window.upgradeIcon();
            moveList.addDamoneList(nextY, nextX, false);
        }
    }

    /**
     * Scorre tutto il campo per vedere se ce una pedina del giocatore OB per
     * vedere se ce una pedina obblicaga a mangiare.
     *
     * @param ob
     * @return true pedina è oblicaga a mangiare
     */
    private boolean somePieceCanEat(Object ob) {
        for (Pezzi[] pezzis : campo) {
            for (Pezzi pezzi : pezzis) {
                if (pezzi != null && (ob instanceof EventWindow) && pezzi.getColore() == Color.WHITE && pezzi.canEat()) {
                    //segnalo che ce una pedina obligata a mangiare 
                    window.showPieceMustEat(pezzi.getY(), pezzi.getX());
                    JOptionPane.showMessageDialog(null, "il pezzo e obbligato a mangiare (" + pezzi.getX() + "," + pezzi.getY() + ")");
                    window.dontShowPieceMustEat(pezzi.getY(), pezzi.getX());
                    return true;
                } else if (pezzi != null && (ob instanceof AI) && pezzi.getColore() == Color.BLACK && pezzi.canEat()) {
                    System.out.println(" il pezzo e obbligato a mangiare (" + pezzi.getX() + "," + pezzi.getY() + ")");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Controlla se la pedena che hai selezionato e un pezzo che ti appartiene.
     *
     * @param y
     * @param x
     * @param ob
     * @return boolean true tua pedina false non è la tua pedina
     */
    private boolean isYourPiece(int y, int x, Object ob) {
        if (ob instanceof EventWindow && getPezzo(y, x).getColore() == Color.BLACK) {
            return false;
        } else if (ob instanceof AI && getPezzo(y, x).getColore() == Color.WHITE) {
            return false;
        }
        return true;
    }

    /**
     * Setta la dificolta facendo scegliere al giocatore.
     */
    public static void setDificult() {
        Object[] possibilities = {"Easy", "Normal", "Hard", "Chuck Norris"};
        String s = (String) JOptionPane.showInputDialog(
                null,
                "Choose your gameplay mode!",
                "Mode",
                JOptionPane.PLAIN_MESSAGE,
                null,//icona
                possibilities,
                "Normal");
        for (int i = 0; i < possibilities.length; i++) {
            if (s != null && s.compareTo(possibilities[i].toString()) == 0) {
                difficult = i;
                break;
            } else if (s == null) {// non è stato scelto il livello di difficolta uscita 
                System.exit(0);
            }
        }
    }

    /**
     * Resetto tutte le variabili per resettare la partita.
     */
    public static void resetGame() {
        removeAllPiece();
        createAllPiece();
        window.upgradeIcon();
        userTurn = true;
        setDificult();
        victory = false;
        moveList.resetList();
        moveListGraphics.updateMoveList();
        System.gc();
    }

    /**
     * Rimuove tutte le pedine dal campo.
     */
    private static void removeAllPiece() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                removePiece(i, j);
            }

        }
    }

    /**
     * |||||||||||||||||||||||||||||||||||||||||||||//////////////////////////////////
     *
     */
    public static void undo() {
        for (int i = 0; i < 2; i++) {
            int[] moveUndo = moveList.getLastMove();
            if (moveUndo != null) {
                if (moveUndo[2] == -1) { // e fatto un damone 
                    boolean turn = moveList.getTurn();
                    campo[moveUndo[0]][moveUndo[1]] = new Pedina(turn ? Color.WHITE : Color.BLACK, moveUndo[0], moveUndo[1], campo);
                    moveList.removeLastMove();
                } else if (moveUndo[4] == -1 && moveUndo[5] == -1) { // l'ultima mossa non è stata una mangiata

                    campo[moveUndo[0]][moveUndo[1]] = campo[moveUndo[2]][moveUndo[3]];
                    removePiece(moveUndo[2], moveUndo[3]);

                    campo[moveUndo[0]][moveUndo[1]].setPos(moveUndo[0], moveUndo[1]);
                    moveList.removeLastMove();

                } else { // l'ultima mossa è una mnagiata o una doppia mangiata
                    boolean turn = moveList.getTurn();
                    boolean momturn;
                    do {
                        if (moveUndo[6] == 0) { // se è 0 la pedina mangiata è una pedina
                            campo[moveUndo[4]][moveUndo[5]] = new Pedina(!turn ? Color.WHITE : Color.BLACK, moveUndo[4], moveUndo[5], campo);
                        } else { // altrimenti è un damone
                            campo[moveUndo[4]][moveUndo[5]] = new Damone(!turn ? Color.WHITE : Color.BLACK, moveUndo[4], moveUndo[5], campo);
                        }
                        campo[moveUndo[0]][moveUndo[1]] = campo[moveUndo[2]][moveUndo[3]];
                        campo[moveUndo[0]][moveUndo[1]].setPos(moveUndo[0], moveUndo[1]);
                        removePiece(moveUndo[2], moveUndo[3]);
                        moveList.removeLastMove();
                        moveUndo = moveList.getLastMove();
                        momturn = moveList.getTurn();
                        window.upgradeIcon();
                        moveListGraphics.updateMoveList();
                    } while ((momturn == turn) && moveUndo[4] != -1 && moveUndo[5] != -1);
                }
                window.upgradeIcon();
                moveListGraphics.updateMoveList();
            } else {
                break;
            }

        }
    }

    /**
     * Restituisce il campo per fare.
     *
     * @return il campo di gioco
     */
    public Pezzi[][] getCampo() {
        return campo;
    }

    public void chuckNorris() {
        victory = true;
        removeAllPiece();
        setPezzo(0, 0, new Pedina(Color.BLACK, 0, 0, campo));
    }

}
