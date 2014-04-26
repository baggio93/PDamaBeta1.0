/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import dama.Controllore;
import dama.Damone;
import dama.Pedina;
import dama.Pezzi;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.LineBorder;

/**
 *
 * @author  Andrea Baggieri & Leonardo Zambaldo
 */
public final class EventWindow extends Thread {

    /**
     * Gestore output.
     */
    private final Window window;
    /**
     * Campo grafico su cui lavoreranno i bottoni.
     */
    private final ButtonPieces[][] campoB = new ButtonPieces[8][8];
    /**
     * Indicatore se primo(true) o secondo(false) click.
     */
    private boolean firstPress = true;
    /**
     * Oggetto contenente un array di elementi della classe Pezzi.
     */
    private static Pezzi[][] campo;
    /**
     * Pezzo che si intede muovere.
     */
    private Pezzi pezzo = null;
    /**
     * Oggetto della classe controllore per fornire il campo.
     */
    private final Controllore control;

    public EventWindow(Controllore control) {
        this.control = control;
        this.campo = control.getCampo(); // inserisco il fittizzio campo del codice
        this.window = new Window();
        generateCampo(); // richiamo il metodo che genera il campo
        window.setVisible(true);
    }

    /**
     * Questo metodo si occupa di capire a quale click del muose e su quale
     * bottone agiamo se è la prima volta che si preme un bottone si devono
     * evidenziare SOLO le posizioni possibili da quel pezzo se invece abbiamo
     * già premuto un pezzo e il click è diretto ad una delle sue posizioni
     * possibili dobbiamo preoccuparci di spostare l'oggetto
     *
     * In ingresso vuole ovviamente le coordinate del bottone che richiede il
     * servizio
     *
     * @param y coordinata
     * @param x coordinata
     */
    public void getCoordinateGraphics(int y, int x) {

        if (firstPress && campo[y][x] != null && pezzo == null && campo[y][x].getColore() == Color.WHITE) { // premo per la prima volta una pedina/damone e controllo che non sia vuoto
            pezzo = campo[y][x];
            firstPress = !firstPress; // variabile booleana inizialmente settata a true e qui negata, se entro qui quello successivo non sarà più il primo click!
            Integer[] posPossible = campo[y][x].posPosible(); // creo il classico array che rimepio con le posizioni possibili dall'oggetto in y,x
            campoB[y][x].setBorderPainted(true);
            if (campo[y][x] instanceof Pedina) { //controllo se è una pedina l'oggetto su cui voglio agire
                if (posPossible[2] != null) {// se l'arrai alla posizione 2 è diverso da null ho sicuramente 2 posizioni possibili

                    campoB[posPossible[0]][posPossible[1]].setBorderPainted(true); // setto il bordo delle corrispondenti posizioni possibili su visibile così da poter visualizzare 
                    campoB[posPossible[2]][posPossible[3]].setBorderPainted(true); // anche nella parte grafica le mosse consentite a quel pezzo
                } else if (posPossible[0] != null) { // se in posizione 2 è nullo ma in 0 c'è qualcosa ho solo una posizione possibile
                    campoB[posPossible[0]][posPossible[1]].setBorderPainted(true); // una sola posizione da evidenziare
                }
            } else { // se non è pedina sarà sicuramente Damone
                if (posPossible[6] != null) { // in Damone sono possibili 4 posizioni quindi 8 coordinate, se alla pos 6 c'è qualcosa ho 4 possibili posizioni
                    campoB[posPossible[0]][posPossible[1]].setBorderPainted(true); // setto il bordo
                    campoB[posPossible[2]][posPossible[3]].setBorderPainted(true); // delle posizioni 
                    campoB[posPossible[4]][posPossible[5]].setBorderPainted(true); // possibili dai
                    campoB[posPossible[6]][posPossible[7]].setBorderPainted(true); // Damoni 
                } else if (posPossible[4] != null) { // se alla 6 non c'è niente ma alla 4 si, ho 3 posizioni possibili
                    campoB[posPossible[0]][posPossible[1]].setBorderPainted(true);
                    campoB[posPossible[2]][posPossible[3]].setBorderPainted(true);
                    campoB[posPossible[4]][posPossible[5]].setBorderPainted(true);
                } else if (posPossible[2] != null) { // se alla 6 e alla 4 non c'è niente ma alla 2 si ho 2 posizioni disponibili
                    campoB[posPossible[0]][posPossible[1]].setBorderPainted(true);
                    campoB[posPossible[2]][posPossible[3]].setBorderPainted(true);
                } else if (posPossible[0] != null) { // se non c'è nulla nelle altre ma nella 0 si ho solo 1 posizione disponibile
                    campoB[posPossible[0]][posPossible[1]].setBorderPainted(true);
                }
            }

        } else if (!firstPress && campo[y][x] != null && pezzo == campo[y][x] && campo[y][x].getColore() == Color.WHITE) {
            firstPress = true;
            Integer[] posPossible = pezzo.posPosible();
            campoB[pezzo.getY()][pezzo.getX()].setBorderPainted(false);
            if (pezzo instanceof Pedina) { // elimino la selezione delle posPossible precedenti
                if (posPossible[2] != null) { // stesso procedimento di prima ma in chiave diversa, in questo caso mi devo occupare di deselezionare le vecchie posizioni
                    campoB[posPossible[0]][posPossible[1]].setBorderPainted(false); // si rifà tutto il procedimento di prima all'inverso sia per le pedine che per i damoni 
                    campoB[posPossible[2]][posPossible[3]].setBorderPainted(false); // con una differenza dopo l'if
                } else if (posPossible[0] != null) {
                    campoB[posPossible[0]][posPossible[1]].setBorderPainted(false);
                }
            } else {
                if (posPossible[6] != null) { // stesso procedimento per i damoni
                    campoB[posPossible[0]][posPossible[1]].setBorderPainted(false);
                    campoB[posPossible[2]][posPossible[3]].setBorderPainted(false);
                    campoB[posPossible[4]][posPossible[5]].setBorderPainted(false);
                    campoB[posPossible[6]][posPossible[7]].setBorderPainted(false);
                } else if (posPossible[4] != null) {
                    campoB[posPossible[0]][posPossible[1]].setBorderPainted(false);
                    campoB[posPossible[2]][posPossible[3]].setBorderPainted(false);
                    campoB[posPossible[4]][posPossible[5]].setBorderPainted(false);
                } else if (posPossible[2] != null) {
                    campoB[posPossible[0]][posPossible[1]].setBorderPainted(false);
                    campoB[posPossible[2]][posPossible[3]].setBorderPainted(false);
                } else if (posPossible[0] != null) {
                    campoB[posPossible[0]][posPossible[1]].setBorderPainted(false);
                }
            }
            pezzo = null; // resetto l'oggetto pezzo
        } else if (!firstPress && campo[y][x] != null && pezzo != null && campo[y][x].getColore() == Color.WHITE) { // se dopo aver premuto una pedina seleziono un altra pedina
            firstPress = true; // resetto a true il boolean 
            Integer[] posPossible = pezzo.posPosible(); // preparo l'array per le pos possible
            campoB[pezzo.getY()][pezzo.getX()].setBorderPainted(false);
            if (pezzo instanceof Pedina) { // elimino la selezione delle posPossible precedenti
                if (posPossible[2] != null) { // stesso procedimento di prima ma in chiave diversa, in questo caso mi devo occupare di deselezionare le vecchie posizioni
                    campoB[posPossible[0]][posPossible[1]].setBorderPainted(false); // si rifà tutto il procedimento di prima all'inverso sia per le pedine che per i damoni 
                    campoB[posPossible[2]][posPossible[3]].setBorderPainted(false); // con una differenza dopo l'if
                } else if (posPossible[0] != null) {
                    campoB[posPossible[0]][posPossible[1]].setBorderPainted(false);
                }
            } else {
                System.out.println("lung" + posPossible.length);
                if (posPossible[6] != null) { // stesso procedimento per i damoni
                    campoB[posPossible[0]][posPossible[1]].setBorderPainted(false);
                    campoB[posPossible[2]][posPossible[3]].setBorderPainted(false);
                    campoB[posPossible[4]][posPossible[5]].setBorderPainted(false);
                    campoB[posPossible[6]][posPossible[7]].setBorderPainted(false);
                } else if (posPossible[4] != null) {
                    campoB[posPossible[0]][posPossible[1]].setBorderPainted(false);
                    campoB[posPossible[2]][posPossible[3]].setBorderPainted(false);
                    campoB[posPossible[4]][posPossible[5]].setBorderPainted(false);
                } else if (posPossible[2] != null) {
                    campoB[posPossible[0]][posPossible[1]].setBorderPainted(false);
                    campoB[posPossible[2]][posPossible[3]].setBorderPainted(false);
                } else if (posPossible[0] != null) {
                    campoB[posPossible[0]][posPossible[1]].setBorderPainted(false);
                }
            }
            pezzo = null; // resetto l'oggetto pezzo
            getCoordinateGraphics(y, x); // e richiamo il metodo getCoordinateGraphics per un nuovo controllo sulle coordinate appena ricevute (ricorsiva) 

        } else if (!firstPress && campo[y][x] == null) { // in questo caso invece sto premendo una posizone possibile dopo aver selezionato una pedina
            campoB[pezzo.getY()][pezzo.getX()].setBorderPainted(false);
            control.nextPosition(pezzo.getY(), pezzo.getX(), y, x, this); // richiamo il metodo nextPosition per spostare l'oggetto

            firstPress = true;  // resetto firstPress a true
            pezzo = null; // resetto l'oggetto pezzo
            generateCampo();
        }
    }

