package sv.gob.mined.dhcomitesso.view;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import sv.gob.mined.dhcomitesso.model.dhcsso.Empleado;
import sv.gob.mined.dhcomitesso.model.dhcsso.Users;
import sv.gob.mined.dhcomitesso.repository.EmailRepo;
import sv.gob.mined.dhcomitesso.repository.LoginRepo;
import sv.gob.mined.dhcomitesso.util.RC4Crypter;
import sv.gob.mined.dhcomitesso.util.VarSession;
import sv.gob.mined.utils.jsf.JsfUtil;
import sv.gob.mined.utils.mail.MailSession;

/**
 *
 * @author misanchez
 */
@Named
@RequestScoped
public class LoginView implements Serializable {

    private static final ResourceBundle UTIL_CORREO = ResourceBundle.getBundle("Bundle");

    private String codigoEmpleado;
    private String claveAcceso;

    private String duiEmpleado;
    private String correo;

    @Inject
    private LoginRepo loginRepo;
    @Inject
    private EmailRepo emailRepo;

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

//    @Inject
//    private Pbkdf2PasswordHash passwordHash;
    public String validarUsuario() {

//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
//        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
//        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
//        passwordHash.initialize(parameters);

        
//        List<Users> lstUsers = loginRepo.findAllUsers();
//        for (Users user : lstUsers) {
//            loginRepo.guardar(user, user.getUserPassword());
//        }
        Users u = loginRepo.usuarioValido(codigoEmpleado, claveAcceso);
        if (u != null) {
            VarSession.setVariableSession("pUsuario", u);
            return "/app/principal?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario o clave de acceso inválido .", null));
        }
        return null;
    }

//    private AuthenticationStatus processAuthentication() {
//        return securityContext.authenticate(
//                (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(),
//                (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse(),
//                AuthenticationParameters.withParams().credential(
//                        new UsernamePasswordCredential(codigoEmpleado, claveAcceso))
//        );
//    }
}
