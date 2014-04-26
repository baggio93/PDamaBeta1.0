/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author  Andrea Baggieri & Leonardo Zambaldo
 */
public class Sound {

    private final AudioClip sound;
    private final AudioClip soundDamoneLUKE;
    private final AudioClip soundDamoneVADERSTARWARS;
    private final AudioClip winSTARWARS;
    private final AudioClip looseSTARWARS;
    private static AudioClip chuckRisataA;
    private static AudioClip mistraA;
    private static AudioClip chuckNorrisA;
    private URL urlMovePiece = getClass().getResource("/sound/PieceSound.wav");
    private URL urlMovePieceDamoneLUKESTARWARS = getClass().getResource("/sound/DamoneLUKESKY.wav");
    private URL urlMovePieceDamoneVADERSTARWARS = getClass().getResource("/sound/DamoneDARTHVADER.wav");
    private URL winSW = getClass().getResource("/sound/winSW.wav");
    private URL looseSW = getClass().getResource("/sound/looseSW.wav");
    private final URL chuckRisata = getClass().getResource("/sound/ChuckNorrisRisata.wav");
    private final URL mistra = getClass().getResource("/sound/ChuckGun.wav");
    private final URL chuckNorris = getClass().getResource("/sound/ChuckNorris.wav");
    private static boolean activeSound = true;

    public Sound() {
        try {
            urlMovePiece = new URL(urlMovePiece.toString().replaceFirst("file:/", "file:///"));
            urlMovePieceDamoneLUKESTARWARS = new URL(urlMovePieceDamoneLUKESTARWARS.toString().replaceFirst("file:/", "file:///"));
            urlMovePieceDamoneVADERSTARWARS = new URL(urlMovePieceDamoneVADERSTARWARS.toString().replaceFirst("file:/", "file:///"));
            winSW = new URL(winSW.toString().replaceFirst("file:/", "file:///"));
            looseSW = new URL(looseSW.toString().replaceFirst("file:/", "file:///"));

        } catch (MalformedURLException ex) {
            Logger.getLogger(EventWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        sound = Applet.newAudioClip(urlMovePiece);
        soundDamoneLUKE = Applet.newAudioClip(urlMovePieceDamoneLUKESTARWARS);
        soundDamoneVADERSTARWARS = Applet.newAudioClip(urlMovePieceDamoneVADERSTARWARS);
        winSTARWARS = Applet.newAudioClip(winSW);
        looseSTARWARS = Applet.newAudioClip(looseSW);
        chuckRisataA = Applet.newAudioClip(chuckRisata);
        chuckNorrisA = Applet.newAudioClip(chuckNorris);
        mistraA = Applet.newAudioClip(mistra);
    }

    /**
     * Suono pedina.
     */
    public void playSoundMove() {
        if (activeSound) {
            sound.play();
        }
    }

    /**
     * Suono diventa damone biache.
     */
    public void playSoundDamoneLUKESTARWARS() {
        if (activeSound) {
            soundDamoneLUKE.play();
        }
    }

    /**
     * Suono diventa damone nere.
     */
    public void playSoundDamoneDARTHVADER() {
        if (activeSound) {
            soundDamoneVADERSTARWARS.play();
        }
    }

    /**
     * Suono partita vinta.
     */
    public void winSTARWARS() {
        if (activeSound) {
            winSTARWARS.play();
        }
    }

    /**
     * Suono partita persa.
     */
    public void looseSTARWARS() {
        if (activeSound) {
            looseSTARWARS.play();
        }
    }

    public static void chuckRisata() {
        if (activeSound) {
            chuckRisataA.play();
        }
    }
    
    public void chuckNorris() {
        if (activeSound) {
            chuckNorrisA.play();
        }
    }

    public static void mitra() {
        if (activeSound) {
            mistraA.play();
        }
    }

    /**
     * Attivazione disattivazione suono.
     */
    public static void setSoundONOFF() {
        activeSound = !activeSound;
    }

    /**
     * Restituisce lo stato del suono
     *
     * @return true attivo, false disattivo
     */
    public static boolean isActiveSound() {
        return activeSound;
    }

}
