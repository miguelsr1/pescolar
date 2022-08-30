package sv.gob.mined.pescolar.repository;

import java.io.Serializable;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.utils.JsfUtil;
import sv.gob.mined.pescolar.utils.RC4Crypter;

/**
 *
 * @author MISanchez
 */
@Named
@ViewScoped
public class ValidarProveedorView implements Serializable {

    private String codigo;
    private String nit;
    private String dui;
    private Long idEmpresa;
    private Boolean showPanel = false;
    private Boolean tokenValido = false;
    private String pass1;
    private String op = "";

    private static final ResourceBundle UTIL_CORREO = ResourceBundle.getBundle("Bundle");

    @Inject
    private UsuarioRepo usuarioRepo;
    @Inject
    private CatalogoGlobal catalogoGlobal;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.containsKey("op")) {
            if (params.get("op").equals("1")) {
                try {
                    op = "1";
                    nit = (new RC4Crypter()).decrypt("ha", params.get("codigo")).split(":")[0];
                    tokenValido = usuarioRepo.tokenUsuarioValido(params.get("codigo"));
                } catch (Exception e) {
                }
            }
        } else if (params.containsKey("codigo")) {
            try {
                String idEmpresaStr = (new RC4Crypter()).decrypt("ha", params.get("codigo")).split(":")[0];
                idEmpresa = Long.parseLong(idEmpresaStr);
                tokenValido = true;
            } catch (NumberFormatException e) {
                idEmpresa = null;
            }
        }
    }

    public Boolean getTokenValido() {
        return tokenValido;
    }

    public void setTokenValido(Boolean tokenValido) {
        this.tokenValido = tokenValido;
    }

    public String getPass1() {
        return pass1;
    }

    public void setPass1(String pass1) {
        this.pass1 = pass1;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public Boolean getShowPanel() {
        return showPanel;
    }

    public void setShowPanel(Boolean showPanel) {
        this.showPanel = showPanel;
    }

    public void validarProveedor() {
        String tituloEmail = UTIL_CORREO.getString("prov.email.titulo");
        String cuerpoEmail = UTIL_CORREO.getString("prov.email.mensaje");

        int validar = usuarioRepo.validarCodigoSegEmpresa(codigo, nit, dui, tituloEmail, cuerpoEmail);
        Empresa emp;
        switch (validar) {
            case 1:
                emp = usuarioRepo.findEmpresaByDuiOrNit(nit, false);
                JsfUtil.mensajeInformacion("Se ha enviado un correo a <b>" + emp.getIdPersona().getEmail() + "</b> para activar su usuario de acceso. <b>Esta es la única vez que podrá realizar este paso</b>.");
                break;
            case 2:
                JsfUtil.mensajeError("Los valores ingresados no coinciden con ningún proveedor");
                break;
            case 3:
                emp = usuarioRepo.findEmpresaByDuiOrNit(nit, false);
                JsfUtil.mensajeInformacion("El correo registrado [<b>" + emp.getIdPersona().getEmail() + "</b>] no es un correo válido, por favor escribir a <a href='mailto:carlos.villegas@mined.edu.sv'>Carlos Villegas</a> para corregir su correo.");
                break;
        }

        showPanel = (validar == 1);
    }

    public String guardarPasswordProv() {
        if (op.equals("1")) {
            usuarioRepo.guardarPasswordPer(nit, catalogoGlobal.encriptar(pass1));
        } else {
            usuarioRepo.guardarPasswordProv(idEmpresa, catalogoGlobal.encriptar(pass1));
        }

        return "inicio.xhtml";
    }
}
