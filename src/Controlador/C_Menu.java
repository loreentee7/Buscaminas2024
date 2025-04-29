/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.V_Menu;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 *
 * @author lorente
 */
public class C_Menu implements ActionListener {

    V_Menu menu;
    C_Tauler tauler;

    public C_Menu() {

        menu = new V_Menu();
        tauler = new C_Tauler();

        creaciosubmenu();

        menu.getjButton_8x9().addActionListener(this);
        menu.getjButton_16x16().addActionListener(this);
        menu.getjButton_30x16().addActionListener(this);
        menu.getjButton_Personalitzat().addActionListener(this);

    }

    public void run() {

        menu.setVisible(true);
        menu.setTitle("Seleccio Dificultat");
        menu.setLocationRelativeTo(null);
        menu.pack();

    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        if (ae.getActionCommand().equals("Puntuacions")) { // Quan es pulsa sobre aquest boto
            mostrarPuntuaciones();
        }

        if (ae.getActionCommand().equals("Acerca de")) { // Quan es pulsa sobre aquest boto
            mostrarAcercaDe();

        }

        if (ae.getSource() == menu.getjButton_8x9()) { // Quan es pulsa sobre aquest boto

            int filas = 8;
            int columnas = 8;
            int mines = 10;
            tauler.run(filas, columnas, mines);

        }

        if (ae.getSource() == menu.getjButton_16x16()) { // Quan es pulsa sobre aquest boto

            int filas = 16;
            int columnas = 16;
            int mines = 40;
            tauler.run(filas, columnas, mines);

        }

        if (ae.getSource() == menu.getjButton_30x16()) { // Quan es pulsa sobre aquest boto

            int filas = 30;
            int columnas = 16;
            int mines = 99;
            tauler.run(filas, columnas, mines);

        }

        if (ae.getSource() == menu.getjButton_Personalitzat()) { // Quan es pulsa sobre aquest boto

            String filasStr = JOptionPane.showInputDialog(menu, "Introdueix Files: ");
            String columnasStr = JOptionPane.showInputDialog(menu, "Introdueix Columnes:");
            String Mines = JOptionPane.showInputDialog(menu, "Introdueix Mines: ");
            int mines = Integer.parseInt(Mines);

            int filas = Integer.parseInt(filasStr);
            int columnas = Integer.parseInt(columnasStr);
            int maxMines = filas * columnas;
            mines = Math.min(mines, maxMines);
            tauler.run(filas, columnas, mines);

        }

    }

    public void mostrarPuntuaciones() {
        
        // Carrega el file puntuacions.txt quan es pulsa el boto

        JFrame framePuntuaciones = new JFrame("Puntuaciones");
        framePuntuaciones.setLocationRelativeTo(null);
        framePuntuaciones.setSize(400, 300);
        framePuntuaciones.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea textAreaPuntuaciones = new JTextArea();
        textAreaPuntuaciones.setBackground(Color.DARK_GRAY);
        textAreaPuntuaciones.setForeground(Color.white);
        textAreaPuntuaciones.setEditable(false);
        textAreaPuntuaciones.setLineWrap(true);

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

        framePuntuaciones.add(new JScrollPane(textAreaPuntuaciones));
        framePuntuaciones.setVisible(true);

    }

    public void mostrarAcercaDe() {

        // Crearem un nou jframe que obrira quan es premi el botó
        JFrame frameAcercaDe = new JFrame("Acerca de");
        frameAcercaDe.setSize(300, 200);
        frameAcercaDe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Personalitzacio del jpanel
        JPanel panelAcercaDe = new JPanel();
        panelAcercaDe.setBackground(Color.DARK_GRAY);
        panelAcercaDe.setForeground(Color.white);
        frameAcercaDe.setBackground(Color.DARK_GRAY);
        
        // Amb aixo posem que el menu s'obri al centre de la pantalla i el text sigui centrat
        
        panelAcercaDe.setLayout(new BoxLayout(panelAcercaDe, BoxLayout.Y_AXIS));
        frameAcercaDe.setLocationRelativeTo(null);
        
        // Afegim el text al jlabel

        JLabel labelAcercaDe = new JLabel("<html><b>Creat per:</b> Edgar Lorente<br><b>Versió:</b> Beta_7.0.0<br><b>Any Creació:</b> 2023</html>");
        labelAcercaDe.setForeground(Color.white);
        labelAcercaDe.setHorizontalAlignment(SwingConstants.CENTER);

        // Personalitzem la font
        
        Font font = labelAcercaDe.getFont();
        labelAcercaDe.setFont(new Font(font.getName(), Font.PLAIN, 20));

        panelAcercaDe.add(Box.createVerticalStrut(10));
        panelAcercaDe.add(labelAcercaDe);
        frameAcercaDe.add(panelAcercaDe);
        frameAcercaDe.setVisible(true);
    }

    public void creaciosubmenu() {

        // Creació del submenu ···
        JMenuBar menuBar = new JMenuBar(); // Afegim un nou menuBar
        JMenu opcionesMenu = new JMenu("···"); // Al boto del menuBar afegim el text ···

        // Personalitzem el submenu
        opcionesMenu.setBackground(Color.DARK_GRAY);
        menuBar.setBackground(Color.DARK_GRAY);

        // Afegim un nou botó per poder visualitzar les puntuacions
        JMenuItem resultadosItem = new JMenuItem("Puntuacions");
        resultadosItem.addActionListener(this);
        opcionesMenu.add(resultadosItem);

        // Afegim un nou botó per poder visualitzar qui a fet el programa
        JMenuItem Acercade = new JMenuItem("Acerca de");
        Acercade.addActionListener(this);
        opcionesMenu.add(Acercade);

        // Afegim que el menu sigui orizontal
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(opcionesMenu);

        // Personalitzem botos, i fons del menu
        menuBar.setBackground(Color.DARK_GRAY);
        opcionesMenu.setBackground(Color.DARK_GRAY);
        Acercade.setBackground(Color.DARK_GRAY);
        resultadosItem.setBackground(Color.DARK_GRAY);
        opcionesMenu.setForeground(Color.white);
        Acercade.setForeground(Color.white);
        resultadosItem.setForeground(Color.white);

        menu.setJMenuBar(menuBar);

    }

}
