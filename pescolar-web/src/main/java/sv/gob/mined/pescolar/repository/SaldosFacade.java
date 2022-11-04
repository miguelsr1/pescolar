package sv.gob.mined.pescolar.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.AccessTimeout;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sv.gob.mined.pescolar.model.CapaInstPorRubro;
import sv.gob.mined.pescolar.model.ContratosOrdenesCompra;
import sv.gob.mined.pescolar.model.EstadoReserva;
import sv.gob.mined.pescolar.model.HistorialCamEstResAdj;
import sv.gob.mined.pescolar.model.InfoGeneralContratacion;
import sv.gob.mined.pescolar.model.Participante;
import sv.gob.mined.pescolar.model.ResolucionesAdjudicativa;
import sv.gob.mined.pescolar.model.TechoRubroEntEdu;

/**
 *
 * @author msanchez
 */
@Singleton
@LocalBean
@AccessTimeout(value = 8000, unit = TimeUnit.MILLISECONDS)
public class SaldosFacade {

    @PersistenceContext
    private EntityManager em;

    @Lock(LockType.WRITE)
    public HashMap<String, Object> aplicarReservaDeFondos(ResolucionesAdjudicativa resAdj,
            Long estadoReserva, String codigoEntidad, String comentarioReversion, String usuario) {
        HashMap<String, Object> param = new HashMap();
        //TODO falta derechos de usuarios y manejo de errores
        TechoRubroEntEdu techoCE;

        if (resAdj.getIdEstadoReserva().getId().intValue() == 4) {
            param.put("error", "Esta reserva se encuentra anulada y no se puede modificar.");
        } else {
            //recuperar techo presupuestario del centro escolar
            techoCE = findTechoRubroEntEdu(codigoEntidad, resAdj.getIdParticipante().getIdOferta().getIdDetProcesoAdq().getId());

            //DIGITADA -> APLICADA  y  REVERTIDAD -> APLICADA
            if (techoCE != null) {
                if ((resAdj.getIdEstadoReserva().getId().intValue() == 1 && estadoReserva.intValue() == 2)
                        || (resAdj.getIdEstadoReserva().getId().intValue() == 3 && estadoReserva.intValue() == 2)) {
                    //verificar disponibilidad presupuestaria del C.E. y capacidad calificada del proveedor
                    param = existeDiponibilidad(techoCE, resAdj, param);
                    if (!param.containsKey("error") || usuario.equals("RMINERO") || usuario.equals("RAFAARIAS") || usuario.equals("CVILLEGAS")) {
                        param = aplicarSaldos(resAdj, techoCE, estadoReserva, comentarioReversion, usuario, param);

                        //Si ya existe un contrato para esta reserva de fondos, se actualizan los datos del resumen de contrataciones
                        /*if (!resAdj.getContratosOrdenesComprasList().isEmpty() || resAdj.getIdEstadoReserva().getId().intValue() == 3) {
                            agregarDatosAResumen(resAdj.getContratosOrdenesComprasList().get(0));
                        }*/
                    }
                } else //DIGITADA -> ANULADA y REVERTIDA -> ANULADA 
                if ((resAdj.getIdEstadoReserva().getId().intValue() == 1 && estadoReserva.intValue() == 4)
                        || (resAdj.getId().intValue() == 3 && estadoReserva.intValue() == 4)) {
                    param = aplicarSaldos(resAdj, techoCE, estadoReserva, comentarioReversion, usuario, param);
                } else //APLICADA -> REVERTIDA
                if (resAdj.getIdEstadoReserva().getId().intValue() == 2 && estadoReserva.intValue() == 3) {
                    param = aplicarSaldos(resAdj, techoCE, estadoReserva, comentarioReversion, usuario, param);
                    //removerDatosResumen(resAdj);
                }
            } else {
                param.put("error", "Se ha generado un error en la aplicación de fondos.");
            }
        }

        return param;
    }

