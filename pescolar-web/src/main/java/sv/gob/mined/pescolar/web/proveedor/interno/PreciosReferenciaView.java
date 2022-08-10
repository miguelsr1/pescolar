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

    private DetalleProcesoAdq detalleProcesoAdq;
    private PreciosRefRubroEmp precioRef = new PreciosRefRubroEmp();
    private PreciosRefRubro preMaxRefPar = new PreciosRefRubro();
    private PreciosRefRubro preMaxRefCi = new PreciosRefRubro();
    private PreciosRefRubro preMaxRefCii = new PreciosRefRubro();
    private PreciosRefRubro preMaxRefCiii = new PreciosRefRubro();
    private PreciosRefRubro preMaxRefCiiiMf = new PreciosRefRubro();
    private PreciosRefRubro preMaxRefBac = new PreciosRefRubro();
    private PreciosRefRubro preMaxRefBacMf = new PreciosRefRubro();

    private List<CatalogoProducto> lstItem = new ArrayList();
    private List<PreciosRefRubroEmp> lstPreciosReferencia = new ArrayList();

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

    @Inject
    private PreciosReferenciaMB preciosReferenciaMB;

    @PostConstruct
    public void init() {
        if (cargaGeneralView.getEmpresa() != null && cargaGeneralView.getEmpresa().getId() != null) {
            cargaInicialDeDatos();
        }
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
            lstPreciosReferencia.forEach((precio) -> {
                precioRepo.save(precio);
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
            param.put("usuarioInsercion", sessionView.getVariableSessionUsuario());
            param.put("pLugar", lugar);
            param.put("pRubro", JsfUtil.getNombreRubroById(cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId()));
            param.put("pIdRubro", cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId().intValue());
            param.put("pCorreoPersona", cargaGeneralView.getEmpresa().getIdPersona().getEmail());
            param.put("pIdGestion", idGestion);

            List<OfertaGlobal> lstDatos = reportes.getLstOfertaGlobal(cargaGeneralView.getEmpresa().getNumeroNit(), cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId(), cargaGeneralView.getProcesoAdquisicion().getIdAnho().getId());
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
                Reportes.generarReporte(jasperPrintList, "oferta_global_" + cargaGeneralView.getEmpresa().getNumeroNit());
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
        params.add(new Filtro(TipoOperador.EQUALS, "idProcesoAdq.id", cargaGeneralView.getProcesoAdquisicion().getId()));
        params.add(new Filtro(TipoOperador.EQUALS, "idRubroAdq.id", cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId()));

        detalleProcesoAdq = catalogoRepo.findByParam(DetalleProcesoAdq.class, params);

        //detalleProcesoAdq = JsfUtil.findDetalle(cargaGeneralView.getProcesoAdquisicion(), cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId());
        preciosReferenciaMB.setEmpresa(cargaGeneralView.getEmpresa());
        preciosReferenciaMB.setDetalleProcesoAdq(detalleProcesoAdq);
        preciosReferenciaMB.setCapacidadInst(cargaGeneralView.getCapacidadInstPorRubro());
        preciosReferenciaMB.cargarDetalleCalificacion();

        cargarPrecioRef();
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
        switch (detalleProcesoAdq.getIdProcesoAdq().getIdAnho().getId().intValue()) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                preMaxRefPar = precioRepo.findPreciosRefRubroByNivelEduAndRubro(1l, detalleProcesoAdq);
                preMaxRefCi = precioRepo.findPreciosRefRubroByNivelEduAndRubro(3l, detalleProcesoAdq);
                preMaxRefCii = precioRepo.findPreciosRefRubroByNivelEduAndRubro(4l, detalleProcesoAdq);
                preMaxRefCiii = precioRepo.findPreciosRefRubroByNivelEduAndRubro(5l, detalleProcesoAdq);
                preMaxRefBac = precioRepo.findPreciosRefRubroByNivelEduAndRubro(6l, detalleProcesoAdq);
                if (detalleProcesoAdq.getIdProcesoAdq().getIdAnho().getId().intValue() > 6
                        && detalleProcesoAdq.getIdRubroAdq().getId().intValue() == 2) {
                    //procesos de contratación mayores a 2018 y rubro de utiles
                    /*preMaxRefG1 = preciosReferenciaEJB.findPreciosRefRubroByNivelEduAndRubro(new BigDecimal(10), detalleProcesoAdq);
                        preMaxRefG2 = preciosReferenciaEJB.findPreciosRefRubroByNivelEduAndRubro(new BigDecimal(11), detalleProcesoAdq);
                        preMaxRefG3 = preciosReferenciaEJB.findPreciosRefRubroByNivelEduAndRubro(new BigDecimal(12), detalleProcesoAdq);
                        preMaxRefG4 = preciosReferenciaEJB.findPreciosRefRubroByNivelEduAndRubro(new BigDecimal(13), detalleProcesoAdq);
                        preMaxRefG5 = preciosReferenciaEJB.findPreciosRefRubroByNivelEduAndRubro(new BigDecimal(14), detalleProcesoAdq);
                        preMaxRefG6 = preciosReferenciaEJB.findPreciosRefRubroByNivelEduAndRubro(new BigDecimal(15), detalleProcesoAdq);
                        preMaxRefG7 = preciosReferenciaEJB.findPreciosRefRubroByNivelEduAndRubro(new BigDecimal(7), detalleProcesoAdq);
                        preMaxRefG8 = preciosReferenciaEJB.findPreciosRefRubroByNivelEduAndRubro(new BigDecimal(8), detalleProcesoAdq);
                        preMaxRefG9 = preciosReferenciaEJB.findPreciosRefRubroByNivelEduAndRubro(new BigDecimal(9), detalleProcesoAdq);
                        preMaxRefB1 = preciosReferenciaEJB.findPreciosRefRubroByNivelEduAndRubro(new BigDecimal(16), detalleProcesoAdq);
                        preMaxRefB2 = preciosReferenciaEJB.findPreciosRefRubroByNivelEduAndRubro(new BigDecimal(17), detalleProcesoAdq);*/
                }
                break;
            default: //año 2021 año 2022
                if (detalleProcesoAdq.getIdRubroAdq().getId().intValue() != 1) { //utiles y zapatos
                    preMaxRefPar = precioRepo.findPreciosRefRubroByNivelEduAndRubro(22l, detalleProcesoAdq);
                }
                preMaxRefCi = precioRepo.findPreciosRefRubroByNivelEduAndRubro(3l, detalleProcesoAdq);
                preMaxRefCii = precioRepo.findPreciosRefRubroByNivelEduAndRubro(4l, detalleProcesoAdq);
                preMaxRefCiii = precioRepo.findPreciosRefRubroByNivelEduAndRubro(5l, detalleProcesoAdq);
                preMaxRefBac = precioRepo.findPreciosRefRubroByNivelEduAndRubro(6l, detalleProcesoAdq);

                if (detalleProcesoAdq.getIdRubroAdq().getId().intValue() == 2) { //utiles
                    preMaxRefCiiiMf = precioRepo.findPreciosRefRubroByNivelEduAndRubro(23l, detalleProcesoAdq);
                    preMaxRefBacMf = precioRepo.findPreciosRefRubroByNivelEduAndRubro(24l, detalleProcesoAdq);
                }
                break;
        }
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
        //DataTable tbl = (DataTable) event.getSource();
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
                agregarPrecio();
            }
        }
    }

    private void editarNumeroDeItem(int rowEdit, String numItem) {
        Boolean itemRepetido = false;
        BigDecimal precioLibro = BigDecimal.ZERO;
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
                                switch (Integer.parseInt(numItem)) {
                                    case 1:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 1l);
                                        break;
                                    case 2:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 3l);
                                        break;
                                    case 3:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 4l);
                                        break;
                                    case 4:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 5l);
                                        break;
                                    case 5:
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
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
                                switch (numItem) {
                                    case "1":
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 1l);
                                        break;
                                    case "2":
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 3l);
                                        break;
                                    case "3":
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 4l);
                                        break;
                                    case "4":
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 5l);
                                        break;
                                    case "5":
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 6l);
                                        break;
                                    case "2.1": //grado 1
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 1l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 10l);
                                        precioLibro = new BigDecimal("4.10");
                                        break;
                                    case "2.2": //grado 2
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 1l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 11l);
                                        precioLibro = new BigDecimal("4.10");
                                        break;
                                    case "2.3": //grado 3
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 1l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 12l);
                                        precioLibro = new BigDecimal("3.62");
                                        break;
                                    case "3.1": //grado 4
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 1l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 13l);
                                        precioLibro = new BigDecimal("3.62");
                                        break;
                                    case "3.2": //grado 5
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 1l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 14l);
                                        precioLibro = new BigDecimal("3.62");
                                        break;
                                    case "3.3": //grado 6
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 1l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 15l);
                                        precioLibro = new BigDecimal("3.62");
                                        break;
                                    case "4.1": //grado 7
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 1l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 7l);
                                        precioLibro = new BigDecimal("3.62");
                                        break;
                                    case "4.2": //grado 8
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 1l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 8l);
                                        precioLibro = new BigDecimal("3.62");
                                        break;
                                    case "4.3": //grado 9
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 1l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 9l);
                                        precioLibro = new BigDecimal("3.62");
                                        break;
                                    case "5.1": //1er año bachillerato
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 1l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 16l);
                                        precioLibro = new BigDecimal("2.05");
                                        break;
                                    case "5.2": //2do año bachillerato
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 1l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 17l);
                                        precioLibro = new BigDecimal("2.05");
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
                                switch (numItem) {
                                    case "1":
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 22l);
                                        break;
                                    case "2":
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 3l);
                                        break;
                                    case "3":
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 4l);
                                        break;
                                    case "4":
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 5l);
                                        break;
                                    case "4.4":
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 23l);
                                        break;
                                    case "5":
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 6l);
                                        break;
                                    case "5.1":
                                        item = catalogoRepo.findEntityByPk(CatalogoProducto.class, 54l);
                                        nivel = catalogoRepo.findEntityByPk(NivelEducativo.class, 24l);
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
                    if (precioLibro.intValue() > 0) {
                        precioRef.setPrecioReferencia(precioLibro);
                    }
                } else {
                    precioRef.setNoItem("");
                    msjError = "El proveedore NO ESTA CALIFICADO para ofertar este ITEM";
                }
            }
        }
    }

    public void agregarPrecio() {
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
                        preRef = getPrecioRefUtiles();
                        break;
                    case 3:
                        if (precioRef.getIdNivelEducativo().getId().compareTo(6l) == 0) {
                            preRef = new BigDecimal("16.00");
                        } else {
                            preRef = new BigDecimal("14.60");
                        }
                        break;
                }
            }

            if (precioRef.getPrecioReferencia() != null && precioRef.getPrecioReferencia().compareTo(preRef) == 1) {
                precioRef.setPrecioReferencia(BigDecimal.ZERO);
                switch (detalleProcesoAdq.getIdRubroAdq().getId().intValue()) {
                    case 1:
                    case 5:
                        msjError = "Precio Máximo de Referencia para: <br/>"
                                + "1)<strong> Parvularia</strong>: - Blusa, Falda y Camisa: $ 4.25 y Pantalon Corto $ 4.00<br />"
                                + "2)<strong> Básica y Bachillerato</strong>: - Blusa, Falda y Camisa: $ 4.50 y Pantalon Corto y Pantalon: $ 6.00<br/>";
                        break;
                    case 2:
                        if (detalleProcesoAdq.getIdProcesoAdq().getIdAnho().getId().intValue() == 9) {
                            switch (detalleProcesoAdq.getIdRubroAdq().getId().intValue()) {
                                case 4:
                                case 5:
                                    msjError = "Precio Máximo de Referencia para: <br/>"
                                            + "1)<strong> Parvularia</strong>: $ " + preMaxRefPar.getPrecioMaxMas() + "<br/>"
                                            + "2)<strong> Primer Ciclo</strong>: $ " + preMaxRefCi.getPrecioMaxMas() + "<br/>"
                                            + "3)<strong> Segundo Ciclo</strong>: $ " + preMaxRefCii.getPrecioMaxMas() + "<br/>"
                                            + "4)<strong> Tercer Ciclo</strong>: $ " + preMaxRefCiii.getPrecioMaxMas() + "<br/>"
                                            + "5)<strong> Bachillerato: $ " + preMaxRefBac.getPrecioMaxMas() + "</strong>";
                                    break;
                                case 2:
                                    msjError = "Precio Máximo de Referencia para: <br/>"
                                            + "1)<strong> Inicial y Parvularia</strong>: $ " + preMaxRefPar.getPrecioMaxMas() + "<br/>"
                                            + "2)<strong> Primer Ciclo</strong>: $ " + preMaxRefCi.getPrecioMaxMas() + "<br/>"
                                            + "3)<strong> Segundo Ciclo</strong>: $ " + preMaxRefCii.getPrecioMaxMas() + "<br/>"
                                            + "4)<strong> Tercer Ciclo</strong>: $ " + preMaxRefCiii.getPrecioMaxMas() + "<br/>"
                                            + "4)<strong> Tercer Ciclo - Mod.Flexible</strong>: $ " + preMaxRefCiiiMf.getPrecioMaxMas() + "<br/>"
                                            + "5)<strong> Bachillerato: </strong>$ " + preMaxRefBac.getPrecioMaxMas() + "<br/>"
                                            + "5)<strong> Bachillerato - Mod.Flexible: </strong>$ " + preMaxRefBacMf.getPrecioMaxMas();
                                    break;
                                case 3:
                                    msjError = "Precio Máximo de Referencia para: <br/>"
                                            + "1)<strong> Inicial y Parvularia</strong>: $ " + preMaxRefPar.getPrecioMaxMas() + "<br/>"
                                            + "2)<strong> Primer Ciclo</strong>: $ " + preMaxRefCi.getPrecioMaxMas() + "<br/>"
                                            + "3)<strong> Segundo Ciclo</strong>: $ " + preMaxRefCii.getPrecioMaxMas() + "<br/>"
                                            + "4)<strong> Tercer Ciclo</strong>: $ " + preMaxRefCiii.getPrecioMaxMas() + "<br/>"
                                            + "5)<strong> Bachillerato: </strong>$ " + preMaxRefBac.getPrecioMaxMas();
                                    break;
                            }
                        } else {
                            msjError = "Precio Máximo de Referencia para: <br/>"
                                    + "1)<strong> Parvularia</strong>: $ " + preMaxRefPar.getPrecioMaxMas() + "<br/>"
                                    + "2)<strong> Primer Ciclo</strong>: $ " + preMaxRefCi.getPrecioMaxMas() + "<br/>"
                                    + "3)<strong> Segundo Ciclo</strong>: $ " + preMaxRefCii.getPrecioMaxMas() + "<br/>"
                                    + "4)<strong> Tercer Ciclo</strong>: $ " + preMaxRefCiii.getPrecioMaxMas() + "<br/>"
                                    + "5)<strong> Bachillerato: $ " + preMaxRefBac.getPrecioMaxMas() + "</strong>";
                        }
                        break;
                    case 3:
                        msjError = "Precio Máximo de Referencia para Zapatos escolares de:<br/> "
                                + "<strong>Parvularia y Básica</strong>: $ 14.60 <br/>"
                                + "<strong>Bachillerato</strong>: $16.00";
                        break;
                }
            }
        }
    }

    private BigDecimal getPrecioRefUniforme() {
        BigDecimal preRef = BigDecimal.ZERO;

        switch (precioRef.getIdProducto().getId().intValue()) {
            case 29:
            case 30:
            case 44:
                switch (precioRef.getIdNivelEducativo().getId().intValue()) {
                    case 1:
                        preRef = new BigDecimal("4.25");
                        break;
                    case 2:
                    case 6:
                        preRef = new BigDecimal("4.50");
                        break;
                }
                break;
            case 31:
                switch (precioRef.getIdNivelEducativo().getId().intValue()) {
                    case 1:
                        preRef = new BigDecimal("4.00");
                        break;
                }
                break;
            case 34:
                switch (precioRef.getIdNivelEducativo().getId().intValue()) {
                    case 1:
                        preRef = new BigDecimal("6.00");
                        break;
                    case 2:
                    case 6:
                        preRef = new BigDecimal("6.00");
                        break;
                }
                break;
        }

        return preRef;
    }

    private BigDecimal getPrecioRefUtiles() {
        BigDecimal preRef = BigDecimal.ZERO;

        switch (precioRef.getIdNivelEducativo().getId().intValue()) {
            case 1: //parvularia
                preRef = preMaxRefPar.getPrecioMaxMas();
                break;
            case 22: //incial y parvularia
                preRef = preMaxRefPar.getPrecioMaxMas();
                break;
            case 3: //ciclo I
                preRef = preMaxRefCi.getPrecioMaxMas();
                break;
            case 4: //ciclo II
                preRef = preMaxRefCii.getPrecioMaxMas();
                break;
            case 5://ciclo III
                preRef = preMaxRefCiii.getPrecioMaxMas();
                break;
            case 23://modalidad flexible y ciclo III
                preRef = preMaxRefCiii.getPrecioMaxMas();
                break;
            case 6: //Bachillerato
                preRef = preMaxRefBac.getPrecioMaxMas();
                break;
            case 24: //modalidad flexible y Bachillerato
                preRef = preMaxRefBac.getPrecioMaxMas();
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
}
