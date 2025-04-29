/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Model.M_Tauler;
import Model.RoundBorder;
import Vista.V_Tauler;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

/**
 *
 * @author lorente
 */
public class C_Tauler extends MouseAdapter {

    // creació de variables
    V_Tauler vistatauler;
    JButton[][] taulerBotons;
    M_Tauler mtauler;
    Timer timer;
    int contadorBanderas = 0;
    int minas = 0;
    int filas = 0;
    int columnas = 0;
    String nombreJugador;
    boolean perdido = false;
    boolean enPausa = false;
    boolean hasperdido = false;
    int fontSize = 50;

    public void run(int filas, int columnas, int minas) {

        vistatauler = new V_Tauler();
        vistatauler.setVisible(true);
        vistatauler.setTitle("Joc");
        vistatauler.setLocationRelativeTo(null);
        vistatauler.getjButton_Sortir().setVisible(false);

        iniciarTemporizador();

        this.minas = minas;
        this.filas = filas;
        this.columnas = columnas;

        // icona de les banderes totals
        ImageIcon iconbandera = new ImageIcon("src/Imatges/bandera.jpg");
        vistatauler.getjLabel1().setIcon(iconbandera);

        // icona del temps
        ImageIcon icontemps = new ImageIcon("src/Imatges/temps.png");
        vistatauler.getjLabel2().setIcon(icontemps);

        vistatauler.getjButton_Puntu().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // obrira el metode per que carregui el fitxer
                mostrarPuntuaciones();
            }
        });

        vistatauler.getjButton_Diff().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // el dispose torna a obrir l'anterior vista es a dir V_Menu i para el timer
                vistatauler.dispose();
                timer.stop();

            }
        });

        vistatauler.getjButton_Pause().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                enPausa = !enPausa;

                for (int i = 0; i < filas; i++) {
                    for (int j = 0; j < columnas; j++) {
                        taulerBotons[i][j].setEnabled(!enPausa);
                    }
                }

                vistatauler.getjButton_Diff().setEnabled(!enPausa);
                if (enPausa) {
                    vistatauler.getjButton_Pause().setText("Resume");
                    timer.stop();
                } else {
                    vistatauler.getjButton_Pause().setText("Pausar");
                    timer.start();
                }
            }
        });

        vistatauler.getjButton_Restart().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // si pulsem sobre Restart començara de nou el joc
                reiniciarJuego();
            }
        });

        vistatauler.getjButton_Sortir().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // tanca el programa
                System.exit(0);
            }
        });

        vistatauler.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Reinicia el temporitzador cada vegada que es tenca la finestra
                reiniciarTemporizador();
                perdido = false;
            }
        });

        if (!perdido) {
            // iniciarTemporizador();
        }

        mtauler = new M_Tauler(filas, columnas, minas);
        creartauler(filas, columnas, minas);

    }

    public void creartauler(int filas, int columnas, int minas) {

        // creació del tauler.
        taulerBotons = new JButton[filas][columnas];
        vistatauler.getPanel_tauler().setLayout(new GridLayout(filas, columnas));

        // recorrer les files i columnes seleccionades, i personalitzem el tauler i botons
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                taulerBotons[i][j] = new JButton();
                taulerBotons[i][j].addMouseListener(this);
                vistatauler.getPanel_tauler().add(taulerBotons[i][j]);
                taulerBotons[i][j].setBackground(Color.LIGHT_GRAY);
                taulerBotons[i][j].setBorder(new RoundBorder(10));
                taulerBotons[i][j].setForeground(Color.BLACK);

            }
        }

        // posa les mines que se li pasen, i tambe posa les mines totals
        String minasStr = Integer.toString(minas);
        vistatauler.getJlabel_minas().setText("/ " + minasStr);

    }

    public void update(JButton button[][], M_Tauler mtauler) {

        // definim els colors de cada numero de buscamines
        Color colorCero = Color.GRAY;
        Color colorUno = Color.decode("#ddfac3");
        Color colorDos = Color.decode("#ecedbf");
        Color colorTres = Color.decode("#eddab4");
        Color colorCuatro = Color.decode("#edc38a");
        Color colorCinco = Color.decode("#f7a1a2");

        // carreguem la imatge de la mina 
        ImageIcon minavermella = new ImageIcon("src/Imatges/minaroja.png");
        Image img = minavermella.getImage();
        Image newImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        minavermella = new ImageIcon(newImg);

        // carreguem la imatge de la bandera
        ImageIcon banderaspain = new ImageIcon("src/Imatges/banderaspain.png");
        Image img2 = banderaspain.getImage();
        Image newImg2 = img2.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        banderaspain = new ImageIcon(newImg2);

        // recorrem l'array de botons, per si treus un 0 fer el metode de descobrir tauler
        for (int i = 0; i < taulerBotons.length; i++) {
            for (int j = 0; j < taulerBotons[i].length; j++) {

                // seleccionem mida, font, i tipus de fons dels numeros
                taulerBotons[i][j].setFont(new Font("Arial", Font.BOLD, fontSize));

                // decobreix les caselles y les pinta
                if (mtauler.descoberta(i, j)) {
                    button[i][j].setText("" + mtauler.t[i][j]);

                    if (taulerBotons[i][j].getText().equals("0")) {
                        taulerBotons[i][j].setText("");
                        taulerBotons[i][j].setBackground(colorCero);
                    } else if (taulerBotons[i][j].getText().equals("1")) {
                        taulerBotons[i][j].setBackground(colorUno);
                    } else if (taulerBotons[i][j].getText().equals("2")) {
                        taulerBotons[i][j].setBackground(colorDos);
                    } else if (taulerBotons[i][j].getText().equals("3")) {
                        taulerBotons[i][j].setBackground(colorTres);
                    } else if (taulerBotons[i][j].getText().equals("4")) {
                        taulerBotons[i][j].setBackground(colorCuatro);
                    } else if (taulerBotons[i][j].getText().equals("5")) {
                        taulerBotons[i][j].setBackground(colorCinco);
                    } else if (taulerBotons[i][j].getText().equals("F")) { // Si troba una F, la remplaza per un text buit i posa l'imatge de la mina
                        taulerBotons[i][j].setIcon(minavermella);
                        taulerBotons[i][j].setBackground(Color.red);
                        taulerBotons[i][j].setText("");

                    } else if (taulerBotons[i][j].getText().equals("⚑")) {
                        taulerBotons[i][j].setIcon(banderaspain); // aixo no funciona era una proba JAJAJAJ

                    }

                }

            }

        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (hasperdido || enPausa) { // Si el joc es pausa o has perdut, queda totalment inutlitzable
            return;
        }

        JButton botonClicado = (JButton) e.getSource();

        int x = coordenadax(botonClicado);
        int y = coordenaday(botonClicado);

        ImageIcon banderaspain = new ImageIcon("src/Imatges/banderaspain.png");
        Image img2 = banderaspain.getImage();
        Image newImg2 = img2.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        banderaspain = new ImageIcon(newImg2);

        if (e.getButton() == MouseEvent.BUTTON3) { // Representa que utilitzes el botó dret
            if (!mtauler.descoberta(x, y)) {
                if (!botonClicado.getText().equals("⚑")) {
                    botonClicado.setText("⚑");
                    contadorBanderas++;
                    verificarVictoria();

                    // Perderas si tens mes banderes que mines posades
                    if (contadorBanderas > minas) { // Posa bandera amb el boto dret
                        JOptionPane.showMessageDialog(null, "Has puesto más banderas que minas!", "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
                        hasperdido = true;

                        // si perds tot el tauler queda inutilitzable
                        for (int i = 0; i < taulerBotons.length; i++) {
                            for (int j = 0; j < taulerBotons.length; j++) {
                                taulerBotons[i][j].setEnabled(false);
                            }
                        }
                        // si perds tot el tauler queda inutilitzable
                        for (int i = 0; i < filas; i++) {
                            for (int j = 0; j < columnas; j++) {
                                taulerBotons[i][j].setEnabled(false);
                            }
                        }
                        // si perds tot el tauler es descobrira, els botons pause i diff queden inutlitzats, i el boto Sortir, es mostra per tancar el programa
                        mtauler.descobreixTauler();
                        vistatauler.getjButton_Pause().setEnabled(false);
                        vistatauler.getjButton_Diff().setEnabled(false);
                        vistatauler.getjButton_Sortir().setVisible(true);
                        perdido = true;
                        detenerTemporizador();
                    }
                } else { // si tornes a polsar botó dret es treura la bandera
                    botonClicado.setText(""); // Eliminar la bandera
                    contadorBanderas--;
                    verificarVictoria();
                }
            }
        } else { // I aqui detecta que estas utilitzan el botó esquerre

            mtauler.descobreixCasella(x, y);
            botonClicado.setText("" + mtauler.t[x][y]);

            ImageIcon minanegra = new ImageIcon("src/Imatges/minaroja.png");
            Image img = minanegra.getImage();
            Image newImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            minanegra = new ImageIcon(newImg);

            if (taulerBotons[x][y].getText().equals("0")) {
                botonClicado.setBackground(Color.GRAY);
            } else if (taulerBotons[x][y].getText().equals("1")) {
                botonClicado.setBackground(Color.GREEN);
            } else if (taulerBotons[x][y].getText().equals("2")) {
                botonClicado.setBackground(Color.YELLOW);
            } else if (taulerBotons[x][y].getText().equals("3")) {
                botonClicado.setBackground(Color.ORANGE);
            } else if (taulerBotons[x][y].getText().equals("4")) {
                botonClicado.setBackground(Color.MAGENTA);
            } else if (taulerBotons[x][y].getText().equals("F")) {
                botonClicado.setIcon(minanegra);
                botonClicado.setText("");

            }

            verificarVictoria();

        }

        if (mtauler.hiHaMina(x, y) && !botonClicado.getText().equals("⚑")) { // si clickas al boto i no hi bandera y hi a mina perds
            JOptionPane.showMessageDialog(null, "BOOOM!.", "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
            hasperdido = true;

            botonClicado.setText("\uD83D\uDCA3");
            for (int i = 0; i < taulerBotons.length; i++) {
                for (int j = 0; j < taulerBotons.length; j++) {
                    taulerBotons[i][j].setEnabled(false);
                }
            }

            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    taulerBotons[i][j].setEnabled(false);
                }
            }
            mtauler.descobreixTauler();
            taulerBotons[x][y].setEnabled(false);
            vistatauler.getjButton_Pause().setEnabled(false);
            vistatauler.getjButton_Diff().setEnabled(false);
            vistatauler.getjButton_Sortir().setVisible(true);
            perdido = true;
            detenerTemporizador();

        }

        String minasposades = Integer.toString(contadorBanderas); // Posa el comptador de banderes totals = mines totals
        vistatauler.getJlabel_contador().setText(minasposades);
        update(taulerBotons, mtauler);

    }

    int coordenadax(JButton botonClicado) {
        for (int fila = 0; fila < taulerBotons.length; fila++) {
            for (int columna = 0; columna < taulerBotons[fila].length; columna++) {
                if (taulerBotons[fila][columna] == botonClicado) {
                    return fila;
                }
            }
        }
        return -1;
    } // Agafar les coordenades x d'on seleccionem amb el ratolí

    int coordenaday(JButton botonClicado) {
        for (int fila = 0; fila < taulerBotons.length; fila++) {
            for (int columna = 0; columna < taulerBotons[fila].length; columna++) {
                if (taulerBotons[fila][columna] == botonClicado) {
                    return columna;
                }
            }
        }
        return -1;
    } // Agafar les coordenades y d'on seleccionem amb el ratolí

    public void verificarVictoria() {
        boolean ganado = true;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (!mtauler.descoberta(i, j) && !mtauler.hiHaMina(i, j)) {
                    ganado = false;
                    taulerBotons[i][j].setEnabled(false); // si guanyat es false els botons queden inutlitzats
                    break;
                }
            }
        }

        if (ganado && perdido == false) {

            // si guanyes et demana nom i es guarda en el metodo guardarPuntuacion
            nombreJugador = JOptionPane.showInputDialog(vistatauler, "¡Has ganado! Ingresa tu nombre:");
            guardarPuntuacion();

            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    taulerBotons[i][j].setEnabled(false);
                }
            }

            // surt del programa
            System.exit(0);
        }
    }

    public void guardarPuntuacion() {
        try {
            // quan guanyes se li pasa el nomdeljugador i el temps y l'escriu al fitxer puntuacions.txt
            FileWriter writer = new FileWriter("puntuacions.txt", true);
            writer.write(nombreJugador + " - " + vistatauler.getjLabel_temps().getText() + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mostrarPuntuaciones() {

        // crea un jframe que contindra un jtextarea on es mostrara les puntuacions
        JFrame framePuntuaciones = new JFrame("Puntuaciones");

        framePuntuaciones.setSize(400, 300);
        framePuntuaciones.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // personalitzem el jtextarea perque se asemeje al programa
        JTextArea textAreaPuntuaciones = new JTextArea();
        textAreaPuntuaciones.setBackground(Color.DARK_GRAY);
        textAreaPuntuaciones.setForeground(Color.white);
        framePuntuaciones.setLocationRelativeTo(null);
        textAreaPuntuaciones.setEditable(false);
        textAreaPuntuaciones.setLineWrap(true);

        // recorrem el fitxer i mostrem les puntuacions
        try {
            BufferedReader reader = new BufferedReader(new FileReader("puntuacions.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                textAreaPuntuaciones.append(line + "\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(textAreaPuntuaciones);

        framePuntuaciones.add(scrollPane);

        framePuntuaciones.setVisible(true);
    }

    public void iniciarTemporizador() {

        // definim un nou timer que comenci quan li pasem aquest metode
        timer = new Timer(1000, new ActionListener() {
            int segundos = 0;
            int minutos = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                segundos++;
                if (segundos == 60) { // si segons arriba a 60, es suma 1 a minuts i segons torna a 0
                    minutos++;
                    segundos = 0;
                }
                vistatauler.getjLabel_temps().setText(String.format("%02d:%02d", minutos, segundos)); // definim format
            }
        });

        timer.start();
    }

    public void detenerTemporizador() {

        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    public void reiniciarTemporizador() {

        // detindra el temporitzador i el reiniciara posantli 00:00 a minuts i segons
        detenerTemporizador();

        vistatauler.getjLabel_temps().setText("00:00");
    }

    public void reiniciarJuego() {

        // detindra el temporitzador, el posara a 0
        reiniciarTemporizador();

        // aplicara a totes les variables a default
        contadorBanderas = 0;
        perdido = false;
        enPausa = false;
        hasperdido = false;

        vistatauler.getjButton_Sortir().setVisible(false);

        vistatauler.getPanel_tauler().removeAll();

        // creara un nou tauler, amb una nova generació
        mtauler = new M_Tauler(filas, columnas, minas);
        creartauler(filas, columnas, minas);

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                taulerBotons[i][j].setEnabled(true);
            }
        }

        // torna els botos utilitzables y posa el temps en Pausar per defecte
        vistatauler.getjButton_Diff().setEnabled(true);
        vistatauler.getjButton_Pause().setEnabled(true);
        vistatauler.getjButton_Pause().setText("Pausar");

        // els contadors tornen a 0
        contadorBanderas = 0;
        vistatauler.getJlabel_contador().setText("0");
        vistatauler.repaint();

        // i comença de nou el temporitzador
        iniciarTemporizador();

    }

}