    private HashMap<String, Object> existeDiponibilidad(TechoRubroEntEdu techoCE, ResolucionesAdjudicativa resAdj, HashMap<String, Object> param) {
        param = existeDisponibilidadProveedor(resAdj, param);
        if (param.containsKey("error")) {
            return param;
        }

        return existeDisponibilidadesCE(techoCE, resAdj.getValor(), param);
    }

    private HashMap<String, Object> existeDisponibilidadProveedor(ResolucionesAdjudicativa resAdj, HashMap<String, Object> param) {
        List lista;
        BigInteger cantidadAdjudicada = resAdj.getIdParticipante().getCantidad(); //getCantidadAdjudicadaActual(resAdj.getIdParticipante().getIdParticipante());
        BigInteger disponibilidadProveedor;
        lista = getSaldoParticipante(resAdj);
        if (lista != null && !lista.isEmpty()) {
            disponibilidadProveedor = ((BigDecimal) lista.get(0)).toBigInteger();
            if (disponibilidadProveedor.compareTo(cantidadAdjudicada) != -1) {
            } else {
                param.put("error", "El proveedor no posee disponibilidad para este rubro! Cantidad disponible:" + disponibilidadProveedor.intValue()
                        + " Cantidad de Adjudicada: " + cantidadAdjudicada.intValue());
            }
        } else {
            param.put("error", "El proveedor no posee disponibilidad para este rubro!");
        }
        return param;
    }

    private HashMap<String, Object> existeDisponibilidadesCE(TechoRubroEntEdu techoCE, BigDecimal valorReserva, HashMap<String, Object> param) {

        if (techoCE.getMontoDisponible().compareTo(valorReserva) != -1) {
        } else {
            param.put("error", "El centro escolar " + techoCE.getCodigoEntidad() + " no posee disponibilidad presupuestaria!"
                    + "Monto Disponible: " + techoCE.getMontoDisponible() + " Monto Adjudicado: " + valorReserva);
        }
        return param;
    }

    private List getSaldoParticipante(ResolucionesAdjudicativa res) {
        String sql = String.format("SELECT NVL(FN_VERIFICAR_SALDO_PROVEEDOR(%d, %d, %d, %d, %d), 0) FROM DUAL",
                res.getIdParticipante().getIdOferta().getIdDetProcesoAdq().getIdProcesoAdq().getIdAnho().getId().intValue(),
                res.getIdParticipante().getIdOferta().getIdDetProcesoAdq().getIdRubroAdq().getId().intValue(),
                res.getIdParticipante().getIdEmpresa().getId().intValue(),
                res.getIdParticipante().getIdOferta().getIdDetProcesoAdq().getId(),
                res.getIdParticipante().getIdOferta().getIdDetProcesoAdq().getIdProcesoAdq().getId());

        Query q = em.createNativeQuery(sql);
        return q.getResultList();
    }

