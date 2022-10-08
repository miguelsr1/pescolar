/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;
import lombok.Setter;
import sv.gob.mined.pescolar.model.ProcesoAdquisicion;
import sv.gob.mined.pescolar.model.RubroPorCe;
import sv.gob.mined.pescolar.model.RubrosAmostrarInteres;
import sv.gob.mined.pescolar.model.view.VwCatalogoEntidadEducativa;
import sv.gob.mined.pescolar.repository.EntidadEducativaRepo;
import sv.gob.mined.pescolar.repository.RubroPorCeRepo;
import sv.gob.mined.pescolar.repository.RubrosAmostrarInteresRepo;
import sv.gob.mined.pescolar.utils.JsfUtil;

/**
 *
 * @author CQuintanilla
 */
@Getter
@Setter
@Named
@ViewScoped
public class RubroPorCeView implements Serializable {

    private String codigoEntidad;
    private String codigoRubro;

    private Boolean deshabilitado = true;

    private ProcesoAdquisicion procesoAdquisicion = new ProcesoAdquisicion();
    private VwCatalogoEntidadEducativa entidadEducativa;
    private RubroPorCe rubroporce = new RubroPorCe();

    private List<RubrosAmostrarInteres> listrubrosamostrarinteres = new ArrayList();
    private List<RubroPorCe> listrubroporce = new ArrayList();

    @Inject
    private SessionView sessionView;
    @Inject
    private RubrosAmostrarInteresRepo rubrosamostrarinteresrepo;
    @Inject
    private EntidadEducativaRepo entidadEducativaRepo;
    @Inject
    private RubroPorCeRepo rubroporceRepo;

    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;

    @PostConstruct
    public void init() {
        prepareEdit();
        listrubrosamostrarinteres = rubrosamostrarinteresrepo.findAll();
        rubroporce = null;
    }

    public void prepareCreate() {
        setDeshabilitado((Boolean) false);
    }

    public void prepareEdit() {
        setDeshabilitado((Boolean) false);
        setCodigoEntidad("");
        entidadEducativa = new VwCatalogoEntidadEducativa();

    }

    public void nuevo() {
        codigoEntidad = "";
        codigoRubro = "";
        rubroporce = null;
        listrubroporce.clear();
        entidadEducativa = null;
    }

    public void buscarEntidadEducativa() {
        if (getCodigoEntidad() != null && getCodigoEntidad().length() == 5) {
            rubroporce = null;

            //if (getProcesoAdquisicion() != null) {
            entidadEducativa = entidadEducativaRepo.findByPk(getCodigoEntidad());

            if (entidadEducativa == null) {
                JsfUtil.mensajeAlerta("No se encuentra el centro educativo con código: " + getCodigoEntidad());
            } else {
                listrubroporce = rubroporceRepo.listarnoborradosporce(codigoEntidad); 
            }
            //} else {
            //JsfUtil.mensajeAlerta("Debe de seleccionar un proceso de adquisición.");
            //}

            ////PrimeFaces.current().executeScript("actualizarDatos();");
        }
    }

    public Boolean validarGuardar() {
        /*
        if (getProcesoAdquisicion() == null) {
            JsfUtil.mensajeAlerta("Debe de seleccionar un proceso de adquisición.");
            return false;
        }
        */
        if (codigoEntidad == null) {
            JsfUtil.mensajeAlerta("Debe de seleccionar un centro educativo.");
            return false;
        }
        if (codigoEntidad.isEmpty()) {
            JsfUtil.mensajeAlerta("Debe de seleccionar un centro educativo.");
            return false;
        }
        if (codigoRubro == null) {
            JsfUtil.mensajeAlerta("Debe de seleccionar un rubro a contratar.");
            return false;
        }
        if (codigoRubro.isEmpty()) {
            JsfUtil.mensajeAlerta("Debe de seleccionar un rubro a contratar.");
            return false;
        }
        if (codigoRubro.equals("0")) {
            JsfUtil.mensajeAlerta("Debe de seleccionar un rubro a contratar.");
            return false;
        }

        return true;
    }

    public void guardar() {
        if (validarGuardar()) {
            if (rubroporceRepo.existeRubro(codigoEntidad, Long.valueOf(codigoRubro))) {
                JsfUtil.mensajeAlerta("El rubro seleccionado ya existe en la lista.");
            } else {
                rubroporce = new RubroPorCe();
                rubroporce.setCodigoEntidad(entidadEducativa);
                rubroporce.setIdProcesoAdq(null);
                rubroporce.setIdRubroInteres(rubrosamostrarinteresrepo.listarrubroporid(codigoRubro));
                rubroporce.setFecha(LocalDate.now());
                rubroporce.setEstadoEliminacion(Boolean.FALSE);
                rubroporce.setUsuario(sessionView.getUsuario().getIdPersona().getUsuario());
                rubroporceRepo.save(rubroporce);
                rubroporce = null;
                listrubroporce = rubroporceRepo.listarnoborradosporce(codigoEntidad);
                JsfUtil.mensajeInsert();
            }
        }
    }

    public void eliminar() {
        if (rubroporce != null) {
            if (rubroporce.getId() != null) {
                rubroporce.setEstadoEliminacion(Boolean.TRUE);
                rubroporce.setFecha(LocalDate.now());
                rubroporce.setUsuario(sessionView.getUsuario().getIdPersona().getUsuario());
                rubroporceRepo.update(rubroporce);
                listrubroporce = rubroporceRepo.listarnoborradosporce(codigoEntidad); 
                JsfUtil.mensajeInformacion("El registro ha sido eliminado");
            } else {
                JsfUtil.mensajeAlerta("Debe de seleccionar un registro a eliminar.");
            }
        } else {
            JsfUtil.mensajeAlerta("Debe de seleccionar un registro a eliminar.");
        }

    }

}
