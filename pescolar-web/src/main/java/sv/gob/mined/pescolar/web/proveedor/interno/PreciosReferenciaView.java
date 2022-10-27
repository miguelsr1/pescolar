package sv.gob.mined.pescolar.web.proveedor.interno;

/**
 *
 * @author misanchez
 */
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import sv.gob.mined.pescolar.model.CatalogoProducto;
import sv.gob.mined.pescolar.model.DetalleProcesoAdq;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.model.NivelEducativo;
import sv.gob.mined.pescolar.model.PreciosRefRubro;
import sv.gob.mined.pescolar.model.PreciosRefRubroEmp;
import sv.gob.mined.pescolar.model.dto.DetalleItemDto;
import sv.gob.mined.pescolar.model.dto.OfertaGlobal;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.repository.ParticipanteRepo;
import sv.gob.mined.pescolar.repository.PrecioRefRubroEmpRepo;
import sv.gob.mined.pescolar.utils.Constantes;
import sv.gob.mined.pescolar.utils.JsfUtil;
import sv.gob.mined.pescolar.utils.Reportes;
import sv.gob.mined.pescolar.utils.db.Filtro;
import sv.gob.mined.pescolar.utils.enums.TipoOperador;
import sv.gob.mined.pescolar.web.SessionView;

@Named
@ViewScoped
public class PreciosReferenciaView implements Serializable {

    private int idRow;

    private Boolean deshabiliar = true;

    private String msjError;
    private String rowEdit;

    private List<Filtro> params = new ArrayList();

    private Empresa empresa;
    private DetalleProcesoAdq detalleProcesoAdq;
    private PreciosRefRubroEmp precioRef = new PreciosRefRubroEmp();

    private List<CatalogoProducto> lstItem = new ArrayList();
    private List<PreciosRefRubroEmp> lstPreciosReferencia = new ArrayList();
    private List<DetalleItemDto> lstPreciosMaximos = new ArrayList();
    private List<PreciosRefRubro> lstPrecios = new ArrayList();

    @Inject
    private SessionView sessionView;
    @Inject
    private Reportes reportes;
    @Inject
    private CatalogoRepo catalogoRepo;
    @Inject
    private ParticipanteRepo participanteRepo;
    @Inject
    private PrecioRefRubroEmpRepo precioRepo;

    @Inject
    private CargaGeneralView cargaGeneralView;