    private HashMap<String, Object> aplicarSaldos(ResolucionesAdjudicativa res,
            TechoRubroEntEdu techoCE,
            Long idEstadoReserva,
            //BigInteger cantidad,
            String comentarioReversion,
            String usuario,
            HashMap<String, Object> param) {
        try {

            Long cantidadAdjudicada = res.getIdParticipante().getCantidad().longValue();
            Long estadoAnterior = res.getIdEstadoReserva().getId();

            //no devuelve nada cuando no hay registros
            Query query;
            //if (res.getIdParticipante().getIdOferta().getIdDetProcesoAdq().getIdProcesoAdq().getIdAnho().getAnho().equals("2018")) {
            query = em.createQuery("SELECT c FROM CapaInstPorRubro c WHERE c.idMuestraInteres.idEmpresa=:idEmpresa and c.idMuestraInteres.idAnho.id=:pIdAnho and c.idMuestraInteres.idRubroInteres.id=:pIdRubro and c.estadoEliminacion = 0 and c.idMuestraInteres.estadoEliminacion=0 and c.idMuestraInteres.idEmpresa.estadoEliminacion=0 and c.idProcesoAdq=:pIdPro", CapaInstPorRubro.class);
            query.setParameter("idEmpresa", res.getIdParticipante().getIdEmpresa());
            query.setParameter("pIdAnho", res.getIdParticipante().getIdOferta().getIdDetProcesoAdq().getIdProcesoAdq().getIdAnho().getId());
            query.setParameter("pIdRubro", res.getIdParticipante().getIdOferta().getIdDetProcesoAdq().getIdRubroAdq().getId());
            query.setParameter("pIdPro", res.getIdParticipante().getIdOferta().getIdDetProcesoAdq().getIdProcesoAdq());

            List<CapaInstPorRubro> lst = query.getResultList();

            if (!lst.isEmpty()) {
                CapaInstPorRubro capaInst = lst.get(0);//em.find(CapaInstPorRubro.class, lst.get(0).getIdCapInstRubro());

                switch (idEstadoReserva.intValue()) {
                    case 2: //aplicar
                        techoCE.setMontoAdjudicado(techoCE.getMontoAdjudicado().add(res.getValor()));
                        techoCE.setMontoDisponible(techoCE.getMontoDisponible().add(res.getValor().negate()));

                        capaInst.setCapacidadAdjudicada(getAdjParticipante(res.getIdParticipante()) + cantidadAdjudicada);
                        break;
                    case 3: //revertir modificada
                    case 5:
                        techoCE.setMontoAdjudicado(techoCE.getMontoAdjudicado().add(res.getValor().negate()));
                        techoCE.setMontoDisponible(techoCE.getMontoDisponible().add(res.getValor()));

                        capaInst.setCapacidadAdjudicada(getAdjParticipante(res.getIdParticipante()) - cantidadAdjudicada);
                        break;
                    case 4: //anular
                        res.setIdEstadoReserva(em.find(EstadoReserva.class, idEstadoReserva));
                        break;
                }

                res.setIdEstadoReserva(em.find(EstadoReserva.class, idEstadoReserva));
                res.setUsuarioModificacion(usuario);
                res.setFechaModificacion(LocalDateTime.now());
                res.setValor(res.getIdParticipante().getMonto());

                em.merge(techoCE);
                res = em.merge(res);

                em.merge(capaInst);

                historialCambioEstado(res, estadoAnterior, idEstadoReserva, comentarioReversion, usuario);
            }
            return param;
        } catch (Exception e) {
            param.put("error", "Se ha generado un error en la aplicación de fondos.");
            Logger.getLogger(SaldosFacade.class.getName()).log(Level.SEVERE, null, e);
            return param;
        }
    }

    private Long getAdjParticipante(Participante par) {
        List lst = getAdjudicacionParticipante(par);

        try {
            return (Long) lst.get(0);
        } catch (Exception ex) {
            return 0l;
        }
    }

    private List getAdjudicacionParticipante(Participante par) {
        String sql = String.format("SELECT NVL(FN_GET_CANT_ADJ_PROVE(%d, %d), 0) FROM DUAL",
                par.getIdOferta().getIdDetProcesoAdq().getId(),
                par.getIdEmpresa().getId().intValue());

        Query q = em.createNativeQuery(sql);
        return q.getResultList();
    }

