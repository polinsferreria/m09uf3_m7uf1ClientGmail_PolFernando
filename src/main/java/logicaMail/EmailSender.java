package logicaMail;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailSender {

    /**
     * Método para enviar un correo electrónico con archivos adjuntos.
     * @param to El destinatario del correo.
     * @param subject El asunto del correo.
     * @param body El cuerpo del correo.
     * @param attachments Los archivos adjuntos a enviar.
     * @throws NoSuchProviderException Si no se encuentra el proveedor de correo.
     */
    public static void sendEmailWithAttachment(String to, String subject, String body, File[] attachments) throws NoSuchProviderException {
        // Obtener la instancia del gestor de sesión de correo electrónico
        EmailSessionManager em = EmailSessionManager.getInstance();
        
        // Obtener el nombre de usuario y contraseña del gestor de sesión
        String username = em.getUsername();
        String password = em.getPassword();

        // Configurar las propiedades para la sesión de correo
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        // Crear una sesión de correo usando las propiedades y la autenticación del usuario
        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Crear un mensaje MIME
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            // Convertir el array de direcciones a una cadena separada por comas
            String recipients = String.join(",", to);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients)); // Concatenar las direcciones separadas por comas
            message.setSubject(subject);

            // Crear una parte MIME multipart para el mensaje
            MimeMultipart multipart = new MimeMultipart();

            // Crear una parte MIME para el texto del mensaje
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            multipart.addBodyPart(textPart);

            // Agregar archivos adjuntos al mensaje
            for (File file : attachments) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(file);
                multipart.addBodyPart(attachmentPart);
            }

            // Establecer el contenido del mensaje como multipart
            message.setContent(multipart);

            // Enviar el mensaje
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
