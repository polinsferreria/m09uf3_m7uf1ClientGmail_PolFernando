package logicaMail;

import gui.MainFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Clase que representa un hilo para recibir correos electrónicos.
 */
public class EmailReceiverThread extends Thread {

    private final EmailSessionManager emailSessionManager;
    private final DefaultTableModel tableModel;
    private final String folderName;
    private int n; // Variable para almacenar el límite de correos a recibir
    private boolean isLimite; // Variable para indicar si se ha establecido un límite

    /**
     * Constructor para el caso en que no se establece un límite en la cantidad de correos a recibir.
     * @param emailSessionManager El gestor de sesión de correo electrónico.
     * @param tableModel El modelo de tabla para mostrar los correos electrónicos.
     * @param folderName El nombre de la carpeta de correo.
     */
    public EmailReceiverThread(EmailSessionManager emailSessionManager, DefaultTableModel tableModel, String folderName) {
        this.emailSessionManager = emailSessionManager;
        this.tableModel = tableModel;
        this.folderName = folderName;
        isLimite = false; // No se establece límite por defecto
    }
    
    /**
     * Constructor para el caso en que se establece un límite en la cantidad de correos a recibir.
     * @param emailSessionManager El gestor de sesión de correo electrónico.
     * @param tableModel El modelo de tabla para mostrar los correos electrónicos.
     * @param folderName El nombre de la carpeta de correo.
     * @param n El límite de correos a recibir.
     */
    public EmailReceiverThread(EmailSessionManager emailSessionManager, DefaultTableModel tableModel, String folderName, int n) {
        this.emailSessionManager = emailSessionManager;
        this.tableModel = tableModel;
        this.folderName = folderName;
        this.n = n; // Establecer el límite de correos a recibir
        isLimite = true; // Indicar que se ha establecido un límite
    }

    @Override
    public void run() {
        try {
            if (isLimite) {
                emailSessionManager.receiveEmail(tableModel, folderName, n); // Recibir correos hasta el límite establecido
            } else {
                emailSessionManager.receiveEmail(tableModel, folderName, -1); // Recibir todos los correos de la carpeta
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