    @PostConstruct
    public void init() {
        if (cargaGeneralView.getEmpresa() != null && cargaGeneralView.getEmpresa().getId() != null) {
            cargaInicialDeDatos();
        }
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<DetalleItemDto> getLstPreciosMaximos() {
        return lstPreciosMaximos;
    }

    public CargaGeneralView getCargaGeneralView() {
        return cargaGeneralView;
    }

    public void setCargaGeneralView(CargaGeneralView cargaGeneralView) {
        this.cargaGeneralView = cargaGeneralView;
    }

    public Boolean getDeshabiliar() {
        return deshabiliar;
    }

    public void setDeshabiliar(Boolean deshabiliar) {
        this.deshabiliar = deshabiliar;
    }

    public List<PreciosRefRubroEmp> getLstPreciosReferencia() {
        return lstPreciosReferencia;
    }

    public void setLstPreciosReferencia(List<PreciosRefRubroEmp> lstPreciosReferencia) {
        this.lstPreciosReferencia = lstPreciosReferencia;
    }

    public PreciosRefRubroEmp getPrecioRef() {
        return precioRef;
    }

    public void setPrecioRef(PreciosRefRubroEmp preciosRef) {
        this.precioRef = preciosRef;
    }

    public int getIdRow() {
        return idRow;
    }

    public void setIdRow(int idRow) {
        this.idRow = idRow;
    }

    public void guardarPreciosRef() {
        String msj;
        Boolean preciosValidos = true;
        for (PreciosRefRubroEmp precio : lstPreciosReferencia) {
            if (precio.getNoItem() != null && !precio.getNoItem().isEmpty() && precio.getIdNivelEducativo() != null && precio.getIdProducto() != null && precio.getPrecioReferencia() != null && precio.getPrecioReferencia().compareTo(BigDecimal.ZERO) == 1) {
            } else {
                preciosValidos = false;
                break;
            }
        }

        if (preciosValidos) {
            lstPreciosReferencia.forEach((PreciosRefRubroEmp precio) -> {
                if (precio.getIdPrecioRefEmp() == null) {
                    precioRepo.save(precio);
                } else {
                    precioRepo.update(precio);
                }
            });
            lstPreciosReferencia = participanteRepo.findPreciosRefRubroEmpRubro(cargaGeneralView.getEmpresa(),
                    cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId(),
                    cargaGeneralView.getIdAnho());

            msj = "Actualización exitosa";

            if (cargaGeneralView.getIdAnho().intValue() > 8) { // anho mayor de 2020
                //validación de ingreso de todos los item para el rubro de uniforme
                if (cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getIdRubroUniforme().intValue() == 1) {
                    if (lstPreciosReferencia.size() < 12) {
                        msj = "Se han guardado los precios, pero es necesario que registre todos los item disponibles.";
                    }
                }
            }

            JsfUtil.mensajeInformacion(msj);
        } else {
            JsfUtil.mensajeInformacion("Los precios de referencia no han sido guardados debido a que existen datos incompletos o erroneos.");
        }
    }

    public void impOfertaGlobal() {
        try {
            String idGestion = ""; //proveedorEJB.datosConfirmados(capacidadInst.getIdMuestraInteres().getIdMuestraInteres(),empresa.getIdEmpresa(),VarSession.getVariableSessionUsuario());

            String lugar = cargaGeneralView.getEmpresa().getIdMunicipio().getNombreMunicipio().concat(", ").concat(cargaGeneralView.getEmpresa().getIdMunicipio().getCodigoDepartamento().getNombreDepartamento());

            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            HashMap param = new HashMap();
            param.put("ubicacionImagenes", ctx.getRealPath(Reportes.PATH_IMAGENES) + File.separator);
            param.put("pEscudo", ctx.getRealPath(Reportes.PATH_IMAGENES) + File.separator);
            param.put("usuarioInsercion", sessionView.getUsuario().getIdPersona().getUsuario());
            param.put("pLugar", lugar);
            param.put("pRubro", JsfUtil.getNombreRubroById(cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId()));
            param.put("pIdRubro", cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId().intValue());
            param.put("pCorreoPersona", cargaGeneralView.getEmpresa().getIdPersona().getEmail());
            param.put("pIdGestion", idGestion);

            List<OfertaGlobal> lstDatos = reportes.getLstOfertaGlobal(cargaGeneralView.getDetRubroMuestraInteres());
            lstDatos.get(0).setRubro(JsfUtil.getNombreRubroById(cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId()));
            if (lstDatos.get(0).getDepartamento().contains("TODO EL PAIS")) {
                param.put("productor", Boolean.TRUE);
            } else {
                param.put("productor", Boolean.FALSE);
            }

            List<JasperPrint> jasperPrintList = new ArrayList();

            jasperPrintList.add(JasperFillManager.fillReport(
                    reportes.getPathReporte("sv/gob/mined/apps/reportes/oferta" + File.separator + "rptOfertaGlobalProv" + cargaGeneralView.getProcesoAdquisicion().getIdAnho().getAnho() + ".jasper"),
                    param, new JRBeanCollectionDataSource(lstDatos)));

            String muni = sessionView.getNombreMunicipio();

            param.put("pLugar", cargaGeneralView.getEmpresa().getIdMunicipio().getCodigoDepartamento().getNombreDepartamento());

            if (cargaGeneralView.getEmpresa().getIdPersoneria().getId().intValue() == 1) {
                jasperPrintList.add(reportes.getReporteAImprimir("sv/gob/mined/apps/reportes/declaracion" + File.separator + "rptDeclaracionJurAceptacionPerProvNat" + cargaGeneralView.getProcesoAdquisicion().getIdAnho().getAnho(), param,
                        new JRBeanCollectionDataSource(reportes.getDeclaracionJurada(cargaGeneralView.getEmpresa(), cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId(), cargaGeneralView.getDetRubroMuestraInteres().getIdAnho().getId(), muni))));

            } else {
                jasperPrintList.add(reportes.getReporteAImprimir("sv/gob/mined/apps/reportes/declaracion" + File.separator + "rptDeclaracionJurAceptacionPerProvJur" + cargaGeneralView.getProcesoAdquisicion().getIdAnho().getAnho(), param, new JRBeanCollectionDataSource(reportes.getDeclaracionJurada(cargaGeneralView.getEmpresa(), cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId(), cargaGeneralView.getDetRubroMuestraInteres().getIdAnho().getId(), muni))));

            }

            if (!jasperPrintList.isEmpty()) {
                reportes.generarReporte(jasperPrintList, "oferta_global_" + cargaGeneralView.getEmpresa().getNumeroNit());
            }
        } catch (JRException | IOException ex) {
            Logger.getLogger(PreciosReferenciaView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void filtroProveedores() {
        lstPreciosReferencia.clear();
        deshabiliar = true;

        Map<String, Object> options = new HashMap();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 500);
        options.put("contentWidth", 750);

        PrimeFaces.current().dialog().openDynamic(Constantes.DLG_BUSCAR_PROVEEDOR, options, null);
    }

    private void cargaInicialDeDatos() {
        params.clear();
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idProcesoAdq.id", cargaGeneralView.getProcesoAdquisicion().getId()).build());
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idRubroAdq.id", cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId()).build());

        detalleProcesoAdq = catalogoRepo.findByParam(DetalleProcesoAdq.class, params);
        empresa = cargaGeneralView.getEmpresa();

        cargarPrecioRef();
        cargarPreciosMaximos();
    }

    public void empSelecPrecioRef(SelectEvent event) {
        if (event.getObject() != null) {
            if (event.getObject() instanceof Empresa) {
                cargaGeneralView.setEmpresa((Empresa) event.getObject());
                cargaGeneralView.cargarDetalleCalificacion();

                cargaInicialDeDatos();

                if (cargaGeneralView.getCapacidadInstPorRubro() != null && cargaGeneralView.getCapacidadInstPorRubro().getId() != null) {
                    cargarPrecioRef();
                }
            } else {
                Logger.getLogger(PreciosReferenciaView.class
                        .getName()).log(Level.WARNING, "No se pudo convertir el objeto a la clase Empresa{0}", event.getObject());
            }
        }
    }

    private void cargarPrecioRef() {
        lstItem = participanteRepo.findItemProveedor(cargaGeneralView.getEmpresa(), detalleProcesoAdq);
        lstPreciosReferencia = participanteRepo.findPreciosRefRubroEmpRubro(cargaGeneralView.getEmpresa(),
                detalleProcesoAdq.getIdRubroAdq().getId(),
                detalleProcesoAdq.getIdProcesoAdq().getIdAnho().getId());
        PrimeFaces.current().ajax().update("frmPrincipal");
    }

    public void agregarNewPrecio() {
        if (detalleProcesoAdq.getId() == null) {
            JsfUtil.mensajeAlerta("Debe de seleccionar un proceso de contratación");
        } else {
            if (detalleProcesoAdq.getId() != null) {
                PreciosRefRubroEmp current = new PreciosRefRubroEmp();
                current.setEstadoEliminacion(0l);
                current.setUsuarioInsercion(sessionView.getUsuario().getIdPersona().getUsuario());
                current.setFechaInsercion(LocalDate.now());
                current.setIdEmpresa(cargaGeneralView.getEmpresa());
                current.setIdMuestraInteres(cargaGeneralView.getDetRubroMuestraInteres());
                lstPreciosReferencia.add(current);
            } else {
                JsfUtil.mensajeInformacion("Debe de seleccionar un proceso de contratación.");
            }
        }
    }

    public void onCellEdit(CellEditEvent event) {
        msjError = "";
        FacesContext context = FacesContext.getCurrentInstance();
        precioRef = context.getApplication().evaluateExpressionGet(context, "#{precio}", PreciosRefRubroEmp.class);
        boolean valido = true;
        if (!valido) {
            precioRef.setIdProducto(null);
            precioRef.setIdNivelEducativo(null);
        } else {
            this.rowEdit = String.valueOf(event.getRowIndex());
            if (event.getColumn().getColumnKey().contains("item")) {
                String numItem = event.getNewValue().toString();
                editarNumeroDeItem(event.getRowIndex(), numItem);
            } else if (event.getColumn().getColumnKey().contains("precio")) {
                if (precioRef.getNoItem() != null && !precioRef.getNoItem().isEmpty()) {
                    agregarPrecio(precioRef.getNoItem());
                } else {
                    JsfUtil.mensajeAlerta("Primero debe de ingresar el número de Item");
                }
            }
        }
    }

    private void editarNumeroDeItem(int rowEdit, String numItem) {
        Boolean itemRepetido = false;
        //BigDecimal precioLibro = BigDecimal.ZERO;
        for (int i = 0; i < lstPreciosReferencia.size(); i++) {
            if (i != rowEdit) {
                if (lstPreciosReferencia.get(i).getNoItem() != null
                        && lstPreciosReferencia.get(i).getNoItem().equals(numItem)) {
                    itemRepetido = true;
                    break;
                }
            }
        }

        if (itemRepetido) {
            precioRef.setNoItem("");
            precioRef.setIdProducto(null);
            precioRef.setIdNivelEducativo(null);
            msjError = "Este Item ya fue ingresado.";
        } else {
            CatalogoProducto item = null;
            NivelEducativo nivel = null;
            if (numItem != null && !numItem.isEmpty()) {
                switch (detalleProcesoAdq.getIdRubroAdq().getId().intValue()) {
                    case 1: //UNIFORMES
                    case 4:
                        switch (Integer.parseInt(numItem)) {
                            case 0:
                                break;
                            case 1:
                            case 6:
                            case 10:
                                item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 30l);
                                switch (Integer.parseInt(numItem)) {
                                    case 1:
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 1l);
                                        break;
                                    case 6:
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 2l);
                                        break;
                                    default:
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 6l);
                                        break;
                                }
                                break;
                            case 2:
                            case 7:
                            case 11:
                                item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 44l);
                                switch (Integer.parseInt(numItem)) {
                                    case 2:
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 1l);
                                        break;
                                    case 7:
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 2l);
                                        break;
                                    default:
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 6l);
                                        break;
                                }
                                break;
                            case 3:
                            case 8:
                            case 12:
                                item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 29l);
                                switch (Integer.parseInt(numItem)) {
                                    case 3:
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 1l);
                                        break;
                                    case 8:
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 2l);
                                        break;
                                    default:
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 6l);
                                        break;
                                }
                                break;
                            case 4:
                                item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 31l);
                                nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 1l);
                                break;
                            case 5:
                            case 9:
                            case 13:
                                item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 34l);
                                switch (Integer.parseInt(numItem)) {
                                    case 5:
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 1l);
                                        break;
                                    case 9:
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 2l);
                                        break;
                                    default:
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 6l);
                                        break;
                                }
                                break;
                            default:
                                item = null;
                                nivel = null;
                                msjError = "El item ingresado no es válido.";
                                break;
                        }
                        break;
                    case 2: //UTILES
                        switch (detalleProcesoAdq.getIdProcesoAdq().getIdAnho().getId().intValue()) {
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                                //procesos antes de la contratacion de 2019
                                item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                switch (Integer.parseInt(numItem)) {
                                    case 1:
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 1l);
                                        break;
                                    case 2:
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 3l);
                                        break;
                                    case 3:
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 4l);
                                        break;
                                    case 4:
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 5l);
                                        break;
                                    case 5:
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 6l);
                                        break;
                                    default:
                                        item = null;
                                        nivel = null;
                                        msjError = "El item ingresado no es válido.";
                                        break;
                                }
                                break;
                            case 7:
                            case 8:
                                //procesos mayor o igual a la contratacion de 2019
                                item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                switch (numItem) {
                                    case "1":
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 1l);
                                        break;
                                    case "2":
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 3l);
                                        break;
                                    case "3":
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 4l);
                                        break;
                                    case "4":
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 5l);
                                        break;
                                    case "5":
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 6l);
                                        break;
                                    default:
                                        item = null;
                                        nivel = null;
                                        msjError = "El item ingresado no es válido.";
                                        break;
                                }
                                break;
                            default:
                                //procesos mayor o igual a la contratacion de 2021
                                item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                switch (numItem) {
                                    case "1":
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 22l);
                                        break;
                                    case "2":
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 3l);
                                        break;
                                    case "3":
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 4l);
                                        break;
                                    case "4":
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 5l);
                                        break;
                                    case "4.4":
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 23l);
                                        break;
                                    case "5":
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 6l);
                                        break;
                                    case "5.1":
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 24l);
                                        break;
                                    case "6":
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 27l);
                                        break;
                                    default:
                                        item = null;
                                        nivel = null;
                                        msjError = "El item ingresado no es válido.";
                                        break;
                                }
                                break;
                        }
                        break;
                    case 3: //ZAPATOS
                        switch (detalleProcesoAdq.getIdProcesoAdq().getIdAnho().getId().intValue()) {
                            case 9: //año 2021
                                switch (Integer.parseInt(numItem)) {
                                    case 1:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 21l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 22l);
                                        break;
                                    case 2:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 43l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 22l);
                                        break;
                                    case 3:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 21l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 3l);
                                        break;
                                    case 4:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 43l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 3l);
                                        break;
                                    case 5:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 21l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 4l);
                                        break;
                                    case 6:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 43l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 4l);
                                        break;
                                    case 7:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 21l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 5l);
                                        break;
                                    case 8:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 43l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 5l);
                                        break;
                                    case 9:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 21l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 6l);
                                        break;
                                    case 10:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 43l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 6l);
                                        break;
                                    default:
                                        item = null;
                                        nivel = null;
                                        msjError = "El item ingresado no es válido.";
                                        break;
                                }
                                break;
                            default:
                                switch (Integer.parseInt(numItem)) {
                                    case 1:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 21l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 1l);
                                        break;
                                    case 2:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 43l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 1l);
                                        break;
                                    case 3:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 21l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 3l);
                                        break;
                                    case 4:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 43l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 3l);
                                        break;
                                    case 5:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 21l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 4l);
                                        break;
                                    case 6:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 43l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 4l);
                                        break;
                                    case 7:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 21l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 5l);
                                        break;
                                    case 8:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 43l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 5l);
                                        break;
                                    case 9:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 21l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 6l);
                                        break;
                                    case 10:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 43l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 6l);
                                        break;
                                    default:
                                        item = null;
                                        nivel = null;
                                        msjError = "El item ingresado no es válido.";
                                        break;
                                }
                                break;
                        }
                        break;
                }

                if (item == null && nivel == null) {
                } else if (isProductoIsValid(item.getId())) {
                    precioRef.setIdProducto(item);
                    precioRef.setIdNivelEducativo(nivel);
                } else {
                    precioRef.setNoItem("");
                    msjError = "El proveedore NO ESTA CALIFICADO para ofertar este ITEM";
                }
            }
        }
    }

    private void agregarPrecio(String noItem) {
        if (precioRef != null) {
            BigDecimal preRef = BigDecimal.ZERO;

            if (precioRef.getIdNivelEducativo() != null) {
                switch (detalleProcesoAdq.getIdRubroAdq().getId().intValue()) {
                    case 1://UNIFORMES
                    case 4:
                    case 5:
                        preRef = getPrecioRefUniforme();
                        break;
                    case 2:
                        preRef = lstPrecios.stream().parallel().filter(p -> p.getIdNivelEducativo().getId() == precioRef.getIdNivelEducativo().getId()).findAny().orElse(new PreciosRefRubro()).getPrecioMaxMas();
                        break;
                    case 3:
                        DetalleItemDto preTemp = lstPreciosMaximos.stream().parallel().filter(pre -> pre.getNoItem().equals(noItem)).findAny().orElse(new DetalleItemDto());
                        preRef = preTemp.getPrecioUnitario();
                        break;
                }

                if (precioRef.getPrecioReferencia() != null && precioRef.getPrecioReferencia().compareTo(preRef) == 1) {
                    precioRef.setPrecioReferencia(BigDecimal.ZERO);
                    switch (detalleProcesoAdq.getIdRubroAdq().getId().intValue()) {
                        case 1:
                        case 4:
                        case 5:
                            msjError = "Precio Máximo de Referencia para: <br/>"
                                    + "1)<strong> Parvularia</strong>: - Blusa, Falda y Camisa: $ 5.10, Pantalon Corto $ 4.80 y Pantalon largo Clima frio: $ 7.20<br />"
                                    + "2)<strong> Básica y Bachillerato</strong>: - Blusa, Falda y Camisa: $ 5.40 y Pantalon: $ 7.20<br/>";
                            break;
                        case 2:
                            if (detalleProcesoAdq.getIdProcesoAdq().getIdAnho().getId().intValue() == 9) {
                                msjError = "Precio Máximo de Referencia para: <br/>"
                                        + "1)<strong> Parvularia</strong>: $ " + lstPrecios.stream().parallel().filter(pre -> pre.getIdNivelEducativo().getId() == 22).findAny().orElse(new PreciosRefRubro()).getPrecioMaxMas() + "<br/>"
                                        + "2)<strong> Primer Ciclo</strong>: $ " + lstPrecios.stream().parallel().filter(pre -> pre.getIdNivelEducativo().getId() == 3).findAny().orElse(new PreciosRefRubro()).getPrecioMaxMas() + "<br/>"
                                        + "3)<strong> Segundo Ciclo</strong>: $ " + lstPrecios.stream().parallel().filter(pre -> pre.getIdNivelEducativo().getId() == 4).findAny().orElse(new PreciosRefRubro()).getPrecioMaxMas() + "<br/>"
                                        + "4)<strong> Tercer Ciclo</strong>: $ " + lstPrecios.stream().parallel().filter(pre -> pre.getIdNivelEducativo().getId() == 5).findAny().orElse(new PreciosRefRubro()).getPrecioMaxMas() + "<br/>"
                                        + "5)<strong> Bachillerato: $ " + lstPrecios.stream().parallel().filter(pre -> pre.getIdNivelEducativo().getId() == 6).findAny().orElse(new PreciosRefRubro()).getPrecioMaxMas() + "<br/>"
                                        + "6)<strong> Empaque y distribución de libros: $ " + lstPrecios.stream().parallel().filter(pre -> pre.getIdNivelEducativo().getId() == 27).findAny().orElse(new PreciosRefRubro()).getPrecioMaxMas() + "</strong>";
                            } else {
                                msjError = "Precio Máximo de Referencia para: <br/>"
                                        + "1)<strong> Parvularia</strong>: $ " + lstPrecios.stream().parallel().filter(pre -> pre.getIdNivelEducativo().getId() == 22).findAny().orElse(new PreciosRefRubro()).getPrecioMaxMas() + "<br/>"
                                        + "2)<strong> Primer Ciclo</strong>: $ " + lstPrecios.stream().parallel().filter(pre -> pre.getIdNivelEducativo().getId() == 3).findAny().orElse(new PreciosRefRubro()).getPrecioMaxMas() + "<br/>"
                                        + "3)<strong> Segundo Ciclo</strong>: $ " + lstPrecios.stream().parallel().filter(pre -> pre.getIdNivelEducativo().getId() == 4).findAny().orElse(new PreciosRefRubro()).getPrecioMaxMas() + "<br/>"
                                        + "4)<strong> Tercer Ciclo</strong>: $ " + lstPrecios.stream().parallel().filter(pre -> pre.getIdNivelEducativo().getId() == 5).findAny().orElse(new PreciosRefRubro()).getPrecioMaxMas() + "<br/>"
                                        + "5)<strong> Bachillerato: $ " + lstPrecios.stream().parallel().filter(pre -> pre.getIdNivelEducativo().getId() == 6).findAny().orElse(new PreciosRefRubro()).getPrecioMaxMas() + "</strong>";
                            }
                            break;
                        case 3:
                            msjError = "Precio Máximo de Referencia para Zapatos escolares de:<br/> "
                                    + "<strong>Inicial y Parvularia</strong>: $ " + lstPrecios.stream().parallel().filter(pre -> pre.getIdNivelEducativo().getId() == 22).findAny().orElse(new PreciosRefRubro()).getPrecioMaxMas() + " <br/>"
                                    + "<strong>Ciclo I</strong>: $ " + lstPrecios.stream().parallel().filter(pre -> pre.getIdNivelEducativo().getId() == 3).findAny().orElse(new PreciosRefRubro()).getPrecioMaxMas() + " <br/>"
                                    + "<strong>Ciclo II</strong>: $ " + lstPrecios.stream().parallel().filter(pre -> pre.getIdNivelEducativo().getId() == 4).findAny().orElse(new PreciosRefRubro()).getPrecioMaxMas() + " <br/>"
                                    + "<strong>Ciclo III</strong>: $ " + lstPrecios.stream().parallel().filter(pre -> pre.getIdNivelEducativo().getId() == 5).findAny().orElse(new PreciosRefRubro()).getPrecioMaxMas() + " <br/>"
                                    + "<strong>Bachillerato</strong>: $ " + lstPrecios.stream().parallel().filter(pre -> pre.getIdNivelEducativo().getId() == 6).findAny().orElse(new PreciosRefRubro()).getPrecioMaxMas();
                            break;
                    }
                }
            } else {
                JsfUtil.mensajeAlerta("Debe de ingresar el número de Item a contratar");
            }
        }
    }

    private BigDecimal getPrecioRefUniforme() {
        BigDecimal preRef = BigDecimal.ZERO;

        //precios max solo para 2023
        switch (precioRef.getIdNivelEducativo().getId().intValue()) {
            case 22: //inicial y parvularia
                switch (precioRef.getIdProducto().getId().intValue()) {
                    case 29://camisa
                    case 30://blusa
                    case 44://falda
                        preRef = new BigDecimal("5.10");
                        break;
                    case 31://pantalon corto
                        preRef = new BigDecimal("4.80");
                        break;
                    case 32://pantalon largo parvularia
                        preRef = new BigDecimal("7.50");
                        break;
                }
                break;
            case 2: //basica
            case 6: //media
                switch (precioRef.getIdProducto().getId().intValue()) {
                    case 29://camisa
                    case 30://blusa
                    case 44://falda
                        preRef = new BigDecimal("5.40");
                        break;
                    case 34://pantalon
                        preRef = new BigDecimal("7.20");
                        break;
                }
                break;
        }

        return preRef;
    }

    private boolean isProductoIsValid(Long idProducto) {
        if (lstItem.stream().anyMatch(producto -> (producto.getId().intValue() == idProducto.intValue()))) {
            return true;
        }
        JsfUtil.mensajeError("El proveedore NO ESTA CALIFICADO para ofertar este ITEM");
        return false;
    }

    public void eliminarDetalle() {
        if (precioRef != null) {
            if (precioRef.getEstadoEliminacion().compareTo(0l) == 0) {
                if (precioRef.getIdPrecioRefEmp() != null) {
                    precioRef.setEstadoEliminacion(1l);
                } else {
                    lstPreciosReferencia.remove(idRow);
                }
            } else {
                precioRef.setEstadoEliminacion(0l);
            }

            precioRef = null;
        } else {
            JsfUtil.mensajeAlerta("Debe seleccionar un precio para poder eliminarlo.");
        }
        idRow = -1;
    }

    public void updateFilaDetalle() {
        if (rowEdit != null) {
            PrimeFaces.current().ajax().update("tblDetallePrecio:" + rowEdit + ":descripcionItem");
            PrimeFaces.current().ajax().update("tblDetallePrecio:" + rowEdit + ":nivelEducativo");
            PrimeFaces.current().ajax().update("tblDetallePrecio:" + rowEdit + ":precio");
            PrimeFaces.current().ajax().update("tblDetallePrecio:" + rowEdit + ":precio2");
        }
        if (!msjError.isEmpty()) {
            JsfUtil.mensajeAlerta(msjError);
        }
    }

    public void cargarPreciosMaximos() {
        lstPrecios = precioRepo.getLstPreciosRefRubroByRubro(detalleProcesoAdq, false);
        DetalleItemDto det = new DetalleItemDto();

        if (detalleProcesoAdq.getIdRubroAdq().getIdRubroUniforme().intValue() == 1) {

            det.setNoItem("1");
            det.setConsolidadoEspTec("Blusas, PARVULARIA");
            det.setPrecioUnitario(new BigDecimal("5.10"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("2");
            det.setConsolidadoEspTec("Falda, PARVULARIA");
            det.setPrecioUnitario(new BigDecimal("5.10"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("3");
            det.setConsolidadoEspTec("Camisas, PARVULARIA");
            det.setPrecioUnitario(new BigDecimal("5.10"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("4");
            det.setConsolidadoEspTec("Pantalon Corto, PARVULARIA");
            det.setPrecioUnitario(new BigDecimal("4.80"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("5");
            det.setConsolidadoEspTec("Pantalon, PARVULARIA");
            det.setPrecioUnitario(new BigDecimal("7.20"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("6");
            det.setConsolidadoEspTec("Blusas, BASICA(I, II Y III)");
            det.setPrecioUnitario(new BigDecimal("5.40"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("7");
            det.setConsolidadoEspTec("Falda, BASICA(I, II Y III)");
            det.setPrecioUnitario(new BigDecimal("5.40"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("8");
            det.setConsolidadoEspTec("Camisas, BASICA(I, II Y III)");
            det.setPrecioUnitario(new BigDecimal("5.40"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("9");
            det.setConsolidadoEspTec("Pantalon, BASICA(I, II Y III)");
            det.setPrecioUnitario(new BigDecimal("7.20"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("10");
            det.setConsolidadoEspTec("Blusas, BACHILLERATO");
            det.setPrecioUnitario(new BigDecimal("5.40"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("11");
            det.setConsolidadoEspTec("Falda, BACHILLERATO");
            det.setPrecioUnitario(new BigDecimal("5.40"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("12");
            det.setConsolidadoEspTec("Camisas, BACHILLERATO");
            det.setPrecioUnitario(new BigDecimal("5.40"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("13");
            det.setConsolidadoEspTec("Pantalon, BACHILLERATO");
            det.setPrecioUnitario(new BigDecimal("7.20"));
            lstPreciosMaximos.add(det);
        }

        for (PreciosRefRubro precio : lstPrecios) {
            det = new DetalleItemDto();
            switch (detalleProcesoAdq.getIdRubroAdq().getId().intValue()) {
                case 2:
                    switch (precio.getIdNivelEducativo().getId().intValue()) {
                        case 1:
                            det.setNoItem("1");
                            det.setConsolidadoEspTec("Utiles Escolares, PARVULARIA");
                            break;
                        case 22:
                            det.setNoItem("1");
                            det.setConsolidadoEspTec("Utiles Escolares, INICIAL Y PARVULARIA");
                            break;
                        case 3:
                            det.setNoItem("2");
                            det.setConsolidadoEspTec("Utiles Escolares, PRIMER CICLO");
                            break;
                        case 4:
                            det.setNoItem("3");
                            det.setConsolidadoEspTec("Utiles Escolares, SEGUNDO CICLO");
                            break;
                        case 5:
                            det.setNoItem("4");
                            det.setConsolidadoEspTec("Utiles Escolares, TERCER CICLO");
                            break;
                        case 23:
                            det.setNoItem("4.4");
                            det.setConsolidadoEspTec("Utiles Escolares, MOD.FLEXIBLE - III CICLO");
                            break;
                        case 6:
                            det.setNoItem("5");
                            det.setConsolidadoEspTec("Utiles Escolares, BACHILLERATO");
                            break;
                        case 24:
                            det.setNoItem("5.1");
                            det.setConsolidadoEspTec("Utiles Escolares, MOD.FLEXIBLE - BACHILLERATO");
                            break;
                        case 27:
                            det.setNoItem("6");
                            det.setConsolidadoEspTec("Empaque y distribución de libros");
                    }
                    det.setPrecioUnitario(precio.getPrecioMaxFem());
                    lstPreciosMaximos.add(det);
                    break;
                case 3:
                    switch (precio.getIdNivelEducativo().getId().intValue()) {
                        case 1:
                            det.setNoItem("1");
                            det.setConsolidadoEspTec("Zapato de niña, PARVULARIA");
                            det.setPrecioUnitario(precio.getPrecioMaxFem());
                            lstPreciosMaximos.add(det);

                            det = new DetalleItemDto();
                            det.setNoItem("2");
                            det.setConsolidadoEspTec("Zapato de niño, PARVULARIA");
                            det.setPrecioUnitario(precio.getPrecioMaxMas());
                            lstPreciosMaximos.add(det);
                            break;
                        case 22:
                            det.setNoItem("1");
                            det.setConsolidadoEspTec("Zapato de niña, INICIAL y PARVULARIA");
                            det.setPrecioUnitario(precio.getPrecioMaxFem());
                            lstPreciosMaximos.add(det);

                            det = new DetalleItemDto();
                            det.setNoItem("2");
                            det.setConsolidadoEspTec("Zapato de niño, INICIAL y PARVULARIA");
                            det.setPrecioUnitario(precio.getPrecioMaxMas());
                            lstPreciosMaximos.add(det);
                            break;
                        case 3:
                            det.setNoItem("3");
                            det.setConsolidadoEspTec("Zapato de niña, PRIMER CICLO");
                            det.setPrecioUnitario(precio.getPrecioMaxFem());
                            lstPreciosMaximos.add(det);

                            det = new DetalleItemDto();
                            det.setNoItem("4");
                            det.setConsolidadoEspTec("Zapato de niño, PRIMER CICLO");
                            det.setPrecioUnitario(precio.getPrecioMaxMas());
                            lstPreciosMaximos.add(det);
                            break;
                        case 4:
                            det.setNoItem("5");
                            det.setConsolidadoEspTec("Zapato de niña, SEGUNDO CICLO");
                            det.setPrecioUnitario(precio.getPrecioMaxFem());
                            lstPreciosMaximos.add(det);

                            det = new DetalleItemDto();
                            det.setNoItem("6");
                            det.setConsolidadoEspTec("Zapato de niño, SEGUNDO CICLO");
                            det.setPrecioUnitario(precio.getPrecioMaxMas());
                            lstPreciosMaximos.add(det);
                            break;
                        case 5:
                            det.setNoItem("7");
                            det.setConsolidadoEspTec("Zapato de niña, TERCER CICLO");
                            det.setPrecioUnitario(precio.getPrecioMaxFem());
                            lstPreciosMaximos.add(det);

                            det = new DetalleItemDto();
                            det.setNoItem("8");
                            det.setConsolidadoEspTec("Zapato de niño, TERCER CICLO");
                            det.setPrecioUnitario(precio.getPrecioMaxMas());
                            lstPreciosMaximos.add(det);
                            break;
                        case 6:
                            det.setNoItem("9");
                            det.setConsolidadoEspTec("Zapato de niña, BACHILLERATO");
                            det.setPrecioUnitario(precio.getPrecioMaxFem());
                            lstPreciosMaximos.add(det);

                            det = new DetalleItemDto();
                            det.setNoItem("10");
                            det.setConsolidadoEspTec("Zapato de niño, BACHILLERATO");
                            det.setPrecioUnitario(precio.getPrecioMaxMas());
                            lstPreciosMaximos.add(det);
                            break;
                    }
                    break;
            }
        }
    }
}
