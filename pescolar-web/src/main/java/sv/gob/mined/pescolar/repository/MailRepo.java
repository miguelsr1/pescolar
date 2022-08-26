package sv.gob.mined.pescolar.repository;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author misanchez
 */
@Stateless
@LocalBean
public class MailRepo {

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean enviarMail(String destinatario, String remitente, String cc, String titulo, String mensaje, Session mailSession) {
        try {

            MimeMessage menssage = new MimeMessage(mailSession);
            Address from = new InternetAddress(remitente);

            menssage.setFrom(from);
            menssage.setRecipients(Message.RecipientType.TO, destinatario);
            menssage.setRecipients(Message.RecipientType.CC, cc);
            //menssage.setRecipients(Message.RecipientType.BCC, "miguel.sanchez@mined.gob.sv");

            menssage.setSubject(titulo, "UTF-8");
            menssage.setText(mensaje, "UTF-8", "html");

            menssage.saveChanges();

            Transport.send(menssage);

            return true;
        } catch (MessagingException ex) {
            Logger.getLogger(MailRepo.class.getName()).log(Level.WARNING, "Error", ex);
            return false;
        }
    }
}
