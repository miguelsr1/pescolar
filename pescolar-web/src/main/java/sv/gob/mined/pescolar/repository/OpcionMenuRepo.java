/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.repository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.pescolar.model.OpcionMenu;

/**
 *
 * @author Carlos Quintanilla
 */
@Stateless
public class OpcionMenuRepo extends AbstractRepository<OpcionMenu, Long> {

    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;

    public OpcionMenuRepo() {
        super(OpcionMenu.class);
    }
    
}
