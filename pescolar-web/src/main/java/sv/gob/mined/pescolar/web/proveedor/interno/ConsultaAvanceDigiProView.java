package sv.gob.mined.pescolar.web.proveedor.interno;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.pescolar.model.DetRubroMuestraIntere;
import sv.gob.mined.pescolar.model.RubrosAmostrarInteres;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.repository.EmpresaRepo;
import sv.gob.mined.pescolar.web.SessionView;

@Named
@ViewScoped
public class ConsultaAvanceDigiProView implements Serializable {

    private Long idRubro;

    private List<DetRubroMuestraIntere> lstDetMuestra = new ArrayList();

    @Inject
    private SessionView sessionView;
    @Inject
    private CatalogoRepo catalogoRepo;
    @Inject
    private EmpresaRepo empresaRepo;

    public Long getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(Long idRubro) {
        this.idRubro = idRubro;
    }

    public List<RubrosAmostrarInteres> getLstRubros() {
        return catalogoRepo.findAllRubrosByIdProceso(sessionView.getIdProcesoAdq());
    }

    public List<DetRubroMuestraIntere> getLstDetMuestra() {
        return lstDetMuestra;
    }

    public void buscarDatos() {
        lstDetMuestra = empresaRepo.findAvanceDigiProv(sessionView.getIdAnho(), idRubro);
    }

    public String getTecnico(String dui) {
        return empresaRepo.findTecnicoByDuiProv(dui, sessionView.getIdAnho()).getMailTecnico();
    }
}
