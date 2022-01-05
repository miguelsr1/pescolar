package sv.gob.mined.pescolar.repository;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sv.gob.mined.pescolar.model.Departamento;
import sv.gob.mined.pescolar.model.EstadoReserva;
import sv.gob.mined.pescolar.model.Municipio;
import sv.gob.mined.pescolar.model.RubrosAmostrarInteres;

/**
 *
 * @author misanchez
 */
@ApplicationScoped
public class CatalogoRepo {
    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;
    
    public List<Departamento> findAllDepartamento(){
        Query q = em.createQuery("SELECT d FROM Departamento d ORDER BY d.id", Departamento.class);
        return q.getResultList();
    }
    
    public List<Municipio> findMunicipiosByDepa(String codigoDepartamento){
        Query q = em.createQuery("SELECT m FROM Municipio m WHERE m.codigoDepartamento.id=:codDepa ORDER BY m.id", Municipio.class);
        q.setParameter("codDepa", codigoDepartamento);
        return q.getResultList();
    }
    
    public List<RubrosAmostrarInteres> findAllRubrosByIdProceso(Integer id){
        Query q = em.createQuery("SELECT d.idRubroAdq FROM DetalleProcesoAdq d WHERE d.idProcesoAdq.id=:id ORDER BY d.idRubroAdq.id", RubrosAmostrarInteres.class);
        q.setParameter("id", id);
        return q.getResultList();
    }
    
    public List<EstadoReserva> findAllEstadoReserva(){
        Query q = em.createQuery("SELECT e FROM EstadoReserva e ORDER BY e.id", EstadoReserva.class);
        return q.getResultList();
    }
    
    public List<SelectItem> getLstDocumentosImp(Boolean uniforme, Integer idAnho, List<SelectItem> lstDocumentosImp) {
        SelectItem garantiaUsoTela = new SelectItem(6, "Garantía  de buen uso y resguardo de la tela");
        
        if (lstDocumentosImp.isEmpty()) {
            //Id son los mismos que estan el la tabla TIPO_RPT
            lstDocumentosImp.add(new SelectItem(7, "Contrato"));
            lstDocumentosImp.add(new SelectItem(5, "Garantía Contrato"));
            lstDocumentosImp.add(new SelectItem(4, "Nota Adjudicación"));
            lstDocumentosImp.add(new SelectItem(3, "Acta Adjudicación"));
            if (idAnho != 10) {
                lstDocumentosImp.add(new SelectItem(12, "Orden de Inicio"));
            }
            lstDocumentosImp.add(new SelectItem(10, "Declaración Adjudicatorio"));
            lstDocumentosImp.add(new SelectItem(13, "Acta de Recomendación"));
            lstDocumentosImp.add(new SelectItem(2, "Cotización"));
            lstDocumentosImp.add(new SelectItem(11, "Oferta Global del Proveedor"));

        }
        if (uniforme) {
            if (!lstDocumentosImp.contains(garantiaUsoTela)) {
                lstDocumentosImp.add(garantiaUsoTela);
            }
        } else {
            lstDocumentosImp.remove(garantiaUsoTela);
        }

        return lstDocumentosImp;
    }
}
