package sv.gob.mined.dhcomitesso.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import sv.gob.mined.dhcomitesso.model.dhcsso.DetalleProceso;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.CandidatoDto;
import sv.gob.mined.dhcomitesso.repository.CandidatoRepo;
import sv.gob.mined.utils.jsf.JsfUtil;

/**
 *
 * @author misanchez
 */
@Named
@ViewScoped
public class DetalleVotacionView implements Serializable {

    private Integer idCandidato;
    private DetalleProceso detalleProceso;

    private List<CandidatoDto> lstCandidatos = new ArrayList();

    @Inject
    private CandidatoRepo candidatoRepo;
    @Inject
    private ParametrosSesionView psv;

    @PostConstruct
    public void init() {
        if (!psv.getVotoRealizado()) {
            lstCandidatos = candidatoRepo.findCandidatosByIdProceso(1);
        }
    }

    public void validarVotoRealizado() {
        if (psv.getVotoRealizado()) {
            FacesContext fc = FacesContext.getCurrentInstance();
            ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
            nav.performNavigation("/app/principal.xhtml?faces-redirect=true");
        }
    }

    public DetalleProceso getDetalleProceso() {
        return detalleProceso;
    }

    public void setDetalleProceso(DetalleProceso detalleProceso) {
        this.detalleProceso = detalleProceso;
    }

    public List<CandidatoDto> getLstCandidatos() {
        return lstCandidatos;
    }

    public Integer getIdCandidato() {
        return idCandidato;
    }

    public void setIdCandidato(Integer idCandidato) {
        this.idCandidato = idCandidato;
    }

    public void guardar() {
        if (idCandidato != null) {
            candidatoRepo.guardarVoto(1, idCandidato, psv.getEmpleado());
            psv.setVotoRealizado(Boolean.TRUE);

            PrimeFaces.current().executeScript("PF('dlgVoto').show()");
        }else{
            JsfUtil.mensajeAlerta("Debe de seleccionar un candidato.");
        }
    }
}
