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
import sv.gob.mined.pescolar.model.ListaNegraEmpresa;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.repository.EmpListaNegraRepo;
import sv.gob.mined.pescolar.repository.EmpresaRepo;
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
public class EmpListaNegraView implements Serializable {

    private ListaNegraEmpresa listaNegraEmpresa;
    private Empresa empresa;
    private List<Filtro> params = new ArrayList();
    private List<Empresa> lstEmpresas = new ArrayList();
    private String nitEmpresa;
    private String razonSocial;
    private Boolean deshabilitado;

    @Inject
    private SessionView sessionView;

    @Inject
    private EmpListaNegraRepo empListaNegraRepo;

    @Inject
    private EmpresaRepo empresaRepo;

    @PostConstruct
    public void init() {

    }

    public void buscarEmp() {
        params.clear();
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "numeroNit", nitEmpresa).build());
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idMunicipio", razonSocial).build());

        lstEmpresas = (List<Empresa>) empresaRepo.findListByParam(params, razonSocial, Boolean.TRUE);
    }

    public void guardar() {

        listaNegraEmpresa = new ListaNegraEmpresa();
        listaNegraEmpresa.setIdEmpresa(empresa);
        listaNegraEmpresa.setEstadoEliminacion((short) 0);
        listaNegraEmpresa.setFecha(new Date());
        listaNegraEmpresa.setUsuario(sessionView.getUsuario().getIdPersona().getUsuario());

        empListaNegraRepo.save(listaNegraEmpresa);

        buscarEmp();
        JsfUtil.mensajeInsert();
    }

    public void eliminar() {
              
        listaNegraEmpresa.setEstadoEliminacion((short) 1);
        empListaNegraRepo.update(listaNegraEmpresa);
        JsfUtil.mensajeInformacion("El registro ha sido eliminado");
    }

}
