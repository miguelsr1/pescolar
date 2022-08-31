package sv.gob.mined.pescolar.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.SecurityContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.PrimeFaces;
import sv.gob.mined.pescolar.model.Anho;
import sv.gob.mined.pescolar.model.Municipio;
import sv.gob.mined.pescolar.model.ProcesoAdquisicion;
import sv.gob.mined.pescolar.model.RubrosAmostrarInteres;
import sv.gob.mined.pescolar.model.Usuario;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.utils.db.Filtro;
import sv.gob.mined.pescolar.utils.JsfUtil;
import sv.gob.mined.pescolar.utils.enums.TipoOperador;

/**
 *
 * @author misanchez
 */
@Named
@SessionScoped
public class SessionView implements Serializable {

    private Boolean usuarioDepartamental = false;
    private Boolean usuarioSoloLectura = false;

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
        params.add(Filtro.builder().tipoOperador(TipoOperador.EQUALS).clave("idPersona.usuario").valor(securityContext.getCallerPrincipal().getName()).build());
        usuario = ((Usuario) catalogoRepo.findListByParam(Usuario.class, params).get(0));

        usuarioSoloLectura = (usuario.getIdTipoUsuario().getIdTipoUsuario().intValue() == 8);
        //Recuperarción de variables almacenadas como COOKIES
        recuperarValoresCookies();
    }

    public Boolean getUsuarioSoloLectura() {
        return usuarioSoloLectura;
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
            if (isCookie("municipio")) {
                idMunicipio = Long.parseLong(getCookieValue("municipio"));
                municipio = catalogoRepo.findEntityByPk(Municipio.class, idMunicipio);
                ubicacion = JsfUtil.getNombreDepartamentoByCodigo(codigoDepartamento) + ", " + municipio.getNombreMunicipio();
            }
            usuarioDepartamental = !codigoDepartamento.equals("00");

        } else if (isCookie("departamento")) {
            codigoDepartamento = getCookieValue("departamento");
        }
        if (isCookie("municipio") && idMunicipio == null) {
            idMunicipio = Long.parseLong(getCookieValue("municipio"));
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
        this.idProcesoAdq = proceso.getId();
    }

    public List<ProcesoAdquisicion> getLstProcesoAdquisicion() {
        params.clear();
        params.add(Filtro.builder().tipoOperador(TipoOperador.EQUALS).clave("idAnho.id").valor(idAnho).build());
        return (List<ProcesoAdquisicion>) catalogoRepo.findListByParam(ProcesoAdquisicion.class, params, "id", true);
    }

    public List<Municipio> getLstMunicipios() {
        params.clear();

        if (codigoDepartamento == null) {
            params.add(Filtro.builder().tipoOperador(TipoOperador.EQUALS).clave("codigoDepartamento.id").valor("00").build());
        } else {
            params.add(Filtro.builder().tipoOperador(TipoOperador.EQUALS).clave("codigoDepartamento.id").valor(codigoDepartamento).build());
        }

        return (List<Municipio>) catalogoRepo.findListByParam(Municipio.class, params, "id", true);
    }

    public List<RubrosAmostrarInteres> getLstRubros() {
        return catalogoRepo.findAllRubrosByIdProceso(getProceso().getId());
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

            crearCookie("departamento", codigoDepartamento);
            crearCookie("municipio", idMunicipio.toString());
            crearCookie("anho", anho.getId().toString());
            crearCookie("proceso", proceso.getId().toString());

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

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().clear();
        context.getExternalContext().invalidateSession();
        return "/inicio.xhtml?faces-redirect=true";
    }

    public String getNombreMunicipio() {
        return municipio.getNombreMunicipio();
    }

    public Boolean isCookie(String name) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getRequestCookieMap().containsKey(name);
    }

    public String getCookieValue(String name) {
        FacesContext context = FacesContext.getCurrentInstance();
        return ((Cookie) context.getExternalContext().getRequestCookieMap().get(name)).getValue();
    }

    public void crearCookie(String nombre, String valor) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        Cookie cookie = new Cookie(nombre, valor);
        cookie.setMaxAge(1 * 365 * 24 * 60 * 60);
        cookie.setPath(request.getContextPath());

        ((HttpServletResponse) context.getExternalContext().getResponse()).addCookie(cookie);
    }

    public Object getVariableSession(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().get(key);
    }

    public int getVariableSessionED() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getExternalContext().getSessionMap().containsKey("estadoEdicion")) {
            return Integer.parseInt(context.getExternalContext().getSessionMap().get("estadoEdicion").toString());
        } else {
            return 0;
        }
    }

    public void setVariableSessionED(String valor) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("estadoEdicion", valor);
    }

    public void setVariableSession(String key, Object value) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put(key, value);
    }

    public Boolean isVariableSession(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().containsKey(key);
    }

    public void removeVariableSession(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().remove(key);
    }

    public void setAnhoProvedor(Long idAnho) {
        this.idAnho = idAnho;
        anho = catalogoRepo.findEntityByPk(Anho.class, idAnho);
        proceso = anho.getProcesoAdquisicionList().stream().filter(pro -> pro.getPadreIdProcesoAdq() == null).findFirst().get();
        anhoProceso = anho.getAnho() + " :: " + proceso.getDescripcionProcesoAdq();
    }
}
