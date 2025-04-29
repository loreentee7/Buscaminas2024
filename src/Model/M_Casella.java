package Model;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author David
 */
public class M_Casella {

    private int nMines;
    private boolean esMina;
    private boolean descoberta;
    private boolean marcada;

    public M_Casella() {
        nMines = 0;
        esMina = false;
        descoberta = false;
        marcada = false;
    }

    public int getnMines() {
        return nMines;
    }

    public void setnMines(int nMines) {
        this.nMines = nMines;
    }

    public boolean getEsMina() {
        return esMina;
    }

    public void setEsMina(boolean esMina) {
        this.esMina = esMina;
    }

    public boolean getDescoberta() {
        return descoberta;
    }

    public void setDescoberta(boolean descoberta) {
        this.descoberta = descoberta;
    }

    public boolean getMarcada() {
        return marcada;
    }

    public void setMarcada(boolean marcada) {
        this.marcada = marcada;
    }

    @Override
    public String toString() {

        if (marcada) {
            ImageIcon minanegra = new ImageIcon("src/Imatges/banderaspain.png");
            Image img = minanegra.getImage();
            Image newImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            minanegra = new ImageIcon(newImg);
            
            return "⚑";
        } else if (!descoberta) {
            return ".";
        } else if (esMina) {
            ImageIcon minanegra = new ImageIcon("src/Imatges/minanegra.png");
            Image img = minanegra.getImage();
            Image newImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            minanegra = new ImageIcon(newImg);

            return "F";
        } else {
            return Integer.toString(nMines);
        }

    }

}
