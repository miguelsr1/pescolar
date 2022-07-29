package sv.gob.mined.pescolar.repository;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.Query;
import sv.gob.mined.pescolar.model.CapaDistribucionAcre;
import sv.gob.mined.pescolar.model.CapaInstPorRubro;
import sv.gob.mined.pescolar.model.Empresa;

/**
 *
 * @author misanchez
 */
@Stateless
public class EmpresaRepo extends AbstractRepository<Empresa, Long> {

    public EmpresaRepo() {
        super(Empresa.class);
    }

    public Boolean guardarCapaInst(CapaDistribucionAcre capaDistribucionAcre, CapaInstPorRubro capaInstPorRubro) {
        try {
            Query query = em.createQuery("UPDATE CapaInstPorRubro c SET c.capacidadAcreditada = :capaCalificada, c.capacidadPropuesta=:capaPropuesta WHERE c.idMuestraInteres.idEmpresa.idEmpresa = :idEmpresa and c.idMuestraInteres.idAnho.idAnho = :pIdAnho and c.idMuestraInteres.idRubroInteres.idRubroInteres = :pIdRubro");
            query.setParameter("capaCalificada", capaInstPorRubro.getCapacidadAcreditada());
            query.setParameter("capaPropuesta", capaInstPorRubro.getCapacidadPropuesta());
            query.setParameter("idEmpresa", capaInstPorRubro.getIdMuestraInteres().getIdEmpresa().getId());
            query.setParameter("pIdAnho", capaInstPorRubro.getIdMuestraInteres().getIdAnho().getId());
            query.setParameter("pIdRubro", capaInstPorRubro.getIdMuestraInteres().getIdRubroInteres().getId());

            query.executeUpdate();

            query = em.createQuery("UPDATE CapaDistribucionAcre c SET c.codigoDepartamento = :codigoDepartamento WHERE c.idMuestraInteres.idEmpresa.idEmpresa = :idEmpresa and c.idMuestraInteres.idAnho.idAnho=:pIdAnho and c.idMuestraInteres.idRubroInteres.idRubroInteres=:pIdRubro");
            query.setParameter("codigoDepartamento", capaDistribucionAcre.getCodigoDepartamento());
            query.setParameter("idEmpresa", capaInstPorRubro.getIdMuestraInteres().getIdEmpresa().getId());
            query.setParameter("pIdAnho", capaInstPorRubro.getIdMuestraInteres().getIdAnho().getId());
            query.setParameter("pIdRubro", capaInstPorRubro.getIdMuestraInteres().getIdRubroInteres().getId());

            query.executeUpdate();

            return true;
        } catch (Exception e) {
            Logger.getLogger(EmpresaRepo.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }
}
