package sv.gob.mined.dhcomitesso.repository;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
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
@ApplicationScoped
public class EmailRepo {

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean enviarMail(String destinatario, String remitente,
            String titulo, String mensaje, Session mailSession) {
        try {
            MimeMessage m = new MimeMessage(mailSession);
            Address from = new InternetAddress(remitente);

            m.setFrom(from);
            m.setRecipients(Message.RecipientType.TO, destinatario);
            //m.setRecipients(Message.RecipientType.BCC, "miguel.sanchez@admin.mined.edu.sv");

            BodyPart messageBodyPart1 = new MimeBodyPart();

            messageBodyPart1.setContent(mensaje, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);

            m.setContent(multipart);
            m.setSubject(titulo, "UTF-8");

            Transport.send(m);
            return true;
        } catch (MessagingException ex) {
            Logger.getLogger(EmailRepo.class.getName()).log(Level.WARNING, "Error en el envio de correo a: {0}", new Object[]{destinatario});
            Logger.getLogger(EmailRepo.class.getName()).log(Level.WARNING, "Error", ex);

            return false;
        }
    }
}
