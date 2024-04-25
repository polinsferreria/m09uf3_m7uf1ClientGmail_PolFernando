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

    // Constructor privado para asegurar la instancia única
    private EmailSessionManager(String username, String password) throws MessagingException {
        this.username = username;
        this.password = password;

        // Configuración de las propiedades para la sesión de correo
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.ssl.enable", "true");
        this.emailSession = Session.getInstance(properties, null);
        this.store = emailSession.getStore("imaps");
        this.store.connect(username, password);
    }

    // Obtener una instancia única de EmailSessionManager
    public static synchronized EmailSessionManager getInstance(String username, String password) throws MessagingException {
        if (instance == null) {
            instance = new EmailSessionManager(username, password);
        }
        return instance;
    }

    // Obtener la instancia actual de EmailSessionManager
    public static synchronized EmailSessionManager getInstance() throws IllegalStateException {
        if (instance == null) {
            throw new IllegalStateException("EmailSessionManager is not initialized. Please login first.");
        }
        return instance;
    }

    // Método para eliminar un correo electrónico
    public void deleteEmail(Message message) throws MessagingException, IOException {
        System.out.println(message.getSubject());   
        // Marcar el mensaje como eliminado
        message.setFlag(Flags.Flag.DELETED, true);
        
        emailFolder.close();
    }

   

    // Método para obtener una carpeta específica
    public Folder getfolder(String folderName) throws MessagingException {
        Folder e = store.getFolder(folderName);
        return e;
    }

    public Message getEmailFromRow(int selectedRow) throws MessagingException {
        // Suponiendo que la columna 0 de la tabla contiene el objeto Message asociado a cada fila
        if (emailFolder != null || !emailFolder.isOpen()) {
            emailFolder.open(Folder.READ_WRITE);
            Object messageObject = emailFolder.getMessage(selectedRow);
            //f.close(true);
            if (messageObject instanceof Message) {

                return (Message) messageObject;

            } else {
                throw new MessagingException("No se pudo obtener el mensaje seleccionado");
            }
        }else{
             throw new MessagingException("No se pudo obtener la carpeta esta vacia o cerrada");
        }

    }

    // Método para recibir correos electrónicos hasta el n-ésimo correo de una carpeta específica
    public void receiveEmail(DefaultTableModel tableModel, String folderName, int n) throws MessagingException, IOException {
        if (emailFolder == null || !emailFolder.isOpen()) {
            emailFolder = store.getFolder("INBOX"); // Cambia "INBOX" al directorio específico que desees
            emailFolder.open(Folder.READ_ONLY);
        }

        // Obtener los mensajes
        Message[] messages = emailFolder.getMessages();

        // Iterar hasta el correo número n-10
        
      
        for (int i = 0; i < ((n == -1) ? messages.length - 1 : n) ; i++) {
            // Obtener el encabezado del mensaje y mostrarlo o procesarlo según sea necesario
            Message message = messages[i];
            String from = InternetAddress.toString(message.getFrom());
            String to = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
            String cc = InternetAddress.toString(message.getRecipients(Message.RecipientType.CC));
            String subject = message.getSubject();
            String sentDate = message.getSentDate().toString();
            String content = "";
            
            Object contents = message.getContent();
           
            if (contents instanceof Multipart) {
                Multipart multipart = (Multipart) contents;
                for (int z = 0; z < multipart.getCount(); z++) {
                    BodyPart bodyPart = multipart.getBodyPart(z);
                    content += bodyPart.getContent();
                    
                }
            } else {
                
            }

            // Agregar los datos al modelo de la tabla
            Object[] rowData = {from, to, cc, subject, sentDate, content};
            tableModel.addRow(rowData);
            tableModel.fireTableDataChanged();
        }
    }

    // Método para cerrar la sesión de correo
    public void close() {
        try {
            // Cerrar la carpeta y la tienda de correo
            if (emailFolder != null && emailFolder.isOpen()) {
                emailFolder.close(false);
            }
            if (store != null && store.isConnected()) {
                store.close();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            // Restablecer la instancia a nula
            instance = null;
        }
    }

    // Métodos de acceso para obtener el nombre de usuario, contraseña y tienda de correo
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
