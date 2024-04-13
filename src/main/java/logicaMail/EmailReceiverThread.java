package logicaMail;

import javax.swing.table.DefaultTableModel;

public class EmailReceiverThread extends Thread {
    private final EmailSessionManager emailSessionManager;
    private final DefaultTableModel tableModel;

    public EmailReceiverThread(EmailSessionManager emailSessionManager, DefaultTableModel tableModel) {
        this.emailSessionManager = emailSessionManager;
        this.tableModel = tableModel;
    }

    @Override
    public void run() {
        try {
            emailSessionManager.receiveEmail(tableModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
