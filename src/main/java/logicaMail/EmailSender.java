package logicaMail;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailSender {

    public static void sendEmailWithAttachment(String to, String subject, String body, File[] attachments) throws NoSuchProviderException {
        EmailSessionManager em = EmailSessionManager.getInstance();
        
        String username = em.getUsername();
        String password = em.getPassword();

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            MimeMultipart multipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            multipart.addBodyPart(textPart);

            for (File file : attachments) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(file);
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email sent successfully with attachments.");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            // Aquí podrías manejar las excepciones de forma más específica, por ejemplo,
            // registrándolas en un archivo de log o notificando al usuario de alguna manera.
        } finally {
            // Cerrar la sesión de correo para liberar recursos.
            if (session != null) {
                try {
                    session.getTransport().close();
                } catch (MessagingException ex) {
                    Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
