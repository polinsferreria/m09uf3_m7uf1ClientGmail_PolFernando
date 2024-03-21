/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logicaMail;

import gui.LoginFrame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.swing.JOptionPane;

/**
 *
 * @author usuario
 */
public class Conexion {

    private Transport transport;

    private String email;
    private String password;

    public Conexion(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public int loginTransport() {

        Properties smtpProps = new Properties();
        smtpProps.setProperty("mail.smtp.host", "smtp.gmail.com");
        smtpProps.setProperty("mail.smtp.auth", "true");
        smtpProps.setProperty("mail.smtp.starttls.enable", "true");
        smtpProps.setProperty("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(smtpProps);

        try {
            transport = session.getTransport("smtp");
            transport.connect(email, new String(password));
            return 1;
        } catch (AuthenticationFailedException ex) {
            return 2;
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            return 3;
        } catch (MessagingException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            return 4;
        }

    }

    public List<Message> obtenerMensajes() throws AuthenticationFailedException {
        List<Message> mensajes = new ArrayList<>();

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imaps.host", "imap.gmail.com");
        props.setProperty("mail.imaps.port", "993");
        props.setProperty("mail.imaps.ssl.trust", "*");

        Session session = Session.getDefaultInstance(props);

        try {
            Store store = session.getStore("imaps");
            store.connect(email, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            mensajes.addAll(Arrays.asList(messages));

            inbox.close(false);
            store.close();
        } catch (AuthenticationFailedException e) {
            throw e; // Re-throw the exception to indicate authentication failure.
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mensajes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    

}
