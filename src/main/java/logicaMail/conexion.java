/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logicaMail;

import gui.LoginFrame;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.swing.JOptionPane;

/**
 *
 * @author usuario
 */
public class Conexion {

    private Transport transport;

    public Conexion() {
    }

    public int loginTransport(String email, String password) {

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

}
