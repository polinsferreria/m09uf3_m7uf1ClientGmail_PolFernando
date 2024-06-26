package logicaMail;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import javax.mail.*;
import javax.mail.internet.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import gui.MainFrame;

public class EmailSessionManager {

    private static EmailSessionManager instance;

    private String username;
    private String password;
    private Session emailSession;
    private Store store;
    private Folder emailFolder;
    private Boolean mainThreadDead = false;
  
    private EmailReceiverThread emailReceiverThreadMain;
    private EmailReceiverThread emailReceiverThreadAct;
    
    private DefaultTableModel tmpTodo;
    private DefaultTableModel actualizar;
    
    private Message[] messages;

	public char[] messageNumber;

	public Map<Integer,Message> emailAttachmentMap;
  
    
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
        
        emailAttachmentMap = new HashMap<Integer, Message>();
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

    public void openOrDownloadAttachment(Message message) throws MessagingException, IOException {
        Object content = message.getContent();
        if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) && bodyPart.getFileName() != null) {
                    String fileName = bodyPart.getFileName();
                    File downloadsFolder = new File(System.getProperty("user.home") + "/Downloads");
                    File file = new File(downloadsFolder, fileName);
                    
                    if (file.exists()) {
                        // Si el archivo ya existe, abrirlo
                        Desktop.getDesktop().open(file);
                    } else {
                        // Si el archivo no existe, descargarlo y luego abrirlo
                        try (InputStream inputStream = bodyPart.getInputStream();
                             OutputStream outputStream = new FileOutputStream(file)) {
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                            System.out.println("Archivo adjunto descargado: " + fileName);
                        }
                        
                        // Abrir el archivo después de descargarlo
                        Desktop.getDesktop().open(file);
                    }
                }
            }
        }
    }
   

    // Método para obtener una carpeta específica
    public Folder getfolder(String folderName) throws MessagingException {
        Folder e = store.getFolder(folderName);
        return e;
    }

    public Message getEmailFromRow(int selectedRow, String name) throws MessagingException {
        
    	
    	if (emailFolder == null || !emailFolder.isOpen()) {
    		
    		 emailFolder = store.getFolder(name);
             emailFolder.open(Folder.READ_ONLY);
    	
    	} 

        int folderRow = messages.length - selectedRow - 1; // Ajustar el índice para obtener el mensaje correcto
        Object messageObject = emailFolder.getMessage(folderRow);

        if (messageObject instanceof Message) {
            return (Message) messageObject;
        } else {
            throw new MessagingException("No se pudo obtener el mensaje seleccionado.");
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

    public  boolean getMainThreadDead() {
        return mainThreadDead;
    }

    public void setMainThreadDead(boolean mainThreadDead) {
        this.mainThreadDead = mainThreadDead;
    }
    
 // Variable para mantener una referencia al hilo de recepción de correo
    

    // Método para crear y ejecutar el hilo de recepción de correo
    public synchronized void startEmailReceiverThread(DefaultTableModel tableModel, String folderName, int n,MainFrame mf) {
        // Verificar si ya hay un hilo en ejecución y detenerlo si es necesario     
    	if (emailReceiverThreadMain == null) {	
    		emailReceiverThreadMain = new EmailReceiverThread(tableModel, folderName, n,false);
    		emailReceiverThreadMain.start();
    		return;
		}	  	
    	if (emailReceiverThreadAct == null ) {
          //  emailReceiverThreadMain.interrupt();
         //   mainThreadDead = true;        	 
        	actualizar(tableModel,folderName,n,mf);
        }       
    }
    
    public void actualizar(DefaultTableModel tableModel, String folderName, int n, MainFrame mf) {
    	
    	
    	 try {
			
    		emailFolder = store.getFolder(folderName);
			emailFolder.open(Folder.READ_ONLY);
			
			if (messages.length < emailFolder.getMessageCount()) {
				
		       	 messages = emailFolder.getMessages();
				 JOptionPane.showMessageDialog(mf, "Hay " + (-messages.length + emailFolder.getMessageCount() ) + " Mensajes nuevos!");	               
				 emailReceiverThreadAct = new EmailReceiverThread(tableModel, folderName, n,true);
				 emailReceiverThreadAct.start();
			} else {
				JOptionPane.showMessageDialog(mf, "No Hay Mensajes nuevos!");	               
				
			}
			
			
			 
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
         
        
    	
    }
    
    public void agregarFilasAlPrincipio(DefaultTableModel origen, DefaultTableModel destino) {
        int rowCount = origen.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            Object[] rowData = new Object[origen.getColumnCount()];
            for (int j = 0; j < origen.getColumnCount(); j++) {
                rowData[j] = origen.getValueAt(i, j);
            }
            destino.insertRow(0, rowData);
        }
    }


    
    private class EmailReceiverThread extends Thread {
        private DefaultTableModel tableModel;
        private String folderName;
        private int n;
        boolean actualizar;

        public EmailReceiverThread(DefaultTableModel tableModel, String folderName, int n, boolean actualizar) {
            this.tableModel = tableModel;
            this.folderName = folderName;
            this.n = n;
            mainThreadDead = false;
            this.actualizar = actualizar;
        }

        @Override
        public void run() {
            Folder emailFolder = null;
            try {
                // Abrir la carpeta de correo
            	
                System.out.println("Abriendo la carpeta de correo...");
                emailFolder = store.getFolder(folderName);
                emailFolder.open(Folder.READ_ONLY);
                System.out.println("Carpeta de correo abierta correctamente.");

                // Obtener los mensajes
                System.out.println("Obteniendo mensajes...");
                messages = emailFolder.getMessages();
                System.out.println("Mensajes obtenidos: " + messages.length);
                
                if (tmpTodo == null && n == -1) {
					
                	tmpTodo = tableModel;
				}
				
					
				

                for (int i = messages.length - 1; i >= ((n == -1) ? 0 : messages.length - n); i--) {
                    if (mainThreadDead) {
                        mainThreadDead = false;
                        tableModel.setRowCount(0);
                        System.out.println("Hilo principal muerto. Limpieza y salida.");
                        return;
                    }

                    // Procesar el mensaje actual
                    System.out.println("Procesando mensaje " + i + "...");
                    processMessage(messages[i],i);
                    System.out.println("Mensaje " + i + " procesado.");
                }
            } catch (MessagingException e) {
                e.printStackTrace();
                // Manejar la excepción de acuerdo a los requerimientos de tu aplicación
            } finally {
                // Cerrar la carpeta de correo al finalizar
                if (emailFolder != null && emailFolder.isOpen()) {
                	
                	if (actualizar) {
                		agregarFilasAlPrincipio(EmailSessionManager.this.actualizar, tableModel);           		
					}
                    try {
                        System.out.println("Cerrando la carpeta de correo...");
                        emailFolder.close(false);
                        System.out.println("Carpeta de correo cerrada correctamente.");
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Método para procesar un mensaje de correo electrónico
        private void processMessage(Message message, int number) {
            try {
                // Obtener el encabezado del mensaje y mostrarlo o procesarlo según sea necesario
                String from = InternetAddress.toString(message.getFrom());
                String to = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
                String cc = InternetAddress.toString(message.getRecipients(Message.RecipientType.CC));
                String subject = message.getSubject();
                Date sentDate = message.getSentDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                String formattedSentDate = dateFormat.format(sentDate);

                // Obtener el contenido del mensaje
                Object content = message.getContent();
                String textContent = "";

                // Lista para almacenar nombres de archivos adjuntos
                List<String> attachments = new ArrayList<>();

                // Verificar si el contenido es multipart
                if (content instanceof Multipart) {
                    Multipart multipart = (Multipart) content;
                    for (int z = 0; z < multipart.getCount(); z++) {
                        BodyPart bodyPart = multipart.getBodyPart(z);
                        if (bodyPart.isMimeType("text/plain")) {
                            textContent = (String) bodyPart.getContent();
                        } else if (bodyPart.getDisposition() != null && bodyPart.getDisposition().equalsIgnoreCase(Part.ATTACHMENT)) {
                            // Si hay archivos adjuntos, obtener el nombre del archivo y agregarlo a la lista
                            String fileName = bodyPart.getFileName();
                            if (fileName != null && !fileName.isEmpty()) {
                                attachments.add(fileName);
                            }
                        }
                    }
                } else {
                    // Si no es un contenido múltiple, asumir que es texto plano
                    textContent = (String) content;
                }

                // Crear un String con los nombres de los archivos adjuntos
                String attachmentNames = String.join(", ", attachments);

                // Crear un arreglo de objetos con los datos del mensaje
                Object[] rowData = {from, to, cc, subject, formattedSentDate, textContent, attachmentNames};

                if (attachmentNames != null) {
                	emailAttachmentMap.put((Integer) number, message);
                }
                
                // Agregar los datos del mensaje al modelo de la tabla
                SwingUtilities.invokeLater(() -> {
                    tableModel.addRow(rowData);
                    tableModel.fireTableDataChanged();
                });
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
                // Manejar la excepción de acuerdo a los requerimientos de tu aplicación
            }
        }
    }
}