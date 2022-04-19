/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.web;

import java.io.Serializable;
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

/**
 *
 * @author misanchez
 */
@Named
@RequestScoped
public class LoginView implements Serializable {

    @NotEmpty
    private String usuario;
    @NotEmpty
    private String claveAcceso;
    
    private static final ResourceBundle UTIL_CORREO = ResourceBundle.getBundle("Bundle");

    @Inject
    private Pbkdf2PasswordHash passwordHash;
    
    @Inject
    private SecurityContext securityContext;

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

    public void updateClave() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
        passwordHash.initialize(parameters);

        System.out.println(passwordHash.generate("mined2013.".toCharArray()));
    }

    public String validarUsuario() {
       // updateClave();
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
                        new UsernamePasswordCredential(usuario, claveAcceso))
        );
    }
}
