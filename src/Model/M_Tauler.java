    package Model;

    import java.util.Random;

    /*
     @author David
     */
    public class M_Tauler {

        private static Random rd = new Random();

        public M_Casella t[][];

        public M_Tauler(int f, int c, int nm) {
            t = new M_Casella[f][c];
            for (int i = 0; i < t.length; i++) {
                for (int j = 0; j < t[i].length; j++) {
                    t[i][j] = new M_Casella();
                }
            }
            posaMines(nm);
            comptaMines();
        }

        public void posaMines(int nm) {

            int i, j;

            while (nm > 0) {
                i = rd.nextInt(t.length);
                j = rd.nextInt(t[i].length);
                if (!t[i][j].getEsMina()) {
                    t[i][j].setEsMina(true);
                    nm--;
                }
            }
        }

        public void comptaMines() {

            int nm;

            for (int i = 0; i < t.length; i++) {
                for (int j = 0; j < t[i].length; j++) {

                    nm = 0;
                    if (!t[i][j].getEsMina()) {
                        for (int k = i - 1; k <= i + 1; k++) {
                            for (int l = j - 1; l <= j + 1; l++) {
                                if (k >= 0 && k < t.length && l >= 0 && l < t[i].length) {
                                    if (t[k][l].getEsMina()) {
                                        nm++;
                                    }
                                }
                            }
                        }
                        t[i][j].setnMines(nm);
                    }
                }
            }
        }

        public boolean hiHaMina(int x, int y) {
            return t[x][y].getEsMina();
        }

        public void descobreixTauler() {
            for (int i = 0; i < t.length; i++) {
                for (int j = 0; j < t[i].length; j++) {
                    t[i][j].setDescoberta(true);
                }
            }
        }

        public boolean descobert() {

            for (int i = 0; i < t.length; i++) {
                for (int j = 0; j < t[i].length; j++) {
                    if (!t[i][j].getEsMina()) {
                        if (!t[i][j].getDescoberta()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        public void descobreixCasella(int x, int y) {

            if (!t[x][y].getDescoberta()) {

                t[x][y].setDescoberta(true);

                if (!t[x][y].getEsMina() && !t[x][y].getMarcada() && t[x][y].getnMines() == 0) {

                    for (int i = x - 1; i <= x + 1; i++) {
                        for (int j = y - 1; j <= y + 1; j++) {
                            if (i >= 0 && j >= 0 && i < t.length && j < t[i].length) {
                                descobreixCasella(i, j);
                            }
                        }
                    }
                }
            }

        }

        public boolean descoberta(int x, int y) {
            return t[x][y].getDescoberta();
        }

        public void marcaMina(int x, int y) {
            if (t[x][y].getMarcada()) {
                t[x][y].setMarcada(false);
            } else {
                t[x][y].setMarcada(true);
            }
        }

        public boolean minaMarcada(int x, int y) {
            return t[x][y].getMarcada();
        }

        @Override
        public String toString() {

            int a = 'a';

            //Primera línia
            System.out.print(" ");
            for (int i = 0; i < t.length; i++) {
                System.out.print((char) a);
                a++;
            }

            //Resta de línies
            a = 'a';
            System.out.println("");
            for (int i = 0; i < t.length; i++) {
                System.out.print((char) a);
                for (int j = 0; j < t[i].length; j++) {
                    System.out.print(t[i][j]);
                }
                System.out.println("");
                a++;
            }
            return "";
        }

    }
