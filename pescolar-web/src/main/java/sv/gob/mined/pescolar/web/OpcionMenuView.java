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
import sv.gob.mined.pescolar.model.Modulo;
import sv.gob.mined.pescolar.model.OpcionMenu;
import sv.gob.mined.pescolar.repository.OpcionMenuRepo;
import sv.gob.mined.pescolar.utils.JsfUtil;

/**
 *
 * @author Carlos Quintanilla
 */
@Named
@ViewScoped
public class OpcionMenuView implements Serializable {

    private List<OpcionMenu> listopciones = new ArrayList();
    private List<OpcionMenu> listopcionespadre = new ArrayList();
    private List<Modulo> listmodulos = new ArrayList();

    private OpcionMenu opcionmenu = new OpcionMenu();

    private Boolean deshabilitado = true;

    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;

    @Inject
    private SessionView sessionView;
    @Inject
    private OpcionMenuRepo opcionmenurepo;

    @PostConstruct
    public void init() {
        Query q;
        q = em.createQuery("select mod from Modulo mod", Modulo.class);
        listmodulos = q.getResultList();

        llenarListaOpciones();
    }

    public void nuevo() {
        opcionmenu = new OpcionMenu();
        deshabilitado = false;
    }

    private void llenarListaOpciones() {
        opcionmenu = null;
        Query q;
        q = em.createQuery("select om from OpcionMenu om order by om.app asc, om.opcIdOpcMenu asc, om.orden asc", OpcionMenu.class);
        listopciones = q.getResultList();
        listopcionespadre = q.getResultList();
    }

    private Boolean validarGuardar() {

        if (opcionmenu.getNombreOpcion() == null) {
            JsfUtil.mensajeAlerta("Debe ingresar nombre para la opción");
            return false;
        }
        if (opcionmenu.getNombreOpcion().isBlank()) {
            JsfUtil.mensajeAlerta("Debe ingresar nombre para la opción");
            return false;
        }

        Query q;
        if (opcionmenu.getIdOpcMenu() == null) {
            q = em.createQuery("select op from OpcionMenu op where op.opcIdOpcMenu.idOpcMenu = " + opcionmenu.getOpcIdOpcMenu().getIdOpcMenu() + " and op.nombreOpcion = '" + opcionmenu.getNombreOpcion() + "'", OpcionMenu.class);
        } else {
            q = em.createQuery("select op from OpcionMenu op where op.opcIdOpcMenu.idOpcMenu = " + opcionmenu.getOpcIdOpcMenu().getIdOpcMenu() + " and op.nombreOpcion = '" + opcionmenu.getNombreOpcion() + "' and op.idOpcMenu <> " + opcionmenu.getIdOpcMenu(), OpcionMenu.class);
        }
        if (!q.getResultList().isEmpty()) {
            JsfUtil.mensajeAlerta("La opción ya existe en la lista");
            return false;
        }

        return true;
    }

    public void guardar() {
        if (validarGuardar()) {

            if (opcionmenu.getOpcIdOpcMenu() != null) {
                if (opcionmenu.getOpcIdOpcMenu().getIdOpcMenu() == 0) {
                    opcionmenu.setOpcIdOpcMenu(null);
                }
            }
            
            if (opcionmenu.getApp()!= null) {
                if (opcionmenu.getApp().getIdModulo()== 0) {
                    opcionmenu.setApp(null);
                }
            }
            
            if (opcionmenu.getIdOpcMenu() == null) {
                opcionmenurepo.save(opcionmenu);
                JsfUtil.mensajeInsert();

            } else {
                opcionmenurepo.update(opcionmenu);
                JsfUtil.mensajeUpdate();
            }

            llenarListaOpciones();
            deshabilitado = true;
        }

    }

    private Boolean validarEliminar() {
        if (opcionmenu.getOpcIdOpcMenu() == null) {
            JsfUtil.mensajeAlerta("Debe seleccionar un registro para eliminar");
            return false;
        }

        Query q;

        q = em.createQuery("select op from OpcionMenu op where op.idOpcMenu = " + opcionmenu.getOpcIdOpcMenu().getIdOpcMenu() + " ", OpcionMenu.class);

        if (!q.getResultList().isEmpty()) {
            JsfUtil.mensajeAlerta("Existen registros relacionados a esta opción y no puede ser eliminada");
            return false;
        }

        return true;
    }

    public void eliminar() {
        if (validarEliminar()) {
            opcionmenurepo.delete(opcionmenu);
            llenarListaOpciones();
            deshabilitado = true;
        }
    }

    public void onItemSelect(SelectEvent event) {
        opcionmenu = (OpcionMenu) event.getObject();
        if (opcionmenu.getOpcIdOpcMenu() == null) {
            OpcionMenu tmp = new OpcionMenu();
            tmp.setIdOpcMenu((long) 0);
            opcionmenu.setOpcIdOpcMenu(tmp);
        }
        if (opcionmenu.getApp() == null) {
            Modulo tmp = new Modulo();
            tmp.setIdModulo((long) 0);
            opcionmenu.setApp(tmp);
        }
        deshabilitado = false;
    }

    public void onItemUnselect() {
        opcionmenu = null;
        deshabilitado = true;
    }

    //**************************************************************************
    //******************************** Getter y Setter *************************
    //**************************************************************************
    public List<OpcionMenu> getListopciones() {
        return listopciones;
    }

    public void setListopciones(List<OpcionMenu> listopciones) {
        this.listopciones = listopciones;
    }

    public List<OpcionMenu> getListopcionespadre() {
        return listopcionespadre;
    }

    public void setListopcionespadre(List<OpcionMenu> listopcionespadre) {
        this.listopcionespadre = listopcionespadre;
    }

    public List<Modulo> getListmodulos() {
        return listmodulos;
    }

    public void setListmodulos(List<Modulo> listmodulos) {
        this.listmodulos = listmodulos;
    }

    public OpcionMenu getOpcionmenu() {
        if (opcionmenu == null) {
            OpcionMenu tmpopciones = new OpcionMenu();
            OpcionMenu tmpmenusuperior = new OpcionMenu();
            tmpmenusuperior.setIdOpcMenu((long) 0);

            Modulo tmpmodulos = new Modulo();
            tmpmodulos.setIdModulo((long) 0);

            tmpopciones.setOpcIdOpcMenu(tmpmenusuperior);
            tmpopciones.setApp(tmpmodulos);
            return tmpopciones;

        } else {
            if (opcionmenu.getIdOpcMenu() == null) {
                OpcionMenu tmpmenusuperior = new OpcionMenu();
                tmpmenusuperior.setIdOpcMenu((long) 0);

                Modulo tmpmodulos = new Modulo();
                tmpmodulos.setIdModulo((long) 0);

                opcionmenu.setOpcIdOpcMenu(tmpmenusuperior);
                opcionmenu.setApp(tmpmodulos);
                return opcionmenu;
            } else {
                return opcionmenu;
            }
        }
    }

    public void setOpcionmenu(OpcionMenu opcionmenu) {
        this.opcionmenu = opcionmenu;
    }

    public Boolean getDeshabilitado() {
        return deshabilitado;
    }

    public void setDeshabilitado(Boolean deshabilitado) {
        this.deshabilitado = deshabilitado;
    }

}
