/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import sv.gob.mined.pescolar.model.OpcionMenu;
import sv.gob.mined.pescolar.model.TipoUsuOpcMenu;
import sv.gob.mined.pescolar.model.TipoUsuario;
import sv.gob.mined.pescolar.repository.OpcionMenuRepo;
import sv.gob.mined.pescolar.repository.TipoUsuOpcMenuRepo;
import sv.gob.mined.pescolar.repository.TipoUsuarioRepo;
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

    private List<SelectItem> opcionesgroup = new ArrayList();

    //@PersistenceContext(unitName = "paquetePU")
    //private EntityManager em;
    @Inject
    private SessionView sessionView;
    @Inject
    private TipoUsuOpcMenuRepo tipousuopcmenurepo;
    @Inject
    private TipoUsuarioRepo tipousuariorepo;
    @Inject
    private OpcionMenuRepo opcionmenurepo;

    @PostConstruct
    public void init() {
        deshabilitado = true;

        llenarListaTipoUsuario();
        llenarListaTipoUsuarioOpcionMenu();
    }

    public void nuevo() {
        llenarListaTipoUsuarioOpcionMenu();

        tipousuopcmenu = new TipoUsuOpcMenu();
        deshabilitado = false;

        listopcionmenu.clear();
        opcionesgroup.clear();
    }

    private void llenarListaTipoUsuario() {
        //Query q;
        //q = em.createQuery("select tu from TipoUsuario tu", TipoUsuario.class);
        //listtipousuario = q.getResultList();
        listtipousuario = tipousuariorepo.listartipousuario();

        listopcionmenu.clear();
        opcionesgroup.clear();

    }

    private void llenarListaTipoUsuarioOpcionMenu() {
        tipousuopcmenu = null;
        //Query q;
        //q = em.createQuery("select tu from TipoUsuOpcMenu tu order by tu.idTipoUsuario", TipoUsuOpcMenu.class);
        //listtipousuopcmenu = q.getResultList();
        listtipousuopcmenu = tipousuopcmenurepo.listartipousuopcmenu();

    }

    public void onClickTipoUsuario() {
        //Query q;
        //q = em.createQuery("select opc from OpcionMenu opc "
        //        + "where opc.idOpcMenu not in ("
        //        + "   select opc2.idOpcMenu.idOpcMenu "
        //        + "   from TipoUsuOpcMenu opc2 "
        //        + "   where opc2.idTipoUsuario.idTipoUsuario = " + ((tipousuopcmenu.getIdTipoUsuario() == null) ? 0 : (tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario() == null) ? 0 : tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario()) + " "
        //        + ") order by opc.orden ", OpcionMenu.class);
        //listopcionmenu = q.getResultList();
        listopcionmenu = opcionmenurepo.listaropcionmenuexcluyente(((tipousuopcmenu.getIdTipoUsuario() == null) ? 0 : (tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario() == null) ? 0 : tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario()));

        opcionesgroup = new ArrayList<>();
        String idsinpadre = "";
        SelectItemGroup encabezadogroup = new SelectItemGroup("Menú Principal");
        Integer contadorpadre = 0;
        for (int i = 0; i < listopcionmenu.size(); i++) {
            if (listopcionmenu.get(i).getOpcIdOpcMenu() == null) {
                contadorpadre = contadorpadre + 1;
            } else {
                if (listopcionmenu.get(i).getOpcIdOpcMenu().getIdOpcMenu() == null) {
                    contadorpadre = contadorpadre + 1;
                }

            }
        }
        SelectItem[] itemsencabezado = new SelectItem[contadorpadre];
        contadorpadre = 0;
        for (int i = 0; i < listopcionmenu.size(); i++) {
            if (listopcionmenu.get(i).getOpcIdOpcMenu() == null) {
                idsinpadre = idsinpadre + "," + listopcionmenu.get(i).getIdOpcMenu();
                itemsencabezado[contadorpadre] = new SelectItem(listopcionmenu.get(i).getIdOpcMenu(), listopcionmenu.get(i).getNombreOpcion());
                contadorpadre = contadorpadre + 1;
            } else {
                if (listopcionmenu.get(i).getOpcIdOpcMenu().getIdOpcMenu() == null) {
                    idsinpadre = idsinpadre + "," + listopcionmenu.get(i).getIdOpcMenu();
                    itemsencabezado[contadorpadre] = new SelectItem(listopcionmenu.get(i).getIdOpcMenu(), listopcionmenu.get(i).getNombreOpcion());
                    contadorpadre = contadorpadre + 1;
                }

            }
        }

        if (contadorpadre > 0) {
            encabezadogroup.setSelectItems(itemsencabezado);
            opcionesgroup.add(encabezadogroup);
        }

        for (int i = 0; i < listopcionmenu.size(); i++) {
            //q = em.createQuery("select opc from OpcionMenu opc "
            //        + "where opc.opcIdOpcMenu.idOpcMenu = " + listopcionmenu.get(i).getIdOpcMenu() + " "
            //        + "and opc.idOpcMenu not in ("
            //        + "   select opc2.idOpcMenu.idOpcMenu "
            //        + "   from TipoUsuOpcMenu opc2 "
            //        + "   where opc2.idTipoUsuario.idTipoUsuario = " + ((tipousuopcmenu.getIdTipoUsuario() == null) ? 0 : (tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario() == null) ? 0 : tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario()) + " "
            //        + ") order by opc.orden ", OpcionMenu.class);

            //List<OpcionMenu> resultado = q.getResultList();
            List<OpcionMenu> resultado = opcionmenurepo.listaropcionmenuhijosexcluyente(listopcionmenu.get(i).getIdOpcMenu(), ((tipousuopcmenu.getIdTipoUsuario() == null) ? 0 : (tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario() == null) ? 0 : tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario()));

            if (!resultado.isEmpty()) {
                SelectItemGroup padregroup = new SelectItemGroup(listopcionmenu.get(i).getNombreOpcion());
                SelectItem[] itemshijos = new SelectItem[resultado.size()];
                for (int j = 0; j < resultado.size(); j++) {
                    itemshijos[j] = new SelectItem(resultado.get(j).getIdOpcMenu(), resultado.get(j).getNombreOpcion());
                }

                padregroup.setSelectItems(itemshijos);
                opcionesgroup.add(padregroup);

            }
        }

        // if (!idsinpadre.equals("")) {
        /*
            q = em.createQuery("select opc from OpcionMenu opc "
                    + "where opc.idOpcMenu in (" + idsinpadre.substring(1) + ") "
                    + "and opc.idOpcMenu not in ("
                    + "   select opc2.idOpcMenu.idOpcMenu "
                    + "   from TipoUsuOpcMenu opc2 "
                    + "   where opc2.idTipoUsuario.idTipoUsuario = " + ((tipousuopcmenu.getIdTipoUsuario() == null) ? 0 : (tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario() == null) ? 0 : tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario()) + " "
                    + ") ", OpcionMenu.class);

            List<OpcionMenu> resultado = q.getResultList();
            if (!resultado.isEmpty()) {
                SelectItemGroup padregroup = new SelectItemGroup("Menú Principal");
                for (int j = 0; j < resultado.size(); j++) {
                    padregroup.setSelectItems(new SelectItem[]{
                        new SelectItem(resultado.get(j).getIdOpcMenu(), resultado.get(j).getNombreOpcion())
                    });
                }
                opcionesgroup.add(padregroup);
            }
         */
        // }
        tipousuopcmenu.getIdOpcMenu().setIdOpcMenu((long) 0);

        //q = em.createQuery("select tu from TipoUsuOpcMenu tu "
        //        + "where tu.idTipoUsuario.idTipoUsuario = " + ((tipousuopcmenu.getIdTipoUsuario() == null) ? 0 : (tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario() == null) ? 0 : tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario()), TipoUsuOpcMenu.class);
        //listtipousuopcmenu = q.getResultList();
        listtipousuopcmenu = tipousuopcmenurepo.listartipousuopcmenuportipousuario(((tipousuopcmenu.getIdTipoUsuario() == null) ? 0 : (tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario() == null) ? 0 : tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario()));
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

        //Query q;
        if (tipousuopcmenu.getId() == null) {
            //q = em.createQuery("select tu from TipoUsuOpcMenu tu where tu.idOpcMenu.idOpcMenu = " + tipousuopcmenu.getIdOpcMenu().getIdOpcMenu() + " and tu.idTipoUsuario.idTipoUsuario = " + tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario() + " ", TipoUsuOpcMenu.class);
            if (!tipousuopcmenurepo.listartipousuopcmenuporopcionmenuytipousuario(tipousuopcmenu.getIdOpcMenu().getIdOpcMenu(), tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario()).isEmpty()) {
                JsfUtil.mensajeAlerta("La relación tipo de usuario y opción menú ya existe en la lista");
                return false;
            }
        } else {
            //q = em.createQuery("select tu from TipoUsuOpcMenu tu where tu.idOpcMenu.idOpcMenu = " + tipousuopcmenu.getIdOpcMenu().getIdOpcMenu() + " and tu.idTipoUsuario.idTipoUsuario = " + tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario() + " and tu.id <> " + tipousuopcmenu.getId(), TipoUsuOpcMenu.class);
            if (!tipousuopcmenurepo.listartipousuopcmenuporopcionmenuytipousuarioconotroid(tipousuopcmenu.getIdOpcMenu().getIdOpcMenu(), tipousuopcmenu.getIdTipoUsuario().getIdTipoUsuario(), tipousuopcmenu.getId()).isEmpty()) {
                JsfUtil.mensajeAlerta("La relación tipo de usuario y opción menú ya existe en la lista");
                return false;
            }
        }
        //if (!q.getResultList().isEmpty()) {
        //    JsfUtil.mensajeAlerta("La relación tipo de usuario y opción menú ya existe en la lista");
        //    return false;
        //}

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

    public List<SelectItem> getOpcionesgroup() {
        return opcionesgroup;
    }

    public void setOpcionesgroup(List<SelectItem> opcionesgroup) {
        this.opcionesgroup = opcionesgroup;
    }

}
