package students.util;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * Created by Ilya Evlampiev on 05.10.2015.
 */
public class MailUtil {
    static Logger log = Logger.getLogger(MailUtil.class);

    public static void sendGreetingEmailTo(String emailTo, String user, String userPassword) {

        final Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("emailconfig.properties");
            log.trace("Getting properties");
            // load a properties file
            prop.load(input);

        } catch (IOException ex) {
            log.error("Prpoperties are not fount due to "+ex.getLocalizedMessage());
            ex.printStackTrace();
        }

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(prop.getProperty("username"), prop.getProperty("password"));
                    }
                });
        log.trace("Setting up smtp session");

        try {
            log.trace("Formatting the new message");
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(prop.getProperty("username")));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailTo));
            message.setSubject("Welcome to my webapp!");
            message.setText("Dear "+user
                    + ",\n\n welcome to my website! Please remember your password \'"+userPassword+"\' since it will not be stored in my db");
            log.trace("Sending message");
            Transport.send(message);
            log.trace("Message successfully sent");

        } catch (MessagingException e) {
            log.error("Message si not sent due to "+e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
}