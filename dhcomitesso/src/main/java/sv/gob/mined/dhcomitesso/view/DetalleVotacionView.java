package sv.gob.mined.dhcomitesso.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import sv.gob.mined.dhcomitesso.model.dhcsso.DetalleProceso;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.CandidatoView;
import sv.gob.mined.dhcomitesso.repository.CandidatoRepo;

/**
 *
 * @author misanchez
 */
@SuppressWarnings("serial")
@Named
@ViewScoped
public class DetalleVotacionView implements Serializable {

    private Integer idCandidato;
    private DetalleProceso detalleProceso;

    private List<sv.gob.mined.dhcomitesso.model.dhcsso.view.CandidatoView> lstCandidatos = new ArrayList();

    @Inject
    private CandidatoRepo candidatoRepo;
    @Inject
    private ParametrosSesionView psv;

    @PostConstruct
    public void init() {
        lstCandidatos = candidatoRepo.findCandidatos();
    }

    public DetalleProceso getDetalleProceso() {
        return detalleProceso;
    }

    public void setDetalleProceso(DetalleProceso detalleProceso) {
        this.detalleProceso = detalleProceso;
    }

    public List<CandidatoView> getLstCandidatos() {
        return lstCandidatos;
    }

    public Integer getIdCandidato() {
        return idCandidato;
    }

    public void setIdCandidato(Integer idCandidato) {
        this.idCandidato = idCandidato;
    }

    public void guardar() {
        candidatoRepo.guardarVoto(1, idCandidato, psv.getEmpleado());
        psv.setVotoRealizado(Boolean.TRUE);

        PrimeFaces.current().executeScript("PF('dlgVoto').show()");
    }
}
