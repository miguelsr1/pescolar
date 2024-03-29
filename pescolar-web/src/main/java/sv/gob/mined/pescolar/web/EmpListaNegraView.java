package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import sv.gob.mined.pescolar.model.ListaNegraEmpresa;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.model.TipoSancion;
import sv.gob.mined.pescolar.model.view.VwCatalogoEntidadEducativa;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.repository.EmpListaNegraRepo;
import sv.gob.mined.pescolar.repository.EmpresaRepo;
import sv.gob.mined.pescolar.repository.EntidadEducativaRepo;
import sv.gob.mined.pescolar.repository.TipoSancionRepo;
import sv.gob.mined.pescolar.utils.Constantes;
import sv.gob.mined.pescolar.utils.JsfUtil;
import sv.gob.mined.pescolar.utils.db.Filtro;
import sv.gob.mined.pescolar.web.proveedor.interno.CargaGeneralView;

/**
 *
 * @author CQuintanilla
 */

@Named
@ViewScoped
public class EmpListaNegraView implements Serializable {

    private ListaNegraEmpresa listaNegraEmpresa;
    private Empresa empresa;
    private List<Filtro> params = new ArrayList();
    private List<Empresa> lstEmpresas = new ArrayList();
    private List<TipoSancion> lstSancion = new ArrayList();
    private String rubroProveedor;
    private String numeroNit;
    private String municipioDepartamento;
    private String domicilio;
    private String razonSocial;
    private String codigoEntidad;
    private Boolean deshabilitado;
    private Boolean deshabilitadoGuardar = true;
    private VwCatalogoEntidadEducativa entidadEducativa;

    @Inject
    private SessionView sessionView;

    @Inject
    private CargaGeneralView cargaGeneralView;

    @Inject
    private EmpListaNegraRepo empListaNegraRepo;

    @Inject
    private TipoSancionRepo tipoSancionRepo;

    @Inject
    private EmpresaRepo empresaRepo;

    @Inject
    private CatalogoRepo catalogoRepo;

    @Inject
    private EntidadEducativaRepo entidadEducativaRepo;

    @PostConstruct
    public void init() {
        listaNegraEmpresa = new ListaNegraEmpresa();
        lstSancion = tipoSancionRepo.findAll();

    }

    public void empresaSeleccionada(SelectEvent event) {
        if (event.getObject() != null) {
            if (event.getObject() instanceof Empresa) {
                empresa = (Empresa) event.getObject();
                deshabilitadoGuardar = false;

                if (getEmpresa().getIdPersoneria().getId().compareTo(Constantes.PERSONA_NATURAL) == 0) {
                    numeroNit = getEmpresa().getIdPersona().getNumeroDui();
                    municipioDepartamento = getEmpresa().getIdPersona().getIdMunicipio().getNombreMunicipio() + ", " + getEmpresa().getIdPersona().getIdMunicipio().getCodigoDepartamento().getNombreDepartamento();
                    domicilio = getEmpresa().getIdPersona().getDomicilio();
                } else {
                    numeroNit = getEmpresa().getNumeroNit();
                    municipioDepartamento = getEmpresa().getIdMunicipio().getNombreMunicipio() + ", " + getEmpresa().getIdMunicipio().getCodigoDepartamento().getNombreDepartamento();
                    domicilio = getEmpresa().getDireccionCompleta();
                }

            }
        } else {
            deshabilitado = false;
            JsfUtil.mensajeAlerta("Debe de seleccionar una empresa");
        }
    }

    public void guardar() {

        listaNegraEmpresa.setIdEmpresa(empresa);
        listaNegraEmpresa.setCodigoEntidad(codigoEntidad);
        listaNegraEmpresa.setEstadoEliminacion((short) 0);
        listaNegraEmpresa.setFecha(new Date());
        listaNegraEmpresa.setUsuario(sessionView.getUsuario().getIdPersona().getUsuario());

        empListaNegraRepo.save(listaNegraEmpresa);
        listaNegraEmpresa = new ListaNegraEmpresa();
        deshabilitadoGuardar = true;

        JsfUtil.mensajeInsert();
    }

    public void eliminar() {

        listaNegraEmpresa.setEstadoEliminacion((short) 1);
        empListaNegraRepo.update(listaNegraEmpresa);
        JsfUtil.mensajeInformacion("El registro ha sido eliminado");
    }

    public void buscarEntidadEducativa() {
        if (codigoEntidad != null && codigoEntidad.length() == 5) {
            deshabilitadoGuardar = false;
            entidadEducativa = entidadEducativaRepo.findByPk(codigoEntidad);
            

            if (entidadEducativa == null) {
                JsfUtil.mensajeAlerta("No se encuentra el centro educativo con código: " + codigoEntidad);
                deshabilitadoGuardar = true;
            }
            
        }

    }

    public ListaNegraEmpresa getListaNegraEmpresa() {
        return listaNegraEmpresa;
    }

    public void setListaNegraEmpresa(ListaNegraEmpresa listaNegraEmpresa) {
        this.listaNegraEmpresa = listaNegraEmpresa;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<Empresa> getLstEmpresas() {
        return lstEmpresas;
    }

    public void setLstEmpresas(List<Empresa> lstEmpresas) {
        this.lstEmpresas = lstEmpresas;
    }

    public List<TipoSancion> getLstSancion() {
        return lstSancion;
    }

    public void setLstSancion(List<TipoSancion> lstSancion) {
        this.lstSancion = lstSancion;
    }

    public String getRubroProveedor() {
        return rubroProveedor;
    }

    public void setRubroProveedor(String rubroProveedor) {
        this.rubroProveedor = rubroProveedor;
    }

    public String getNumeroNit() {
        return numeroNit;
    }

    public void setNumeroNit(String numeroNit) {
        this.numeroNit = numeroNit;
    }

    public String getMunicipioDepartamento() {
        return municipioDepartamento;
    }

    public void setMunicipioDepartamento(String municipioDepartamento) {
        this.municipioDepartamento = municipioDepartamento;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCodigoEntidad() {
        return codigoEntidad;
    }

    public void setCodigoEntidad(String codigoEntidad) {
        this.codigoEntidad = codigoEntidad;
    }

    public Boolean getDeshabilitado() {
        return deshabilitado;
    }

    public void setDeshabilitado(Boolean deshabilitado) {
        this.deshabilitado = deshabilitado;
    }

    public Boolean getDeshabilitadoGuardar() {
        return deshabilitadoGuardar;
    }

    public void setDeshabilitadoGuardar(Boolean deshabilitadoGuardar) {
        this.deshabilitadoGuardar = deshabilitadoGuardar;
    }

    public VwCatalogoEntidadEducativa getEntidadEducativa() {
        return entidadEducativa;
    }

    public void setEntidadEducativa(VwCatalogoEntidadEducativa entidadEducativa) {
        this.entidadEducativa = entidadEducativa;
    }

    public SessionView getSessionView() {
        return sessionView;
    }

    public void setSessionView(SessionView sessionView) {
        this.sessionView = sessionView;
    }

    public EmpresaRepo getEmpresaRepo() {
        return empresaRepo;
    }

    public void setEmpresaRepo(EmpresaRepo empresaRepo) {
        this.empresaRepo = empresaRepo;
    }
    
}
