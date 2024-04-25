package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import logicaMail.AttachmentChooser;
import logicaMail.EmailReceiverThread;
import logicaMail.EmailSender;
import logicaMail.EmailSessionManager;

public class MainFrame extends JFrame {

    private final EmailSessionManager emailSessionManager;
    private JTable inboxTable;

    public MainFrame(String username, String password) throws MessagingException {

        setTitle("Mail Application");
        setSize(800, 600);
        setLocationRelativeTo(null);

        emailSessionManager = EmailSessionManager.getInstance(username, password);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel de bandeja de entrada
        JPanel inboxPanel = new JPanel(new BorderLayout());
        String[] columnNames = {"From", "To", "CC", "Subject", "Sent Date", "Message"};
        DefaultTableModel inboxTableModel = new DefaultTableModel(columnNames, 0);
        inboxTable = new JTable(inboxTableModel);
        JScrollPane inboxScrollPane = new JScrollPane(inboxTable);
        JButton refreshButton = new JButton("Refresh");
        JButton deleteButton = new JButton("DELETE");

        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    refreshFolder(inboxTableModel, "INBOX");
                } catch (MessagingException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Carpeta, no existe.", "vales", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = inboxTable.getSelectedRow();
                //if (selectedRow != -1) {
                    try {
                        Folder f = emailSessionManager.getfolder("INBOX");
                        // Obtener el mensaje seleccionado
                        //Message selectedMessage = getEmailFromRow(selectedRow,f); // Debes implementar este método para obtener el mensaje desde la fila seleccionada en la tabla
                        Message selectedMessage = emailSessionManager.getEmailFromRow(selectedRow);
                        // Eliminar el mensaje
                        emailSessionManager.deleteEmail(selectedMessage);
                        //f.close(true);
                        // Actualizar la tabla
                        refreshFolder(inboxTableModel, "INBOX"); // Debes tener un método refreshFolder que actualice la tabla
                    } catch (MessagingException ex) {
                        ex.printStackTrace();
                        // Manejar la excepción según sea necesario
                    } catch (IOException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                /*} else {
                    JOptionPane.showMessageDialog(MainFrame.this, "Por favor seleccione un correo electrónico para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
                }*/
            }
        });

        inboxPanel.add(inboxScrollPane, BorderLayout.CENTER);
        inboxPanel.add(refreshButton, BorderLayout.NORTH);
        inboxPanel.add(deleteButton, BorderLayout.SOUTH);
        
        // Panel de correos enviados
        JPanel sentPanel = new JPanel(new BorderLayout());
        String[] sentColumnNames = {"From", "To", "CC", "Subject", "Sent Date", "Message"};
        DefaultTableModel sentTableModel = new DefaultTableModel(sentColumnNames, 0);
        JTable sentTable = new JTable(sentTableModel);
        JScrollPane sentScrollPane = new JScrollPane(sentTable);
        JButton refreshSentButton = new JButton("Refresh");

        refreshSentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    refreshFolder(sentTableModel, "SEND");
                } catch (MessagingException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        sentPanel.add(sentScrollPane, BorderLayout.CENTER);
        sentPanel.add(refreshSentButton, BorderLayout.SOUTH);

        // Agregar la pestaña de correos enviados al panel de pestañas
        
        
        //boton para configurar los correos que se quiere obtener:
        JButton getHeadersButton = new JButton("Obtener Encabezados");
        getHeadersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Solicitar al usuario un número del 0 al 10
                String input = JOptionPane.showInputDialog("Ingrese un número del 0 al 10:");
                try {
                    int n = Integer.parseInt(input);
                    if (n >= 0 && n <= 10) {
                        // Llamar a un método para obtener los encabezados del correo desde el primer correo hasta el n-10
                        refreshFolderLimit(inboxTableModel, "INBOX", n);
                    } else {
                        JOptionPane.showMessageDialog(MainFrame.this, "Por favor ingrese un número válido del 0 al 10", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Por favor ingrese un número válido del 0 al 10", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(getHeadersButton, BorderLayout.NORTH); // Agregar el botón en la parte superior de la ventana

        // Panel de enviar mensaje
        JPanel composePanel = new JPanel(new BorderLayout());
        JLabel toLabel = new JLabel("To:");
        JTextField toField = new JTextField(30);
        JLabel subjectLabel = new JLabel("Subject:");
        JTextField subjectField = new JTextField(30);
        JLabel bodyLabel = new JLabel("Body:");
        JTextArea bodyArea = new JTextArea(10, 30);
        JScrollPane bodyScrollPane = new JScrollPane(bodyArea);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String to = toField.getText();
                String subject = subjectField.getText();
                String body = bodyArea.getText();
                try {
                    // Llamar al método para enviar el correo electrónico usando la clase de logicaMail
                    EmailSender.sendEmailWithAttachment(to, subject, body, AttachmentChooser.chooseAttachments());
                    JOptionPane.showMessageDialog(MainFrame.this, "Email sent successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Limpiar los campos después de enviar el correo electrónico
                    toField.setText("");
                    subjectField.setText("");
                    bodyArea.setText("");
                } catch (MessagingException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(MainFrame.this, "Error sending email: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JPanel composeFieldsPanel = new JPanel(new GridLayout(3, 2));
        composeFieldsPanel.add(toLabel);
        composeFieldsPanel.add(toField);
        composeFieldsPanel.add(subjectLabel);
        composeFieldsPanel.add(subjectField);
        composeFieldsPanel.add(bodyLabel);
        composeFieldsPanel.add(bodyScrollPane);
        composePanel.add(composeFieldsPanel, BorderLayout.CENTER);
        composePanel.add(sendButton, BorderLayout.SOUTH);

        // Agregar las pestañas al panel de pestañas
        tabbedPane.addTab("Inbox", inboxPanel);
        tabbedPane.addTab("Sent", sentPanel);
        tabbedPane.addTab("Compose", composePanel);

        // Agregar el panel de pestañas al marco principal
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void refreshFolder(DefaultTableModel tableModel, String folderName) throws MessagingException, IOException {
        tableModel.setRowCount(0); // Limpiar la tabla antes de actualizarla
        EmailReceiverThread receiverThread = new EmailReceiverThread(emailSessionManager, tableModel, folderName);
        receiverThread.start();
    }

    private void refreshFolderLimit(DefaultTableModel tableModel, String folderName, int n) {

        tableModel.setRowCount(0); // Limpiar la tabla antes de actualizarla
        EmailReceiverThread receiverThread = new EmailReceiverThread(emailSessionManager, tableModel, folderName, n);
        receiverThread.start(); // ¡No olvides iniciar el hilo!
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Ejemplo de uso
                    // Debes reemplazar los valores "@", "addsf" con el nombre de usuario y contraseña reales
                    new MainFrame("", "").setVisible(true);
                } catch (MessagingException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
