/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sv.gob.mined.pescolar.model.Persona;

/**
 *
 * @author Carlos Quintanilla
 */
@Stateless
public class PersonaRepo extends AbstractRepository<Persona, Long> {

    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;

    public PersonaRepo() {
        super(Persona.class);
    }

    public List<Persona> listarpersonapordui(String strdui) {
        Query q = em.createQuery("select p from Persona p where p.numeroDui = '" + strdui + "' and p.estadoEliminacion = 0 ", Persona.class);
        return q.getResultList();
    }

    public List<Persona> listarpersonapornit(String strnit) {
        Query q = em.createQuery("select p from Persona p where p.numeroNit='" + strnit + "' and p.estadoEliminacion = 0 ", Persona.class);
        return q.getResultList();
    }

    public List<Persona> listarpersonaporusuario(String strusuario) {
        Query q = em.createQuery("select p from Persona p where p.usuario='" + strusuario + "' and p.estadoEliminacion = 0 ", Persona.class);
        return q.getResultList();
    }

    public List<Persona> listarpersonaporduiconotroid(String strdui, Long id) {
        Query q = em.createQuery("select p from Persona p where p.numeroDui = '" + strdui + "' and p.estadoEliminacion = 0 and p.id <> " + id, Persona.class);
        return q.getResultList();
    }

    public List<Persona> listarpersonapornitconotroid(String strnit, Long id) {
        Query q = em.createQuery("select p from Persona p where p.numeroNit='" + strnit + "' and p.estadoEliminacion = 0 and p.id <> " + id, Persona.class);
        return q.getResultList();
    }

    public List<Persona> listarpersonaporusuarioconotroid(String strusuario, Long id) {
        Query q = em.createQuery("select p from Persona p where p.usuario='" + strusuario + "' and p.estadoEliminacion = 0 and p.id <> " + id, Persona.class);
        return q.getResultList();
    }

    public List<Persona> listarpersonapordocumentos(String numdoc) {
        Query q = em.createQuery("select p from Persona p where p.estadoEliminacion = 0 and (p.numeroDui = '" + numdoc + "' or p.numeroNit = '" + numdoc + "')", Persona.class);
        return q.getResultList();
    }

    public List<Persona> listarpersonapornombresoapellidos(String filtrowhere) {
        Query q = em.createQuery("select p from Persona p "
                + "where p.estadoEliminacion = 0 "
                + "and ("
                + filtrowhere + ")", Persona.class);
        return q.getResultList();
    }

}
