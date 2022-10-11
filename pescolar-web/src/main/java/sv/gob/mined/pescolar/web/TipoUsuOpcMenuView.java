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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.primefaces.event.SelectEvent;
import sv.gob.mined.pescolar.model.OpcionMenu;
import sv.gob.mined.pescolar.model.TipoUsuOpcMenu;
import sv.gob.mined.pescolar.model.TipoUsuario;
import sv.gob.mined.pescolar.repository.TipoUsuOpcMenuRepo;
import sv.gob.mined.pescolar.utils.JsfUtil;

/**
 *
 * @author Carlos Quintanilla
 */
@Named
@ViewScoped
public class TipoUsuOpcMenuView implements Serializable {

    private List<TipoUsuario> listtipousuario = new ArrayList();
    private List<OpcionMenu> listopcionmenu = new ArrayList();
    private List<TipoUsuOpcMenu> listtipousuopcmenu = new ArrayList();

    private TipoUsuOpcMenu tipousuopcmenu = new TipoUsuOpcMenu();

    private Boolean deshabilitado = true;

    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;

    @Inject
    private SessionView sessionView;
    @Inject
    private TipoUsuOpcMenuRepo tipousuopcmenurepo;

    @PostConstruct
    public void init() {
        deshabilitado = true;

        llenarListaTipoUsuario();
        llenarListaTipoUsuarioOpcionMenu();
    }

    public void nuevo() {
        tipousuopcmenu = new TipoUsuOpcMenu();
        deshabilitado = false;
        listopcionmenu.clear();
    }

    private void llenarListaTipoUsuario() {
        Query q;
        q = em.createQuery("select tu from TipoUsuario tu", TipoUsuario.class);
        listtipousuario = q.getResultList();

        listopcionmenu.clear();

    }

    private void llenarListaTipoUsuarioOpcionMenu() {
        tipousuopcmenu = null;
        Query q;
        q = em.createQuery("select tu from TipoUsuOpcMenu tu", TipoUsuOpcMenu.class);
        listtipousuopcmenu = q.getResultList();

    }

    public void onClickTipoUsuario() {
        Query q;
        q = em.createQuery("select opc from OpcionMenu opc "
                + "where opc.idOpcMenu not in ("
                + "select opc2.idOpcMenu.idOpcMenu TipoUsuOpcMenu opc2 where opc2.idTipoUsuario.idTipoUsuario = " + tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario() + ""
                + ") ", OpcionMenu.class);
        listtipousuario = q.getResultList();
    }

    private Boolean validarGuardar() {

        if (tipousuopcmenu.getIdTipoUsuario() == null) {
            JsfUtil.mensajeAlerta("Debe seleccionar un tipo de usuario");
            return false;
        }
        if (tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario() == null) {
            JsfUtil.mensajeAlerta("Debe seleccionar un tipo de usuario");
            return false;
        }
        if (tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario() == 0) {
            JsfUtil.mensajeAlerta("Debe seleccionar un tipo de usuario");
            return false;
        }
        if (tipousuopcmenu.getIdOpcMenu() == null) {
            JsfUtil.mensajeAlerta("Debe seleccionar una opción de menú");
            return false;
        }
        if (tipousuopcmenu.getIdOpcMenu().getIdOpcMenu() == null) {
            JsfUtil.mensajeAlerta("Debe seleccionar una opción de menú");
            return false;
        }
        if (tipousuopcmenu.getIdOpcMenu().getIdOpcMenu() == 0) {
            JsfUtil.mensajeAlerta("Debe seleccionar una opción de menú");
            return false;
        }

        Query q;
        if (tipousuopcmenu.getId() == null) {
            q = em.createQuery("select tu from TipoUsuOpcMenu tu where tu.idOpcMenu.idOpcMenu = " + tipousuopcmenu.getIdOpcMenu().getIdOpcMenu() + " and tu.idTipoUsuario.idTipoUsuario = " + tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario() + " ", TipoUsuOpcMenu.class);
        } else {
            q = em.createQuery("select tu from TipoUsuOpcMenu tu where tu.idOpcMenu.idOpcMenu = " + tipousuopcmenu.getIdOpcMenu().getIdOpcMenu() + " and tu.idTipoUsuario.idTipoUsuario = " + tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario() + " and tu.id <> " + tipousuopcmenu.getId(), TipoUsuOpcMenu.class);
        }
        if (!q.getResultList().isEmpty()) {
            JsfUtil.mensajeAlerta("La relación tipo de usuario y opción menú ya existe en la lista");
            return false;
        }

        return true;
    }

