package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import sv.gob.mined.pescolar.model.Departamento;
import sv.gob.mined.pescolar.model.Municipio;
import sv.gob.mined.pescolar.model.CeClimaFrio;
import sv.gob.mined.pescolar.model.view.VwCeClimaFrio;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.repository.CeClimaFrioRepo;
import sv.gob.mined.pescolar.repository.EntidadEducativaClimaFrio;
import sv.gob.mined.pescolar.utils.JsfUtil;
import sv.gob.mined.pescolar.utils.db.Filtro;
import sv.gob.mined.pescolar.utils.enums.TipoOperador;

/**
 *
 * @author CQuintanilla
 */

@Named
@ViewScoped
public class CeClimaFrioView implements Serializable {

    private CeClimaFrio entidadEducativa;
    private VwCeClimaFrio entidadBorrar;
    private List<Filtro> params = new ArrayList();
    private List<VwCeClimaFrio> lstCheck = new ArrayList();
    private List<VwCeClimaFrio> lstCeNoSeleccionados = new ArrayList();
    private List<VwCeClimaFrio> lstCeClimaFrio = new ArrayList();
    private String codigoDepartamento;
    private Long idMunicipio;
    private String orden;
    private Boolean deshabilitado;

    @Inject
    private SessionView sessionView;

    @Inject
    private EntidadEducativaClimaFrio entidadCFRepo;

    @Inject
    private CatalogoRepo catalogoRepo;

    @Inject
    private CeClimaFrioRepo frioRepo;

    @PostConstruct
    public void init() {

    }

    public void buscar() {
        buscar(0);
        buscar(1);

    }

    public void buscar(Integer busqueda) {
        params.clear();
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "codigoDepartamento", codigoDepartamento).build());
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idMunicipio", idMunicipio).build());
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "estadoEliminacion", busqueda).build());  
        
        if (busqueda == 1) {
            
            lstCeNoSeleccionados = (List<VwCeClimaFrio>) entidadCFRepo.findListByParam(params, orden, Boolean.TRUE);
        } else {
            lstCeClimaFrio = (List<VwCeClimaFrio>) entidadCFRepo.findListByParam(params, orden, Boolean.TRUE);
        }
    }

    public void guardar() {

        if (!lstCheck.isEmpty()) {

            for (VwCeClimaFrio n : lstCheck) {
                entidadEducativa = new CeClimaFrio();
                entidadEducativa.setCodigoEntidad(n.getCodigoEntidad());
                entidadEducativa.setEstadoEliminacion((short) 0);
                entidadEducativa.setFecha(new Date());
                entidadEducativa.setUsuario(sessionView.getUsuario().getIdPersona().getUsuario());

                frioRepo.save(entidadEducativa);
                //lstCeClimaFrio.add(n);

            }
            buscar();
            JsfUtil.mensajeInsert();
            PrimeFaces.current().executeScript("PF('tblCeDisponibles').unselectAllRows();");
        }
    }

    public void eliminar() {
        params.clear();
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "codigoEntidad", entidadBorrar.getCodigoEntidad()).build());
        entidadEducativa = frioRepo.findEntityByParam(params);      
        entidadEducativa.setEstadoEliminacion((short)1);
        frioRepo.update(entidadEducativa);
        buscar();
        JsfUtil.mensajeInformacion("El registro ha sido eliminado");
    }

    public void onAllRowsSelect(ToggleSelectEvent tse) {

    }

    public void onRowSelect(SelectEvent se) {
        lstCheck.add((VwCeClimaFrio) se.getObject());
        //lstCheck.forEach((n) -> System.out.println(n.getCodigoEntidad()));

    }

    public void onRowUnselect(SelectEvent use) {
        lstCheck.remove((VwCeClimaFrio) use.getObject());
    }

    public List<Departamento> getLstDepartamento() {
        return catalogoRepo.findListByParam(Departamento.class, new ArrayList(), "id", true);
    }

    public List<Municipio> getLstMunicipio() {
        params.clear();
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "codigoDepartamento.id", codigoDepartamento).build());
        return (List<Municipio>) catalogoRepo.findListByParam(Municipio.class, params, "id", false);
    }

    public CeClimaFrio getEntidadEducativa() {
        return entidadEducativa;
    }

    public void setEntidadEducativa(CeClimaFrio entidadEducativa) {
        this.entidadEducativa = entidadEducativa;
    }

    public VwCeClimaFrio getEntidadBorrar() {
        return entidadBorrar;
    }

    public void setEntidadBorrar(VwCeClimaFrio entidadBorrar) {
        this.entidadBorrar = entidadBorrar;
    }

    public List<Filtro> getParams() {
        return params;
    }

    public void setParams(List<Filtro> params) {
        this.params = params;
    }

    public List<VwCeClimaFrio> getLstCheck() {
        return lstCheck;
    }

    public void setLstCheck(List<VwCeClimaFrio> lstCheck) {
        this.lstCheck = lstCheck;
    }

    public List<VwCeClimaFrio> getLstCeNoSeleccionados() {
        return lstCeNoSeleccionados;
    }

    public void setLstCeNoSeleccionados(List<VwCeClimaFrio> lstCeNoSeleccionados) {
        this.lstCeNoSeleccionados = lstCeNoSeleccionados;
    }

    public List<VwCeClimaFrio> getLstCeClimaFrio() {
        return lstCeClimaFrio;
    }

    public void setLstCeClimaFrio(List<VwCeClimaFrio> lstCeClimaFrio) {
        this.lstCeClimaFrio = lstCeClimaFrio;
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

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public Boolean getDeshabilitado() {
        return deshabilitado;
    }

    public void setDeshabilitado(Boolean deshabilitado) {
        this.deshabilitado = deshabilitado;
    }

    public EntidadEducativaClimaFrio getEntidadCFRepo() {
        return entidadCFRepo;
    }

    public void setEntidadCFRepo(EntidadEducativaClimaFrio entidadCFRepo) {
        this.entidadCFRepo = entidadCFRepo;
    }
    
    

}