    /**
     * Questo metodo in pratica cancella tutto quello all'interno del Jpanel e
     * ricrea il campo di gioco grafico ogni volta basandosi sulla situazione
     * del fittizio campo (Array di Array su cui lavorano i metodi) che appunto
     * cambia in ogni posizione.
     *
     */
    private void generateCampo() {

        window.getContentPane().removeAll(); // rimuovo tutti gli oggetti dentro al jpanel quindi bottoni e tutto 
        boolean b = false; // necessario per stampare il colore del background dei bottoni che fa da sfondo scacchiera
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) { // doppio for per scorrere tutto il campo fittizio
                campoB[i][j] = new ButtonPieces((b) ? Color.GRAY : Color.WHITE, i, j, this, (i * 8 + j)); // ad ogni i,j genero un bottone che (dipendente da b) sarà grigio o bianco
                window.add(campoB[i][j]); // aggiungerà alla finestra il bottone e le sue proprietà
                if (control.getPezzo(i, j) instanceof Pedina) { // infine inserisco le icone sui bottoni, se sarà pedina userà il metodo che fornirà il bottone dell'icona pedina
                    campoB[i][j].setIcon(control.getPezzo(i, j) != null ? control.getPezzo(i, j).getColore() : null); // se alla posizione i,j c'è un oggetto, passo il colore al metodo setIcon che in base a quello mi darà l'icona giusta                   
                } else { // se invece sarà un damone lo fornirà dell'icona damone, valido sia per il giocatore che per il PC
                    campoB[i][j].setSuperIcon(control.getPezzo(i, j) != null ? control.getPezzo(i, j).getColore() : null); // metodo identico al precedente che però setta icone differenti se l'oggetto è un damone
                }

                b = (j != 7) ? !b : b; // inverte il boolean del colore del campo
            }
        }
        window.revalidate(); // Aggiorna la situazione grafica rivalutando il tutto, questo comando permette di visualizzare i cambiamenti
    }

    /**
     * Evidenzia il bottone della pedine che deve mangiare
     *
     * @param y coordinata
     * @param x coordinata
     */
    public void showPieceMustEat(int y, int x) {
        campoB[y][x].setBorder(new LineBorder(Color.GREEN, 4));
        campoB[y][x].setBorderPainted(true);
    }

    /**
     * Deselezione il bottone della pedina che deve mangiare
     *
     * @param y coordinata
     * @param x coordinata
     */
    public void dontShowPieceMustEat(int y, int x) {
        campoB[y][x].setBorderPainted(false);
        campoB[y][x].setBorder(new LineBorder(Color.RED, 4));
    }

    /**
     * Scorre tutto il campo e setta le il colore delle pedine nei bottoni.
     */
    public void upgradeIcon() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (control.getPezzo(i, j) != null && control.getPezzo(i, j) instanceof Pedina) {
                    campoB[i][j].setIcon(control.getPezzo(i, j).getColore());
                } else if (control.getPezzo(i, j) != null && control.getPezzo(i, j) instanceof Damone) {
                    campoB[i][j].setSuperIcon(control.getPezzo(i, j).getColore());
                } else {
                    campoB[i][j].setIcon(Color.PINK);
                }
            }
        }
    }

    /**
     * Chiama la funzione speciale dei bottoni.
     */
    public void chuckNorris() {
        int n = 0;
        Sound.mitra();
        for (ButtonPieces[] buttonPieceses : campoB) {
            for (ButtonPieces buttonPieces : buttonPieceses) {
                try {
                    sleep(32);
                } catch (InterruptedException ex) {
                    Logger.getLogger(EventWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
                buttonPieces.setChuckNorris();
            }
        }
        try {
            sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(EventWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        Sound.chuckRisata();
    }

}
