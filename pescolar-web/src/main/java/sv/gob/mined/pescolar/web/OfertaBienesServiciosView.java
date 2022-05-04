package sv.gob.mined.pescolar.web;

import sv.gob.mined.pescolar.model.OfertaBienesServicio;
import sv.gob.mined.pescolar.model.Participante;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import sv.gob.mined.pescolar.model.Departamento;
import sv.gob.mined.pescolar.model.DetalleProcesoAdq;
import sv.gob.mined.pescolar.model.Municipio;
import sv.gob.mined.pescolar.model.RubrosAmostrarInteres;
import sv.gob.mined.pescolar.model.view.VwCatalogoEntidadEducativa;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.repository.NivelEducativoRepo;
import sv.gob.mined.pescolar.repository.OfertaRepo;
import sv.gob.mined.pescolar.repository.ParticipanteRepo;
import sv.gob.mined.pescolar.utils.JsfUtil;
import sv.gob.mined.pescolar.utils.db.Filtro;
import sv.gob.mined.pescolar.utils.enums.TipoOperacion;
import sv.gob.mined.pescolar.utils.enums.TipoOperador;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class OfertaBienesServiciosView implements Serializable {

    private TipoOperacion operacion;
    private Integer idDetProcesoAdq;
    private String codigoDepartamento;
    private String codigoEntidad;
    private String nombre;
    private String nivelesEducativos;

    private Long idParticipante;
    private Long idRubro;
    private Long idMunicipio;
    private Long cantidadAlumnos;

    private DetalleProcesoAdq detalleProceso;
    private OfertaBienesServicio ofertaBienesServicio;
    private VwCatalogoEntidadEducativa entidadEducativa;
    private List<Participante> lstParticipantes = new ArrayList();
    private List<RubrosAmostrarInteres> lstRubros = new ArrayList();
    private List<Filtro> params = new ArrayList();

    @Inject
    private ParticipanteRepo participanteRepo;
    @Inject
    private CatalogoRepo catalogoRepo;
    @Inject
    private SessionView sessionView;
    @EJB
    private NivelEducativoRepo nivelEducativoRepo;
    @EJB
    private OfertaRepo ofertaRepo;

    public OfertaBienesServiciosView() {
    }

    @PostConstruct
    public void init() {
        Map<String, String> parametros = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (parametros.containsKey("operacion")) {
            operacion = TipoOperacion.valueOf(parametros.get("operacion"));
        }
        inicializarVariables();
    }

    public Long getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(Long idParticipante) {
        this.idParticipante = idParticipante;
    }

    private void inicializarVariables() {
        lstRubros = catalogoRepo.findAllRubrosByIdProceso(sessionView.getIdProcesoAdq());
        entidadEducativa = new VwCatalogoEntidadEducativa();
    }

    public VwCatalogoEntidadEducativa getEntidadEducativa() {
        return entidadEducativa;
    }

    public List<Participante> getLstParticipantes() {
        return lstParticipantes;
    }

    public List<Departamento> getLstDepartamento() {
        return catalogoRepo.findListByParam(Departamento.class, new ArrayList(), "id", true);
    }

    public List<Municipio> getLstMunicipio() {
        params.clear();
        params.add(new Filtro(TipoOperador.EQUALS, "codigoDepartamento.id", codigoDepartamento));
        return (List<Municipio>) catalogoRepo.findListByParam(Municipio.class, params, "id", false);
    }

    public List<RubrosAmostrarInteres> getLstRubros() {
        return lstRubros;
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

    public void setOferta(OfertaBienesServicio ofertaBienesServicio) {
        this.ofertaBienesServicio = ofertaBienesServicio;
    }

    public OfertaBienesServicio getOferta() {
        return ofertaBienesServicio;
    }

    public void buscar() {
        params.clear();
        params.add(new Filtro(TipoOperador.EQUALS, "idOferta.codigoEntidad.codigoDepartamento.id", codigoDepartamento));
        params.add(new Filtro(TipoOperador.EQUALS, "idOferta.codigoEntidad.idMunicipio.id", idMunicipio));
        params.add(new Filtro(TipoOperador.EQUALS, "idOferta.codigoEntidad.codigoEntidad", codigoEntidad));
        params.add(new Filtro(TipoOperador.LIKE, "idOferta.codigoEntidad.nombre", nombre.isEmpty() ? null : nombre));
        params.add(new Filtro(TipoOperador.EQUALS, "idOferta.idDetProcesoAdq.idProcesoAdq.id", sessionView.getIdProcesoAdq()));
        params.add(new Filtro(TipoOperador.EQUALS, "idOferta.idDetProcesoAdq.idRubroAdq.id", idRubro));
        lstParticipantes = (List<Participante>) catalogoRepo.findListByParam(Participante.class, params, "idOferta.id", Boolean.TRUE);
    }

    public void buscarCe() {
        entidadEducativa = catalogoRepo.findEntityByPk(VwCatalogoEntidadEducativa.class, codigoEntidad);

        if (entidadEducativa == null) {
            JsfUtil.mensajeAlerta("No se ha encontrado el centro escolar con código: " + codigoEntidad);
        } else {
            params.clear();
            params.add(new Filtro(TipoOperador.EQUALS, "idProcesoAdq.id", sessionView.getIdProcesoAdq()));
            params.add(new Filtro(TipoOperador.EQUALS, "idRubroAdq.id", idRubro));

            detalleProceso = catalogoRepo.findByParam(DetalleProcesoAdq.class, params);

            List<Long> lstNiveles = catalogoRepo.getLstNivelesConMatriculaReportadaByIdProcesoAdqAndCodigoEntidad(sessionView.getIdProcesoAdq(), codigoEntidad);
            nivelesEducativos = String.join(",", lstNiveles.stream().map(String::valueOf).collect(Collectors.toList()));
            cantidadAlumnos = nivelEducativoRepo.getCantidadTotalByCodEntAndIdProcesoAdq(nivelesEducativos, codigoEntidad, sessionView.getIdProcesoAdq());

            if (cantidadAlumnos == null || cantidadAlumnos.intValue() == 0) {
                JsfUtil.mensajeAlerta("Es necesario registrar las estadisticas para este centro educativo");
            } else {
                if (sessionView.getCodigoDepartamento() != null) {
                    String dep = sessionView.getUsuario().getCodigoDepartamento().getId();
                    if (entidadEducativa.getCodigoDepartamento().getId().equals(dep) || sessionView.getUsuario().getIdTipoUsuario().getIdTipoUsuario().compareTo(1l) == 0) {
                        params.clear();

                        params.add(new Filtro(TipoOperador.EQUALS, "codigoEntidad.codigoEntidad", codigoEntidad));
                        params.add(new Filtro(TipoOperador.EQUALS, "idDetProcesoAdq.id", detalleProceso.getId()));
                        params.add(new Filtro(TipoOperador.EQUALS, "estadoEliminacion", 0l));

                        ofertaBienesServicio = ofertaRepo.findEntityByParam(params);

                        switch (operacion) {
                            case NUEVO:
                                if (ofertaBienesServicio != null) {
                                    JsfUtil.mensajeError("Ya existe un proceso de contratación para este centro escolar.");
                                } else {
                                    ofertaBienesServicio = new OfertaBienesServicio();
                                    ofertaBienesServicio.setIdDetProcesoAdq(detalleProceso);
                                    ofertaBienesServicio.setCodigoEntidad(entidadEducativa);
                                    cargaItemsPorCe();
                                }
                                break;
                            case MODIFICAR:
                                if (ofertaBienesServicio == null) {
                                    JsfUtil.mensajeError("No existe un proceso de contratación para este centro escolar.");
                                } else {
                                    cargaItemsPorCe();
                                }
                                break;
                        }
                    } else {
                        JsfUtil.mensajeAlerta("El codigo del centro escolar no pertenece al departamento " + JsfUtil.getNombreDepartamentoByCodigo(dep) + "<br/>"
                                + "Departamento del CE: " + entidadEducativa.getCodigoEntidad() + " es " + entidadEducativa.getCodigoDepartamento().getNombreDepartamento());
                    }
                } else {
                    JsfUtil.mensajeAlerta("Debe de seleccionar un departamento y municipio.");
                }
            }
        }
    }

    private void cargaItemsPorCe() {
        /*deshabilitar = !current.getIdDetProcesoAdq().getHabilitarRegistro();
        abrirDialogCe = true;
        if (!current.getIdDetProcesoAdq().getHabilitarRegistro()) {
            JsfUtil.mensajeInformacion("El registro de contratos ha sido deshabilitado por el Administrador.");
        } else {
            mapItems = entidadEducativaEJB.getNoItemsByCodigoEntidadAndIdProcesoAdq(codigoEntidad, detalleProceso, nivelesEducativos,
                    detalleProceso.getIdRubroAdq().getIdRubroUniforme().intValue() == 1);
        }*/
    }

    public String nuevo() {
        codigoEntidad = "";
        codigoDepartamento = null;
        idMunicipio = null;
        return "reg02OfertaEdit?faces-redirect=true&operacion=NUEVO";
    }

    public String editarOferta() {
        return "reg02DetalleOferta?faces-redirect=true&idParticipante=" + idParticipante;
    }
}
