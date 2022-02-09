package sv.gob.mined.dhcomitesso.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.CandidatoView;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.VotanteView;
import sv.gob.mined.dhcomitesso.repository.CandidatoRepo;

/**
 *
 * @author misanchez
 */
@SuppressWarnings("serial")
@Named
@ViewScoped
public class ResultadosView implements Serializable {

    private List<VotanteView> lstEmpleadosVotantes = new ArrayList();
    private List<sv.gob.mined.dhcomitesso.model.dhcsso.view.CandidatoView> lstCandidatos = new ArrayList();

    @Inject
    private CandidatoRepo candidatoRepo;

    @PostConstruct
    public void init() {
        lstEmpleadosVotantes = candidatoRepo.findAllVotantes();
        lstCandidatos = candidatoRepo.findCandidatos();
    }

    public List<VotanteView> getLstEmpleadosVotantes() {
        return lstEmpleadosVotantes;
    }

    public List<CandidatoView> getLstCandidatos() {
        return lstCandidatos;
    }

}
