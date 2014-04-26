package dama;

import java.awt.Color;

/**
 *
 * @author  Andrea Baggieri & Leonardo Zambaldo
 */
public class Damone extends Pezzi {

    public Damone(Color colore, int y, int x, Pezzi[][] campo) {
        super(colore, y, x, campo);
    }

    /**
     * Restituisce a coppie di due le posizoni che puo muovere le pedine con
     * l'obbligo di di una direzione specifica se deve mangiare le coppie sono
     * [y,x,y,x,y,x,y,x]; vettore di 8 perche una pedina puo vare solo due
     * movimnti DX,SX se deve mangia la risposta e di [y,x,y,x,y,x,y,x] ed è
     * costretto a fare quella mossa.
     *
     * @return Arrei di Integer di 8 elementi
     */
    @Override
    public Integer[] posPosible() {
        Integer[] pos = new Integer[8];
        for (int i = 0; i < pos.length; i++) {
            pos[i] = null;
        }
        int i = 0;
        if (canEat()) {
            //controllo se puoi mangiare
            if (canEat(y + 2, x - 2)) {
                pos[i] = y + 2;
                pos[i + 1] = x - 2;
                i += 2;
            }
            if (canEat(y + 2, x + 2)) {
                pos[i] = y + 2;
                pos[i + 1] = x + 2;
                i += 2;
            }
            if (canEat(y - 2, x - 2)) {
                pos[i] = y - 2;
                pos[i + 1] = x - 2;
                i += 2;
            }
            if (canEat(y - 2, x + 2)) {
                pos[i] = y - 2;
                pos[i + 1] = x + 2;
                i += 2;
            }
        } else if (canMove()) {
            if (canMove(y + 1, x - 1)) {
                pos[i] = y + 1;
                pos[i + 1] = x - 1;
                i += 2;
            }
            if (canMove(y + 1, x + 1)) {
                pos[i] = y + 1;
                pos[i + 1] = x + 1;
                i += 2;
            }
            if (canMove(y - 1, x - 1)) {
                pos[i] = y - 1;
                pos[i + 1] = x - 1;
                i += 2;
            }
            if (canMove(y - 1, x + 1)) {
                pos[i] = y - 1;
                pos[i + 1] = x + 1;
                i += 2;
            }
        }
        return pos;
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
        if (canEat() && nextY >= 0 && nextY <= 7 && nextX >= 0 && nextX <= 7) {
            if (y - 2 == nextY && x - 2 == nextX) {
                return getCampo()[nextY][nextX] == null && getCampo()[nextY + 1][nextX + 1] != null && getCampo()[nextY + 1][nextX + 1].getColore() != getColore();
            } else if (y + 2 == nextY && x - 2 == nextX) {
                return getCampo()[nextY][nextX] == null && getCampo()[nextY - 1][nextX + 1] != null && getCampo()[nextY - 1][nextX + 1].getColore() != getColore();
            } else if (y - 2 == nextY && x + 2 == nextX) {
                return getCampo()[nextY][nextX] == null && getCampo()[nextY + 1][nextX - 1] != null && getCampo()[nextY + 1][nextX - 1].getColore() != getColore();
            } else if (y + 2 == nextY && x + 2 == nextX) {
                return getCampo()[nextY][nextX] == null && getCampo()[nextY - 1][nextX - 1] != null && getCampo()[nextY - 1][nextX - 1].getColore() != getColore();
            }
        }
        return false;
    }

