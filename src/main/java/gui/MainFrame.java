package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.awt.HeadlessException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import logicaMail.Conexion;
import logicaMail.ConexionEmails;

public class MainFrame extends JFrame {

    public MainFrame(ConexionEmails c) {
        
        
        setTitle("Mail Application");
        setSize(400, 300);
        setLocationRelativeTo(null);

        //ConexionEmails c = new ConexionEmails(user, password);
        
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
               
               c.downloadEmails();
                
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
    
    private void crearTablaMensajes(List<Message> mensajes) throws MessagingException {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("From");

    for (Message mensaje : mensajes) {
        Address[] fromAddresses = mensaje.getFrom();
        if (fromAddresses != null && fromAddresses.length > 0) {
            String from = fromAddresses[0].toString();
            model.addRow(new Object[]{from});
        }
    }

    JTable tabla = new JTable(model);
    this.add(new JScrollPane(tabla), BorderLayout.CENTER);
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame(new ConexionEmails("@", "addsf")).setVisible(true);
            }
        });
    }
}