    public void guardar() {
        if (validarGuardar()) {
            if (tipousuopcmenu.getIdTipoUsuario() == null) {
                tipousuopcmenurepo.save(tipousuopcmenu);
                JsfUtil.mensajeInsert();

            } else {
                tipousuopcmenurepo.update(tipousuopcmenu);
                JsfUtil.mensajeUpdate();
            }

            llenarListaTipoUsuarioOpcionMenu();
            llenarListaTipoUsuario();
            deshabilitado = true;
        }

    }

    private Boolean validarEliminar() {
        if (tipousuopcmenu.getId() == null) {
            JsfUtil.mensajeAlerta("Debe seleccionar un registro para eliminar");
            return false;
        }
        return true;
    }

    public void eliminar() {
        if (validarEliminar()) {
            tipousuopcmenurepo.delete(tipousuopcmenu);
            llenarListaTipoUsuarioOpcionMenu();
            llenarListaTipoUsuario();
            deshabilitado = true;
        }
    }

    public void onItemSelect(SelectEvent event) {
        tipousuopcmenu = (TipoUsuOpcMenu) event.getObject();

        if (tipousuopcmenu.getIdTipoUsuario() == null) {
            TipoUsuario tmp = new TipoUsuario();
            tmp.setIdTipoUsuario((long) 0);
            tipousuopcmenu.setIdTipoUsuario(tmp);
        }
        if (tipousuopcmenu.getIdOpcMenu() == null) {
            OpcionMenu tmp = new OpcionMenu();
            tmp.setIdOpcMenu((long) 0);
            tipousuopcmenu.setIdOpcMenu(tmp);
        }

        llenarListaTipoUsuario();
        deshabilitado = false;
    }

    public void onItemUnselect() {
        tipousuopcmenu = null;
        llenarListaTipoUsuario();
        deshabilitado = true;
    }

    //**************************************************************************
    //********************* Getter y Setter ************************************
    //**************************************************************************
    public List<TipoUsuario> getListtipousuario() {
        return listtipousuario;
    }

    public void setListtipousuario(List<TipoUsuario> listtipousuario) {
        this.listtipousuario = listtipousuario;
    }

    public List<OpcionMenu> getListopcionmenu() {
        return listopcionmenu;
    }

    public void setListopcionmenu(List<OpcionMenu> listopcionmenu) {
        this.listopcionmenu = listopcionmenu;
    }

    public TipoUsuOpcMenu getTipousuopcmenu() {
        if (tipousuopcmenu == null) {
            TipoUsuOpcMenu tmpusuopcmenu = new TipoUsuOpcMenu();
            TipoUsuario tmptipousuario = new TipoUsuario();
            tmptipousuario.setIdTipoUsuario((long) 0);

            OpcionMenu tmpopcionmenu = new OpcionMenu();
            tmpopcionmenu.setIdOpcMenu((long) 0);

            tmpusuopcmenu.setIdTipoUsuario(tmptipousuario);
            tmpusuopcmenu.setIdOpcMenu(tmpopcionmenu);
            return tmpusuopcmenu;

        } else {
            if (tipousuopcmenu.getIdOpcMenu() == null) {
                TipoUsuario tmptipousuario = new TipoUsuario();
                tmptipousuario.setIdTipoUsuario((long) 0);

                OpcionMenu tmpopcionmenu = new OpcionMenu();
                tmpopcionmenu.setIdOpcMenu((long) 0);

                tipousuopcmenu.setIdTipoUsuario(tmptipousuario);
                tipousuopcmenu.setIdOpcMenu(tmpopcionmenu);
                return tipousuopcmenu;
            } else {
                return tipousuopcmenu;
            }
        }
    }

    public void setTipousuopcmenu(TipoUsuOpcMenu tipousuopcmenu) {
        this.tipousuopcmenu = tipousuopcmenu;
    }

    public Boolean getDeshabilitado() {
        return deshabilitado;
    }

    public void setDeshabilitado(Boolean deshabilitado) {
        this.deshabilitado = deshabilitado;
    }

    public List<TipoUsuOpcMenu> getListtipousuopcmenu() {
        return listtipousuopcmenu;
    }

    public void setListtipousuopcmenu(List<TipoUsuOpcMenu> listtipousuopcmenu) {
        this.listtipousuopcmenu = listtipousuopcmenu;
    }
    
    

}
