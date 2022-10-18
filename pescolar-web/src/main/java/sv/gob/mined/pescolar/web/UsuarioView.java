package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import org.primefaces.event.SelectEvent;
import sv.gob.mined.pescolar.model.Departamento;
import sv.gob.mined.pescolar.model.Genero;
import sv.gob.mined.pescolar.model.Persona;
import sv.gob.mined.pescolar.model.TipoUsuario;
import sv.gob.mined.pescolar.model.Usuario;
import sv.gob.mined.pescolar.repository.PersonaRepo;
import sv.gob.mined.pescolar.repository.RubroPorCeRepo;
import sv.gob.mined.pescolar.repository.TipoUsuarioRepo;
import sv.gob.mined.pescolar.repository.UsuarioRepo;
import sv.gob.mined.pescolar.utils.JsfUtil;

/**
 *
 * @author Carlos Quintanilla
 */
@Named
@ViewScoped
public class UsuarioView implements Serializable {

    //@PersistenceContext(unitName = "paquetePU")
    //private EntityManager em;
    private Persona personaObj = new Persona();
    private Persona personaseleccionada = new Persona();
    private Usuario usuarioObj = new Usuario();

    private List<TipoUsuario> listTipoUsuario = new ArrayList();
    private List<Departamento> listDepartamento = new ArrayList();
    private List<Genero> listGenero = new ArrayList();
    private List<Persona> listPersona = new ArrayList();

    private String nombrepersonabuscar;

    private String password;
    private String password2;

    private Boolean deshabilitado = true;
    private Boolean periodoDeAcceso = false;
    private Boolean usuarioActivo = false;
    private Boolean cambiarClave = false;
    private Boolean showCambiarClave = false;
    private Boolean visibleClave = true;
    private Boolean deshabilitadoModificar = true;
    private Boolean deshabilitarcambioclave = true;

    private Boolean errorPassword = false;
    private Boolean dialogFiltroPersona;

    private Date fechaVencimientoClave = new Date();
    private Date fechaInicioLogin;
    private Date fechaFinLogin;

    @Inject
    transient private Pbkdf2PasswordHash passwordHash;

    @Inject
    private SessionView sessionView;

    @Inject
    private PersonaRepo personarepo;

    @Inject
    private UsuarioRepo usuariorepo;

    @Inject
    private RubroPorCeRepo rubroporceRepo;

    @Inject
    private TipoUsuarioRepo tipousuarioRepo;

    @PostConstruct
    public void init() {
        //prepareEdit();
        //Query q;
        //q = em.createQuery("select d from Departamento d order by d.id asc", Departamento.class);
        //listDepartamento = q.getResultList();
        listDepartamento = rubroporceRepo.listardepartamentos();

        //q = em.createQuery("select tu from TipoUsuario tu", TipoUsuario.class);
        //listTipoUsuario = q.getResultList();
        listTipoUsuario = tipousuarioRepo.listartipousuario();

        //q = em.createQuery("select g from Genero g", Genero.class);
        //listGenero = q.getResultList();
        listGenero = usuariorepo.listargenero();

        nombrepersonabuscar = "";
        usuarioActivo = false;
        periodoDeAcceso = false;
        cambiarClave = false;
        showCambiarClave = false;
        password = "";
        password2 = "";
        deshabilitadoModificar = true;
        deshabilitarcambioclave = true;
    }

    public String prepareCreate() {
        deshabilitado = false;
        visibleClave = true;
        showCambiarClave = false;

        usuarioActivo = false;
        periodoDeAcceso = false;
        fechaInicioLogin = null;
        fechaFinLogin = null;

        password = "";
        password2 = "";

        usuarioObj = new Usuario();
        personaObj = new Persona();

        deshabilitadoModificar = true;

        return null;
    }

