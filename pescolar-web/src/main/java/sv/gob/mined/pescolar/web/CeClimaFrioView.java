package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import sv.gob.mined.pescolar.model.Departamento;
import sv.gob.mined.pescolar.model.Municipio;
import sv.gob.mined.pescolar.model.view.VwCatalogoEntidadEducativa;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.repository.EntidadEducativaRepo;
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

    private VwCatalogoEntidadEducativa entidadEducativa;
    private List<Filtro> params = new ArrayList();
    private String codigoDepartamento;
    private Long idMunicipio;
    private String orden;
    
    List<VwCatalogoEntidadEducativa> lstCentrosEducativos = new ArrayList();
    
    @Inject
    private SessionView sessionView;
    
    @Inject
    private EntidadEducativaRepo entidadRepo;
    
    @Inject
    private CatalogoRepo catalogoRepo;

    @PostConstruct
    public void init() {
        
    }

    public void buscar() {
        params.clear();
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "codigoDepartamento.id", codigoDepartamento).build());
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idMunicipio.id", idMunicipio).build());
        lstCentrosEducativos = (List<VwCatalogoEntidadEducativa>) entidadRepo.findListByParam(params, orden, Boolean.TRUE);
        
    }
    
   
    public void guardar() {
//        if (tecnicoProveedor.getIdTecnico() == null) {
//            tecnicoProveedor.setEstadoEliminacion(Boolean.FALSE);
//            tecnicoProveedor.setFechaInsercion(LocalDate.now());
//            tecnicoProveedor.setIdAnho(sessionView.getProceso().getIdAnho());
//            tecnicoProveedor.setIdEmpresa(empresa);
//            tecnicoProveedor.setMailTecnico(selectTecnico);
//            tecnicoProveedor.setUsuarioInsercion(sessionView.getUsuario().getIdPersona().getUsuario());
//            tpRepo.save(tecnicoProveedor);
//            lstTecnicoProveedor.add(tecnicoProveedor);
//            tecnicoProveedor = new TecnicoProveedor();
//            JsfUtil.mensajeInsert();
//        }
    }

    public void eliminar() {
//        tecnicoProveedor.setEstadoEliminacion(Boolean.TRUE);
//        tecnicoProveedor.setFechaEliminacion(LocalDate.now());
//        tpRepo.update(tecnicoProveedor);
//        lstTecnicoProveedor = tpRepo.findAll();
//        JsfUtil.mensajeInformacion("El registro ha sido eliminado");
    }

//    public List<Empresa> completeEmpresa(String query) {
//        String queryLowerCase = query.toLowerCase();
//        List<Empresa> lstEmpresas = cgRepo.getLstEmpresa();
//        return lstEmpresas.stream().filter(emp -> emp.getRazonSocial().toLowerCase().contains(queryLowerCase) || emp.getIdPersona().getNumeroDui().contains(query)).collect(Collectors.toList());
//    }
    
    public List<Departamento> getLstDepartamento() {
        return catalogoRepo.findListByParam(Departamento.class, new ArrayList(), "id", true);
    }

    public List<Municipio> getLstMunicipio() {
        params.clear();
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "codigoDepartamento.id", codigoDepartamento).build());
        return (List<Municipio>) catalogoRepo.findListByParam(Municipio.class, params, "id", false);
    }
}
