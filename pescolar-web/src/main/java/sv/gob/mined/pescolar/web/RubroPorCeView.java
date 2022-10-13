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
import javax.persistence.Query;
import lombok.Getter;
import lombok.Setter;
import sv.gob.mined.pescolar.model.Departamento;
import sv.gob.mined.pescolar.model.Municipio;
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
    private String tiposeleccion;
    private String iddepartamento;
    private String idmunicipio;

    private Boolean deshabilitado = true;

    private ProcesoAdquisicion procesoAdquisicion = new ProcesoAdquisicion();
    private VwCatalogoEntidadEducativa entidadEducativa;
    private RubroPorCe rubroporce = new RubroPorCe();

    private List<RubrosAmostrarInteres> listrubrosamostrarinteres = new ArrayList();
    private List<RubroPorCe> listrubroporce = new ArrayList();
    private List<RubroPorCe> listseleccionrubroporce = new ArrayList();

    private List<VwCatalogoEntidadEducativa> listentidadeseducativas = new ArrayList();
    private List<VwCatalogoEntidadEducativa> seleccionentidadEducativa = new ArrayList();
    private List<Departamento> listdepartamento = new ArrayList();
    private List<Municipio> listmunicipio = new ArrayList();

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
        listentidadeseducativas.clear();
        Query q = em.createQuery("select dep from Departamento dep order by dep.id", Departamento.class);
        listdepartamento = q.getResultList();
        listmunicipio.clear();
        seleccionentidadEducativa.clear();
        tiposeleccion = "0";
        iddepartamento = "";
        idmunicipio = "";
        rubroporce = null;
        listseleccionrubroporce.clear();
    }

    public void prepareCreate() {
        setDeshabilitado((Boolean) false);
    }

    public void prepareEdit() {
        setDeshabilitado((Boolean) false);
        setCodigoEntidad("");
        entidadEducativa = new VwCatalogoEntidadEducativa();

        listentidadeseducativas.clear();
        listmunicipio.clear();
        seleccionentidadEducativa.clear();
        tiposeleccion = "0";
        iddepartamento = "";
        idmunicipio = "";
        listseleccionrubroporce.clear();

    }

    public void nuevo() {
        codigoEntidad = "";
        codigoRubro = "";
        rubroporce = null;
        listrubroporce.clear();
        entidadEducativa = null;

        listentidadeseducativas.clear();
        listmunicipio.clear();
        seleccionentidadEducativa.clear();
        tiposeleccion = "0";
        iddepartamento = "";
        idmunicipio = "";
        listseleccionrubroporce.clear();
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

    public void onSelectDepartamento() {
        seleccionentidadEducativa.clear();
        listmunicipio.clear();
        listentidadeseducativas.clear();
        listseleccionrubroporce.clear();

        if (iddepartamento == null) {
            iddepartamento = "";
        }

        if (iddepartamento.equals("")) {
            idmunicipio = "";
        } else {
            Query q, q2;
            if (iddepartamento.equals("00")) {
                q = em.createQuery("select mun from Municipio mun order by mun.codigoDepartamento asc, mun.codigoMunicipio asc", Municipio.class);
            } else {
                q = em.createQuery("select mun from Municipio mun where mun.codigoDepartamento = '" + iddepartamento + "' order by mun.codigoDepartamento asc, mun.codigoMunicipio asc", Municipio.class);
                q2 = em.createQuery("select vw from VwCatalogoEntidadEducativa vw where vw.codigoDepartamento = '" + iddepartamento + "' order by vw.codigoDepartamento asc, vw.codigoMunicipio asc, vw.codigoCanton asc", VwCatalogoEntidadEducativa.class);
                listentidadeseducativas = q2.getResultList();
            }
            listmunicipio = q.getResultList();
        }
    }

    public void onSelectMunicipio() {
        seleccionentidadEducativa.clear();
        listentidadeseducativas.clear();
        listseleccionrubroporce.clear();

        // System.out.println("idMunicipio:" + idmunicipio);
        // System.out.println("select vw from VwCatalogoEntidadEducativa vw where vw.idMunicipio.id = " + idmunicipio + " order by vw.codigoDepartamento asc, vw.codigoMunicipio asc, vw.codigoCanton asc");
        if (idmunicipio == null) {
            idmunicipio = "";
        }

        if (idmunicipio.equals("")) {
            if (iddepartamento == null) {
                iddepartamento = "";
            }

            if (!iddepartamento.equals("00") && !iddepartamento.equals("")) {
                Query q;
                q = em.createQuery("select vw from VwCatalogoEntidadEducativa vw where vw.codigoDepartamento = '" + iddepartamento + "' order by vw.codigoDepartamento asc, vw.codigoMunicipio asc, vw.codigoCanton asc", VwCatalogoEntidadEducativa.class);
                listentidadeseducativas = q.getResultList();
            }

        } else {
            Query q;
            q = em.createQuery("select vw from VwCatalogoEntidadEducativa vw where vw.idMunicipio.id = " + idmunicipio + " order by vw.codigoDepartamento asc, vw.codigoMunicipio asc, vw.codigoCanton asc", VwCatalogoEntidadEducativa.class);
            listentidadeseducativas = q.getResultList();
        }
    }

    public void onSelectListaCE() {
        String listcodigos = "";
        listrubroporce.clear();
        listseleccionrubroporce.clear();

        for (int i = 0; i < seleccionentidadEducativa.size(); i++) {
            listcodigos = listcodigos + "," + "'" + seleccionentidadEducativa.get(i).getCodigoEntidad() + "'";
        }

        if (!seleccionentidadEducativa.isEmpty()) {
            if (listcodigos.length() > 1) {
                Query q;
                q = em.createQuery("SELECT rpc "
                        + "FROM RubroPorCe rpc "
                        + "WHERE rpc.codigoEntidad.codigoEntidad in (" + listcodigos.substring(1) + ") "
                        + "and rpc.estadoEliminacion = 0 ", RubroPorCe.class);
                listrubroporce = q.getResultList();
            }

        }
    }

    public void onClickOption() {
        codigoEntidad = "";
        codigoRubro = "";
        rubroporce = null;
        listrubroporce.clear();
        entidadEducativa = null;

        listentidadeseducativas.clear();
        listmunicipio.clear();
        seleccionentidadEducativa.clear();
        iddepartamento = "";
        idmunicipio = "";
        listseleccionrubroporce.clear();
    }

    public void guardar() {
        if (tiposeleccion.equals("0")) {
            guardarporce();
        } else {
            guardarporlista();
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

    public void guardarporce() {
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

    public Boolean validarGuardarPorLista() {

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

    public void guardarporlista() {
        if (validarGuardarPorLista()) {
            Boolean guardado = false;
            String listcodigos = "";

            for (int i = 0; i < seleccionentidadEducativa.size(); i++) {
                listcodigos = listcodigos + "," + "'" + seleccionentidadEducativa.get(i).getCodigoEntidad() + "'";

                if (rubroporceRepo.existeRubro(seleccionentidadEducativa.get(i).getCodigoEntidad(), Long.valueOf(codigoRubro))) {
                    //JsfUtil.mensajeAlerta("El rubro seleccionado ya existe en la lista.");
                } else {
                    rubroporce = new RubroPorCe();
                    rubroporce.setCodigoEntidad(seleccionentidadEducativa.get(i));
                    rubroporce.setIdProcesoAdq(null);
                    rubroporce.setIdRubroInteres(rubrosamostrarinteresrepo.listarrubroporid(codigoRubro));
                    rubroporce.setFecha(LocalDate.now());
                    rubroporce.setEstadoEliminacion(Boolean.FALSE);
                    rubroporce.setUsuario(sessionView.getUsuario().getIdPersona().getUsuario());
                    rubroporceRepo.save(rubroporce);
                    rubroporce = null;

                    guardado = true;

                }
            }

            if (!seleccionentidadEducativa.isEmpty()) {
                if (listcodigos.length() > 1) {
                    Query q;
                    q = em.createQuery("SELECT rpc "
                            + "FROM RubroPorCe rpc "
                            + "WHERE rpc.codigoEntidad.codigoEntidad in (" + listcodigos.substring(1) + ") "
                            + "and rpc.estadoEliminacion = 0 ", RubroPorCe.class);
                    listrubroporce = q.getResultList();
                }

            }

            if (guardado) {
                JsfUtil.mensajeInsert();
            } else {
                JsfUtil.mensajeAlerta("Proceso guardar finalizado con éxito. No se agregaron nuevos registros.");
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

    public void eliminarenlote() {
        if (!listseleccionrubroporce.isEmpty()) {
            for (int i = 0; i < listseleccionrubroporce.size(); i++) {
                if (listseleccionrubroporce.get(i) != null) {
                    if (listseleccionrubroporce.get(i).getId() != null) {
                        listseleccionrubroporce.get(i).setEstadoEliminacion(Boolean.TRUE);
                        listseleccionrubroporce.get(i).setFecha(LocalDate.now());
                        listseleccionrubroporce.get(i).setUsuario(sessionView.getUsuario().getIdPersona().getUsuario());
                        rubroporceRepo.update(listseleccionrubroporce.get(i));
                        JsfUtil.mensajeInformacion("El registro ha sido eliminado");
                    }
                }
            }

            if (tiposeleccion.equals("0")) {
                listrubroporce = rubroporceRepo.listarnoborradosporce(codigoEntidad);
            } else {
                String listcodigos = "";

                for (int i = 0; i < seleccionentidadEducativa.size(); i++) {
                    listcodigos = listcodigos + "," + "'" + seleccionentidadEducativa.get(i).getCodigoEntidad() + "'";
                }

                if (!seleccionentidadEducativa.isEmpty()) {
                    if (listcodigos.length() > 1) {
                        Query q;
                        q = em.createQuery("SELECT rpc "
                                + "FROM RubroPorCe rpc "
                                + "WHERE rpc.codigoEntidad.codigoEntidad in (" + listcodigos.substring(1) + ") "
                                + "and rpc.estadoEliminacion = 0 ", RubroPorCe.class);
                        listrubroporce = q.getResultList();
                    }

                }

            }

        } else {
            JsfUtil.mensajeAlerta("Debe de seleccionar un registro a eliminar.");
        }

    }

}
