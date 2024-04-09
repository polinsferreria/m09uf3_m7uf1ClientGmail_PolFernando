
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class borrar {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Obtain user name and password
        System.out.print("USER NAME: ");
        String userName = scanner.nextLine();
        System.out.print("PASSWORD: ");
        String password = scanner.nextLine();

        // Fetch and print emails from INBOX
        try {
            fetchEmailsFromInbox(userName, password);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static void fetchEmailsFromInbox(String userName, String password) throws MessagingException {
        // IMAP properties
        Properties imapProps = new Properties();
        imapProps.setProperty("mail.store.protocol", "imaps");

        // Obtain a session
        Session session = Session.getDefaultInstance(imapProps);

        // Connect to the store
        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", userName, password);

        // Open the INBOX folder
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        // Fetch messages
        Message[] messages = inbox.getMessages();

        // Print messages
        for (int i = 0; i < messages.length; i++) {
            System.out.println("Message " + (i + 1));
            System.out.println("From: " + messages[i].getFrom()[0]);
            System.out.println("Subject: " + messages[i].getSubject());
            System.out.println("Sent Date: " + messages[i].getSentDate());
            System.out.println("-----------------------------------");
        }

        // Close the folder and store
        inbox.close(false);
        store.close();
    }
}
