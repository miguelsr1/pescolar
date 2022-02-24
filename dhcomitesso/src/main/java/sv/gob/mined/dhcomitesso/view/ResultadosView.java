package sv.gob.mined.dhcomitesso.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import sv.gob.mined.dhcomitesso.model.dhcsso.ProcesoEleccion;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.CandidatoDto;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.ResultadosDto;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.VotanteDto;
import sv.gob.mined.dhcomitesso.repository.CandidatoRepo;

/**
 *
 * @author misanchez
 */
@SuppressWarnings("serial")
@Named
@ViewScoped
public class ResultadosView implements Serializable {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("Bundle");
    
    private ProcesoEleccion idProceso;

    private List<VotanteDto> lstEmpleadosVotantes = new ArrayList();
    private List<ResultadosDto> lstResultado = new ArrayList();
    private List<CandidatoDto> lstCandidatos = new ArrayList();

    @Inject
    private CandidatoRepo candidatoRepo;

    @PostConstruct
    public void init() {
        idProceso = candidatoRepo.findProcesoByPk();
        lstEmpleadosVotantes = candidatoRepo.findAllVotantesByIdProceso(idProceso.getId());
        lstCandidatos = candidatoRepo.findCandidatosByIdProceso(idProceso.getId());
        lstResultado = candidatoRepo.findResultadosByIdProceso(idProceso.getId());
    }

    public List<VotanteDto> getLstEmpleadosVotantes() {
        return lstEmpleadosVotantes;
    }

    public List<CandidatoDto> getLstCandidatos() {
        return lstCandidatos;
    }

    public List<ResultadosDto> getLstResultado() {
        return lstResultado;
    }
    
    public List<ResultadosDto> getLstCandidatosElectos() {
        return candidatoRepo.findCandidatosElectosByIdProceso(idProceso.getId());
    }
    
    public Integer getCantidadEmpleados(){
        return candidatoRepo.findAllEmpleados().size();
    }
    
    public Integer getCantidadVotantes(){
        return lstEmpleadosVotantes.size();
    }

    public ProcesoEleccion getIdProceso() {
        return idProceso;
    }

    public StreamedContent getGraphicText(String path) {
        return DefaultStreamedContent.builder()
                .contentType("image/png")
                .stream(() -> {
                    try {
                        String pathImg;
                        if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
                            pathImg = RESOURCE_BUNDLE.getString("path_images_win");
                        } else {
                            pathImg = RESOURCE_BUNDLE.getString("path_images_linux");
                        }

                        return new FileInputStream(pathImg + File.separator + path + ".png");
                    } catch (IOException e) {
                        return null;
                    }
                })
                .build();
    }

}
