
package dama;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author  Andrea Baggieri & Leonardo Zambaldo
 */
public abstract class AI extends Thread {

    protected Controllore control;
    /**
     * campo dove verrano fatte le prove.
     */
    protected Pezzi copyCampo[][] = new Pezzi[8][8];

    /**
     * -1 la pedina non puo fare nulla case 1 posso muovere 3 obbligato a
     * mangiare.
     */
    protected int priorityCampo[][] = new int[8][8];

    public AI(Controllore control) {
        this.control = control;
    }

    /**
     * lancia le varie routin.
     */
    @Override
    public void run() {
        try {
            sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(AI.class.getName()).log(Level.SEVERE, null, ex);
        }
        long n = System.currentTimeMillis();
        copyCampo();
        givePriority();
        moveAI();
        System.out.println(System.currentTimeMillis() - n + "ms");
    }

    /**
     * Questo semplice metodo void si occupa di fare una esatta copia del campo
     * di gioco nella attuale situazione.
     */
    private void copyCampo() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (control.getPezzo(i, j) != null) {
                    copyCampo[i][j] = control.getPezzo(i, j).createClone(copyCampo);
                } else {
                    copyCampo[i][j] = null;
                }
            }
        }
    }

    /**
     * La nostra idea è quella di gestire il campo in base alle priorità che
     * ogni spazio ha; assegnate tramite questo metodo, le priorità si
     * distinguono in -1 (non posso muovermi, dato sia alle pedine che non
     * possono muoversi e alle caselle vuote o occupate da pedine avversarie) 1
     * (posso muovere, ancora da definire in parte) 3 (posso mangiare,
     * sfruttando il metodo canEat col campo appena copiato) quindi il 3 sarà il
     * primo controllo poichè siamo OBBLIGATI a mangiare se ce n'è la
     * possibilità!.

     */
    protected abstract void givePriority();

    /**
     * dopo aver calcolato la priorita di tutte le pedina, vado a sciegliere 
     * quella che mi conviene di piu e faccio il movimento.
     */
    protected abstract void moveAI();


    protected String stampaCampo() {
        String campoS = "- 0 1 2 3 4 5 6 7 X\n -----------------\n";
        int i = 0;
        for (int[] ria : priorityCampo) {
            campoS += i++;
            for (int j : ria) {
                campoS += "|" + (j == Integer.MIN_VALUE ? "_" : j);
            }

            campoS += "|\n -----------------\n";
        }
        return campoS;
    }

}
