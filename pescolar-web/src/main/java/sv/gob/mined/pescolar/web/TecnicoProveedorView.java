package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.model.TecnicoProveedor;
import sv.gob.mined.pescolar.repository.CatalogoGlobal;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.repository.TecnicoProveedorRepo;
import sv.gob.mined.pescolar.utils.DescriptorDto;
import sv.gob.mined.pescolar.utils.JsfUtil;

/**
 *
 * @author misanchez
 */
@Named
@ViewScoped
public class TecnicoProveedorView implements Serializable {

    private Empresa empresa;
    private TecnicoProveedor tecnicoProveedor;
    private SelectItem selectTecnico;
    private List<SelectItem> lstTecnicos = new ArrayList();
    private List<TecnicoProveedor> lstTecnicoProveedor = new ArrayList();

    @Inject
    private TecnicoProveedorRepo tpRepo;
    @Inject
    private CatalogoRepo catalogoRepo;
    @Inject
    private CatalogoGlobal cgRepo;
    @Inject
    private SessionView sessionView;

    @PostConstruct
    public void init() {
        catalogoRepo.getLstTecnicosPaquete().stream().forEach(tec -> {
            lstTecnicos.add(new SelectItem(tec, tec.getAtributo()));
        });

        lstTecnicoProveedor = tpRepo.findAll();
    }

    public SelectItem getSelectTecnico() {
        return selectTecnico;
    }

    public void setSelectTecnico(SelectItem selectTecnico) {
        this.selectTecnico = selectTecnico;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public TecnicoProveedor getTecnicoProveedor() {
        return tecnicoProveedor;
    }

    public void setTecnicoProveedor(TecnicoProveedor tecnicoProveedor) {
        this.tecnicoProveedor = tecnicoProveedor;
    }

    public List<SelectItem> getLstTecnicos() {
        return lstTecnicos;
    }

    public List<TecnicoProveedor> getLstTecnicoProveedor() {
        return lstTecnicoProveedor;
    }

    public void guardar() {
        if (tecnicoProveedor.getIdTecnico() == null) {
            tecnicoProveedor.setEstadoEliminacion(Boolean.FALSE);
            tecnicoProveedor.setFechaInsercion(LocalDate.now());
            tecnicoProveedor.setIdAnho(sessionView.getProceso().getIdAnho());
            tecnicoProveedor.setIdEmpresa(empresa);
            tecnicoProveedor.setMailTecnico(((DescriptorDto) selectTecnico.getValue()).getValor());
            tecnicoProveedor.setUsuarioInsercion(sessionView.getVariableSessionUsuario());
            tpRepo.save(tecnicoProveedor);
            JsfUtil.mensajeInsert();
        }
    }

    public void eliminar() {
        tpRepo.update(tecnicoProveedor);
        JsfUtil.mensajeInformacion("El registro ha sido eliminado");
    }

    public List<Empresa> completeEmpresa(String query) {
        String queryLowerCase = query.toLowerCase();
        List<Empresa> lstEmpresas = cgRepo.getLstEmpresa();
        return lstEmpresas.stream().filter(emp -> emp.getRazonSocial().toLowerCase().contains(queryLowerCase) || emp.getIdPersona().getNumeroDui().contains(query)).collect(Collectors.toList());
    }
}
