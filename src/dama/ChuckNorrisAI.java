/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dama;

import graphics.EventWindow;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author  Andrea Baggieri & Leonardo Zambaldo
 */
public class ChuckNorrisAI extends AI{

    /**
     * Collegamento diretto per la modifica del immagine.
     */
    private final EventWindow eWindow;

    public ChuckNorrisAI( Controllore control, EventWindow eWindow) {
        super(control);
        this.eWindow = eWindow;
    }


    @Override
    protected void givePriority() {
        try {
            sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ChuckNorrisAI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void moveAI() {
        //modificazione campo
        control.chuckNorris();
        control.nextPosition(0, 0, 1, 1, this);
        //attivazione immagine
        eWindow.chuckNorris();
        JOptionPane.showMessageDialog(null, "HAI PERSO!!!\n");
        JOptionPane.showMessageDialog(null, "Visto che hai sfidato Chuck Norris... finché lo vedi davanti a te puoi stare tranquillo ....\n quando chiuderai il programma potrebbe essere già troppo tardi.\n");
    }
    
}
