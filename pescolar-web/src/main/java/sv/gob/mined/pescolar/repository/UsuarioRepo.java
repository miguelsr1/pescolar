package sv.gob.mined.pescolar.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Session;
import javax.persistence.Query;
import sv.gob.mined.pescolar.model.Departamento;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.model.Persona;
import sv.gob.mined.pescolar.model.TipoUsuario;
import sv.gob.mined.pescolar.model.Usuario;
import sv.gob.mined.pescolar.utils.RC4Crypter;

/**
 *
 * @author misanchez
 */
@Stateless
public class UsuarioRepo extends AbstractRepository<Usuario, Long> {

    @Inject
    private MailRepo mailRepo;

    public UsuarioRepo() {
        super(Usuario.class);
    }

    public HashMap<String, String> solicitarEnlaceNuevaClave(String valorDeBusqueda) {

        HashMap<String, String> mapa = new HashMap<>();

        Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.idPersona.numeroDui = :pNumeroDui OR u.idPersona.numeroNit = :pNumeroNit", Usuario.class);
        q.setParameter("pNumeroDui", valorDeBusqueda);
        q.setParameter("pNumeroNit", valorDeBusqueda);

        Usuario usu = q.getResultList().isEmpty() ? null : (Usuario) q.getResultList().get(0);

        if (usu != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
            usu.setActivo((short) 0);
            usu.setTokenCambioClave(new RC4Crypter().encrypt("ha", valorDeBusqueda.concat(":").concat(sdf.format(new Date()))));
            update(usu);

            mapa.put("correo", usu.getIdPersona().getEmail());
            mapa.put("nombreCompleto", usu.getIdPersona().getNombreCompleto());
            mapa.put("token", usu.getTokenCambioClave());
            //usu.getTokenCambioClave();
        }

        return mapa;
    }

    public Boolean tokenUsuarioValido(String token) {
        Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.tokenCambioClave=:token", Usuario.class);
        q.setParameter("token", token);
        return !q.getResultList().isEmpty();
    }

    public void guardarPasswordPer(String valorDeBusqueda, String password) {
        Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.idPersona.numeroDui = :pNumeroDui OR u.idPersona.numeroNit = :pNumeroNit", Usuario.class);
        q.setParameter("pNumeroDui", valorDeBusqueda);
        q.setParameter("pNumeroNit", valorDeBusqueda);
        Usuario usr = (Usuario) q.getResultList().get(0);
        usr.setActivo((short) 1);
        usr.setTokenCambioClave(null);

        update(usr);

        Persona per = usr.getIdPersona();
        per.setClaveAcceso(password);

        em.merge(per);
    }

    public void guardarPasswordProv(Long idEmpresa, String password) {
        Empresa emp = em.find(Empresa.class, idEmpresa);
        Persona per = emp.getIdPersona();

        per.setClaveAcceso(password);
        per.setUsuario(per.getNumeroNit());

        em.merge(per);

        Query q = em.createQuery("SELECT u FROM Usuario u where u.idPersona.id = :pIdPersona", Usuario.class);
        q.setParameter("pIdPersona", per.getId());
        Usuario usu;

        if (q.getResultList().isEmpty()) {
            usu = new Usuario();
            usu.setActivo((short) 1);
            usu.setCodigoDepartamento(em.find(Departamento.class, "00"));
            usu.setEstadoEliminacion(0l);
            usu.setFechaInsercion(LocalDate.now());
            usu.setRangoFechaLogin((short) 0);
            usu.setIdPersona(per);
            usu.setIdTipoUsuario(em.find(TipoUsuario.class, new BigDecimal(9)));
            usu.setUsuarioInsercion("AUTOMATICO");

            save(usu);
        } else {
            usu = (Usuario) q.getResultList().get(0);
            usu.setIdTipoUsuario(em.find(TipoUsuario.class, new BigDecimal(9)));
            update(usu);
        }
        updateCodigoValidacionProveedor(idEmpresa, null, true);
    }

    private void updateCodigoValidacionProveedor(Long idEmpresa, String codigoGenerado, Boolean fechaActivacion) {
        String sql;
        if (fechaActivacion) {
            sql = "UPDATE EMPRESA_CODIGO_SEG SET fecha_activacion = sysdate WHERE ID_EMPRESA=" + idEmpresa;
        } else {
            sql = "UPDATE EMPRESA_CODIGO_SEG SET USUARIO_ACTIVADO = 1, TOKEN_ACTIVACION='" + codigoGenerado + "' WHERE ID_EMPRESA=" + idEmpresa;
        }
        Query q = em.createNativeQuery(sql);
        q.executeUpdate();
    }

    public int validarCodigoSegEmpresa(String codigoSeg, String nit, String dui, String tituloEmail, String cuerpoEmail, Session sesionMail) {
        int codigoOperacion = 1;
        Empresa emp = findEmpresaByCodSeg(codigoSeg);

        if (emp == null) {
            codigoOperacion = 2;
        } else if (emp.getIdPersona().getNumeroNit().equals(nit)
                && emp.getIdPersona().getNumeroDui().equals(dui)) {

        } else {
            codigoOperacion = 2;
        }

        if (codigoOperacion == 1) {
            String codigoGenerado = (new RC4Crypter()).encrypt("ha", emp.getId().toString().concat(":").concat(codigoSeg));

            String cuerpo = MessageFormat.format(cuerpoEmail, codigoGenerado);

            if (mailRepo.enviarMail(tituloEmail, cuerpo, emp.getIdPersona().getEmail(), "", sesionMail)) {
                updateCodigoValidacionProveedor(emp.getId(), codigoGenerado, false);
            } else {
                codigoOperacion = 3;
            }
        }
        return codigoOperacion;
    }

    private Empresa findEmpresaByCodSeg(String codigoSeg) {
        Empresa emp = null;
        Query q;
        List lst;

        q = em.createNativeQuery("SELECT id_empresa FROM EMPRESA_CODIGO_SEG WHERE codigo_seguridad=?1 AND usuario_activado = 0");
        q.setParameter(1, codigoSeg);

        lst = q.getResultList();

        if (!lst.isEmpty()) {
            emp = em.find(Empresa.class, (Long) lst.get(0));
        }

        return emp;
    }

    public Empresa findEmpresaByDuiOrNit(String valorDeBusqueda, Boolean empresa) {
        Query q = em.createQuery("SELECT e FROM Empresa e WHERE (e.idPersona.numeroDui = :pNumeriDui OR e.idPersona.numeroNit = :pNumeroNit) and e.estadoEliminacion=0", Empresa.class);
        q.setParameter("pNumeroDui", valorDeBusqueda);
        q.setParameter("pNumeroNit", valorDeBusqueda);

        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (Empresa) q.getSingleResult();
        }
    }
}
