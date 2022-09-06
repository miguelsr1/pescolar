package sv.gob.mined.pescolar.web.contratacion;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.pescolar.model.DiasPlazoContrato;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.repository.DiasPlazoContratoRepo;
import sv.gob.mined.pescolar.web.SessionView;

/**
 *
 * @author misanchez
 */
@Named
@ViewScoped
public class DiasPlazoContratoView implements Serializable {

    private Long idRubro;
    private Integer numeroDias;
    private DiasPlazoContrato diasPlazoContrato;
    private List<DiasPlazoContrato> lstDias = new ArrayList();

    @Inject
    private SessionView sessionView;
    @Inject
    private DiasPlazoContratoRepo diasPlazoContratoRepo;

    @PostConstruct
    public void init() {
        lstDias = diasPlazoContratoRepo.findAll();
    }

    public DiasPlazoContrato getDiasPlazoContrato() {
        return diasPlazoContrato;
    }

    public void setDiasPlazoContrato(DiasPlazoContrato diasPlazoContrato) {
        this.diasPlazoContrato = diasPlazoContrato;
    }

    public Long getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(Long idRubro) {
        this.idRubro = idRubro;
    }

    public Integer getNumeroDias() {
        return numeroDias;
    }

    public void setNumeroDias(Integer numeroDias) {
        this.numeroDias = numeroDias;
    }

    public List<DiasPlazoContrato> getLstDias() {
        return lstDias;
    }

    public void nuevo() {
        diasPlazoContrato = new DiasPlazoContrato();
    }

    public void guardar() {
        diasPlazoContrato.setDiasPlazo(numeroDias);
        diasPlazoContrato.setIdAnho(sessionView.getProceso().getIdAnho());
        diasPlazoContrato.setIdRubroInteres(sessionView.getLstRubros().stream().parallel().filter(rub -> rub.getId().compareTo(idRubro) == 0).findAny().orElse(null));
        diasPlazoContrato.setUsuario(sessionView.getUsuario().getIdPersona().getUsuario());
        diasPlazoContrato.setFecha(LocalDate.now());
        diasPlazoContrato.setEstadoEliminacion(Boolean.FALSE);

        if (diasPlazoContrato.getId() == null) {
            diasPlazoContratoRepo.save(diasPlazoContrato);
        } else {
            diasPlazoContratoRepo.update(diasPlazoContrato);
        }
    }

    public void eliminarDia() {

    }

}
