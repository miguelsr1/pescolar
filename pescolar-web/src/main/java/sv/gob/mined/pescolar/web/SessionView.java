package sv.gob.mined.pescolar.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.SecurityContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;
import sv.gob.mined.pescolar.model.Anho;
import sv.gob.mined.pescolar.model.Municipio;
import sv.gob.mined.pescolar.model.ProcesoAdquisicion;
import sv.gob.mined.pescolar.model.RubrosAmostrarInteres;
import sv.gob.mined.pescolar.model.Usuario;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.utils.db.Filtro;
import sv.gob.mined.pescolar.utils.JsfUtil;
import sv.gob.mined.pescolar.utils.VarSession;
import sv.gob.mined.pescolar.utils.enums.TipoOperador;

/**
 *
 * @author misanchez
 */
@SuppressWarnings("serial")
@SessionScoped
@Named
public class SessionView implements Serializable {

    private Boolean usuarioDepartamental = false;

    private String anhoProceso = "";
    private String codigoDepartamento;
    private String ubicacion;
    private Long idAnho;
    private Long idMunicipio;
    private Long idRubro;
    private Long idProcesoAdq;

    private Anho anho;
    private ProcesoAdquisicion proceso;
    private Usuario usuario;
    private Municipio municipio;

    private List<Filtro> params = new ArrayList();

    @Inject
    private SecurityContext securityContext;
    @Inject
    private CatalogoRepo catalogoRepo;

    @PostConstruct
    public void init() {
        params = new ArrayList();
        params.add(new Filtro(TipoOperador.EQUALS, "idPersona.usuario", securityContext.getCallerPrincipal().getName()));
        usuario = ((Usuario) catalogoRepo.findListByParam(Usuario.class, params).get(0));

        //Recuperarción de variables almacenadas como COOKIES
        recuperarValoresCookies();
    }

    private void recuperarValoresCookies() {
        Map<String, Object> requestCookieMap = FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();

        if (requestCookieMap.containsKey("anho")) {
            idAnho = Long.parseLong(((Cookie) requestCookieMap.get("anho")).getValue());
            anho = catalogoRepo.findEntityByPk(Anho.class, idAnho);
        } else {
            idAnho = 1l;
        }

        if (requestCookieMap.containsKey("proceso")) {
            idProcesoAdq = Long.parseLong(((Cookie) requestCookieMap.get("proceso")).getValue());
            proceso = catalogoRepo.findEntityByPk(ProcesoAdquisicion.class, idProcesoAdq);
            anhoProceso = anho.getAnho() + " :: " + proceso.getDescripcionProcesoAdq();
        }

        if (requestCookieMap.containsKey("rubro")) {
            idRubro = Long.parseLong(((Cookie) requestCookieMap.get("rubro")).getValue());
        }

        codigoDepartamento = usuario.getCodigoDepartamento().getId();

        if (codigoDepartamento != null) {
            if (VarSession.isCookie("municipio")) {
                idMunicipio = Long.parseLong(VarSession.getCookieValue("municipio"));
                municipio = catalogoRepo.findEntityByPk(Municipio.class, idMunicipio);
                ubicacion = JsfUtil.getNombreDepartamentoByCodigo(codigoDepartamento) + ", " + municipio.getNombreMunicipio();
            }
            usuarioDepartamental = !codigoDepartamento.equals("00");

        } else if (VarSession.isCookie("departamento")) {
            codigoDepartamento = VarSession.getCookieValue("departamento");
        }
        if (VarSession.isCookie("municipio") && idMunicipio == null) {
            idMunicipio = Long.parseLong(VarSession.getCookieValue("municipio"));
            municipio = catalogoRepo.findEntityByPk(Municipio.class, idMunicipio);
            ubicacion = JsfUtil.getNombreDepartamentoByCodigo(codigoDepartamento) + ", " + municipio.getNombreMunicipio();
        }
    }

    public Long getIdRubro() {
        return idRubro;
    }

