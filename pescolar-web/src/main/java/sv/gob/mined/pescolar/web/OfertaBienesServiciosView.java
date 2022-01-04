package sv.gob.mined.pescolar.web;

import sv.gob.mined.pescolar.model.OfertaBienesServicio;
import sv.gob.mined.pescolar.model.Participante;
import sv.gob.mined.pescolar.repository.OfertaBienesServiciosRepo;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import sv.gob.mined.pescolar.model.Departamento;
import sv.gob.mined.pescolar.model.Municipio;
import sv.gob.mined.pescolar.model.RubrosAmostrarInteres;
import sv.gob.mined.pescolar.repository.CatalogoRepo;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class OfertaBienesServiciosView implements Serializable {

    @Inject
    private OfertaBienesServiciosRepo ofertaBienesServiciosRepo;
    @Inject
    private CatalogoRepo catalogoRepo;

    private Integer idDetProcesoAdq;
    private String codigoDepartamento;
    private String codigoEntidad;
    private String nombre;
    private Long idRubro;
    private Long idMunicipio;

    private List<Participante> lstParticipantes = new ArrayList();

    public OfertaBienesServiciosView() {
    }

    @PostConstruct
    public void init() {
        //lstParticipantes = ofertaBienesServiciosRepo.findParticipatesByCodEndByIdAnho("10001", 10l);
    }

    public List<OfertaBienesServicio> getLstOfertas() {
        return ofertaBienesServiciosRepo.findOfertaByCodEntAndIdDetPro("10001", 49);
    }

    public List<Participante> getLstParticipantes() {
        return lstParticipantes;
    }

    public List<Departamento> getLstDepartamento() {
        return catalogoRepo.findAllDepartamento();
    }

    public List<Municipio> getLstMunicipio() {
        return catalogoRepo.findMunicipiosByDepa(codigoDepartamento);
    }
    
    public List<RubrosAmostrarInteres> getLstRubros() {
        return catalogoRepo.findAllRubrosByIdProceso(20);
    }

    public String getCodigoDepartamento() {
        return codigoDepartamento;
    }

    public void setCodigoDepartamento(String codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }

    public Long getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Long idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getCodigoEntidad() {
        return codigoEntidad;
    }

    public void setCodigoEntidad(String codigoEntidad) {
        this.codigoEntidad = codigoEntidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(Long idRubro) {
        this.idRubro = idRubro;
    }

    public void buscar() {
        lstParticipantes = ofertaBienesServiciosRepo.findParticipantesByParam(codigoDepartamento, idMunicipio, codigoEntidad, nombre, idRubro);
    }
}
