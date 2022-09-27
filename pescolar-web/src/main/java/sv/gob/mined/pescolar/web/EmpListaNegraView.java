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
import org.primefaces.event.SelectEvent;
import sv.gob.mined.pescolar.model.ListaNegraEmpresa;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.model.TipoSancion;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.repository.EmpListaNegraRepo;
import sv.gob.mined.pescolar.repository.EmpresaRepo;
import sv.gob.mined.pescolar.repository.TipoSancionRepo;
import sv.gob.mined.pescolar.utils.Constantes;
import sv.gob.mined.pescolar.utils.JsfUtil;
import sv.gob.mined.pescolar.utils.db.Filtro;
import sv.gob.mined.pescolar.web.proveedor.interno.CargaGeneralView;

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
    private List<TipoSancion> lstSancion = new ArrayList();
    private String rubroProveedor;
    private String numeroNit;
    private String municipioDepartamento;
    private String domicilio;
    private String razonSocial;
    private Boolean deshabilitado;
    private Boolean deshabilitadoGuardar = true;

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
        listaNegraEmpresa.setEstadoEliminacion((short) 0);
        listaNegraEmpresa.setFecha(new Date());
        listaNegraEmpresa.setUsuario(sessionView.getUsuario().getIdPersona().getUsuario());

        empListaNegraRepo.save(listaNegraEmpresa);
        listaNegraEmpresa = new ListaNegraEmpresa();
        deshabilitadoGuardar= true;
        
        JsfUtil.mensajeInsert();
    }

    public void eliminar() {

        listaNegraEmpresa.setEstadoEliminacion((short) 1);
        empListaNegraRepo.update(listaNegraEmpresa);
        JsfUtil.mensajeInformacion("El registro ha sido eliminado");
    }

}
