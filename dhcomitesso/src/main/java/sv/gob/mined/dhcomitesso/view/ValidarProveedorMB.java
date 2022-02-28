/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.dhcomitesso.view;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import org.apache.commons.codec.digest.DigestUtils;
import sv.gob.mined.dhcomitesso.model.dhcsso.Empleado;
import sv.gob.mined.dhcomitesso.repository.LoginRepo;
import sv.gob.mined.dhcomitesso.util.RC4Crypter;

/**
 *
 * @author MISanchez
 */
@ManagedBean
@ViewScoped
public class ValidarProveedorMB implements Serializable {

    private Integer idEmpleado;
    private Empleado empleado = new Empleado();
    private Boolean tokenValido = false;
    private String codigoEmpleado = "";
    private String pass1;
    private String msj = "";

    private static final ResourceBundle UTIL_CORREO = ResourceBundle.getBundle("Bundle");

    @Inject
    private LoginRepo loginRepo;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.containsKey("codigo")) {
            try {
                idEmpleado = Integer.parseInt((new RC4Crypter()).decrypt("ha", params.get("codigo")).split(":")[0]);
                codigoEmpleado = (new RC4Crypter()).decrypt("ha", params.get("codigo")).split(":")[1];

                empleado = loginRepo.findEmpleadoByCodigo(idEmpleado, codigoEmpleado);
                tokenValido = (empleado != null);

                if (empleado == null) {
                    msj = "Código mal generado. Por favor, intente registrar una vez más su correo electrónico.";
                    tokenValido = false;
                } else if (empleado.getClaveAcceso() == null) {

                } else {
                    msj = "Ya se registro la clave de acceso para este empleado. Este código ya NO ES VÁLIDO";
                    tokenValido = false;
                }

            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                tokenValido = false;
                msj = "Código mal generado. Por favor, intente registrar una vez más su correo electrónico.";
            }
        }
    }

    public String getMsj() {
        return msj;
    }

    public String getPass1() {
        return pass1;
    }

    public void setPass1(String pass1) {
        this.pass1 = pass1;
    }

    public Boolean getTokenValido() {
        return tokenValido;
    }

    public String guardarPassword() {
        //encriptación MD5 usando Apache Commons
        empleado.setClaveAcceso(DigestUtils.md5Hex(pass1).toUpperCase());
        empleado.setActivo(true);
        empleado.setFechaActivacion(LocalDate.now());
        loginRepo.guardar(empleado);

        return "inicio?faces-redirect=true";
    }
}
