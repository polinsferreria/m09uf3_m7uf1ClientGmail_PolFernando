package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import logicaMail.AttachmentChooser;
import logicaMail.EmailReceiverThread;
import logicaMail.EmailSender;
import logicaMail.EmailSessionManager;

public class MainFrame extends JFrame {

    private final EmailSessionManager emailSessionManager;

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
        JTable inboxTable = new JTable(inboxTableModel);
        JScrollPane inboxScrollPane = new JScrollPane(inboxTable);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    refreshInbox(inboxTableModel);
                } catch (MessagingException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        inboxPanel.add(inboxScrollPane, BorderLayout.CENTER);
        inboxPanel.add(refreshButton, BorderLayout.SOUTH);

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
        tabbedPane.addTab("Compose", composePanel);

        // Agregar el panel de pestañas al marco principal
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void refreshInbox(DefaultTableModel inboxTableModel) throws MessagingException, IOException {
        inboxTableModel.setRowCount(0); // Limpiar la tabla antes de actualizarla
        EmailReceiverThread receiverThread = new EmailReceiverThread(emailSessionManager, inboxTableModel);
        receiverThread.start();
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
