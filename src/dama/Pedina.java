package dama;

import java.awt.Color;

/**
 *
 * @author  Andrea Baggieri & Leonardo Zambaldo
 */
public class Pedina extends Pezzi {

    public Pedina(Color colore, int y, int x, Pezzi[][] campo) {
        super(colore, y, x, campo);
    }

    /**
     * Restituisce a coppie di due le posizoni che puo muovere le pedine con
     * l'obbligo di di una direzione specifica se deve mangiare le coppie sono
     * [y,x,y,x]; vettore di 4 perche una pedina puo vare solo due movimnti
     * DX,SX se deve mangia la risposta e di [y,x,y,x] ed è costretto a fare
     * quella mossa.
     *
     * @return Arrei di Integer di 4 elementi
     */
    @Override
    public Integer[] posPosible() {
        Integer[] pos = new Integer[4];
        for (int i = 0; i < pos.length; i++) {
            pos[i] = null;

        }
        int i = 0;
        if (canEat()) {// cotrollo se puo mangiare
            if (canEat(y - 2, x - 2) && getColore() == Color.WHITE) {// puo mangiare in a sinistra
                pos[i] = y - 2;
                pos[i + 1] = x - 2;
                i += 2;
            }
            if (canEat(y - 2, x + 2) && getColore() == Color.WHITE) {//puo mangiare a destra
                pos[i] = y - 2;
                pos[i + 1] = x + 2;
                i += 2;
            }
            if (canEat(y + 2, x - 2) && getColore() == Color.BLACK) {// puo mangiare in a sinistra
                pos[i] = y + 2;
                pos[i + 1] = x - 2;
                i += 2;
            }
            if (canEat(y + 2, x + 2) && getColore() == Color.BLACK) {//puo mangiare a destra
                pos[i] = y + 2;
                pos[i + 1] = x + 2;
                i += 2;
            }
        } else if (canMove()) {
            if (canMove(y - 1, x - 1) && getColore() == Color.WHITE) {
                pos[i] = y - 1;
                pos[i + 1] = x - 1;
                i += 2;
            }
            if (canMove(y - 1, x + 1) && getColore() == Color.WHITE) {
                pos[i] = y - 1;
                pos[i + 1] = x + 1;
                i += 2;
            }
            if (canMove(y + 1, x - 1) && getColore() == Color.BLACK) {
                pos[i] = y + 1;
                pos[i + 1] = x - 1;
                i += 2;
            }
            if (canMove(y + 1, x + 1) && getColore() == Color.BLACK) {
                pos[i] = y + 1;
                pos[i + 1] = x + 1;
                i += 2;
            }
        }

        return pos;
    }

    /**
     * Controlla solo se puoi mangiare in una delle direzioni possibili della
     * pedina in base al colore
     *
     * @return si ce un caso che pioi mangiare , non puoi mangiare
     */
    @Override
    public boolean canEat() {
        if (getColore() == Color.WHITE) {// la pedian e bianca
            //controllo se almeno una delle due direzioni puo mangiare
            if (y - 2 >= 0) {
                if (x - 2 >= 0 && x + 2 <= 7) {
                    return (getCampo()[y - 2][x - 2] == null && getCampo()[y - 1][x - 1] != null && getCampo()[y - 1][x - 1].getColore() == Color.BLACK && (getCampo()[y - 1][x - 1] instanceof Pedina)) || (getCampo()[y - 2][x + 2] == null && getCampo()[y - 1][x + 1] != null && getCampo()[y - 1][x + 1].getColore() == Color.BLACK && (getCampo()[y - 1][x + 1] instanceof Pedina));
                } else if (x - 2 >= 0) {
                    return getCampo()[y - 2][x - 2] == null && getCampo()[y - 1][x - 1] != null && getCampo()[y - 1][x - 1].getColore() == Color.BLACK && (getCampo()[y - 1][x - 1] instanceof Pedina);
                } else if (x + 2 <= 7) {
                    return getCampo()[y - 2][x + 2] == null && getCampo()[y - 1][x + 1] != null && getCampo()[y - 1][x + 1].getColore() == Color.BLACK && (getCampo()[y - 1][x + 1] instanceof Pedina);
                }
            }
        } else {
            if (y + 2 <= 7) {
                if (x - 2 >= 0 && x + 2 <= 7) {
                    return (getCampo()[y + 2][x - 2] == null && getCampo()[y + 1][x - 1] != null && getCampo()[y + 1][x - 1].getColore() == Color.WHITE && (getCampo()[y + 1][x - 1] instanceof Pedina)) || (getCampo()[y + 2][x + 2] == null && getCampo()[y + 1][x + 1] != null && getCampo()[y + 1][x + 1].getColore() == Color.WHITE && (getCampo()[y + 1][x + 1] instanceof Pedina));
                } else if (x - 2 >= 0) {
                    return (getCampo()[y + 2][x - 2] == null && getCampo()[y + 1][x - 1] != null && getCampo()[y + 1][x - 1].getColore() == Color.WHITE && (getCampo()[y + 1][x - 1] instanceof Pedina));
                } else if (x + 2 <= 7) {
                    return (getCampo()[y + 2][x + 2] == null && getCampo()[y + 1][x + 1] != null && getCampo()[y + 1][x + 1].getColore() == Color.WHITE && (getCampo()[y + 1][x + 1] instanceof Pedina));
                }
            }
        }
        return false;

    }

