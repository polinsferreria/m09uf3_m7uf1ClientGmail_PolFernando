package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.HeadlessException;
import javax.mail.Transport;
import javax.swing.JFrame;
import logicaMail.Conexion;

public class MainFrame extends JFrame {

    public MainFrame(Conexion c) {
        setTitle("Mail Application");
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
        Image escribirImage = escribirIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
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
                new MainFrame(new Conexion("s","s")).setVisible(true);
            }
        });
    }
}
