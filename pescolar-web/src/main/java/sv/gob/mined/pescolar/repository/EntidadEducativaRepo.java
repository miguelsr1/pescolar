package sv.gob.mined.pescolar.repository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import sv.gob.mined.pescolar.model.EstadisticaCenso;
import sv.gob.mined.pescolar.model.OrganizacionEducativa;
import sv.gob.mined.pescolar.model.ProcesoAdquisicion;
import sv.gob.mined.pescolar.model.TechoRubroEntEdu;
import sv.gob.mined.pescolar.model.dto.contratacion.VwRptCertificacionPresupuestaria;
import sv.gob.mined.pescolar.model.view.VwCatalogoEntidadEducativa;

/**
 *
 * @author misanchez
 */
@ApplicationScoped
public class EntidadEducativaRepo extends AbstractRepository2<VwCatalogoEntidadEducativa, String> {

    public EntidadEducativaRepo() {
        super(VwCatalogoEntidadEducativa.class);
    }

    public OrganizacionEducativa getPresidenteOrganismoEscolar(String codigoEntidad) {
        Query query = getEm().createQuery("SELECT o FROM OrganizacionEducativa o WHERE o.codigoEntidad=:pCodigoEntidad and o.firmaContrato=:pFirmaContrato", OrganizacionEducativa.class);
        query.setParameter("pCodigoEntidad", codigoEntidad);
        query.setParameter("pFirmaContrato", 1);
        List<OrganizacionEducativa> lst = query.getResultList();
        if (lst.isEmpty()) {
            return new OrganizacionEducativa();
        } else {
            return (OrganizacionEducativa) query.getResultList().get(0);
        }
    }

    public OrganizacionEducativa getMiembro(String codigoEntidad, String cargo) {
        Query query = getEm().createQuery("SELECT o FROM OrganizacionEducativa o WHERE o.codigoEntidad=:pCodigoEntidad and o.firmaContrato=:pFirmaContrato and o.cargo=:pCargo", OrganizacionEducativa.class);
        query.setParameter("pCodigoEntidad", codigoEntidad);
        query.setParameter("pFirmaContrato", 0);
        query.setParameter("pCargo", cargo);
        List<OrganizacionEducativa> lst = query.getResultList();
        if (lst.isEmpty()) {
            return new OrganizacionEducativa();
        } else {
            return (OrganizacionEducativa) query.getResultList().get(0);
        }
    }

    public List<TechoRubroEntEdu> getLstTechosByProceso(Long idProcesoAdq, String codigoEntidad) {
        Query q = getEm().createQuery("SELECT t FROM TechoRubroEntEdu t WHERE t.idDetProcesoAdq.idProcesoAdq.id=:pIdPro and t.codigoEntidad=:pCodigoEntidad", TechoRubroEntEdu.class);
        q.setParameter("pIdPro", idProcesoAdq);
        q.setParameter("pCodigoEntidad", codigoEntidad);
        return q.getResultList();
    }

    public List<EstadisticaCenso> getLstEstadisticaByCodEntAndProceso(String codigoEntidad, Long idProcesoAdq) {
        Query q = getEm().createQuery("SELECT e FROM EstadisticaCenso e WHERE e.codigoEntidad=:pCodigoEntidad and e.idProcesoAdq.id=:pIdPro", EstadisticaCenso.class);
        q.setParameter("pCodigoEntidad", codigoEntidad);
        q.setParameter("pIdPro", idProcesoAdq);
        return q.getResultList();
    }