    public String getAnhoProceso() {
        return anhoProceso;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public Boolean getUsuarioDepartamental() {
        return usuarioDepartamental;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Long getIdAnho() {
        return idAnho;
    }

    public void setIdAnho(Long idAnho) {
        this.idAnho = idAnho;
    }

    public Long getIdProcesoAdq() {
        return idProcesoAdq;
    }

    public void setIdProcesoAdq(Long idProcesoAdq) {
        this.idProcesoAdq = idProcesoAdq;
    }

    public String getCodigoDepartamento() {
        return codigoDepartamento;
    }

    public void setCodigoDepartamento(String codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }

    public Long getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Long idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public ProcesoAdquisicion getProceso() {
        return proceso;
    }

    public void setProceso(ProcesoAdquisicion proceso) {
        this.proceso = proceso;
    }

    public List<ProcesoAdquisicion> getLstProcesoAdquisicion() {
        params.clear();
        params.add(new Filtro(TipoOperador.EQUALS, "idAnho.id", idAnho));
        return (List<ProcesoAdquisicion>) catalogoRepo.findListByParam(ProcesoAdquisicion.class, params, "id", true);
    }

    public List<Municipio> getLstMunicipios() {
        params.clear();

        if (codigoDepartamento == null) {
            params.add(new Filtro(TipoOperador.EQUALS, "codigoDepartamento.id", "00"));
        } else {
            params.add(new Filtro(TipoOperador.EQUALS, "codigoDepartamento.id", codigoDepartamento));
        }

        return (List<Municipio>) catalogoRepo.findListByParam(Municipio.class, params, "id", true);
    }

    public List<RubrosAmostrarInteres> getLstRubros() {
        return catalogoRepo.findAllRubrosByIdProceso(getIdProcesoAdq());
    }

    public void guardarParametros() throws IOException {
        String msj = "Debe Seleccionar: ";
        if (codigoDepartamento == null || idMunicipio == null) {
            if (!msj.replace("Debe Seleccionar: ", "").isEmpty()) {
                msj += "<br/>";
            }
            msj += "Un departamento y municipio";
        }

        if (idAnho == null || idProcesoAdq == null) {
            if (!msj.replace("Debe Seleccionar: ", "").isEmpty()) {
                msj += "<br/>";
            }
            msj += "El anho y un proceso";
        }

        if (!msj.replace("Debe Seleccionar: ", "").isEmpty()) {
            JsfUtil.mensajeAlerta(msj);
        } else {
            ubicacion = JsfUtil.getNombreDepartamentoByCodigo(codigoDepartamento) + ", " + ((Municipio) catalogoRepo.findEntityByPk(Municipio.class, idMunicipio)).getNombreMunicipio();
            anho = ((Anho) catalogoRepo.findEntityByPk(Anho.class, idAnho));
            proceso = ((ProcesoAdquisicion) catalogoRepo.findEntityByPk(ProcesoAdquisicion.class, idProcesoAdq));
            anhoProceso = anho.getAnho() + " :: " + proceso.getDescripcionProcesoAdq();

            VarSession.crearCookie("departamento", codigoDepartamento);
            VarSession.crearCookie("municipio", idMunicipio.toString());
            VarSession.crearCookie("anho", anho.getId().toString());
            VarSession.crearCookie("proceso", proceso.getId().toString());

            PrimeFaces.current().executeScript("buttonConfig()");

            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        }
    }

    public Boolean getIsUsuarioDigitador() {
        if (proceso.getDescripcionProcesoAdq().contains("SOBREDEMANDA")) {
            switch (usuario.getIdTipoUsuario().getIdTipoUsuario().intValue()) {
                case 1:
                case 2:
                case 6:
                    return false;
                default:
                    return true;

            }
        } else {
            return (usuario.getIdTipoUsuario().getIdTipoUsuario().intValue() != 1);
        }
    }

    public void logout() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getSessionMap().clear();
            ExternalContext externalContext = context.getExternalContext();
            externalContext.redirect(((ServletContext) externalContext.getContext()).getContextPath() + "/index.mined");
            System.gc();
        } catch (IOException ex) {
            Logger.getLogger(SessionView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
