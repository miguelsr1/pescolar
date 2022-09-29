/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.repository;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sv.gob.mined.pescolar.model.RubroPorCe;

/**
 *
 * @author Carlos Quintanilla
 */
@Stateless
public class RubroPorCeRepo extends AbstractRepository<RubroPorCe, Long> {

    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;

    public RubroPorCeRepo() {
        super(RubroPorCe.class);
    }

    public List<RubroPorCe> listarnoborradosporce(String codEntidad) {
        List<RubroPorCe> lista = new ArrayList();
        if (codEntidad != null && !codEntidad.isBlank() ) {
            Query q;
            q = em.createQuery("SELECT rpc "
                    + "FROM RubroPorCe rpc "
                    + "WHERE rpc.codigoEntidad.codigoEntidad =:pCodEntidad "
                    + "and rpc.estadoEliminacion = 0 ", RubroPorCe.class);
            q.setParameter("pCodEntidad", codEntidad);
            lista = q.getResultList();
        }
        return lista;
    }

    public Boolean existeRubro(String codEntidad, Long idRubroInt) {
        Boolean valor = false;
        if (codEntidad != null && !codEntidad.isBlank() && idRubroInt != null ) {
            Query q;
            q = em.createQuery("SELECT count(rpc.id) "
                    + "FROM RubroPorCe rpc "
                    + "WHERE rpc.codigoEntidad.codigoEntidad =:pCodEntidad "
                    + "and rpc.idRubroInteres.id =:pIdRubroInt "
                    + "and rpc.estadoEliminacion = 0 ", Long.class);
            q.setParameter("pCodEntidad", codEntidad);
            q.setParameter("pIdRubroInt", idRubroInt);

            Long contador = (Long) q.getSingleResult();
            if (contador == 0) {
                valor = false;
            } else {
                valor = true;
            }
        }
        return valor;
    }

}
