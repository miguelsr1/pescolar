/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.web.proveedor.interno;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import sv.gob.mined.pescolar.model.CapaInstPorRubro;
import sv.gob.mined.pescolar.model.CatalogoProducto;
import sv.gob.mined.pescolar.model.DetalleProcesoAdq;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.model.NivelEducativo;
import sv.gob.mined.pescolar.model.PreciosRefRubro;
import sv.gob.mined.pescolar.model.PreciosRefRubroEmp;
import sv.gob.mined.pescolar.model.dto.DetalleItemDto;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.repository.ParticipanteRepo;
import sv.gob.mined.pescolar.repository.PrecioRefRubroEmpRepo;
import sv.gob.mined.pescolar.utils.JsfUtil;
import sv.gob.mined.pescolar.web.SessionView;

/**
 *
 * @author MISanchez
 */
@Named
@ViewScoped
public class PreciosReferenciaMB implements Serializable {

    private int idRow;

    private Boolean datosVerificados = false;
    private String msjError = "";
    private String rowEdit;

    private Empresa empresa = new Empresa();
    private CapaInstPorRubro capacidadInst = new CapaInstPorRubro();
    private DetalleProcesoAdq detalleProcesoAdq = new DetalleProcesoAdq();

    private List<CatalogoProducto> lstItem = new ArrayList();
    private List<PreciosRefRubroEmp> lstPreciosReferencia = new ArrayList();
    private List<DetalleItemDto> lstPreciosMaximos = new ArrayList();

    private PreciosRefRubroEmp precioRef = new PreciosRefRubroEmp();

    private PreciosRefRubro preMaxRefPar = new PreciosRefRubro();
    private PreciosRefRubro preMaxRefCi = new PreciosRefRubro();
    private PreciosRefRubro preMaxRefCii = new PreciosRefRubro();
    private PreciosRefRubro preMaxRefCiii = new PreciosRefRubro();
    private PreciosRefRubro preMaxRefCiiiMf = new PreciosRefRubro();
    private PreciosRefRubro preMaxRefBac = new PreciosRefRubro();
    private PreciosRefRubro preMaxRefBacMf = new PreciosRefRubro();

    @Inject
    private ParticipanteRepo participanteRepo;
    @Inject
    private PrecioRefRubroEmpRepo preciosRepo;
    @Inject
    private CatalogoRepo catalogoRepo;
    @Inject
    private SessionView sessionView;

    @PostConstruct
    public void ini() {
       
    }

    public void setCapacidadInst(CapaInstPorRubro capacidadInst) {
        this.capacidadInst = capacidadInst;
    }
    
    public int getIdRow() {
        return idRow;
    }

    public void setIdRow(int idRow) {
        this.idRow = idRow;
    }

    public Boolean getDatosVerificados() {
        return datosVerificados;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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

    public List<DetalleItemDto> getLstPreciosMaximos() {
        return lstPreciosMaximos;
    }

    public DetalleProcesoAdq getDetalleProcesoAdq() {
        return detalleProcesoAdq;
    }

    public void setDetalleProcesoAdq(DetalleProcesoAdq detalleProcesoAdq) {
        this.detalleProcesoAdq = detalleProcesoAdq;
    }

    public void guardarPreciosRef() {
        if (detalleProcesoAdq.getId() == null) {
            JsfUtil.mensajeAlerta("Debe de seleccionar un proceso de contratación");
        } else {
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
                    precio.setFechaModificacion(LocalDate.now());
                    precio.setUsuarioModificacion(sessionView.getVariableSessionUsuario());
                    preciosRepo.save(precio);
                });
                lstPreciosReferencia = participanteRepo.findPreciosRefRubroEmpRubro(getEmpresa(), 
                        getDetalleProcesoAdq().getIdRubroAdq().getId(),
                        getDetalleProcesoAdq().getIdProcesoAdq().getIdAnho().getId());

                msj = "Actualización exitosa";

                if (detalleProcesoAdq.getIdProcesoAdq().getIdAnho().getId().intValue() > 8) { // anho mayor de 2020
                    //validación de ingreso de todos los item para el rubro de uniforme
                    if (detalleProcesoAdq.getIdRubroAdq().getIdRubroUniforme().intValue() == 1) {
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
    }

    public void cargarDetalleCalificacion() {
        if (capacidadInst.getIdMuestraInteres().getDatosVerificados() != null && capacidadInst.getIdMuestraInteres().getDatosVerificados()) {
            datosVerificados = true;
        }
        detalleProcesoAdq = JsfUtil.findDetalleByRubroAndAnho(detalleProcesoAdq.getIdProcesoAdq(),
                capacidadInst.getIdMuestraInteres().getIdRubroInteres().getId(),
                capacidadInst.getIdMuestraInteres().getIdAnho().getId());

        cargarPrecioRef();
        cargarPreciosMaximos();
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
                agregarPrecio();
            }
        }
    }

