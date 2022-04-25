/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.SecurityContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;
import sv.gob.mined.pescolar.model.Anho;
import sv.gob.mined.pescolar.model.Municipio;
import sv.gob.mined.pescolar.model.ProcesoAdquisicion;
import sv.gob.mined.pescolar.model.Usuario;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.utils.Filtro;
import sv.gob.mined.pescolar.utils.JsfUtil;
import sv.gob.mined.pescolar.utils.VarSession;

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
    private Integer idProcesoAdq;

    private Anho anho;
    private ProcesoAdquisicion proceso;
    private Usuario usuario;

    private List<Filtro> params = new ArrayList();

    @Inject
    private SecurityContext securityContext;
    @Inject
    private CatalogoRepo catalogoRepo;

    @PostConstruct
    public void init() {
        params = new ArrayList();
        params.add(new Filtro(Filtro.EQUALS, "idPersona.usuario", securityContext.getCallerPrincipal().getName()));
        usuario = ((Usuario) catalogoRepo.findListByParam(Usuario.class, params).get(0));
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

    public Integer getIdProcesoAdq() {
        return idProcesoAdq;
    }

    public void setIdProcesoAdq(Integer idProcesoAdq) {
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

    public List<ProcesoAdquisicion> getLstProcesoAdquisicion() {
        params.clear();
        params.add(new Filtro(Filtro.EQUALS, "idAnho.id", idAnho));
        return (List<ProcesoAdquisicion>) catalogoRepo.findListByParam(ProcesoAdquisicion.class, params, "id", true);
    }

    public List<Municipio> getLstMunicipios() {
        params.clear();

        if (codigoDepartamento == null) {
            params.add(new Filtro(Filtro.EQUALS, "codigoDepartamento.id", "00"));
        } else {
            params.add(new Filtro(Filtro.EQUALS, "codigoDepartamento.id", codigoDepartamento));
        }

        return (List<Municipio>) catalogoRepo.findListByParam(Municipio.class, params, "id", true);
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
}
