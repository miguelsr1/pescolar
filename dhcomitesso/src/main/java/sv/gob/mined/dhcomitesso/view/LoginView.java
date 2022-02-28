package sv.gob.mined.dhcomitesso.view;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import org.primefaces.PrimeFaces;
import sv.gob.mined.dhcomitesso.model.dhcsso.Empleado;
import sv.gob.mined.dhcomitesso.repository.EmailRepo;
import sv.gob.mined.dhcomitesso.repository.LoginRepo;
import sv.gob.mined.dhcomitesso.util.RC4Crypter;
import sv.gob.mined.utils.jsf.JsfUtil;
import sv.gob.mined.utils.mail.MailSession;

/**
 *
 * @author misanchez
 */
@SuppressWarnings("serial")
@Named
@RequestScoped
public class LoginView implements Serializable {

    private static final ResourceBundle UTIL_CORREO = ResourceBundle.getBundle("Bundle");

    @NotEmpty
    private String codigoEmpleado;
    @NotEmpty
    private String claveAcceso;

    private String duiEmpleado;
    private String correo;

    @Inject
    private LoginRepo loginRepo;
    @Inject
    private EmailRepo emailRepo;

    @Inject
    private SecurityContext securityContext;

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getDuiEmpleado() {
        return duiEmpleado;
    }

    public void setDuiEmpleado(String duiEmpleado) {
        this.duiEmpleado = duiEmpleado;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public void enviarCorreo() {
        String tituloEmail = UTIL_CORREO.getString("email.titulo");
        String cuerpoEmail = UTIL_CORREO.getString("email.mensaje");

        Empleado empleado = loginRepo.findEmpleadoByCodigo(codigoEmpleado, duiEmpleado);

        if (empleado == null) {
            JsfUtil.mensajeInformacion("No existe empleado con este código ni DUI, por favor verificar.");
        } else if (empleado.getCorreoElectronico() != null) {
            JsfUtil.mensajeInformacion("Ya se registro el correo electronico para este usuario, si desea recuperar su contraseña por favor dar clic en <p>¿Olvidó su contraseña?</p>");
        } else {
            String codigoGenerado = (new RC4Crypter()).encrypt("ha", empleado.getId().toString().concat(":").concat(codigoEmpleado));

            String cuerpo = MessageFormat.format(cuerpoEmail, codigoGenerado);

            emailRepo.enviarMail(correo,
                    "miguel.sanchez@admin.mined.edu.sv",
                    tituloEmail,
                    cuerpo,
                    MailSession.getMailSessionOffice("miguel.sanchez@admin.mined.edu.sv", "miguelsr15."));

            empleado.setCorreoElectronico(correo);
            empleado.setActivo(Boolean.FALSE);
            loginRepo.guardar(empleado);

            JsfUtil.mensajeInformacion("Recibira un correo para poder crear su contraseña de acceso");
            PrimeFaces.current().executeScript("PF('dlgActivarCorreo').hide();");
        }
        PrimeFaces.current().executeScript("PF('dlgActivarCorreo').hide();");
    }
    
    @Inject
    private Pbkdf2PasswordHash passwordHash;

    public String validarUsuario() {
        
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
//        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
//        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
//        passwordHash.initialize(parameters);
//
//        System.out.println(passwordHash.generate(claveAcceso.toCharArray()));
        
        switch (processAuthentication()){
            case SEND_CONTINUE:
                //facesContext.responseComplete();
                break;
            case SEND_FAILURE:
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid user name and/or password.", null));
                break;
            case SUCCESS:
                // It really passes here, but I'm not redirected to the start
                // page. I keep in the login page.
                return "/app/principal?faces-redirect=true";
            default:
                break;
        }
        return null;
    }
    
    private AuthenticationStatus processAuthentication() {
        return securityContext.authenticate(
                (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(),
                (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse(),
                AuthenticationParameters.withParams().credential(
                        new UsernamePasswordCredential(codigoEmpleado, claveAcceso))
        );
    }
}
