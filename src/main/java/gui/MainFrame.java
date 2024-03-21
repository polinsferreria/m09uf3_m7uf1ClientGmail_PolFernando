package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.awt.HeadlessException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import logicaMail.Conexion;

public class MainFrame extends JFrame {

    public MainFrame(String user, String password) {
        
        
        setTitle("Mail Application");
        setSize(400, 300);
        setLocationRelativeTo(null);

        Conexion c = new Conexion(user, password);
        
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
    
    private void crearTablaMensajes(List<Message> mensajes) throws MessagingException{
        
        JTable tabla = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        
        for (Message mensaje : mensajes) {
            model.addRow(mensaje.getFrom());
        }
        
        tabla.setModel(model);
        this.add(tabla,BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame(new Conexion("adam22fhidalgo@inslaferreria.cat","Kacharel13!")).setVisible(true);
            }
        });
    }
}