    /**
     * Controlla solo se puoi mangiare in una delle direzioni possibili della
     * damone
     *
     * @return si ce un caso che pioi mangiare , non puoi mangiare
     */
    @Override
    public boolean canEat() {
        if (y - 2 >= 0 && y + 2 <= 7) {
            if (x - 2 >= 0 && x + 2 <= 7) {
                //non è in un estremo deve controllare tutte le direzioni
                return getCampo()[y - 2][x - 2] == null && getCampo()[y - 1][x - 1] != null && getCampo()[y - 1][x - 1].getColore() != this.getColore()
                        || getCampo()[y - 2][x + 2] == null && getCampo()[y - 1][x + 1] != null && getCampo()[y - 1][x + 1].getColore() != this.getColore()
                        || getCampo()[y + 2][x - 2] == null && getCampo()[y + 1][x - 1] != null && getCampo()[y + 1][x - 1].getColore() != this.getColore()
                        || getCampo()[y + 2][x + 2] == null && getCampo()[y + 1][x + 1] != null && getCampo()[y + 1][x + 1].getColore() != this.getColore();
            } else if (x - 2 >= 0) {
                return getCampo()[y - 2][x - 2] == null && getCampo()[y - 1][x - 1] != null && getCampo()[y - 1][x - 1].getColore() != this.getColore()
                        || getCampo()[y + 2][x - 2] == null && getCampo()[y + 1][x - 1] != null && getCampo()[y + 1][x - 1].getColore() != this.getColore();
            } else if (x + 2 <= 7) {
                return getCampo()[y - 2][x + 2] == null && getCampo()[y - 1][x + 1] != null && getCampo()[y - 1][x + 1].getColore() != this.getColore()
                        || getCampo()[y + 2][x + 2] == null && getCampo()[y + 1][x + 1] != null && getCampo()[y + 1][x + 1].getColore() != this.getColore();
            }
        } else if (y - 2 >= 0) {
            if (x - 2 >= 0 && x + 2 <= 7) {

                return getCampo()[y - 2][x - 2] == null && getCampo()[y - 1][x - 1] != null && getCampo()[y - 1][x - 1].getColore() != this.getColore()
                        || getCampo()[y - 2][x + 2] == null && getCampo()[y - 1][x + 1] != null && getCampo()[y - 1][x + 1].getColore() != this.getColore();
            } else if (x - 2 >= 0) {
                return getCampo()[y - 2][x - 2] == null && getCampo()[y - 1][x - 1] != null && getCampo()[y - 1][x - 1].getColore() != this.getColore();
            } else if (x + 2 <= 7) {
                return getCampo()[y - 2][x + 2] == null && getCampo()[y - 1][x + 1] != null && getCampo()[y - 1][x + 1].getColore() != this.getColore();
            }
        } else if (y + 2 <= 7) {
            if (x - 2 >= 0 && x + 2 <= 7) {

                return getCampo()[y + 2][x - 2] == null && getCampo()[y + 1][x - 1] != null && getCampo()[y + 1][x - 1].getColore() != this.getColore()
                        || getCampo()[y + 2][x + 2] == null && getCampo()[y + 1][x + 1] != null && getCampo()[y + 1][x + 1].getColore() != this.getColore();
            } else if (x - 2 >= 0) {
                return getCampo()[y + 2][x - 2] == null && getCampo()[y + 1][x - 1] != null && getCampo()[y + 1][x - 1].getColore() != this.getColore();
            } else if (x + 2 <= 7) {
                return getCampo()[y + 2][x + 2] == null && getCampo()[y + 1][x + 1] != null && getCampo()[y + 1][x + 1].getColore() != this.getColore();
            }
        }
        return false;

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
            if (y > nextY) {
                //sta andatdo verso l'alto
                if (x > nextX) {
                    //sta andado verso SX
                    return y - 1 == nextY && x - 1 == nextX && getCampo()[nextY][nextX] == null;
                } else if (x < nextX) {
                    //sta andado verso DX
                    return y - 1 == nextY && x + 1 == nextX && getCampo()[nextY][nextX] == null;
                }
            } else if (y < nextY) {
                //sta andado verso il basso
                if (x > nextX) {
                    //sta andado verso SX 
                    return y + 1 == nextY && x - 1 == nextX && getCampo()[nextY][nextX] == null;
                } else if (x < nextX) {
                    //sta andado verso DX
                    return y + 1 == nextY && x + 1 == nextX && getCampo()[nextY][nextX] == null;
                }
            }
        }
        //fuori campo, nextpos = pos
        return false;

    }

    /**
     * Controllo se le posizioni che puo moversi la pedina sono vuote.
     *
     * @return Restiusice true se la pedina puo fare almeno un movimento,
     * altrimenti false.
     */
    @Override
    public boolean canMove() {
        if (y - 1 >= 0 && y + 1 <= 7) {
            if (x - 1 >= 0 && x + 1 <= 7) {
                //non è in un estremo deve controllare tutte le direzioni
                return getCampo()[y - 1][x - 1] == null || getCampo()[y - 1][x + 1] == null
                        || getCampo()[y + 1][x - 1] == null || getCampo()[y + 1][x + 1] == null;
            } else if (x - 1 >= 0) {
                return getCampo()[y - 1][x - 1] == null || getCampo()[y + 1][x - 1] == null;
            } else if (x + 1 <= 7) {
                return getCampo()[y - 1][x + 1] == null || getCampo()[y + 1][x + 1] == null;
            }
        } else if (y - 1 >= 0) {
            if (x - 1 >= 0 && x + 1 <= 7) {
                return getCampo()[y - 1][x - 1] == null || getCampo()[y - 1][x + 1] == null;
            } else if (x - 1 >= 0) {
                return getCampo()[y - 1][x - 1] == null;
            } else if (x + 1 <= 7) {
                return getCampo()[y - 1][x + 1] == null;
            }
        } else if (y + 1 <= 7) {
            if (x - 1 >= 0 && x + 1 <= 7) {
                return getCampo()[y + 1][x - 1] == null || getCampo()[y + 1][x + 1] == null;
            } else if (x - 1 >= 0) {
                return getCampo()[y + 1][x - 1] == null;
            } else if (x + 1 <= 7) {
                return getCampo()[y + 1][x + 1] == null;
            }
        }
        return false;

    }

    @Override
    public Pezzi createClone(Pezzi[][] campo){
        return new Damone(getColore(), getY(), getX(), campo);     
    }


}
