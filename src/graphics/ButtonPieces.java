/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

/**
 * Classe deicata ai bottoni.
 * @author  Andrea Baggieri & Leonardo Zambaldo
 */
public class ButtonPieces extends JButton {

    /**
     * Ognuno di questi oggetti ImageIcon gestisce una icona montata sul bottone deciso.
     */
    private final ImageIcon iconWHITE = new ImageIcon(getClass().getResource("/images/pedinaLuke.gif"));    
    private final ImageIcon iconBLACK = new ImageIcon(getClass().getResource("/images/PedinaDARTVADER.gif"));
    private final ImageIcon iconSUPERBLACK = new ImageIcon(getClass().getResource("/images/DamoneDARTVADER.gif"));
    private final ImageIcon iconSUPERWHITE = new ImageIcon(getClass().getResource("/images/DamoneLuke.gif"));
    private final ImageIcon iconChuckNorris;

    /**
     * Coordiante del bottone.
     */
    int y, x;
    final EventWindow eventWindow;

    public ButtonPieces(Color colore, final int y, final int x, final EventWindow eventWindow, int chuckNorris) {
        this.y = y;
        this.x = x;
        this.eventWindow = eventWindow;
        setBackground(colore);
        setSize(100, 100);
        setBorder(new LineBorder(Color.RED, 4));
        setBorderPainted(false);
        iconChuckNorris = new ImageIcon(getClass().getResource("/chucknorris64/ChuckNorris_"+ (chuckNorris+1) +".gif"));

        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) { // nel caso che il bottone venga premuto

                eventWindow.getCoordinateGraphics(y, x); // si richiama il metodo della classe eventWindow che fornendogli le sue cordinate 
                // evidenzierà le posizioni possibili oppure sposterà la pedina / damone
            }
        });
    }

    /**
     * Questi metodi settano a seconda del colore l'icona giusta sopra il
     * pulsante il primo metodo per le pedine semplici il secondo metodo per le
     * pedine.
     *
     * @param color colore della pedina nel bottone
     */
    public void setIcon(Color color) {
        if (color != null && color == Color.WHITE || color == Color.BLACK) {
            setIcon(color == Color.WHITE ? iconWHITE : iconBLACK);
            setPressedIcon(color == Color.WHITE ? iconWHITE : iconBLACK);
        } else {
            super.setIcon(null);
        }
    }

    /**
     * Questi metodi settano a seconda del colore l'icona giusta sopra il
     * pulsante il primo metodo per le pedine semplici il secondo metodo per i
     * damoni
     *
     * @param color colore della pedina nel bottone
     */
    public void setSuperIcon(Color color) {
        if (color != null && color == Color.WHITE || color == Color.BLACK) {
            setIcon(color == Color.WHITE ? iconSUPERWHITE : iconSUPERBLACK);
            setPressedIcon(color == Color.WHITE ? iconSUPERWHITE : iconSUPERBLACK);
        } else {
            super.setIcon(null);
        }
    }

    public void setChuckNorris() {
        setIcon(iconChuckNorris);
        setPressedIcon(iconChuckNorris);
    }

}
