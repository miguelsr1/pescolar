package sv.gob.mined.pescolar.repository;

import java.util.List;
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
    public Boolean enviarMail(String titulo, String mensaje, String destinatario, String cc, Session mailSession) {
        try {

            MimeMessage menssage = new MimeMessage(mailSession);
            Address from = new InternetAddress(mailSession.getProperty("mail.from"));

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

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean enviarMail(String titulo, String mensaje, List<String> to, List<String> cc, List<String> bcc, Session mailSession) {
        try {
            MimeMessage menssage = new MimeMessage(mailSession);
            Address from = new InternetAddress(mailSession.getProperty("mail.from"));

            Address[] destinatarios = new Address[to.size()];
            Address[] copia = new Address[cc.size()];
            Address[] copiaOcultos = new Address[bcc.size()];

            for (int i = 0; i < to.size(); i++) {
                destinatarios[i] = new InternetAddress(to.get(i));
            }
            for (int i = 0; i < cc.size(); i++) {
                copia[i] = new InternetAddress(cc.get(i));
            }
            for (int i = 0; i < bcc.size(); i++) {
                copiaOcultos[i] = new InternetAddress(bcc.get(i));
            }

            menssage.setFrom(from);
            menssage.setRecipients(Message.RecipientType.TO, destinatarios);
            menssage.setRecipients(Message.RecipientType.CC, copia);
            menssage.setRecipients(Message.RecipientType.BCC, copiaOcultos);

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