    public String prepareEdit() {
        if (deshabilitado) {
            deshabilitado = false;
            deshabilitarcambioclave = false;
            visibleClave = false;
        } else {
            errorPassword = false;
            dialogFiltroPersona = true;
            deshabilitadoModificar = false;

            deshabilitado = true;
            deshabilitarcambioclave = false;
            visibleClave = false;
        }
        return null;
    }

    public Boolean validarGuardar() {
        if (personaObj.getIdGenero() == null) {
            JsfUtil.mensajeAlerta("Debe seleccionar un género");
            return false;
        }
        if (personaObj.getIdGenero().getId() == null) {
            JsfUtil.mensajeAlerta("Debe seleccionar un género");
            return false;
        }
        if (personaObj.getIdGenero().getId() == 0) {
            JsfUtil.mensajeAlerta("Debe seleccionar un género");
            return false;
        }
        if (personaObj.getPrimerNombre() == null) {
            JsfUtil.mensajeAlerta("Debe ingresar al menos el primer nombre");
            return false;
        }
        if (personaObj.getPrimerNombre().isBlank()) {
            JsfUtil.mensajeAlerta("Debe ingresar al menos el primer nombre");
            return false;
        }
        if (personaObj.getPrimerApellido() == null) {
            JsfUtil.mensajeAlerta("Debe ingresar al menos el primer apellido");
            return false;
        }
        if (personaObj.getPrimerApellido().isBlank()) {
            JsfUtil.mensajeAlerta("Debe ingresar al menos el primer apellido");
            return false;
        }
        if (personaObj.getNumeroDui() == null) {
            JsfUtil.mensajeAlerta("Debe ingresar número de DUI");
            return false;
        }
        if (personaObj.getNumeroDui().isBlank()) {
            JsfUtil.mensajeAlerta("Debe ingresar número de DUI");
            return false;
        }
        if (personaObj.getNumeroNit() == null) {
            JsfUtil.mensajeAlerta("Debe ingresar número de NIT");
            return false;
        }
        if (personaObj.getNumeroNit().isBlank()) {
            JsfUtil.mensajeAlerta("Debe ingresar número de NIT");
            return false;
        }
        if (usuarioObj.getIdTipoUsuario() == null) {
            JsfUtil.mensajeAlerta("Debe seleccionar un tipo de usuario");
            return false;
        }
        if (usuarioObj.getIdTipoUsuario().getIdTipoUsuario() == null) {
            JsfUtil.mensajeAlerta("Debe seleccionar un tipo de usuario");
            return false;
        }
        if (usuarioObj.getIdTipoUsuario().getIdTipoUsuario() == 0) {
            JsfUtil.mensajeAlerta("Debe seleccionar un tipo de usuario");
            return false;
        }
        if (usuarioObj.getCodigoDepartamento() == null) {
            JsfUtil.mensajeAlerta("Debe seleccionar un departamento");
            return false;
        }
        if (usuarioObj.getCodigoDepartamento().getId() == null) {
            JsfUtil.mensajeAlerta("Debe seleccionar un departamento");
            return false;
        }
        if (usuarioObj.getCodigoDepartamento().getId().equals("")) {
            JsfUtil.mensajeAlerta("Debe seleccionar un departamento");
            return false;
        }
        if (personaObj.getUsuario() == null) {
            JsfUtil.mensajeAlerta("Debe ingresar un usuario");
            return false;
        }
        if (personaObj.getUsuario().isBlank()) {
            JsfUtil.mensajeAlerta("Debe ingresar un usuario");
            return false;
        }

        //Query q;
        if (personaObj.getId() == null) {
            if (password.isBlank()) {
                JsfUtil.mensajeAlerta("Debe ingresar una contraseña");
                return false;
            } else {
                if (password2.isBlank()) {
                    JsfUtil.mensajeAlerta("Debe ingresar una confirmación de contraseña");
                    return false;
                } else {
                    if (!password.equals(password2)) {
                        JsfUtil.mensajeAlerta("Contraseña y confirmación de contraseña no coinciden");
                        return false;
                    }
                }
            }

            //q = em.createQuery("select p from Persona p where p.numeroDui = '" + personaObj.getNumeroDui() + "' and p.estadoEliminacion = 0 ", Persona.class);
            if (!personarepo.listarpersonapordui(personaObj.getNumeroDui()).isEmpty()) {
                JsfUtil.mensajeAlerta("El número de DUI ya existe registrado en la lista de personas");
                return false;
            }
            //q = em.createQuery("select p from Persona p where p.numeroNit='" + personaObj.getNumeroNit() + "' and p.estadoEliminacion = 0 ", Persona.class);
            if (!personarepo.listarpersonapornit(personaObj.getNumeroNit()).isEmpty()) {
                JsfUtil.mensajeAlerta("El número de NIT ya existe registrado en la lista de personas");
                return false;
            }
            //q = em.createQuery("select p from Persona p where p.usuario='" + personaObj.getUsuario() + "' and p.estadoEliminacion = 0 ", Persona.class);
            if (!personarepo.listarpersonaporusuario(personaObj.getUsuario()).isEmpty()) {
                JsfUtil.mensajeAlerta("El Usuario ya existe registrado en la lista de personas");
                return false;
            }

        } else {
            if (deshabilitarcambioclave) {
                if (password.isBlank()) {
                    JsfUtil.mensajeAlerta("Debe ingresar una contraseña");
                    return false;
                } else {
                    if (password2.isBlank()) {
                        JsfUtil.mensajeAlerta("Debe ingresar una confirmación de contraseña");
                        return false;
                    } else {
                        if (!password.equals(password2)) {
                            JsfUtil.mensajeAlerta("Contraseña y confirmación de contraseña no coinciden");
                            return false;
                        }
                    }
                }
            }

            //q = em.createQuery("select p from Persona p where p.numeroDui = '" + personaObj.getNumeroDui() + "' and p.estadoEliminacion = 0 and p.id <> " + personaObj.getId(), Persona.class);
            if (!personarepo.listarpersonaporduiconotroid(personaObj.getNumeroDui(), personaObj.getId()).isEmpty()) {
                JsfUtil.mensajeAlerta("El número de DUI ya existe registrado en la lista de personas");
                return false;
            }
            //q = em.createQuery("select p from Persona p where p.numeroNit='" + personaObj.getNumeroNit() + "' and p.estadoEliminacion = 0 and p.id <> " + personaObj.getId(), Persona.class);
            if (!personarepo.listarpersonapornitconotroid(personaObj.getNumeroNit(), personaObj.getId()).isEmpty()) {
                JsfUtil.mensajeAlerta("El número de NIT ya existe registrado en la lista de personas");
                return false;
            }
            //q = em.createQuery("select p from Persona p where p.usuario='" + personaObj.getUsuario() + "' and p.estadoEliminacion = 0 and p.id <> " + personaObj.getId(), Persona.class);
            if (!personarepo.listarpersonaporusuarioconotroid(personaObj.getUsuario(), personaObj.getId()).isEmpty()) {
                JsfUtil.mensajeAlerta("El Usuario ya existe registrado en la lista de personas");
                return false;
            }
        }

        if (periodoDeAcceso) {
            if (usuarioObj.getFechaInicioLogin() == null) {
                JsfUtil.mensajeAlerta("Debe ingresar una fecha de inicio de período de acceso");
                return false;
            }
            if (usuarioObj.getFechaFinLogin() == null) {
                JsfUtil.mensajeAlerta("Debe ingresar una fecha final de período de acceso");
                return false;
            }
        }

        return true;
    }

