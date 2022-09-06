package sv.gob.mined.pescolar.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import sv.gob.mined.pescolar.model.CapaDistribucionAcre;
import sv.gob.mined.pescolar.model.CapaInstPorRubro;
import sv.gob.mined.pescolar.model.DetRubroMuestraIntere;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.model.EmpresaNoItem;
import sv.gob.mined.pescolar.model.EmpresaPreciosRef;
import sv.gob.mined.pescolar.model.PreciosRefRubroEmp;
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
            em.merge(capaDistribucionAcre);
            em.merge(capaInstPorRubro);

            return true;
        } catch (Exception e) {
            Logger.getLogger(EmpresaRepo.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    public List<Empresa> findEmpresaByValorBusqueda(String valor) {
        Query q = em.createQuery("SELECT e FROM Empresa e WHERE e.idPersona.numeroDui = :pNumeroDui OR  e.numeroNit like :pNumeroNit OR e.razonSocial like :pRazonSocial", Empresa.class);
        q.setParameter("pNumeroDui", valor.toUpperCase());
        q.setParameter("pNumeroNit", "%" + valor.toUpperCase() + "%");
        q.setParameter("pRazonSocial", "%" + valor.toUpperCase().replace(" ", "") + "%");
        return q.getResultList();
    }

    public TecnicoProveedor getTecnicoProveedor(Long idEmpresa) {
        Query q = em.createQuery("SELECT t FROM TecnicoProveedor t WHERE t.idEmpresa.id = :pIdEmpresa", TecnicoProveedor.class);
        q.setParameter("pIdEmpresa", idEmpresa);

        return q.getResultList().isEmpty() ? null : (TecnicoProveedor) q.getResultList().get(0);
    }

    public Empresa findEmpresaByNit(String nit, Boolean empresa) {
        Query q = em.createQuery("SELECT e FROM Empresa e WHERE e" + (empresa ? "" : ".id") + ".numeroNit=:nit and e.estadoEliminacion=0", Empresa.class);
        q.setParameter("nit", nit);

        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (Empresa) q.getSingleResult();
        }
    }

    public String datosConfirmados(Long idDetRubro, Long idEmpresa, String usuario) {
        DetRubroMuestraIntere detRubro = em.find(DetRubroMuestraIntere.class, idDetRubro);

        if (detRubro.getAceptacionTerminos()) {

        } else {
            detRubro.setFechaModificacion(LocalDate.now());
            detRubro.setDatosVerificados(true);
            detRubro.setAceptacionTerminos(true);
            detRubro.setUsuarioModificacion(usuario);
            em.merge(detRubro);

            calcularNoItems(detRubro.getIdRubroInteres().getId(), detRubro.getIdAnho().getId(), detRubro.getIdEmpresa().getNumeroNit(), detRubro);
            calcularPreRefByNit(detRubro.getIdRubroInteres().getId(), detRubro.getIdAnho().getId(), detRubro.getIdEmpresa().getNumeroNit());
        }

        return getIdGestionByProceso(idEmpresa, detRubro.getId());
    }

    private void calcularNoItems(Long idRubro, Long idAnho, String numeroNit, DetRubroMuestraIntere detRubro) {
        Query q = em.createQuery("SELECT p FROM PreciosRefRubroEmp p WHERE p.idEmpresa.numeroNit=:pNit and p.estadoEliminacion = 0 and p.idMuestraInteres.idRubroInteres.id=:pIdRubro and p.idMuestraInteres.idAnho.id=:pIdAnho ORDER BY p.idEmpresa", PreciosRefRubroEmp.class);
        q.setParameter("pNit", numeroNit);
        q.setParameter("pIdRubro", idRubro);
        q.setParameter("pIdAnho", idAnho);

        List<PreciosRefRubroEmp> lstPre = q.getResultList();
        Long idEmpTemp = 0l;
        EmpresaNoItem emp = new EmpresaNoItem();
        if (detRubro == null) {
            detRubro = lstPre.get(0).getIdMuestraInteres();
        }

        for (PreciosRefRubroEmp precios : lstPre) {

            if (idEmpTemp.intValue() == 0) {
                idEmpTemp = precios.getIdEmpresa().getId();
                emp = new EmpresaNoItem();
                emp.setIdEmpresa(idEmpTemp);
                emp.setIdMuestraInteres(detRubro.getId());
            } else if (idEmpTemp.intValue() == precios.getIdEmpresa().getId().intValue()) {

            } else {
                em.persist(emp);

                idEmpTemp = precios.getIdEmpresa().getId();
                emp = new EmpresaNoItem();
                emp.setIdEmpresa(idEmpTemp);
                emp.setIdMuestraInteres(detRubro.getId());
            }

            switch (precios.getNoItem()) {
                case "1":
                    emp.setItem1("1");
                    break;
                case "2":
                    emp.setItem2("2");
                    break;
                case "3":
                    emp.setItem3("3");
                    break;
                case "4":
                    emp.setItem4("4");
                    break;
                case "5":
                    emp.setItem5("5");
                    break;
                case "6":
                    emp.setItem6("6");
                    break;
                case "7":
                    emp.setItem7("7");
                    break;
                case "8":
                    emp.setItem8("8");
                    break;
                case "9":
                    emp.setItem9("9");
                    break;
                case "10":
                    emp.setItem10("10");
                    break;
                case "11":
                    emp.setItem11("11");
                    break;
                case "12":
                    emp.setItem12("12");
                    break;
                case "13":
                    emp.setItem13("13");
                    break;
            }
        }

        em.persist(emp);
    }

    public void calcularPreRefByNit(Long idRubro, Long idAnho, String numeroNit) {
        Query q = em.createQuery("SELECT p FROM PreciosRefRubroEmp p WHERE p.idMuestraInteres.idEmpresa.numeroNit=:pNit and p.idMuestraInteres.idRubroInteres.id=:pIdRubro and p.idMuestraInteres.idAnho.id=:pIdAnho ORDER BY p.idEmpresa", PreciosRefRubroEmp.class);
        q.setParameter("pNit", numeroNit);
        q.setParameter("pIdRubro", idRubro);
        q.setParameter("pIdAnho", idAnho);

        List<PreciosRefRubroEmp> lstPre = q.getResultList();
        Long idEmpTemp = 0l;
        EmpresaPreciosRef emp = null;

        for (PreciosRefRubroEmp precios : lstPre) {

            if (idEmpTemp.intValue() == 0) {
                idEmpTemp = precios.getIdEmpresa().getId();
                emp = new EmpresaPreciosRef();
                emp.setIdEmpresa(idEmpTemp);
                emp.setIdMuestraInteres(precios.getIdMuestraInteres().getId());
            } else {
                if (idEmpTemp.intValue() == precios.getIdEmpresa().getId().intValue()) {

                } else {
                    em.persist(emp);

                    idEmpTemp = precios.getIdEmpresa().getId();
                    emp = new EmpresaPreciosRef();
                    emp.setIdEmpresa(idEmpTemp);
                    emp.setIdMuestraInteres(precios.getIdMuestraInteres().getId());
                }

                switch (precios.getNoItem()) {
                    case "1":
                        emp.setItem1(precios.getPrecioReferencia());
                        break;
                    case "2":
                        emp.setItem2(precios.getPrecioReferencia());
                        break;
                    case "3":
                        emp.setItem3(precios.getPrecioReferencia());
                        break;
                    case "4":
                        emp.setItem4(precios.getPrecioReferencia());
                        break;
                    case "5":
                        emp.setItem5(precios.getPrecioReferencia());
                        break;
                    case "6":
                        emp.setItem6(precios.getPrecioReferencia());
                        break;
                    case "7":
                        emp.setItem7(precios.getPrecioReferencia());
                        break;
                    case "8":
                        emp.setItem8(precios.getPrecioReferencia());
                        break;
                    case "9":
                        emp.setItem9(precios.getPrecioReferencia());
                        break;
                    case "10":
                        emp.setItem10(precios.getPrecioReferencia());
                        break;
                    case "11":
                        emp.setItem11(precios.getPrecioReferencia());
                        break;
                    case "12":
                        emp.setItem12(precios.getPrecioReferencia());
                        break;
                    case "13":
                        emp.setItem13(precios.getPrecioReferencia());
                        break;
                }
            }
        }
    }

    private String getIdGestionByProceso(Long idEmpresa, Long idMuestraInteres) {
        StoredProcedureQuery q = em.createStoredProcedureQuery("SP_GET_ID_GESTION");

        q.registerStoredProcedureParameter("P_ID_EMPRESA", Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("P_ID_MUESTRA_INTERES", Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("P_ID_GESTION", String.class, ParameterMode.OUT);

        q.setParameter("P_ID_EMPRESA", idEmpresa);
        q.setParameter("P_ID_MUESTRA_INTERES", idMuestraInteres.intValue());

        q.execute();

        return (String) q.getOutputParameterValue("P_ID_GESTION");
    }
}