    /**
     * Indica che la posizone inserita e effetivamente giusta per mangiare una
     * pedina aversaria.
     *
     * @param nextY Coordinata Y del movimento.
     * @param nextX Coordinata X del movimento.
     * @return Restituisce true se la posizione e' valida, altrimenti false.
     */
    @Override
    public boolean canEat(int nextY, int nextX) {
        if (canEat() && nextY >= 0 && nextY <= 7 && nextX >= 0 && nextX <= 7) {// controllo se puo manbaire 

            if (getColore() == Color.WHITE) {//trovo il colore nella pedina
                //non possono essere entrabe vere, ma masta che una sia vera la poszione e corretta
                return (y - 2 == nextY && x - 2 == nextX && getCampo()[nextY][nextX] == null && getCampo()[nextY + 1][nextX + 1] != null && getCampo()[nextY + 1][nextX + 1].getColore() == Color.BLACK && (getCampo()[y - 1][x - 1] instanceof Pedina))
                        || (y - 2 == nextY && x + 2 == nextX && getCampo()[nextY][nextX] == null && getCampo()[nextY + 1][nextX - 1] != null && getCampo()[nextY + 1][nextX - 1].getColore() == Color.BLACK && (getCampo()[y - 1][x + 1] instanceof Pedina));
            } else {
                return (y + 2 == nextY && x - 2 == nextX && getCampo()[nextY][nextX] == null && getCampo()[nextY - 1][nextX + 1] != null && getCampo()[nextY - 1][nextX + 1].getColore() == Color.WHITE && (getCampo()[y + 1][x - 1] instanceof Pedina))
                        || (y + 2 == nextY && x + 2 == nextX && getCampo()[nextY][nextX] == null && getCampo()[nextY - 1][nextX - 1] != null && getCampo()[nextY - 1][nextX - 1].getColore() == Color.WHITE && (getCampo()[y + 1][x + 1] instanceof Pedina));
            }
        } else {
            return false;
        }
    }

    /**
     * Controllo se la posizione inserita e possibile eseguirla.
     *
     * @param nextY Coordinata Y del movimento.
     * @param nextX Coordinata X del movimento.
     * @return Restituisce true se la se il movimento nelle nuove coordinate e
     * valida, altrimenti false.
     */
    @Override
    public boolean canMove(int nextY, int nextX) {
        if (nextY >= 0 && nextY <= 7 && nextX >= 0 && nextX <= 7) {
            if (getColore() == Color.WHITE) {
                return getCampo()[nextY][nextX] == null && y > nextY && y - 1 == nextY && (x - 1 == nextX || x + 1 == nextX);
            } else {
                return getCampo()[nextY][nextX] == null && y < nextY && y + 1 == nextY && (x - 1 == nextX || x + 1 == nextX);
            }
        } else {
            return false;
        }
    }

    /**
     * Controllo se le posizioni che puo moversi la pedina sono vuote.
     *
     * @return Restiusice true se la pedina puo fare almeno un movimento,
     * altrimenti false.
     */
    @Override
    public boolean canMove() {
        if (getColore() == Color.WHITE) {
            if (y - 1 >= 0) {
                if (x - 1 >= 0 && x + 1 <= 7) {
                    //non è un estremo DX/SX deve controllare DX-1/SX-1
                    return getCampo()[y - 1][x - 1] == null || getCampo()[y - 1][x + 1] == null;
                } else if (x - 1 >= 0) {
                    //estremo DX controllo solo SX-1
                    return getCampo()[y - 1][x - 1] == null;
                } else if (x + 1 <= 7) {
                    //estremo SX controllo solo DX-1
                    return getCampo()[y - 1][x + 1] == null;
                }
            }
        } else {
            if (y + 1 <= 7) {
                if (x - 1 >= 0 && x + 1 <= 7) {
                    //non è un estremo DX/SX deve controllare DX+1/SX+1
                    return getCampo()[y + 1][x - 1] == null || getCampo()[y + 1][x + 1] == null;
                } else if (x - 1 >= 0) {
                    //estremo DX controllo solo SX+1
                    return getCampo()[y + 1][x - 1] == null;
                } else if (x + 1 <= 7) {
                    //estremo DX controllo solo DX+1
                    return getCampo()[y + 1][x + 1] == null;
                }
            }
        }
        return false;
    }

    @Override
    public Pezzi createClone(Pezzi[][] campo){        
        return new Pedina(getColore(), getY(), getX(), campo);
        
    }
}
