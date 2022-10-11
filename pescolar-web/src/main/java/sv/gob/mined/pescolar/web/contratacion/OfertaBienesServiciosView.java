package sv.gob.mined.pescolar.web.contratacion;

import java.io.File;
import sv.gob.mined.pescolar.model.OfertaBienesServicio;
import sv.gob.mined.pescolar.model.Participante;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.PrimeFaces;
import sv.gob.mined.pescolar.model.CapaDistribucionAcre;
import sv.gob.mined.pescolar.model.Departamento;
import sv.gob.mined.pescolar.model.DetalleProcesoAdq;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.model.Municipio;
import sv.gob.mined.pescolar.model.RubrosAmostrarInteres;
import sv.gob.mined.pescolar.model.dto.contratacion.PrecioReferenciaEmpresaDto;
import sv.gob.mined.pescolar.model.dto.contratacion.ProveedorDisponibleDto;
import sv.gob.mined.pescolar.model.view.VwCatalogoEntidadEducativa;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.repository.NivelEducativoRepo;
import sv.gob.mined.pescolar.repository.OfertaRepo;
import sv.gob.mined.pescolar.repository.ParticipanteRepo;
import sv.gob.mined.pescolar.repository.PrecioRefRubroEmpRepo;
import sv.gob.mined.pescolar.utils.JsfUtil;
import sv.gob.mined.pescolar.utils.db.Filtro;
import sv.gob.mined.pescolar.utils.enums.TipoOperacion;
import sv.gob.mined.pescolar.utils.enums.TipoOperador;
import sv.gob.mined.pescolar.web.RepositorioAplicacionView;
import sv.gob.mined.pescolar.web.SessionView;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class OfertaBienesServiciosView implements Serializable {

    private TipoOperacion operacion;
    //private Integer idDetProcesoAdq;
    private String codigoDepartamento;
    private String codigoEntidad;
    private String nombreEmp = "";
    private String nombreEmpOtro = "";
    private String municipioCe;
    private String nivelesEducativos;
    private String nombre;

    private Long idParticipante;
    private Long idRubro;
    private Long idMunicipio;
    private Long cantidadAlumnos;

    private Empresa empresaSeleccionada;
    private DetalleProcesoAdq detalleProceso;
    private OfertaBienesServicio ofertaBienesServicio;
    private VwCatalogoEntidadEducativa entidadEducativa;

    private ProveedorDisponibleDto proveedorSeleccionado = new ProveedorDisponibleDto();

    private HashMap<String, String> mapItems;
    private SelectItem[] lstEstilos = new SelectItem[0];

    private List<Participante> lstParticipantes = new ArrayList();
    private List<RubrosAmostrarInteres> lstRubros = new ArrayList();
    //private List<RubrosAmostrarInteres> lstRubros = new ArrayList();
    private List<Filtro> params = new ArrayList();

    private List<ProveedorDisponibleDto> lstCapaEmpresas = new ArrayList();
    private List<ProveedorDisponibleDto> lstCapaEmpresasOtros = new ArrayList();
    private List<ProveedorDisponibleDto> lstEmpresas = new ArrayList();
    private List<ProveedorDisponibleDto> lstEmpresasOtros = new ArrayList();
    private List<PrecioReferenciaEmpresaDto> lstPrecios = new ArrayList();

    @Inject
    private SessionView sessionView;
    @Inject
    private ParticipanteRepo participanteRepo;
    @Inject
    private RepositorioAplicacionView repoView;
    @Inject
    private PrecioRefRubroEmpRepo preciosRepo;
    @Inject
    private CatalogoRepo catalogoRepo;
    @Inject
    private NivelEducativoRepo nivelEducativoRepo;
    @Inject
    private OfertaRepo ofertaRepo;

    public OfertaBienesServiciosView() {
    }

    @PostConstruct
    public void init() {
        Map<String, String> parametros = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        inicializarVariables();

        if (parametros.containsKey("operacion")) {
            operacion = TipoOperacion.valueOf(parametros.get("operacion"));
            switch (operacion) {
                case NUEVO:

                    break;
                case MODIFICAR:
                    cargarOferta(parametros.get("idRubro"), parametros.get("codigoEntidad"));
                    break;
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Empresa getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public ProveedorDisponibleDto getProveedorSeleccionado() {
        return proveedorSeleccionado;
    }

    public void setProveedorSeleccionado(ProveedorDisponibleDto proveedorSeleccionado) {
        this.proveedorSeleccionado = proveedorSeleccionado;
    }

    private void cargarOferta(String idRubro, String codigoEntidad) {
        this.idRubro = Long.parseLong(idRubro);
        this.codigoEntidad = codigoEntidad;
        buscarCe();
    }

    public Long getCantidadAlumnos() {
        return cantidadAlumnos;
    }

    public String getMunicipioCe() {
        return municipioCe;
    }

    public String getNombreEmp() {
        return nombreEmp;
    }

    public void setNombreEmp(String nombreEmp) {
        this.nombreEmp = nombreEmp;
    }

    public String getNombreEmpOtro() {
        return nombreEmpOtro;
    }

    public void setNombreEmpOtro(String nombreEmpOtro) {
        this.nombreEmpOtro = nombreEmpOtro;
    }

    public Long getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(Long idParticipante) {
        this.idParticipante = idParticipante;
    }

    private void inicializarVariables() {
        //lstRubros = catalogoRepo.findAllRubrosByIdProceso(sessionView.getIdProcesoAdq());
        entidadEducativa = new VwCatalogoEntidadEducativa();
    }

    public VwCatalogoEntidadEducativa getEntidadEducativa() {
        return entidadEducativa;
    }

    public void setEntidadEducativa(VwCatalogoEntidadEducativa entidadEducativa) {
        this.entidadEducativa = entidadEducativa;
    }

    public List<Participante> getLstParticipantes() {
        return lstParticipantes;
    }

    public List<Departamento> getLstDepartamento() {
        return catalogoRepo.findListByParam(Departamento.class, new ArrayList(), "id", true);
    }

    public List<Municipio> getLstMunicipio() {
        params.clear();
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "codigoDepartamento.id", codigoDepartamento).build());
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

    public List<ProveedorDisponibleDto> getLstCapaEmpresas() {
        return lstCapaEmpresas;
    }

    public void setLstCapaEmpresas(List<ProveedorDisponibleDto> lstCapaEmpresas) {
        this.lstCapaEmpresas = lstCapaEmpresas;
    }

    public List<ProveedorDisponibleDto> getLstCapaEmpresasOtros() {
        return lstCapaEmpresasOtros;
    }

    public void setLstCapaEmpresasOtros(List<ProveedorDisponibleDto> lstCapaEmpresasOtros) {
        this.lstCapaEmpresasOtros = lstCapaEmpresasOtros;
    }

    public DetalleProcesoAdq getDetalleProceso() {
        return detalleProceso;
    }

    public void setDetalleProceso(DetalleProcesoAdq detalleProceso) {
        this.detalleProceso = detalleProceso;
    }
    // </editor-fold>

    public void buscar() {
        params.clear();

        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idOferta.codigoEntidad.codigoDepartamento.id", codigoDepartamento).build());
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idOferta.codigoEntidad.idMunicipio.id", idMunicipio).build());
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idOferta.codigoEntidad.codigoEntidad", codigoEntidad).build());
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idOferta.codigoEntidad.nombre", nombre.isEmpty() ? null : nombre).build());
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idOferta.idDetProcesoAdq.idProcesoAdq.id", sessionView.getIdProcesoAdq()).build());
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idOferta.idDetProcesoAdq.idRubroAdq.id", idRubro).build());
        lstParticipantes = (List<Participante>) catalogoRepo.findListByParam(Participante.class, params, "idOferta.id", Boolean.TRUE);
        
        if(lstParticipantes.isEmpty()){
            JsfUtil.mensajeAlerta("No se encontraron datos.");
        }
    }

    public void buscarCe() {
        entidadEducativa = catalogoRepo.findEntityByPk(VwCatalogoEntidadEducativa.class, codigoEntidad);

        if (entidadEducativa == null) {
            JsfUtil.mensajeAlerta("No se ha encontrado el centro escolar con código: " + codigoEntidad);
        } else {

            List<Long> lstNiveles = catalogoRepo.getLstNivelesConMatriculaReportadaByIdProcesoAdqAndCodigoEntidad(sessionView.getIdProcesoAdq(), codigoEntidad);
            if(lstNiveles.isEmpty()){
                JsfUtil.mensajeAlerta("El centro escolar no tiene registrado la matricula para el año: "+ sessionView.getAnhoProceso());
                return;
            }
            
            nivelesEducativos = String.join(",", lstNiveles.stream().map(String::valueOf).collect(Collectors.toList()));
            cantidadAlumnos = nivelEducativoRepo.getCantidadTotalByCodEntAndIdProcesoAdq(nivelesEducativos, codigoEntidad, sessionView.getIdProcesoAdq());

            if (cantidadAlumnos == null || cantidadAlumnos.intValue() == 0) {
                JsfUtil.mensajeAlerta("Es necesario registrar las estadisticas para este centro educativo");
            } else {
                if (sessionView.getCodigoDepartamento() != null) {
                    lstRubros = catalogoRepo.findRubrosByCe(codigoEntidad, sessionView.getIdProcesoAdq());

                    if (lstRubros.isEmpty()) {
                        JsfUtil.mensajeAlerta("No se han registrado rubros de contratación para el código: " + codigoEntidad);
                    }
                } else {
                    JsfUtil.mensajeAlerta("Debe de seleccionar un departamento y municipio.");
                }
            }
        }
    }

    public void validacionCodigoEntidadByRubro() {
        params.clear();
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idProcesoAdq.id", sessionView.getIdProcesoAdq()).build());
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idRubroAdq.id", idRubro).build());

        detalleProceso = catalogoRepo.findByParam(DetalleProcesoAdq.class, params);

        if (entidadEducativa.getCodigoDepartamento().getId().equals(sessionView.getUsuario().getCodigoDepartamento().getId()) || sessionView.getUsuario().getIdTipoUsuario().getIdTipoUsuario().compareTo(1l) == 0) {
            params.clear();

            params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "codigoEntidad.codigoEntidad", codigoEntidad).build());
            params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idDetProcesoAdq.id", detalleProceso.getId()).build());
            params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "estadoEliminacion", 0l).build());

            ofertaBienesServicio = ofertaRepo.findEntityByParam(params);

            if (ofertaBienesServicio != null) {
                PrimeFaces.current().executeScript("onClick('btnModificarOferta')");
            } else {
                ofertaBienesServicio = new OfertaBienesServicio();
                ofertaBienesServicio.setIdDetProcesoAdq(detalleProceso);
                ofertaBienesServicio.setCodigoEntidad(entidadEducativa);
                cargaItemsPorCe();
            }
        } else {
            JsfUtil.mensajeAlerta("El codigo del centro escolar no pertenece al departamento " + JsfUtil.getNombreDepartamentoByCodigo(sessionView.getUsuario().getCodigoDepartamento().getId()) + "<br/>"
                    + "Departamento del CE: " + entidadEducativa.getCodigoEntidad() + " es " + entidadEducativa.getCodigoDepartamento().getNombreDepartamento());
        }
    }

    private void cargaItemsPorCe() {
        if (!getOferta().getIdDetProcesoAdq().getHabilitar()) {
            JsfUtil.mensajeInformacion("El registro de contratos ha sido deshabilitado por el Administrador.");
        } else {
            mapItems = ofertaRepo.getNoItemsByCodigoEntidadAndIdProcesoAdq(codigoEntidad, detalleProceso, nivelesEducativos,
                    detalleProceso.getIdRubroAdq().getIdRubroUniforme().intValue() == 1);
        }
    }

    public String nuevo() {
        codigoEntidad = "";
        codigoDepartamento = null;
        idMunicipio = null;
        return "reg02OfertaEdit?faces-redirect=true&operacion=" + TipoOperacion.NUEVO;
    }

    public String editarOferta() {
        return MessageFormat.format("reg02OfertaEdit?faces-redirect=true&operacion={0}&idRubro={1}&codigoEntidad={2}", TipoOperacion.MODIFICAR, idRubro, codigoEntidad);
    }

    public String editarDetalleOferta() {
        return "reg02DetalleOferta?faces-redirect=true&idParticipante=" + idParticipante;
    }

    public void agregarProveedor() {
        Long cantidad;
        String idMunicipios;
        municipioCe = entidadEducativa.getIdMunicipio().getNombreMunicipio().concat((entidadEducativa.getNombreCanton() == null ? "" : entidadEducativa.getNombreCanton()));

        if (detalleProceso.getIdRubroAdq().getIdRubroUniforme() == 1l) {
            cantidad = cantidadAlumnos * 2l; //dos piezas por uniforme
        } else {
            cantidad = cantidadAlumnos;
        }

        idMunicipios = repoView.findIdMunicipios(entidadEducativa.getIdMunicipio().getId()).getIdMunicipios();

        //calcular el menor precio de ambos listados
        lstEmpresas = participanteRepo.findLstProveedoresDisponibles(detalleProceso, codigoEntidad, entidadEducativa.getCodigoDepartamento().getId(), entidadEducativa.getCodigoMunicipio(), entidadEducativa.getCodigoCanton(),
                entidadEducativa.getIdMunicipio().getId(), idMunicipios, Boolean.TRUE, cantidad, mapItems);
        lstEmpresasOtros = participanteRepo.findLstProveedoresDisponibles(detalleProceso, codigoEntidad, entidadEducativa.getCodigoDepartamento().getId(), entidadEducativa.getCodigoMunicipio(), entidadEducativa.getCodigoCanton(),
                entidadEducativa.getIdMunicipio().getId(), idMunicipios, Boolean.FALSE, cantidad, mapItems);

        lstCapaEmpresas.clear();
        lstCapaEmpresas.addAll(lstEmpresas);
        lstCapaEmpresasOtros.clear();
        lstCapaEmpresasOtros.addAll(lstEmpresasOtros);

        if (lstEmpresas.isEmpty()) {
            JsfUtil.mensajeInformacion("No se encontrarón proveedores para este centro escolar.");
        } else {
            PrimeFaces.current().executeScript("PF('dlgProveedor').show();");
        }
    }

    public void onSelect(Long idEmpresa) {
        Boolean isDepaCalificado;
        try {
            if (idEmpresa == null) {
                JsfUtil.mensajeAlerta("Debe de seleccionar un proveedor");
            } else {
                empresaSeleccionada = catalogoRepo.findEntityByPk(Empresa.class, idEmpresa);
                lstPrecios = participanteRepo.getLstPreciosByIdEmpresaAndIdProcesoAdq(empresaSeleccionada.getId(), detalleProceso.getIdProcesoAdq().getIdAnho().getId(), mapItems.get("idNivelesCe"));
                if (lstPrecios.isEmpty()) {
                    JsfUtil.mensajeAlerta("Este proveedor no posee precios de referencia. No se puede ingresar a la oferta.");
                } else {
                    if (sessionView.getCodigoDepartamento() != null) {
                        params.clear();
                        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idMuestraInteres.idEmpresa", empresaSeleccionada).build());
                        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idMuestraInteres.idRubroInteres.id", idRubro).build());
                        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idMuestraInteres.idAnho.id", sessionView.getIdAnho()).build());

                        List<CapaDistribucionAcre> lstCapa = (List<CapaDistribucionAcre>) catalogoRepo.findListByParam(CapaDistribucionAcre.class, params);
                        CapaDistribucionAcre capa = lstCapa.get(0);

                        if (capa.getCodigoDepartamento().getId().equals("00")) {
                            isDepaCalificado = capa.getCodigoDepartamento().getId().equals("00");
                        } else {
                            isDepaCalificado = capa.getCodigoDepartamento().getId().equals(entidadEducativa.getCodigoDepartamento().getId());
                        }

                        if (isDepaCalificado) {
                            if (!findParticipanteEnOferta(empresaSeleccionada)) {
                                Participante participante = new Participante();

                                participante.setEstadoEliminacion(0l);
                                participante.setFechaInsercion(LocalDate.now());
                                participante.setIdEmpresa(empresaSeleccionada);
                                participante.setIdOferta(getOferta());
                                participante.setModificativa(0l);
                                participante.setUsuarioInsercion(sessionView.getUsuario().getIdPersona().getUsuario());

                                participante.setPorcentajeCapacidad(new BigDecimal(proveedorSeleccionado.getPorcentajeCapacidad() + proveedorSeleccionado.getPorcentajeCapacidadItem()));
                                participante.setPorcentajeGeo(new BigDecimal(proveedorSeleccionado.getPorcentajeGeo()));
                                participante.setPorcentajePrecio(new BigDecimal(proveedorSeleccionado.getPorcentajePrecio()));

                                getOferta().getParticipantesList().add(participante);

                                JsfUtil.mensajeInformacion("Se agrego el proveedor seleccionado");
                            }
                        } else {
                            JsfUtil.mensajeAlerta("Este proveedor no esta calificado para este departamento.");
                        }
                    } else {
                        JsfUtil.mensajeAlerta("Debe de seleccionar un departamento y municipio.");
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(OfertaBienesServiciosView.class.getName()).log(Level.INFO, null, "Error OfertaBienesServiciosController.onSelect()");
            Logger.getLogger(OfertaBienesServiciosView.class.getName()).log(Level.INFO, null, "Codigo Entidad " + codigoEntidad);
            Logger.getLogger(OfertaBienesServiciosView.class.getName()).log(Level.INFO, null, "id Empresa " + idEmpresa);
            Logger.getLogger(OfertaBienesServiciosView.class.getName()).log(Level.INFO, null, "Error: " + e.getMessage());
        }
    }

    private boolean findParticipanteEnOferta(Empresa empresa) {
        if (getOferta().getParticipantesList() == null) {
            getOferta().setParticipantesList(new ArrayList(0));
        }

        for (Participante par : getOferta().getParticipantesList()) {
            if (par.getIdEmpresa().getNumeroNit().equals(empresa.getNumeroNit()) && par.getEstadoEliminacion().compareTo(0l) == 0) {
                JsfUtil.mensajeError("El proveedor seleccionado ya existe en la oferta actual.");
                return true;
            }
        }
        return false;
    }

    public void cargarDetalleProveedor(BigDecimal idEmpresa) {
        empresaSeleccionada = catalogoRepo.findEntityByPk(Empresa.class, idEmpresa);
        File carpetaNfs = new File("/imagenes/PaqueteEscolar/Fotos_Zapatos/" + empresaSeleccionada.getNumeroNit() + "/");
        lstPrecios = preciosRepo.getLstPreciosByIdEmpresaAndIdProcesoAdq(empresaSeleccionada.getId(), detalleProceso.getIdProcesoAdq().getIdAnho().getId(), mapItems.get("idNivelesCe"));

        if (carpetaNfs.list() != null) {
            lstEstilos = new SelectItem[carpetaNfs.list().length + 1];
            int i = 0;
            lstEstilos[i] = new SelectItem("-", "Seleccione");
            i++;
            for (String string : carpetaNfs.list()) {
                lstEstilos[i] = new SelectItem(string, string);
                i++;
            }
        }
    }
}
