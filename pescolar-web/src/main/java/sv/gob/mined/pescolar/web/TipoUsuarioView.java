/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import sv.gob.mined.pescolar.model.TipoUsuario;
import sv.gob.mined.pescolar.repository.TipoUsuOpcMenuRepo;
import sv.gob.mined.pescolar.repository.TipoUsuarioRepo;
import sv.gob.mined.pescolar.utils.JsfUtil;

/**
 *
 * @author Carlos Quintanilla
 */
@Named
@ViewScoped
public class TipoUsuarioView implements Serializable {

    private List<TipoUsuario> listtipousuario = new ArrayList();

    private TipoUsuario tipousuario = new TipoUsuario();

    private Boolean deshabilitado = true;
    private Boolean booladministrador = false;

    //@PersistenceContext(unitName = "paquetePU")
    //private EntityManager em;
    @Inject
    private SessionView sessionView;
    @Inject
    private TipoUsuarioRepo tipousuariorepo;
    @Inject
    private TipoUsuOpcMenuRepo tipousuopcmenurepo;

    @PostConstruct
    public void init() {
        deshabilitado = true;
        booladministrador = false;
        llenarListaTipoUsuario();
    }

    public void nuevo() {
        tipousuario = new TipoUsuario();
        deshabilitado = false;
    }

    private void llenarListaTipoUsuario() {
        tipousuario = null;
        //Query q;
        //q = em.createQuery("select tu from TipoUsuario tu", TipoUsuario.class);
        //listtipousuario = q.getResultList();
        listtipousuario = tipousuariorepo.listartipousuario();

    }

    private Boolean validarGuardar() {

        if (tipousuario.getDescripcion() == null) {
            JsfUtil.mensajeAlerta("Debe ingresar una descripción");
            return false;
        }
        if (tipousuario.getDescripcion().isBlank()) {
            JsfUtil.mensajeAlerta("Debe ingresar una descripción");
            return false;
        }
        if (tipousuario.getRol() == null) {
            JsfUtil.mensajeAlerta("Debe ingresar un rol");
            return false;
        }
        if (tipousuario.getRol().isBlank()) {
            JsfUtil.mensajeAlerta("Debe ingresar un rol");
            return false;
        }

        //Query q;
        if (tipousuario.getIdTipoUsuario() == null) {
            //q = em.createQuery("select tu from TipoUsuario tu where tu.descripcion = '" + tipousuario.getDescripcion() + "' ", TipoUsuario.class);
            if (!tipousuariorepo.listartipousuariopordescripcion(tipousuario.getDescripcion()).isEmpty()) {
                JsfUtil.mensajeAlerta("La descripción para el tipo de usuario ya existe en la lista");
                return false;
            }
        } else {
            //q = em.createQuery("select tu from TipoUsuario tu where tu.descripcion = '" + tipousuario.getDescripcion() + "' and tu.idTipoUsuario <> " + tipousuario.getIdTipoUsuario(), TipoUsuario.class);
            if (!tipousuariorepo.listartipousuariopordescripcionconotroid(tipousuario.getDescripcion(), tipousuario.getIdTipoUsuario()).isEmpty()) {
                JsfUtil.mensajeAlerta("La descripción para el tipo de usuario ya existe en la lista");
                return false;
            }
        }
        //if (!q.getResultList().isEmpty()) {
        //    JsfUtil.mensajeAlerta("La descripción para el tipo de usuario ya existe en la lista");
        //    return false;
        //}

        if (tipousuario.getIdTipoUsuario() == null) {
            //q = em.createQuery("select tu from TipoUsuario tu where tu.rol = '" + tipousuario.getRol() + "' ", TipoUsuario.class);
            if (!tipousuariorepo.listartipousuarioporrol(tipousuario.getRol()).isEmpty()) {
                JsfUtil.mensajeAlerta("El rol ingresado ya existe en la lista");
                return false;
            }
        } else {
            //q = em.createQuery("select tu from TipoUsuario tu where tu.rol = '" + tipousuario.getRol() + "' and tu.idTipoUsuario <> " + tipousuario.getIdTipoUsuario(), TipoUsuario.class);
            if (!tipousuariorepo.listartipousuarioporrolconotroid(tipousuario.getRol(), tipousuario.getIdTipoUsuario()).isEmpty()) {
                JsfUtil.mensajeAlerta("El rol ingresado ya existe en la lista");
                return false;
            }
        }
        //if (!q.getResultList().isEmpty()) {
        //    JsfUtil.mensajeAlerta("El rol ingresado ya existe en la lista");
        //    return false;
        //}

        return true;
    }

    public void guardar() {
        if (validarGuardar()) {

            if (booladministrador) {
                tipousuario.setAdministrador((long) 1);
            } else {
                tipousuario.setAdministrador((long) 0);
            }

            if (tipousuario.getIdTipoUsuario() == null) {
                tipousuariorepo.save(tipousuario);
                JsfUtil.mensajeInsert();

            } else {
                tipousuariorepo.update(tipousuario);
                JsfUtil.mensajeUpdate();
            }

            llenarListaTipoUsuario();
            deshabilitado = true;
        }

    }

    private Boolean validarEliminar() {
        if (tipousuario.getIdTipoUsuario() == null) {
            JsfUtil.mensajeAlerta("Debe seleccionar un registro para eliminar");
            return false;
        }

        //Query q;

        //q = em.createQuery("select tuom from TipoUsuOpcMenu tuom where tuom.idTipoUsuario.idTipoUsuario = " + tipousuario.getIdTipoUsuario() + " ", TipoUsuOpcMenu.class);
        if (!tipousuopcmenurepo.listartipousuopcmenuportipousuario(tipousuario.getIdTipoUsuario()).isEmpty()) {
            JsfUtil.mensajeAlerta("Existen registros de opciones de menú relacionados a este rol y no puede ser eliminado");
            return false;
        }

        return true;
    }

    public void eliminar() {
        if (validarEliminar()) {
            tipousuariorepo.delete(tipousuario);
            llenarListaTipoUsuario();
            deshabilitado = true;
        }
    }

    public void onItemSelect(SelectEvent event) {
        tipousuario = (TipoUsuario) event.getObject();
        if (tipousuario.getAdministrador() == 0) {
            booladministrador = false;
        } else {
            booladministrador = true;
        }
        deshabilitado = false;
    }

    public void onItemUnselect() {
        tipousuario = null;
        deshabilitado = true;
    }

    //**************************************************************************
    //****************************** Getter y Setter ***************************
    //**************************************************************************
    public List<TipoUsuario> getListtipousuario() {
        return listtipousuario;
    }

    public void setListtipousuario(List<TipoUsuario> listtipousuario) {
        this.listtipousuario = listtipousuario;
    }

    public TipoUsuario getTipousuario() {
        return tipousuario;
    }

    public void setTipousuario(TipoUsuario tipousuario) {
        this.tipousuario = tipousuario;
    }

    public Boolean getDeshabilitado() {
        return deshabilitado;
    }

    public void setDeshabilitado(Boolean deshabilitado) {
        this.deshabilitado = deshabilitado;
    }

    public Boolean getBooladministrador() {
        return booladministrador;
    }

    public void setBooladministrador(Boolean booladministrador) {
        this.booladministrador = booladministrador;
    }

}
