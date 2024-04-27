package logicaMail;

import javax.swing.table.DefaultTableModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.swing.SwingUtilities;

public class EmailProcessingTask implements Runnable {
    private DefaultTableModel tableModel;
    private Message[] messages;
    private int startIndex;
    private int endIndex;

    public EmailProcessingTask(DefaultTableModel tableModel, Message[] messages, int startIndex, int endIndex) {
        this.tableModel = tableModel;
        this.messages = messages;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public void run() {
        for (int i = startIndex; i < endIndex; i++) {
            if (Thread.currentThread().isInterrupted()) {
                // Si el hilo ha sido interrumpido, sal del bucle
                return;
            }

            Message message = messages[i];
            try {
                String from = InternetAddress.toString(message.getFrom());
                String to = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
                String cc = InternetAddress.toString(message.getRecipients(Message.RecipientType.CC));
                String subject = message.getSubject();
                Date sentDate = message.getSentDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                String formattedSentDate = dateFormat.format(sentDate);
                String content = "";

                Object contents = message.getContent();

                if (contents instanceof Multipart) {
                    Multipart multipart = (Multipart) contents;
                    for (int z = 0; z < multipart.getCount(); z++) {
                        BodyPart bodyPart = multipart.getBodyPart(z);
                        content += bodyPart.getContent();
                    }
                }

                Object[] rowData = {from, to, cc, subject, formattedSentDate, content};

                // no se por que se queja sin lo de abajo
                int y = i;
                
                // Modificar la tabla desde el hilo de Swing
                SwingUtilities.invokeLater(() -> {
                    
                    int modelIndex = y - startIndex;
                    // Agregar fila en la posición adecuada
                    tableModel.insertRow(modelIndex, rowData);
                    // No es necesario invocar fireTableDataChanged ya que insertRow automáticamente notifica los cambios en el modelo
                });
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