    private TechoRubroEntEdu findTechoRubroEntEdu(String codigoEntidad, Integer idDetProcesoAdq) {
        Query query = em.createQuery("SELECT t FROM TechoRubroEntEdu t WHERE t.codigoEntidad=:codigoEntidad and t.idDetProcesoAdq.id=:idProceso and t.estadoEliminacion=0", TechoRubroEntEdu.class);
        query.setParameter("codigoEntidad", codigoEntidad);
        query.setParameter("idProceso", idDetProcesoAdq);

        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            return (TechoRubroEntEdu) query.getSingleResult();
        }
    }

    private void agregarDatosAResumen(ContratosOrdenesCompra contrato) {
        Query q = em.createNamedQuery("SP_ADD_RESUMEN_CE_PROCESADO");
        q.setParameter("P_ID_DET_PROCESO_ADQ", contrato.getIdResolucionAdj().getIdParticipante().getIdOferta().getIdDetProcesoAdq().getId());
        q.setParameter("P_CODIGO_ENTIDAD", contrato.getIdResolucionAdj().getIdParticipante().getIdOferta().getCodigoEntidad().getCodigoEntidad());
        q.setParameter("P_CODIGO_DEPARTAMENTO", contrato.getIdResolucionAdj().getIdParticipante().getIdOferta().getCodigoEntidad().getCodigoDepartamento().getId());
        q.setParameter("P_CODIGO_MUNICIPIO", contrato.getIdResolucionAdj().getIdParticipante().getIdOferta().getCodigoEntidad().getCodigoMunicipio());
        q.getResultList();

        q = em.createNamedQuery("SP_ADD_RESUMEN_ADJ_EMP");
        q.setParameter("P_ID_DET_PROCESO_ADQ", contrato.getIdResolucionAdj().getIdParticipante().getIdOferta().getIdDetProcesoAdq().getId());
        q.setParameter("P_ID_EMPRESA", contrato.getIdResolucionAdj().getIdParticipante().getIdEmpresa().getId());
        q.setParameter("P_ID_TIPO_EMPRESA", contrato.getIdResolucionAdj().getIdParticipante().getIdEmpresa().getIdTipoEmpresa().getId());
        q.setParameter("P_CODIGO_DEPARTAMENTO", contrato.getIdResolucionAdj().getIdParticipante().getIdOferta().getCodigoEntidad().getCodigoDepartamento().getId());
        q.setParameter("P_CODIGO_ENTIDAD", contrato.getIdResolucionAdj().getIdParticipante().getIdOferta().getCodigoEntidad().getCodigoEntidad());
        q.setParameter("P_SUBTOTAL", contrato.getIdResolucionAdj().getIdParticipante().getMonto());
        q.setParameter("P_CANTIDAD", contrato.getIdResolucionAdj().getIdParticipante().getCantidad());
        q.getResultList();
    }

    private void removerDatosResumen(ResolucionesAdjudicativa resAdj) {
        Query q = em.createNativeQuery("delete RESUMEN_ADJ_EMP where id_det_proceso_adq = ?1 and codigo_entidad =?2 and id_empresa = ?3");
        q.setParameter(1, resAdj.getIdParticipante().getIdOferta().getIdDetProcesoAdq().getId());
        q.setParameter(2, resAdj.getIdParticipante().getIdOferta().getCodigoEntidad().getCodigoEntidad());
        q.setParameter(3, resAdj.getIdParticipante().getIdEmpresa().getId());

        q.executeUpdate();

        q = em.createNativeQuery("delete resumen_ce_procesados where id_det_proceso_adq = ?1 and codigo_entidad = ?2");
        q.setParameter(1, resAdj.getIdParticipante().getIdOferta().getIdDetProcesoAdq().getId());
        q.setParameter(2, resAdj.getIdParticipante().getIdOferta().getCodigoEntidad().getCodigoEntidad());

        q.executeUpdate();

        q = em.createQuery("SELECT c FROM ContratosOrdenesCompra c WHERE c.idResolucionAdj.id=:idRes and c.estadoEliminacion=0", InfoGeneralContratacion.class);
        q.setParameter("idRes", resAdj.getId());

        if (!q.getResultList().isEmpty()) {
            q = em.createNativeQuery("delete INFO_GENERAL_CONTRATACION where id_contrato = ?1");
            q.setParameter(1, 0);

            q.executeUpdate();
        }
    }

    private void historialCambioEstado(ResolucionesAdjudicativa res,
            Long estadoAnterior,
            Long estadoNuevo,
            String comentarioReversion, String usuario) {
        HistorialCamEstResAdj historial = new HistorialCamEstResAdj();
        if (estadoAnterior.compareTo(1l) == 0 && estadoNuevo.compareTo(2l) == 0) {
            historial.setComentarioHistorial("Primera aplicación de reserva de fondos");
        } else if (estadoAnterior.compareTo(2l) == 0 && estadoNuevo.compareTo(3l) == 0) {
            historial.setComentarioHistorial(comentarioReversion);
        }
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(estadoNuevo);
        historial.setFechaCambioEstado(LocalDateTime.now());
        historial.setIdResolucionAdj(res);
        historial.setUsuario(usuario);

        em.persist(historial);
    }
}
