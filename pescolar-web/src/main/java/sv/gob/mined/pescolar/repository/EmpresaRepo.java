package sv.gob.mined.pescolar.repository;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.Query;
import sv.gob.mined.pescolar.model.CapaDistribucionAcre;
import sv.gob.mined.pescolar.model.CapaInstPorRubro;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.model.TecnicoProveedor;

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
            Query query = em.createQuery("UPDATE CapaInstPorRubro c SET c.capacidadAcreditada = :capaCalificada, c.capacidadPropuesta=:capaPropuesta WHERE c.id=:pId");
            query.setParameter("capaCalificada", capaInstPorRubro.getCapacidadAcreditada());
            query.setParameter("capaPropuesta", capaInstPorRubro.getCapacidadPropuesta());
            /*query.setParameter("idEmpresa", capaInstPorRubro.getIdMuestraInteres().getIdEmpresa().getId());
            query.setParameter("pIdAnho", capaInstPorRubro.getIdMuestraInteres().getIdAnho().getId());*/
            query.setParameter("pId", capaInstPorRubro.getId());

            query.executeUpdate();

            query = em.createQuery("UPDATE CapaDistribucionAcre c SET c.codigoDepartamento = :codigoDepartamento WHERE c.id=:pId");
            query.setParameter("codigoDepartamento", capaDistribucionAcre.getCodigoDepartamento());
            /*query.setParameter("idEmpresa", capaInstPorRubro.getIdMuestraInteres().getIdEmpresa().getId());
            query.setParameter("pIdAnho", capaInstPorRubro.getIdMuestraInteres().getIdAnho().getId());*/
            query.setParameter("pId", capaInstPorRubro.getId());

            query.executeUpdate();

            return true;
        } catch (Exception e) {
            Logger.getLogger(EmpresaRepo.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    public List<Empresa> findEmpresaByValorBusqueda(String valor) {
        //Query q = em.createQuery("SELECT e FROM Empresa e WHERE e.numeroNit like :nit OR FUNC('REGEXP_REPLACE', e.razonSocial,' ','') like :razonSocial", Empresa.class);
        Query q = em.createQuery("SELECT e FROM Empresa e WHERE e.numeroNit like :nit OR e.razonSocial like :razonSocial", Empresa.class);
        q.setParameter("nit", "%" + valor + "%");
        q.setParameter("razonSocial", "%" + valor.replace(" ", "") + "%");
        return q.getResultList();
    }

    public TecnicoProveedor getTecnicoProveedor(Long idEmpresa) {
        Query q = em.createQuery("SELECT t FROM TecnicoProveedor t WHERE t.idEmpresa.id = :pIdEmpresa", TecnicoProveedor.class);
        q.setParameter("pIdEmpresa", idEmpresa);

        return (TecnicoProveedor) q.getResultList().get(0);
    }
}