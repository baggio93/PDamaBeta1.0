/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package graphics;

import dama.MoveList;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author  Andrea Baggieri & Leonardo Zambaldo
 */
public class moveListGraphics extends JFrame{
    
    private static final JTextArea JML = new JTextArea();
    private boolean visible = false;
    private static JScrollPane scrol;
    
    public moveListGraphics(){
        scrol=new JScrollPane(JML);
        scrol.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JML.setEditable(false);
        setTitle("List of Move");
        setSize(250, 500);
        setLayout(new GridLayout(1,1));
        setResizable(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        
        add(scrol);
         
        setVisible(false);
    }
    
    public boolean getVisible(){
        return visible; 
    }
    
    public void setBoolVisibile(boolean b){
        visible = b; 
    }
    
    
    public static void updateMoveList(){
        JML.setText(MoveList.tostring());
        scrol.getVerticalScrollBar().setValue(0);
    }
}
