package logicaMail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class ConexionEmails {

    private final String email;
    private final String password;

    public ConexionEmails(String email,String password) {
        this.email = email;
        this.password = password;
        
    }
    
    

   private Properties getServerProperties(String protocol, String host,
            String port) {
        Properties properties = new Properties();
 
        // server setting
        properties.put(String.format("mail.%s.host", protocol), host);
        properties.put(String.format("mail.%s.port", protocol), port);
 
        // SSL setting
        properties.setProperty(
                String.format("mail.%s.socketFactory.class", protocol),
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(
                String.format("mail.%s.socketFactory.fallback", protocol),
                "false");
        properties.setProperty(
                String.format("mail.%s.socketFactory.port", protocol),
                String.valueOf(port));
 
        return properties;
    }
 
    /**
     * Downloads new messages and fetches details for each message.
     * @param protocol
     * @param host
     * @param port
     * @param userName
     * @param password
     */
    public void downloadEmails() {
        Properties properties = getServerProperties("imap","imap.gmail.com", "993");
        Session session = Session.getDefaultInstance(properties);
 
        try {
            // connects to the message store
            Store store = session.getStore("imap");
            store.connect(email, password);
 
            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);
 
            // fetches new messages from server
            Message[] messages = folderInbox.getMessages();
 
            for (int i = 0; i < messages.length; i++) {
                Message msg = messages[i];
                Address[] fromAddress = msg.getFrom();
                String from = fromAddress[0].toString();
                String subject = msg.getSubject();
                String toList = parseAddresses(msg
                        .getRecipients(RecipientType.TO));
                String ccList = parseAddresses(msg
                        .getRecipients(RecipientType.CC));
                String sentDate = msg.getSentDate().toString();
 
                String contentType = msg.getContentType();
                String messageContent = "";
 
                if (contentType.contains("text/plain")
                        || contentType.contains("text/html")) {
                    try {
                        Object content = msg.getContent();
                        if (content != null) {
                            messageContent = content.toString();
                        }
                    } catch (Exception ex) {
                        messageContent = "[Error downloading content]";
                        ex.printStackTrace();
                    }
                }
 
                // print out details of each message
                System.out.println("Message #" + (i + 1) + ":");
                System.out.println("\t From: " + from);
                System.out.println("\t To: " + toList);
                System.out.println("\t CC: " + ccList);
                System.out.println("\t Subject: " + subject);
                System.out.println("\t Sent Date: " + sentDate);
                System.out.println("\t Message: " + messageContent);
            }
 
            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + "imap");
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        }
    }
 
    /**
     * Returns a list of addresses in String format separated by comma
     *
     * @param address an array of Address objects
     * @return a string represents a list of addresses
     */
    private String parseAddresses(Address[] address) {
        String listAddress = "";
 
        if (address != null) {
            for (int i = 0; i < address.length; i++) {
                listAddress += address[i].toString() + ", ";
            }
        }
        if (listAddress.length() > 1) {
            listAddress = listAddress.substring(0, listAddress.length() - 2);
        }
 
        return listAddress;
    }
}
