/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.dhcomitesso.view;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import sv.gob.mined.dhcomitesso.model.dhcsso.Empleado;
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
@SuppressWarnings("serial")
@Named
@ViewScoped
public class LoginView implements Serializable {

    private static final ResourceBundle UTIL_CORREO = ResourceBundle.getBundle("Bundle");

    private String codigoEmpleado;
    private String duiEmpleado;
    private String correo;
    private String claveAcceso;

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

    public String validarUsuario() {
        Integer idEmpleado = loginRepo.validarUsuario(codigoEmpleado, claveAcceso);
        if (idEmpleado != null) {
            VarSession.setVariableSession("idEmpleado", idEmpleado);
            return "app/principal?faces-redirect=true";
        } else {
            JsfUtil.mensajeAlerta("Este código de empleado no existe o no esta habilitado.");
            return "";
        }
    }
}
