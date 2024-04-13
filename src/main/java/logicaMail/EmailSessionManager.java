package logicaMail;

import java.io.IOException;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.swing.table.DefaultTableModel;

public class EmailSessionManager {
    private static EmailSessionManager instance;

    private String username;
    private String password;
    private Session emailSession;
    private Store store;
    private Folder emailFolder;

    private EmailSessionManager(String username, String password) throws MessagingException {
        this.username = username;
        this.password = password;
        
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.ssl.enable", "true");
        this.emailSession = Session.getInstance(properties, null);
        this.store = emailSession.getStore("imaps");
        this.store.connect(username, password);
    }

    public static synchronized EmailSessionManager getInstance(String username, String password) throws MessagingException {
        if (instance == null) {
            instance = new EmailSessionManager(username, password);
        }
        return instance;
    }

    public static synchronized EmailSessionManager getInstance() throws IllegalStateException {
        if (instance == null) {
            throw new IllegalStateException("EmailSessionManager is not initialized. Please login first.");
        }
        return instance;
    }

     public void receiveEmail(DefaultTableModel tableModel) throws MessagingException, IOException {
        if (emailFolder == null || !emailFolder.isOpen()) {
            emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
        }
        
       
        
        Message[] messages = emailFolder.getMessages();
        for (Message message : messages) {
         
            String from = InternetAddress.toString(message.getFrom());
            String to = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
            String cc = InternetAddress.toString(message.getRecipients(Message.RecipientType.CC));
            String subject = message.getSubject();
            String sentDate = message.getSentDate().toString();
            String content = message.getContent().toString();

            // Agregar los datos al modelo de la tabla
            Object[] rowData = {from, to, cc, subject, sentDate, content};
            tableModel.addRow(rowData);
            tableModel.fireTableDataChanged();
        }
    }

    public void close() {
        try {
            if (emailFolder != null && emailFolder.isOpen()) {
                emailFolder.close(false);
            }
            if (store != null && store.isConnected()) {
                store.close();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            instance = null;
        }
    }
    
     public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
    
    
}
