package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Session;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import sv.gob.mined.pescolar.repository.MailRepo;
import sv.gob.mined.pescolar.repository.UsuarioRepo;
import sv.gob.mined.pescolar.utils.JsfUtil;

/**
 *
 * @author misanchez
 */
@Named
@RequestScoped
public class LoginView implements Serializable {

    private final String UR_WELCOME_PROVE = "/pro/reg03DatosGenerales";
    private final String UR_WELCOME_USU = "/app/principal";
    @NotEmpty
    private String usuario;
    @NotEmpty
    private String claveAcceso;
    @NotEmpty
    private String duiPro;
    @NotEmpty
    private String claveAccesoPro;

    private static final ResourceBundle UTIL_CORREO = ResourceBundle.getBundle("Bundle");

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private UsuarioRepo usuarioRepo;
    @Inject
    private MailRepo mailRepo;

    @Resource(mappedName = "java:/PaqueteEscolar")
    private Session mailSession;

    public String getDuiPro() {
        return duiPro;
    }

    public void setDuiPro(String duiPro) {
        this.duiPro = duiPro;
    }

    public String getClaveAccesoPro() {
        return claveAccesoPro;
    }

    public void setClaveAccesoPro(String claveAccesoPro) {
        this.claveAccesoPro = claveAccesoPro;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    /*public void updateClave() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
        passwordHash.initialize(parameters);

        System.out.println(passwordHash.generate(claveAccesoPro.toCharArray()));
    }*/

    public String validarUsuario() {
        return validarLogin(usuario, claveAcceso, UR_WELCOME_USU);
    }

    public String validarProveedor() {
        //updateClave();
        return validarLogin(duiPro, claveAccesoPro, UR_WELCOME_PROVE);
    }

    private String validarLogin(String usuario, String clave, String urlWelcome) {
        switch (processAuthentication(usuario, clave)) {
            case SEND_CONTINUE:
                break;
            case SEND_FAILURE:
                JsfUtil.mensajeError("Usuario/Clave de acceso incorrectos!");
                break;
            case SUCCESS:
                return urlWelcome + "?faces-redirect=true";
            default:
                break;
        }
        return null;
    }

    private AuthenticationStatus processAuthentication(String user, String pass) {
        return securityContext.authenticate(
                (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(),
                (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse(),
                AuthenticationParameters.withParams().credential(
                        new UsernamePasswordCredential(user, pass))
        );
    }

    public void asignarNuevaClave() {
        HashMap<String, String> valor = usuarioRepo.solicitarEnlaceNuevaClave(usuario);

        if (valor.isEmpty()) {
            JsfUtil.mensajeAlerta("Este DUI/NIT no existe en la base.");
        } else {
            String titulo = UTIL_CORREO.getString("prov.email.solicitudcambiocontrasenha.titulo");
            String mensaje = UTIL_CORREO.getString("prov.email.solicitudcambiocontrasenha.mensaje");

            mensaje = MessageFormat.format(mensaje, valor.get("nombreCompleto"), valor.get("token"));

            List<String> to = new ArrayList();
            List<String> cc = new ArrayList();

            to.add(valor.get("correo"));

            if (mailRepo.enviarMail(titulo,
                    mensaje,
                    to,
                    cc,
                    new ArrayList(),
                    mailSession)) {
                JsfUtil.mensajeInformacion("Por favor revisar su correo para poder cambiar la clave de acceso");
            }

        }
    }
}
