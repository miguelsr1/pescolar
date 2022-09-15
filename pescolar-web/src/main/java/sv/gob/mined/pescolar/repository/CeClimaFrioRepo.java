/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.repository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.pescolar.model.CeClimaFrio;

/**
 *
 * @author CQuintanilla
 */
@Stateless
public class CeClimaFrioRepo extends AbstractRepository<CeClimaFrio,Long> {

    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;

    
    public CeClimaFrioRepo() {
        super(CeClimaFrio.class);
    }
    
    public void guardarCeClimaFrio(CeClimaFrio ceClimaFrio) {
        if (ceClimaFrio.getIdCe()== null) {
            em.persist(ceClimaFrio);
        } else {
            em.merge(ceClimaFrio);
        }
    }
}
