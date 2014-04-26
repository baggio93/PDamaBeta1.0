/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Classe interamente dedicata alla gestione della finestra ove compaiono tutti
 * i bottoni
 *
 * @author  Andrea Baggieri & Leonardo Zambaldo
 */
public class Window extends JFrame {

    public Window() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/pedina.gif"));
        setIconImage(icon.getImage());
        setTitle("CHECKERS TITANS!");
        setSize(900, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8, 5, 5));
        setResizable(true);
        getContentPane().setBackground(Color.BLACK);
        setMenuBar(new BarMenu());
    }
}
