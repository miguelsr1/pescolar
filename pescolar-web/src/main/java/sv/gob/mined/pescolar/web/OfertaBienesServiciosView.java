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
import sv.gob.mined.pescolar.repository.ParticipanteRepo;
import sv.gob.mined.pescolar.utils.Filtro;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class OfertaBienesServiciosView implements Serializable {

    @Inject
    private OfertaBienesServiciosRepo ofertaBienesServiciosRepo;
    @Inject
    private ParticipanteRepo participanteRepo;
    @Inject
    private CatalogoRepo catalogoRepo;
    @Inject
    private SessionView sessionView;

    private Integer idDetProcesoAdq;
    private String codigoDepartamento;
    private String codigoEntidad;
    private String nombre;
    private Long idRubro;
    private Long idMunicipio;

    private List<Participante> lstParticipantes = new ArrayList();

    private List<Filtro> params = new ArrayList();

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
        return catalogoRepo.findListByParam(Departamento.class, new ArrayList(), "id", false);
    }

    public List<Municipio> getLstMunicipio() {
        params.clear();
        params.add(new Filtro(Filtro.EQUALS, "codigoDepartamento.id", codigoDepartamento));
        return (List<Municipio>) catalogoRepo.findListByParam(Municipio.class, params, "id", false);
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
        params.clear();
        params.add(new Filtro(Filtro.EQUALS, "idOferta.codigoEntidad.codigoDepartamento", codigoDepartamento));
        params.add(new Filtro(Filtro.EQUALS, "idOferta.codigoEntidad.idMunicipio", idMunicipio));
        params.add(new Filtro(Filtro.LIKE, "idOferta.codigoEntidad.codigoEntidad", codigoEntidad));
        params.add(new Filtro(Filtro.LIKE, "idOferta.codigoEntidad.nombre", nombre.isEmpty() ? null : nombre));
        params.add(new Filtro(Filtro.LIKE, "idOferta.idDetProcesoAdq.idProcesoAdq.id", sessionView.getIdProcesoAdq()));
        params.add(new Filtro(Filtro.LIKE, "idOferta.idDetProcesoAdq.idRubroAdq.id", idRubro));
        lstParticipantes = (List<Participante>) catalogoRepo.findListByParam(Participante.class, params, "idOferta.id", Boolean.TRUE);
    }
}
