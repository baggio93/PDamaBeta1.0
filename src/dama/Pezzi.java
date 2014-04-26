package dama;

import java.awt.Color;

/**
 *
 * @author  Andrea Baggieri & Leonardo Zambaldo
 */
public abstract class Pezzi {

    /**
     * Colore della pezzo.
     */
    private Color colore;
    /**
     * Coordinata Y del pezzo.
     */
    protected int y;
    /**
     * Coordinata X del pezzo.
     */
    protected int x;
    /**
     * Collegamento a campo per fare i cari controlli.
     */
    private Pezzi[][] campo;

    public Pezzi(Color colore, int y, int x, Pezzi[][] campo) {
        this.colore = colore;
        this.y = y;
        this.x = x;
        this.campo = campo;
    }

    /**
     * setta x e y in una volta sola
     *
     * @param y Coordinata Y del pezzo.
     * @param x Coordinata Y del pezzo.
     */
    public void setPos(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Color getColore() {
        return colore;
    }

    public Pezzi[][] getCampo() {
        return campo;
    }

    /**
     * In base base al tipo di pezzo che veine richiamato restituisce un vettore
     * che indica le coordinate delle possibili mosse.
     *
     * @return Vettore Integer lungo 4 o 8.
     */
    public abstract Integer[] posPosible();

    /**
     * Controlla se la nuove coordinate sono valide per mangiare una pedina
     * avversaria.
     *
     * @param nextY Coordinata Y del movimento.
     * @param nextX Coordinata X del movimento.
     * @return Restituisce true se e' possibile altrimenti false.
     */
    public abstract boolean canEat(int nextY, int nextX);

    /**
     * Controlla solo se puoi mangiare in una delle direzioni possibili del
     * pezzo.
     *
     * @return si ce un caso che pioi mangiare , non puoi mangiare
     */
    public abstract boolean canEat();

    /**
     * Indica che la posizone inserita e effetivamente giusta per mangiare una
     * pedina aversaria.
     *
     * @param nextY Coordinata Y del movimento.
     * @param nextX Coordinata X del movimento.
     * @return Restituisce true se la posizione e' valida, altrimenti false.
     */
    public abstract boolean canMove(int nextY, int nextX);

    /**
     * Controlla se i una delle direzioni possibile in base al pezzo e al colore
     * se e' possibile mouoversi
     *
     * @return Restituisce true se e' possibile altrimenti false
     */
    public abstract boolean canMove();
    
    
    /**
     * Crea il clone del pezzo indicato e lo restituisce.
     * @param campo indica di quale campo fa parte
     * @return copia indentica del pezzo
     */
    public abstract Pezzi createClone(Pezzi[][] campo);
        
    public void invertColor(){
        if(colore==Color.BLACK){
            colore=Color.WHITE;
        }else{
            colore=Color.BLACK;
        }
    }

    @Override
    public String toString() {
        return this.getClass().toString() +"(" +y+","+x+ ")" + colore.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
