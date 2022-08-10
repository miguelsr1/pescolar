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
    public Boolean enviarMail(String destinatario, String remitente, String titulo, String mensaje, String nombreMesAnho, File path, String codDepa, Session mailSession) {
        try {

            MimeMessage menssage = new MimeMessage(mailSession);
            Address from = new InternetAddress(remitente);

            menssage.setFrom(from);
            menssage.setRecipients(Message.RecipientType.TO, "miguel.sanchez@admin.mined.edu.sv");

            BodyPart messageBodyPart1 = new MimeBodyPart();

            messageBodyPart1.setContent(mensaje, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);

            addAttachment(path, multipart);

            menssage.setContent(multipart);
            menssage.setSubject(titulo, "UTF-8");
            menssage.saveChanges();

            Transport.send(menssage, menssage.getAllRecipients());

            return true;
        } catch (MessagingException ex) {
            Logger.getLogger(MailRepo.class.getName()).log(Level.WARNING, "Error en el envio de correo a: {0} - código: {1}", new Object[]{destinatario, path.getName()});
            Logger.getLogger(MailRepo.class.getName()).log(Level.WARNING, "Error", ex);
            return false;
        }
    }

    private void addAttachment(File value, Multipart multipart) {
        try {
            DataSource source = new FileDataSource(value);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(value.getName());
            multipart.addBodyPart(messageBodyPart);
        } catch (MessagingException ex) {
            Logger.getLogger(MailRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendBlockMails(List<MimeMessage> lstEmails, Session mailSession) {
        try ( Transport t = mailSession.getTransport()) {
            t.connect();
            for (MimeMessage message : lstEmails) {

                t.sendMessage(message, message.getAllRecipients());
            }
        } catch (MessagingException e) {
            Logger.getLogger(MailRepo.class.getName()).log(Level.WARNING, "Error", e);
        }
    }
}