    public void guardarUsuario() {
        if (validarGuardar()) {
            if (personaObj.getId() == null) {
                String npass = encriptarClave(password);
                //System.out.println(npass);
                personaObj.setClaveAcceso(npass);
                personaObj.setFechaInsercion(LocalDateTime.now().toLocalDate());
                personaObj.setUsuarioCreacion(sessionView.getUsuario().getIdPersona().getUsuario());
                personaObj.setEstadoEliminacion((long) 0);
                personarepo.save(personaObj);

                if (periodoDeAcceso) {
                    usuarioObj.setRangoFechaLogin((short) 1);
                } else {
                    usuarioObj.setRangoFechaLogin((short) 0);
                }

                if (cambiarClave) {
                    usuarioObj.setCambiarClave((short) 1);
                } else {
                    usuarioObj.setCambiarClave((short) 0);
                }

                if (usuarioActivo) {
                    usuarioObj.setActivo((short) 1);
                } else {
                    usuarioObj.setActivo((short) 0);
                }

                usuarioObj.setEstadoEliminacion(Long.valueOf(0));
                usuarioObj.setIdPersona(personaObj);
                usuarioObj.setUsuarioInsercion(sessionView.getUsuario().getIdPersona().getUsuario());
                usuarioObj.setFechaInsercion(LocalDateTime.now().toLocalDate());
                usuariorepo.save(usuarioObj);
                JsfUtil.mensajeInsert();

            } else {
                if (deshabilitarcambioclave) {
                    personaObj.setClaveAcceso(encriptarClave(password));
                }
                personaObj.setFechaModificacion(LocalDateTime.now().toLocalDate());
                personaObj.setUsuarioModificacion(sessionView.getUsuario().getIdPersona().getUsuario());
                personarepo.update(personaObj);

                if (periodoDeAcceso) {
                    usuarioObj.setRangoFechaLogin((short) 1);
                } else {
                    usuarioObj.setRangoFechaLogin((short) 0);
                }

                if (cambiarClave) {
                    usuarioObj.setCambiarClave((short) 1);
                } else {
                    usuarioObj.setCambiarClave((short) 0);
                }

                if (usuarioActivo) {
                    usuarioObj.setActivo((short) 1);
                } else {
                    usuarioObj.setActivo((short) 0);
                }

                usuarioObj.setUsuarioModificacion(sessionView.getUsuario().getIdPersona().getUsuario());
                usuarioObj.setFechaModificacion(LocalDateTime.now().toLocalDate());
                usuariorepo.update(usuarioObj);
                JsfUtil.mensajeUpdate();
            }

            cambiarClave = false;
            showCambiarClave = true;
            password = "";
            password2 = "";
            visibleClave = false;

            deshabilitado = true;

            deshabilitadoModificar = false;
            deshabilitarcambioclave = false;
        }
    }