    private void cargarPrecioRef() {
        if (capacidadInst != null && capacidadInst.getId() != null) {
            lstItem = participanteRepo.findItemProveedor(empresa, detalleProcesoAdq);
            lstPreciosReferencia = participanteRepo.findPreciosRefRubroEmpRubro(getEmpresa(), 
                    getDetalleProcesoAdq().getIdRubroAdq().getId(),
                    getDetalleProcesoAdq().getIdProcesoAdq().getIdAnho().getId());

            switch (detalleProcesoAdq.getIdProcesoAdq().getIdAnho().getId().intValue()) {
                case 9:
                case 10:
                    preMaxRefPar = preciosRepo.findPreciosRefRubroByNivelEduAndRubro(22l, detalleProcesoAdq);
                    preMaxRefCi = preciosRepo.findPreciosRefRubroByNivelEduAndRubro(3l, detalleProcesoAdq);
                    preMaxRefCii = preciosRepo.findPreciosRefRubroByNivelEduAndRubro(4l, detalleProcesoAdq);
                    preMaxRefCiii = preciosRepo.findPreciosRefRubroByNivelEduAndRubro(5l, detalleProcesoAdq);
                    preMaxRefBac = preciosRepo.findPreciosRefRubroByNivelEduAndRubro(6l, detalleProcesoAdq);

                    switch (detalleProcesoAdq.getIdRubroAdq().getId().intValue()) {
                        case 1:
                        case 4:
                        case 5:
                            break;
                        case 2:
                            preMaxRefCiiiMf = preciosRepo.findPreciosRefRubroByNivelEduAndRubro(23l, detalleProcesoAdq);
                            preMaxRefBacMf = preciosRepo.findPreciosRefRubroByNivelEduAndRubro(24l, detalleProcesoAdq);
                            break;
                        case 3:
                            break;
                    }

                    break;
                default:
                    preMaxRefPar = preciosRepo.findPreciosRefRubroByNivelEduAndRubro(1l, detalleProcesoAdq);
                    preMaxRefCi = preciosRepo.findPreciosRefRubroByNivelEduAndRubro(3l, detalleProcesoAdq);
                    preMaxRefCii = preciosRepo.findPreciosRefRubroByNivelEduAndRubro(4l, detalleProcesoAdq);
                    preMaxRefCiii = preciosRepo.findPreciosRefRubroByNivelEduAndRubro(5l, detalleProcesoAdq);
                    preMaxRefBac = preciosRepo.findPreciosRefRubroByNivelEduAndRubro(6l, detalleProcesoAdq);
                    break;
            }
        }
        PrimeFaces.current().ajax().update("frmPrincipal");
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

    public void cargarPreciosMaximos() {
        List<PreciosRefRubro> lstPrecios = preciosRepo.getLstPreciosRefRubroByRubro(detalleProcesoAdq);
        DetalleItemDto det = new DetalleItemDto();

        if (detalleProcesoAdq.getIdRubroAdq().getIdRubroUniforme().intValue() == 1) {

            det.setNoItem("1");
            det.setConsolidadoEspTec("Blusas, PARVULARIA");
            det.setPrecioUnitario(new BigDecimal("4.25"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("2");
            det.setConsolidadoEspTec("Falda, PARVULARIA");
            det.setPrecioUnitario(new BigDecimal("4.25"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("3");
            det.setConsolidadoEspTec("Camisas, PARVULARIA");
            det.setPrecioUnitario(new BigDecimal("4.25"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("4");
            det.setConsolidadoEspTec("Pantalon Corto, PARVULARIA");
            det.setPrecioUnitario(new BigDecimal("4.00"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("5");
            det.setConsolidadoEspTec("Pantalon, PARVULARIA");
            det.setPrecioUnitario(new BigDecimal("6.00"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("6");
            det.setConsolidadoEspTec("Blusas, BASICA(I, II Y III)");
            det.setPrecioUnitario(new BigDecimal("4.50"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("7");
            det.setConsolidadoEspTec("Falda, BASICA(I, II Y III)");
            det.setPrecioUnitario(new BigDecimal("4.50"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("8");
            det.setConsolidadoEspTec("Camisas, BASICA(I, II Y III)");
            det.setPrecioUnitario(new BigDecimal("4.50"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("9");
            det.setConsolidadoEspTec("Pantalon, BASICA(I, II Y III)");
            det.setPrecioUnitario(new BigDecimal("6.00"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("10");
            det.setConsolidadoEspTec("Blusas, BACHILLERATO");
            det.setPrecioUnitario(new BigDecimal("4.50"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("11");
            det.setConsolidadoEspTec("Falda, BACHILLERATO");
            det.setPrecioUnitario(new BigDecimal("4.50"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("12");
            det.setConsolidadoEspTec("Camisas, BACHILLERATO");
            det.setPrecioUnitario(new BigDecimal("4.50"));
            lstPreciosMaximos.add(det);

            det = new DetalleItemDto();
            det.setNoItem("13");
            det.setConsolidadoEspTec("Pantalon, BACHILLERATO");
            det.setPrecioUnitario(new BigDecimal("6.00"));
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
                            case 9:
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

    private boolean isProductoIsValid(Long idProducto) {
        if (lstItem.stream().anyMatch(producto -> (producto.getId().intValue() == idProducto.intValue()))) {
            return true;
        }
        JsfUtil.mensajeError("El proveedore NO ESTA CALIFICADO para ofertar este ITEM");
        return false;
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
}
