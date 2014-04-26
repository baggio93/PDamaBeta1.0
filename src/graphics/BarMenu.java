/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import dama.Controllore;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author  Andrea Baggieri & Leonardo Zambaldo
 */
public class BarMenu extends MenuBar {

    private final Menu file = new Menu("Game");
    private final Menu edit = new Menu("Edit");
    private final Menu help = new Menu("Help");

    private final MenuItem setdifficult = new MenuItem("Mode"); // si crea la voce "mode" per il menu
    private final MenuItem newGame = new MenuItem("New Game");
    private final MenuItem soundActive = new MenuItem("Sound ON");
    private final MenuItem showMoveList = new MenuItem("Show/Hide Move List");
    private final MenuItem undo = new MenuItem("Undo");
    private final MenuItem about = new MenuItem("About");
    private final MenuItem rules = new MenuItem("Rules");

    private final moveListGraphics mlg = new moveListGraphics();

    public BarMenu() {
        addActionALL();
        file.add(newGame);
        file.add(setdifficult);
        edit.add(soundActive);
        edit.addSeparator();
        edit.add(undo);
        edit.add(showMoveList);
        help.add(rules);
        help.add(about);

        add(file);
        add(edit);
        add(help);
    }

    private void addActionALL() {

        rules.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mlg, "1: Vince chi mangia tutte le pedine o impedisce ogni movimento avversario\n"
                        + "2: Le pedine si muovono diagonalmente e unidirezionalmente\n"
                        + "3: Le pedine sono OBBLIGATE  a mangiare se la situazione lo permette\n"
                        + "4: Il DAMONE si crea quando una pedina raggiunge l'altro lato del campo\n"
                        + "5: Il DAMONE può muoversi in diagonale bidirezionalmente\n"
                        + "6: Il Damone mangia le pedine ma non può essere mangiato da esse\n", "Rules", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        about.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mlg,"                     Created by \n Andrea Baggieri & Leonardo Zambaldo", "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        undo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Controllore.undo();
            }
        });

        showMoveList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (mlg.getVisible()) {
                    mlg.setVisible(false);
                    mlg.setBoolVisibile(false);
                } else {
                    mlg.setVisible(true);
                    mlg.setBoolVisibile(true);
                }
            }
        });

        setdifficult.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Controllore.setDificult();
                JOptionPane.showMessageDialog(null, "ATTENZIONE! La partità continuerà con la nuova difficoltà impostata!");
            }
        });

        newGame.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Controllore.resetGame();
                // metodo nuovo gioco

            }
        });
        soundActive.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.setSoundONOFF();
                if (Sound.isActiveSound()) {
                    soundActive.setLabel("Sound ON");
                } else {
                    soundActive.setLabel("Sound OFF");
                }
            }
        });
    }

}