    public void eliminarUsuario() {
        if (personaObj.getId() != null && usuarioObj.getIdUsuario() != null) {
            if (Objects.equals(personaObj.getId(), usuarioObj.getIdPersona().getId())) {
                personaObj.setUsuarioModificacion(sessionView.getUsuario().getIdPersona().getUsuario());
                personaObj.setFechaEliminacion(LocalDateTime.now().toLocalDate());
                personaObj.setEstadoEliminacion((long) 1);
                personarepo.update(personaObj);

                usuarioObj.setUsuarioModificacion(sessionView.getUsuario().getIdPersona().getUsuario());
                usuarioObj.setFechaEliminacion(LocalDateTime.now().toLocalDate());
                usuarioObj.setEstadoEliminacion((long) 1);
                usuariorepo.update(usuarioObj);

                deshabilitado = false;
                visibleClave = true;
                showCambiarClave = false;

                usuarioActivo = false;
                periodoDeAcceso = false;
                fechaInicioLogin = null;
                fechaFinLogin = null;

                password = "";
                password2 = "";

                usuarioObj = new Usuario();
                personaObj = new Persona();

                deshabilitadoModificar = true;

                JsfUtil.mensajeInformacion("Registro eliminado exitosamente");

                cambiarClave = false;
                showCambiarClave = true;
                password = "";
                password2 = "";
                visibleClave = false;

                deshabilitado = true;

                deshabilitadoModificar = true;
                deshabilitarcambioclave = false;
            }
        }
    }

