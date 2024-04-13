package logicaMail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Folder;
import javax.mail.Store;

public class EmailReceiver {

    public static Message[] receiveEmail() throws MessagingException {
        EmailSessionManager manager = EmailSessionManager.getInstance();
        Store store = manager.getStore();
        Folder emailFolder = store.getFolder("INBOX");

        if (!emailFolder.isOpen()) {
            emailFolder.open(Folder.READ_ONLY);
        }

        int totalMessages = emailFolder.getMessageCount();
        int initialMessage = Math.max(1, totalMessages - 10); // Cargar solo los últimos 10 mensajes
        Message[] messages = emailFolder.getMessages(initialMessage, totalMessages);
        
        // Cerrar la carpeta después de obtener los mensajes
        emailFolder.close(false);
        
        return messages;
    }
}
