package sv.gob.mined.dhcomitesso.view;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import sv.gob.mined.dhcomitesso.model.dhcsso.Candidato;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.CandidatoDto;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.EmpleadoDto;
import sv.gob.mined.dhcomitesso.model.dhevaluacion.EstOrganizativa;
import sv.gob.mined.dhcomitesso.repository.CandidatoRepo;
import sv.gob.mined.dhcomitesso.repository.EstructuraOrganizativaRepo;
import sv.gob.mined.utils.jsf.JsfUtil;

/**
 *
 * @author misanchez
 */
@Named
@ViewScoped
public class CandidatoView implements Serializable {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("Bundle");

    private String codOrg;
    private String nomOrg;

    private Candidato candidato = new Candidato();
    private EmpleadoDto dataEmpleadoView;
//    private List<SelectItem> direccionGroup;
    private Map<String, String> lstUnidadOrg = new HashMap();
    private List<EstOrganizativa> lstOrg = new ArrayList();
    private List<EstOrganizativa> lstOrgDep;
    private List<EmpleadoDto> lstEmpleados = new ArrayList();
    private List<CandidatoDto> lstCandidatos = new ArrayList();

    private UploadedFile file;
    private StreamedContent sContent;

    private byte[] byteImagen;

    @Inject
    private EstructuraOrganizativaRepo estOrgRepo;

    @Inject
    private CandidatoRepo candidatoRepo;

    @PostConstruct
    public void init() {
        estOrgRepo.findDirecciones2().forEach(nombreOrg -> {
            lstUnidadOrg.put(nombreOrg, nombreOrg);
        });

//        direccionGroup = new ArrayList();
//        for (EstOrganizativa est : lstOrg) {
//            SelectItemGroup direccion = new SelectItemGroup(est.getNombreEstructura());
//            lstOrgDep = estOrgRepo.findDependencia(est.getInuniorg());
//
//            SelectItem[] hijo = new SelectItem[lstOrgDep.size()];
//            for (int i = 0; i < lstOrgDep.size(); i++) {
//                hijo[i] = new SelectItem(lstOrgDep.get(i).getInuniorg(), lstOrgDep.get(i).getNombreEstructura());
//            }
//
//            direccion.setSelectItems(hijo);
//            direccionGroup.add(direccion);
//        }
        cargarImagen();
        cargarCandidatos();
    }

    public String getNomOrg() {
        return nomOrg;
    }

    public void setNomOrg(String nomOrg) {
        this.nomOrg = nomOrg;
    }

    public Map<String, String> getLstUnidadOrg() {
        return lstUnidadOrg;
    }

    

    public List<sv.gob.mined.dhcomitesso.model.dhcsso.view.CandidatoDto> getLstCandidatos() {
        return lstCandidatos;
    }

    public EmpleadoDto getDataEmpleadoView() {
        return dataEmpleadoView;
    }

    public void setDataEmpleadoView(EmpleadoDto dataEmpleadoView) {
        this.dataEmpleadoView = dataEmpleadoView;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getCodOrg() {
        return codOrg;
    }

    public void setCodOrg(String codOrg) {
        this.codOrg = codOrg;
    }

    public List<EmpleadoDto> getLstEmpleados() {
        return lstEmpleados;
    }

//    public List<SelectItem> getDireccionGroup() {
//        return direccionGroup;
//    }

    public void buscarEmpleados() {
        lstEmpleados = estOrgRepo.findEmpleadosByOrg(nomOrg);
    }

    public void inicializarRegistro() {
        byteImagen = null;
        file = null;
        sContent = null;
        dataEmpleadoView = null;
        candidato = new Candidato();
    }

    public void cargarCandidatos() {
        lstCandidatos = candidatoRepo.findCandidatosByIdProceso(1);
    }

    public void guardar() {
        if (file == null || file.getFileName().equals("sin_foto.png")) {
            JsfUtil.mensajeAlerta("Por favor, agregar una foto del candidato.");
        } else {
            cargarFoto();

            candidato.setCodigoEmpleado(dataEmpleadoView.getCodigoEmpleado());
            candidato.setNumeroTelefono(dataEmpleadoView.getNumeroTelefono());
            candidato.setPathImg(dataEmpleadoView.getCodigoEmpleado() + ".png");
            candidato.setProcesoEleccion(candidatoRepo.findProcesoByPk());

            candidatoRepo.guardar(candidato);

            inicializarRegistro();
            cargarCandidatos();
            JsfUtil.mensajeInformacion("Operación realizada con éxito.");
            PrimeFaces.current().executeScript("PF('dlgEmpleado').hide()");
        }
    }

    public void cancelarEdicion() {
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        cargarImagen();
    }

    public void cargarImagen() {
        sContent = DefaultStreamedContent.builder()
                .contentType("image/png")
                .stream(() -> {
                    try {
                        if (candidato.getId() == null && file == null) {
                            String path = candidato.getId() == null ? "sin_foto.png" : candidato.getPathImg();
                            String pathImg;

                            if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
                                pathImg = RESOURCE_BUNDLE.getString("path_images_win");
                            } else {
                                pathImg = RESOURCE_BUNDLE.getString("path_images_linux");
                            }

                            return new FileInputStream(pathImg + File.separator + path);
                        } else {
                            byteImagen = file.getContent();

                            return file.getInputStream();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .build();
    }

    public void cargarFoto() {
        File folderProyecto = new File(JsfUtil.getPathReportes(RESOURCE_BUNDLE, "path_images"));
        if (!folderProyecto.exists()) {
            folderProyecto.mkdir();
        }

        try {
            Path folder = Paths.get(JsfUtil.getPathReportes(RESOURCE_BUNDLE, "path_images") + File.separator + dataEmpleadoView.getCodigoEmpleado() + ".png");
            Path arc;
            if (folder.toFile().exists()) {
                arc = folder;
            } else {
                arc = Files.createFile(folder);
            }

            try ( InputStream input = new ByteArrayInputStream(byteImagen)) {
                Files.copy(input, arc, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            Logger.getLogger(CandidatoView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public StreamedContent getImagen() {
        return sContent;
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
