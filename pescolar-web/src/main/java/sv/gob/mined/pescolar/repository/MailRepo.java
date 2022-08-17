package sv.gob.mined.pescolar.repository;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author misanchez
 */
@Stateless
@LocalBean
public class MailRepo {

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean enviarMail(String destinatario, String remitente, String titulo, String mensaje, Session mailSession) {
        try {

            MimeMessage menssage = new MimeMessage(mailSession);
            Address from = new InternetAddress(remitente);

            menssage.setFrom(from);
            menssage.setRecipients(Message.RecipientType.TO, "miguel.sanchez@admin.mined.edu.sv");

            BodyPart messageBodyPart1 = new MimeBodyPart();

            messageBodyPart1.setContent(mensaje, "text/html; charset=utf-8");

            menssage.setSubject(titulo, "UTF-8");
            menssage.saveChanges();

            Transport.send(menssage, menssage.getAllRecipients());

            return true;
        } catch (MessagingException ex) {
            Logger.getLogger(MailRepo.class.getName()).log(Level.WARNING, "Error", ex);
            return false;
        }
    }
}
