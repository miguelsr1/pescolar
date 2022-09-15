package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.context.PrimeRequestContext;
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
@Getter
@Setter
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
        //params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "entidadFrio", busqueda).build());
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

}
