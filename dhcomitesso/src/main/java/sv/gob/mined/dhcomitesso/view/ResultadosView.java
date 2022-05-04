package sv.gob.mined.dhcomitesso.view;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import sv.gob.mined.dhcomitesso.model.dhcsso.ProcesoEleccion;
import sv.gob.mined.dhcomitesso.model.dhcsso.dto.Postulante;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.CandidatoDto;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.ResultadosDto;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.VotanteDto;
import sv.gob.mined.dhcomitesso.repository.CandidatoRepo;

/**
 *
 * @author misanchez
 */
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

    public Integer getCantidadEmpleados() {
        return candidatoRepo.findAllEmpleados().size();
    }

    public Integer getCantidadVotantes() {
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

    public void imprimir() throws IOException {
        try {
            List<JasperPrint> jasperPrintList = new ArrayList();
            List<Postulante> postulantes = new ArrayList<>();

            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

            //String jasperFileNameTab = ;
            //String pdfFileNameTab = "tabulacion.pdf";
            HashMap paramsMapActa = new HashMap<>();
            paramsMapActa.put("pImagenLogo", ctx.getRealPath(File.separator + "resources" + File.separator + "images" + File.separator));
            paramsMapActa.put("p_horas", "NUEVE");
            paramsMapActa.put("p_dias", "SIETE");
            paramsMapActa.put("p_mes", "MARZO");
            paramsMapActa.put("p_anio", "DOS MIL VEINTIDÓS");
            paramsMapActa.put("p_hora_fin", "QUINCE");

            int i = 1;
            for (ResultadosDto resultadosDto : lstResultado) {
                paramsMapActa.put("p_representante" + i, resultadosDto.getNombreEmpleado());
                paramsMapActa.put("p_dui" + i, resultadosDto.getDui());
                i++;
            }

            paramsMapActa.put("p_titular", "José Mauricio Pineda Rodríguez");
            paramsMapActa.put("p_atramec", "Fracisco Javier Guerra Rivas");
            paramsMapActa.put("p_ddh", "Juan Carlos Fernández Saca");

            JasperPrint jprintActa = (JasperPrint) JasperFillManager.fillReport(
                    ResultadosView.class.getClassLoader().getResourceAsStream(("/sv/gob/mined/reportes" + File.separator + "actacsso.jasper")),
                    paramsMapActa,
                    new JREmptyDataSource());
            jasperPrintList.add(jprintActa);

            paramsMapActa.clear();

            HashMap paramsMapTab = new HashMap<>();
            i = 1;
            for (ResultadosDto resultado : lstResultado) {
                postulantes.add(new Postulante(i, resultado.getNombreEmpleado(), resultado.getVotos()));
                i++;
            }

            JRBeanCollectionDataSource listado = new JRBeanCollectionDataSource(postulantes);
            paramsMapTab.put("pImagenLogo", ctx.getRealPath(File.separator + "resources" + File.separator + "images" + File.separator));
            paramsMapTab.put("p_totalvotantes", "512");
            paramsMapTab.put("p_dias", "SIETE");
            paramsMapTab.put("p_mes", "MARZO");
            paramsMapTab.put("p_anio", "DOS MIL VEINTIDÓS");
            paramsMapTab.put("p_titular", "José Mauricio Pineda Rodríguez");
            paramsMapTab.put("p_atramec", "Fracisco Javier Guerra Rivas");
            paramsMapTab.put("p_ddh", "Juan Carlos Fernández Saca");
            paramsMapTab.put("p_PostulantesCollection", listado);

            JasperPrint jprintTab = (JasperPrint) JasperFillManager.fillReport(
                    ResultadosView.class.getClassLoader().getResourceAsStream(("/sv/gob/mined/reportes" + File.separator + "tabulacioncsso.jasper")),
                    paramsMapTab,
                    new JREmptyDataSource());
            jasperPrintList.add(jprintTab);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JRPdfExporter exporter = new JRPdfExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
            exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.FALSE);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
            exporter.exportReport();

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

            response.setContentType("application/pdf");
            response.setContentLength(baos.toByteArray().length);
            response.setHeader("Content-disposition", "attachment; filename=actas.pdf");
            try ( OutputStream outStream = response.getOutputStream()) {
                outStream.write(baos.toByteArray());
                outStream.flush();
            }
            facesContext.responseComplete();
            facesContext.renderResponse();
        } catch (JRException ex) {
            Logger.getLogger(ResultadosView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
