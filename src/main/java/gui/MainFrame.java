package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Mail Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Crear el menú
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opcions");

        // Crear las opciones del menú con iconos
        ImageIcon actualizarIcon = new ImageIcon("actualizar.png");
        Image actualizarImage = actualizarIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon actualizarScaledIcon = new ImageIcon(actualizarImage);

        JMenuItem actualizarItem = new JMenuItem("Actualitzar", actualizarScaledIcon);
        actualizarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lógica para actualizar correos

            }
        });

        ImageIcon escribirIcon = new ImageIcon("escribir.png");
        Image  escribirImage = escribirIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon escribirI = new ImageIcon(actualizarImage);
        JMenuItem escribirItem = new JMenuItem("Escriure", escribirI);
        escribirItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        // Agregar las opciones al menú
        menu.add(actualizarItem);
        menu.add(escribirItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

       
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
