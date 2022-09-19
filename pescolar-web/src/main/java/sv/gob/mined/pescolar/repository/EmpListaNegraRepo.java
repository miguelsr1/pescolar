/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.repository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.pescolar.model.ListaNegraEmpresa;

/**
 *
 * @author CQuintanilla
 */
@Stateless
public class EmpListaNegraRepo extends AbstractRepository<ListaNegraEmpresa,Long> {

    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;

    
    public EmpListaNegraRepo() {
        super(ListaNegraEmpresa.class);
    }
    
    public void guardarCeClimaFrio(ListaNegraEmpresa lisNegra) {
        if (lisNegra.getIdListaNegra()== null) {
            em.persist(lisNegra);
        } else {
            em.merge(lisNegra);
        }
    }
}