    private void modificarUsuario() {
        if (periodoDeAcceso) {
            usuarioObj.setRangoFechaLogin((short) 1);
            //usuarioObj.setFechaInicioLogin(fechaInicioLogin);
            //usuarioObj.setFechaFinLogin(fechaFinLogin);
        } else {
            usuarioObj.setRangoFechaLogin((short) 0);
            usuarioObj.setFechaInicioLogin(null);
            usuarioObj.setFechaFinLogin(null);
        }

        //edicion.setUsuarioModificacion(VarSession.getVariableSession("Usuario").toString());
        //edicion.setFechaModificacion(new Date());
        //personaEJB.edit(edicion);
        usuarioObj.setActivo(usuarioActivo ? (short) 1 : 0);
        //usuarioObj.setFechaModificacion(new Date());
        //usuarioObj.setUsuarioModificacion(VarSession.getVariableSession("Usuario").toString());

        //usuarioObj.setCodigoDepartamento(departamento.getCodigoDepartamento());
        //personaEJB.setOpcionesUsuario(usuarioObj, edicion);
        JsfUtil.mensajeUpdate();
        prepareCreate();
    }

    public void buscarUsuario() {
        if (!nombrepersonabuscar.isBlank()) {
            String buscaduinit, buscanombre;
            Query q;
            try {
                Integer probarnum = Integer.valueOf(nombrepersonabuscar.replace("-", ""));
                buscaduinit = nombrepersonabuscar;
                buscanombre = "";
            } catch (Exception e) {
                buscaduinit = "";
                buscanombre = nombrepersonabuscar;
            }

            if (!buscaduinit.equals("")) {
                //q = em.createQuery("select p from Persona p where p.estadoEliminacion = 0 and (p.numeroDui = '" + buscaduinit + "' or p.numeroNit = '" + buscaduinit + "')", Persona.class);
                listPersona = personarepo.listarpersonapordocumentos(buscaduinit);
            } else {
                String[] cadenabuscar = buscanombre.split(" ");
                String mwhere = "";
                for (int i = 0; i < cadenabuscar.length; i++) {
                    mwhere = mwhere + " upper(p.primerNombre) = '" + cadenabuscar[i].toUpperCase() + "' or upper(p.segundoNombre) = '" + cadenabuscar[i].toUpperCase() + "' or upper(p.primerApellido) = '" + cadenabuscar[i].toUpperCase() + "' or upper(p.segundoApellido) = '" + cadenabuscar[i].toUpperCase() + "' or upper(p.acasada) = '" + cadenabuscar[i].toUpperCase() + "' or upper(p.usuario) = '" + cadenabuscar[i].toUpperCase() + "' ";
                }
                //q = em.createQuery("select p from Persona p "
                //        + "where p.estadoEliminacion = 0 "
                //        + "and ("
                //        + mwhere + ")", Persona.class);
                listPersona = personarepo.listarpersonapornombresoapellidos(mwhere);
            }

            personaseleccionada = null;
            //listPersona = q.getResultList();

        } else {
            JsfUtil.mensajeAlerta("Debe ingresar un criterio de búsqueda");
        }

    }

    public String encriptarClave(String strclave) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
        passwordHash.initialize(parameters);

