package rockthenet.view;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Send and Check Emails using JavaMail and gmail as a provider
 * Created by Sam
 */
public class Email {
    private static final String from = "rockthenet.gg@gmail.com";
    private static final String username = "rockthenet.gg@gmail.com";
    private static final String password = "rFnV2ARzLSLe8nzhxA6ScqeH"; // hardcoded as fuck
    private static final String host = "pop.gmail.com";
    private static final String mailStoreType = "pop3";

    /**
     * Sends a mail to a target with a subject and a message
     *
     * @param recipient
     */
    public void sendMail(String recipient, String subject, String message) {

        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a default MimeMessage object.
            Message mailMessage = new MimeMessage(session);

            // Set From: header field of the header.
            mailMessage.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            mailMessage.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            // Set Subject: header field
            mailMessage.setSubject(subject);

            // Now set the actual message
            mailMessage.setText(message);

            // Send message
            Transport.send(mailMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * For testing purposes, this class returns true if a mail was sent from a specified subject.
     * only implemented to check for a gmail account
     *
     * @param subject
     * @return whether or not this mail was received
     */
    public boolean checkMail(String subject) {
        try {

            //create properties field
            Properties properties = new Properties();

            properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");

            store.connect(host, username, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();

            for (int i = 0, n = messages.length; i < n; i++) {
                Message mailMessage = messages[i];
                if (mailMessage.getSubject().equals(subject)) {
                    //close the store and folder objects
                    emailFolder.close(false);
                    store.close();
                    return true;
                }
            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();
            return false;

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
