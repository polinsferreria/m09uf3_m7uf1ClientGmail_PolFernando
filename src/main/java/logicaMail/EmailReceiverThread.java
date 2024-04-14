package logicaMail;

import gui.MainFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class EmailReceiverThread extends Thread {

    private final EmailSessionManager emailSessionManager;
    private final DefaultTableModel tableModel;
    private final String folderName;
    private int n;
    private boolean isLimite;

    public EmailReceiverThread(EmailSessionManager emailSessionManager, DefaultTableModel tableModel, String folderName) {
        this.emailSessionManager = emailSessionManager;
        this.tableModel = tableModel;
        this.folderName = folderName;
        isLimite = false;
    }
    
    
    public EmailReceiverThread(EmailSessionManager emailSessionManager, DefaultTableModel tableModel, String folderName, int n) {
        this.emailSessionManager = emailSessionManager;
        this.tableModel = tableModel;
        this.folderName = folderName;
        this.n = n;
        isLimite = true;
    }

    @Override
    public void run() {
        try {
            if (isLimite) {
                emailSessionManager.receiveEmail(tableModel, folderName,n); // Modificar para recibir el nombre de la carpeta

            } else {
                emailSessionManager.receiveEmail(tableModel, folderName); // Modificar para recibir el nombre de la carpeta
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }
    }
}