        return passwordHash.generate(strclave.toCharArray());
    }

    public void validarPassword() {

    }

    private void cargarDatosPersonaEdicion() {

    }

    public void iniciarventanabuscar() {
        nombrepersonabuscar = "";
        personaseleccionada = null;
        listPersona.clear();
    }

    public void onItemSelect(SelectEvent event) {
        personaObj = (Persona) event.getObject();
        Query q;
        //q = em.createQuery("select u from Usuario u "
        //        + "where u.estadoEliminacion = 0 "
        //        + "and u.idPersona.id = " + personaObj.getId(), Usuario.class);
        if (usuariorepo.listarusuarioporpersona(personaObj.getId()).isEmpty()) {
            usuarioObj = new Usuario();
            periodoDeAcceso = false;
            usuarioActivo = false;
        } else {
            //usuarioObj = (Usuario) q.setMaxResults(1).getSingleResult();
            usuarioObj = usuariorepo.usuarioporpersona(personaObj.getId());
            if (usuarioObj.getRangoFechaLogin() == 0) {
                periodoDeAcceso = false;
            } else {
                periodoDeAcceso = true;
            }

            if (usuarioObj.getActivo() == 0) {
                usuarioActivo = false;
            } else {
                usuarioActivo = true;
            }

        }
        cambiarClave = false;
        showCambiarClave = true;
        password = "";
        password2 = "";
        visibleClave = false;

        deshabilitado = true;

        deshabilitadoModificar = false;
        deshabilitarcambioclave = false;

    }

    public void onItemUnselect() {
        personaObj = null;
        usuarioObj = null;
        periodoDeAcceso = false;
        usuarioActivo = false;

    }

    public void onClickPeriodoAcceso() {
        if (usuarioObj.getRangoFechaLogin() != null) {
            if (usuarioObj.getRangoFechaLogin() == 1) {
                usuarioObj.setFechaInicioLogin(LocalDate.now());
                usuarioObj.setFechaFinLogin(LocalDate.now());
            } else {
                usuarioObj.setFechaInicioLogin(null);
                usuarioObj.setFechaFinLogin(null);
            }
        } else {
            usuarioObj.setFechaInicioLogin(null);
            usuarioObj.setFechaFinLogin(null);
        }

    }

    public void onClickCambioClave() {
        if (deshabilitarcambioclave) {
            visibleClave = true;
        } else {
            visibleClave = false;
        }
        password = "";
        password2 = "";
    }

    public Persona getPersonaObj() {
        if (personaObj == null) {
            Persona tmppersona = new Persona();
            Genero tmpgenero = new Genero();
            tmpgenero.setId((long) 0);
            tmppersona.setIdGenero(tmpgenero);
            return tmppersona;

        } else {
            if (personaObj.getIdGenero() == null) {
                Genero tmpgenero = new Genero();
                tmpgenero.setId((long) 0);
                personaObj.setIdGenero(tmpgenero);
                return personaObj;
            } else {
                return personaObj;
            }
        }
    }

    public void setPersonaObj(Persona personaObj) {
        this.personaObj = personaObj;
    }

    public Usuario getUsuarioObj() {
        if (usuarioObj == null) {
            Usuario tmpusuario = new Usuario();
            Departamento tmpdepartamento = new Departamento();
            tmpdepartamento.setId("");
            tmpusuario.setCodigoDepartamento(tmpdepartamento);
            TipoUsuario tmptipousuario = new TipoUsuario();
            tmptipousuario.setIdTipoUsuario((long) 0);
            tmpusuario.setIdTipoUsuario(tmptipousuario);
            return tmpusuario;
        } else {
            if (usuarioObj.getCodigoDepartamento() == null) {
                Departamento tmpdepartamento = new Departamento();
                tmpdepartamento.setId("");
                usuarioObj.setCodigoDepartamento(tmpdepartamento);
            }
            if (usuarioObj.getIdTipoUsuario() == null) {
                TipoUsuario tmptipousuario = new TipoUsuario();
                tmptipousuario.setIdTipoUsuario((long) 0);
                usuarioObj.setIdTipoUsuario(tmptipousuario);
            }
            return usuarioObj;

        }
    }

    public void setUsuarioObj(Usuario usuarioObj) {
        this.usuarioObj = usuarioObj;
    }

    public List<TipoUsuario> getListTipoUsuario() {
        return listTipoUsuario;
    }

    public void setListTipoUsuario(List<TipoUsuario> listTipoUsuario) {
        this.listTipoUsuario = listTipoUsuario;
    }

    public List<Departamento> getListDepartamento() {
        return listDepartamento;
    }

    public void setListDepartamento(List<Departamento> listDepartamento) {
        this.listDepartamento = listDepartamento;
    }

    public List<Genero> getListGenero() {
        return listGenero;
    }

    public void setListGenero(List<Genero> listGenero) {
        this.listGenero = listGenero;
    }

    public String getNombrepersonabuscar() {
        return nombrepersonabuscar;
    }

    public void setNombrepersonabuscar(String nombrepersonabuscar) {
        this.nombrepersonabuscar = nombrepersonabuscar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public Boolean getCambiarClave() {
        return cambiarClave;
    }

    public void setCambiarClave(Boolean cambiarClave) {
        this.cambiarClave = cambiarClave;
    }

    public Boolean getVisibleClave() {
        return visibleClave;
    }

    public void setVisibleClave(Boolean visibleClave) {
        this.visibleClave = visibleClave;
    }

    public Boolean getDeshabilitado() {
        return deshabilitado;
    }

    public void setDeshabilitado(Boolean deshabilitado) {
        this.deshabilitado = deshabilitado;
    }

    public Boolean getDeshabilitadoModificar() {
        return deshabilitadoModificar;
    }

    public void setDeshabilitadoModificar(Boolean deshabilitadoModificar) {
        this.deshabilitadoModificar = deshabilitadoModificar;
    }

    public Boolean getPeriodoDeAcceso() {
        return periodoDeAcceso;
    }

    public void setPeriodoDeAcceso(Boolean periodoDeAcceso) {
        this.periodoDeAcceso = periodoDeAcceso;
    }

    public Boolean getDialogFiltroPersona() {
        return dialogFiltroPersona;
    }

    public void setDialogFiltroPersona(Boolean dialogFiltroPersona) {
        this.dialogFiltroPersona = dialogFiltroPersona;
    }

    public Boolean getUsuarioActivo() {
        return usuarioActivo;
    }

    public void setUsuarioActivo(Boolean usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }

    public Date getFechaVencimientoClave() {
        return fechaVencimientoClave;
    }

    public void setFechaVencimientoClave(Date fechaVencimientoClave) {
        this.fechaVencimientoClave = fechaVencimientoClave;
    }

    public Date getFechaInicioLogin() {
        return fechaInicioLogin;
    }

    public void setFechaInicioLogin(Date fechaInicioLogin) {
        this.fechaInicioLogin = fechaInicioLogin;
    }

    public Date getFechaFinLogin() {
        return fechaFinLogin;
    }

    public void setFechaFinLogin(Date fechaFinLogin) {
        this.fechaFinLogin = fechaFinLogin;
    }

    public Boolean getShowCambiarClave() {
        return showCambiarClave;
    }

    public void setShowCambiarClave(Boolean showCambiarClave) {
        this.showCambiarClave = showCambiarClave;
    }

    public Boolean getErrorPassword() {
        return errorPassword;
    }

    public void setErrorPassword(Boolean errorPassword) {
        this.errorPassword = errorPassword;
    }

    public Persona getPersonaseleccionada() {
        return personaseleccionada;
    }

    public void setPersonaseleccionada(Persona personaseleccionada) {
        this.personaseleccionada = personaseleccionada;
    }

    public List<Persona> getListPersona() {
        return listPersona;
    }

    public void setListPersona(List<Persona> listPersona) {
        this.listPersona = listPersona;
    }

    public Boolean getDeshabilitarcambioclave() {
        return deshabilitarcambioclave;
    }

    public void setDeshabilitarcambioclave(Boolean deshabilitarcambioclave) {
        this.deshabilitarcambioclave = deshabilitarcambioclave;
    }

}