    public Boolean guardarEstadistica(EstadisticaCenso estadistica, String usuario) {
        try {
            if (estadistica.getMasculino() == null) {
                estadistica.setMasculino(BigInteger.ZERO);
            }
            if (estadistica.getFemenimo() == null) {
                estadistica.setFemenimo(BigInteger.ZERO);
            }
            if (estadistica.getId() == null) {
                estadistica.setUsuarioInsercion(usuario);
                getEm().persist(estadistica);
            } else {
                estadistica.setFechaModificacion(LocalDate.now());
                estadistica.setUsuarioModificacion(usuario);
                getEm().merge(estadistica);
            }

            return false;
        } catch (Exception e) {
            Logger.getLogger(EntidadEducativaRepo.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    public Boolean guardarPresupuesto(String usuario, TechoRubroEntEdu... lstTecho) {
        try {
            for (TechoRubroEntEdu techo : lstTecho) {
                if (techo != null) {
                    if (techo.getId() == null) {
                        getEm().persist(techo);
                    } else {
                        techo.setFechaModificacion(LocalDate.now());
                        techo.setUsuarioModificacion(usuario);
                        getEm().merge(techo);
                    }
                }
            }
            return false;
        } catch (Exception e) {
            Logger.getLogger(EntidadEducativaRepo.class.getName()).log(Level.SEVERE, null, e);
            return true;
        }
    }

    public void updateNombreDirectorContratoYPago(String codigoEntidad, Long idAnho, String nombreDirector) {
        StoredProcedureQuery q = getEm().createStoredProcedureQuery("SP_UPD_NOMBRE_DIRECTOR");

        q.registerStoredProcedureParameter("P_COD_ENT", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("P_ID_ANHO", Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("P_NOMBRE", String.class, ParameterMode.IN);

        q.setParameter("P_COD_ENT", codigoEntidad);
        q.setParameter("P_ID_ANHO", idAnho);
        q.setParameter("P_NOMBRE", nombreDirector);

        q.execute();
    }

    public void guardarOrganizacionEducativa(OrganizacionEducativa organizacionEducativa) {
        if (organizacionEducativa.getIdOrganizacionEducativa() == null) {
            getEm().persist(organizacionEducativa);
        } else {
            organizacionEducativa.setFechaModificacion(LocalDate.now());
            getEm().merge(organizacionEducativa);
        }
    }

    public VwRptCertificacionPresupuestaria getCertificacion(String codigoEntidad, ProcesoAdquisicion proceso, Boolean utiles) {
        VwRptCertificacionPresupuestaria cabecera = new VwRptCertificacionPresupuestaria();
        Query q = getEm().createNativeQuery("SELECT * FROM vw_cabecera_certificacion_pre WHERE codigo_entidad = '" + codigoEntidad + "'");
        List lst = q.getResultList();
        for (Object object : lst) {
            Object[] datos = (Object[]) object;

            cabecera.setDepartamento(datos[0].toString());
            cabecera.setMunicipio(datos[1].toString());
            cabecera.setCodigoEntidad(datos[2].toString());
            cabecera.setNombreCe(datos[3].toString());
            cabecera.setModalidadDeAdministracion(datos[4].toString());
        }
        try {
            q = getEm().createQuery("SELECT e FROM EstadisticaCenso e WHERE e.codigoEntidad=:codigoEntidad and e.idNivelEducativo.id=:pIdNivelEdu and e.idProcesoAdq=:proceso", EstadisticaCenso.class);
            q.setParameter("codigoEntidad", codigoEntidad);
            q.setParameter("pIdNivelEdu", (proceso.getIdAnho().getId().intValue() > 8 ? Long.parseLong("22") : Long.parseLong("1")));
            q.setParameter("proceso", proceso);
            cabecera.setParvularia((EstadisticaCenso) q.getSingleResult());

            q = getEm().createQuery("SELECT e FROM EstadisticaCenso e WHERE e.codigoEntidad=:codigoEntidad and e.idNivelEducativo.id=:pIdNivelEdu and e.idProcesoAdq=:proceso", EstadisticaCenso.class);
            q.setParameter("codigoEntidad", codigoEntidad);
            q.setParameter("pIdNivelEdu", 3l);
            q.setParameter("proceso", proceso);
            cabecera.setCiclo1((EstadisticaCenso) q.getSingleResult());

            q = getEm().createQuery("SELECT e FROM EstadisticaCenso e WHERE e.codigoEntidad=:codigoEntidad and e.idNivelEducativo.id=:pIdNivelEdu and e.idProcesoAdq=:proceso", EstadisticaCenso.class);
            q.setParameter("codigoEntidad", codigoEntidad);
            q.setParameter("pIdNivelEdu", 4l);
            q.setParameter("proceso", proceso);
            cabecera.setCiclo2((EstadisticaCenso) q.getSingleResult());

            q = getEm().createQuery("SELECT e FROM EstadisticaCenso e WHERE e.codigoEntidad=:codigoEntidad and e.idNivelEducativo.id=:pIdNivelEdu and e.idProcesoAdq=:proceso", EstadisticaCenso.class);
            q.setParameter("codigoEntidad", codigoEntidad);
            q.setParameter("pIdNivelEdu", 5l);
            q.setParameter("proceso", proceso);
            cabecera.setCiclo3((EstadisticaCenso) q.getSingleResult());

            q = getEm().createQuery("SELECT e FROM EstadisticaCenso e WHERE e.codigoEntidad=:codigoEntidad and e.idNivelEducativo.id=:pIdNivelEdu and e.idProcesoAdq=:proceso", EstadisticaCenso.class);
            q.setParameter("codigoEntidad", codigoEntidad);
            q.setParameter("pIdNivelEdu", 6l);
            q.setParameter("proceso", proceso);
            cabecera.setBachillerato((EstadisticaCenso) q.getSingleResult());

            if (utiles) {
                q = getEm().createQuery("SELECT e FROM EstadisticaCenso e WHERE e.codigoEntidad=:codigoEntidad and e.idNivelEducativo.id=:pIdNivelEdu and e.idProcesoAdq=:proceso", EstadisticaCenso.class);
                q.setParameter("codigoEntidad", codigoEntidad);
                q.setParameter("pIdNivelEdu", 10l);
                q.setParameter("proceso", proceso);
                cabecera.setGrado1((EstadisticaCenso) q.getSingleResult());

                q = getEm().createQuery("SELECT e FROM EstadisticaCenso e WHERE e.codigoEntidad=:codigoEntidad and e.idNivelEducativo.id=:pIdNivelEdu and e.idProcesoAdq=:proceso", EstadisticaCenso.class);
                q.setParameter("codigoEntidad", codigoEntidad);
                q.setParameter("pIdNivelEdu", 11l);
                q.setParameter("proceso", proceso);
                cabecera.setGrado2((EstadisticaCenso) q.getSingleResult());

                q = getEm().createQuery("SELECT e FROM EstadisticaCenso e WHERE e.codigoEntidad=:codigoEntidad and e.idNivelEducativo.id=:pIdNivelEdu and e.idProcesoAdq=:proceso", EstadisticaCenso.class);
                q.setParameter("codigoEntidad", codigoEntidad);
                q.setParameter("pIdNivelEdu", 12l);
                q.setParameter("proceso", proceso);
                cabecera.setGrado3((EstadisticaCenso) q.getSingleResult());

                q = getEm().createQuery("SELECT e FROM EstadisticaCenso e WHERE e.codigoEntidad=:codigoEntidad and e.idNivelEducativo.id=:pIdNivelEdu and e.idProcesoAdq=:proceso", EstadisticaCenso.class);
                q.setParameter("codigoEntidad", codigoEntidad);
                q.setParameter("pIdNivelEdu", 13l);
                q.setParameter("proceso", proceso);
                cabecera.setGrado4((EstadisticaCenso) q.getSingleResult());

                q = getEm().createQuery("SELECT e FROM EstadisticaCenso e WHERE e.codigoEntidad=:codigoEntidad and e.idNivelEducativo.id=:pIdNivelEdu and e.idProcesoAdq=:proceso", EstadisticaCenso.class);
                q.setParameter("codigoEntidad", codigoEntidad);
                q.setParameter("pIdNivelEdu", 14l);
                q.setParameter("proceso", proceso);
                cabecera.setGrado5((EstadisticaCenso) q.getSingleResult());

                q = getEm().createQuery("SELECT e FROM EstadisticaCenso e WHERE e.codigoEntidad=:codigoEntidad and e.idNivelEducativo.id=:pIdNivelEdu and e.idProcesoAdq=:proceso", EstadisticaCenso.class);
                q.setParameter("codigoEntidad", codigoEntidad);
                q.setParameter("pIdNivelEdu", 15l);
                q.setParameter("proceso", proceso);
                cabecera.setGrado6((EstadisticaCenso) q.getSingleResult());

                q = getEm().createQuery("SELECT e FROM EstadisticaCenso e WHERE e.codigoEntidad=:codigoEntidad and e.idNivelEducativo.id=:pIdNivelEdu and e.idProcesoAdq=:proceso", EstadisticaCenso.class);
                q.setParameter("codigoEntidad", codigoEntidad);
                q.setParameter("pIdNivelEdu", 7l);
                q.setParameter("proceso", proceso);
                cabecera.setGrado7((EstadisticaCenso) q.getSingleResult());

                q = getEm().createQuery("SELECT e FROM EstadisticaCenso e WHERE e.codigoEntidad=:codigoEntidad and e.idNivelEducativo.id=:pIdNivelEdu and e.idProcesoAdq=:proceso", EstadisticaCenso.class);
                q.setParameter("codigoEntidad", codigoEntidad);
                q.setParameter("pIdNivelEdu", 8l);
                q.setParameter("proceso", proceso);
                cabecera.setGrado8((EstadisticaCenso) q.getSingleResult());

                q = getEm().createQuery("SELECT e FROM EstadisticaCenso e WHERE e.codigoEntidad=:codigoEntidad and e.idNivelEducativo.id=:pIdNivelEdu and e.idProcesoAdq=:proceso", EstadisticaCenso.class);
                q.setParameter("codigoEntidad", codigoEntidad);
                q.setParameter("pIdNivelEdu", 9l);
                q.setParameter("proceso", proceso);
                cabecera.setGrado9((EstadisticaCenso) q.getSingleResult());

                q = getEm().createQuery("SELECT e FROM EstadisticaCenso e WHERE e.codigoEntidad=:codigoEntidad and e.idNivelEducativo.id=:pIdNivelEdu and e.idProcesoAdq=:proceso", EstadisticaCenso.class);
                q.setParameter("codigoEntidad", codigoEntidad);
                q.setParameter("pIdNivelEdu", 16l);
                q.setParameter("proceso", proceso);
                cabecera.setGradob1((EstadisticaCenso) q.getSingleResult());

                q = getEm().createQuery("SELECT e FROM EstadisticaCenso e WHERE e.codigoEntidad=:codigoEntidad and e.idNivelEducativo.id=:pIdNivelEdu and e.idProcesoAdq=:proceso", EstadisticaCenso.class);
                q.setParameter("codigoEntidad", codigoEntidad);
                q.setParameter("pIdNivelEdu", 17l);
                q.setParameter("proceso", proceso);
                cabecera.setGradob2((EstadisticaCenso) q.getSingleResult());
            }

            return cabecera;
        } catch (NumberFormatException ex) {
            Logger.getLogger(EntidadEducativaRepo.class.getName()).log(Level.WARNING, null, "Error en obteniendo las estadisticas de censo rápido");
            Logger.getLogger(EntidadEducativaRepo.class.getName()).log(Level.WARNING, null, "Codigo Entidad: " + codigoEntidad);
            Logger.getLogger(EntidadEducativaRepo.class.getName()).log(Level.WARNING, null, "Proceso de compra: " + proceso.getIdAnho().getAnho());
            return null;
        } finally {
        }
    }

    public List<OrganizacionEducativa> lstCorreosDirectores() {
        Query q = getEm().createQuery("SELECT o FROM OrganizacionEducativa o WHERE o.firmaContrato=:pFirmaContrato and o.correoElectronico is not null", OrganizacionEducativa.class);
        q.setParameter("pFirmaContrato", 1);
        return q.getResultList();
    }
}
