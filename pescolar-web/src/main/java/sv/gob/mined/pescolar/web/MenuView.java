/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.SecurityContext;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import sv.gob.mined.pescolar.model.Usuario;
import sv.gob.mined.pescolar.model.dto.OpcionMenuUsuarioDto;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.utils.Filtro;
import sv.gob.mined.pescolar.utils.JsfUtil;

/**
 *
 * @author misanchez
 */
@SuppressWarnings("serial")
@Named
@SessionScoped
public class MenuView implements Serializable {

    private Long idOpcionMenu;
    private Usuario usuario;

    private MenuModel model;
    private List<OpcionMenuUsuarioDto> lstOpcionMenu = new ArrayList();
    private List<Filtro> params = new ArrayList();

    @Inject
    private SecurityContext securityContext;

    @Inject
    private CatalogoRepo catalogoRepo;

    @PostConstruct
    public void init() {
        params = new ArrayList();
        params.add(new Filtro(Filtro.EQUALS, "idPersona.usuario", securityContext.getCallerPrincipal().getName()));
        usuario = ((Usuario) catalogoRepo.findListByParam(Usuario.class, params).get(0));
    }

    public MenuModel getModel() {
        return model;
    }

    public Long getIdOpcionMenu() {
        return idOpcionMenu;
    }

    public void setIdOpcionMenu(Long idOpcionMenu) {
        this.idOpcionMenu = idOpcionMenu;
    }

    public void armarMenu() {
        model = new DefaultMenuModel();
        if (idOpcionMenu != null) {
            params.clear();
            params.add(new Filtro(Filtro.EQUALS, "idOpcMenu", idOpcionMenu));
            params.add(new Filtro(Filtro.EQUALS, "idUsuario", usuario.getIdUsuario()));

            lstOpcionMenu = catalogoRepo.findAllOpcionMenuByUsuarioAndApp(usuario.getIdUsuario(), idOpcionMenu);
            
            crearArbolMenu();
        }
    }

    private void crearArbolMenu() {
        DefaultMenuModel menu = new DefaultMenuModel();

        try {
            DefaultMenuItem itemMenu = new DefaultMenuItem();
            itemMenu.setValue(" Slim Menu");
            itemMenu.setIcon("fa fa-thumb-tack");
            itemMenu.setCommand("#{guestPreferences.setSlimMenu(true)}");
            itemMenu.setAjax(false);

            menu.getElements().add(itemMenu);

            for (OpcionMenuUsuarioDto opc : lstOpcionMenu) {
                if (opc.getOpcIdOpcMenu() != null) {
                    itemMenu = new DefaultMenuItem();

                    itemMenu.setValue(" " + opc.getNombreOpcion());
                    itemMenu.setOutcome(opc.getNombrePanel());
                    itemMenu.setIcon(opc.getIcono());
                    itemMenu.setAjax(false);
                    itemMenu.setId("item" + opc.getIdOpcMenu().toString());

                    menu.getElements().add(itemMenu);
                } else {
                    DefaultSubMenu subMenu = new DefaultSubMenu();
                    subMenu.setIcon(opc.getIcono());
                    subMenu.setLabel(opc.getNombreOpcion());
                    subMenu.setId("sub" + opc.getIdOpcMenu().toString());
                    getHijo(subMenu, opc.getIdOpcMenu());

                    menu.getElements().add(subMenu);
                }
            }
            model = menu;
        } catch (Exception ex) {
            JsfUtil.mensajeError("Ocurrio una excepción en el proceso de creación del arbol del menu. Contactese con el administrador del sistema.");
            Logger.getLogger(MenuView.class.getName()).
                    log(Level.SEVERE, "\n\n===============================================\n"
                            + "Ocurrio un error inesperado en el proceso de creaci\u00f3n del arbol del menu para el usuario: {0}\n"
                            + "Clase: {1}\n"
                            + "Metodo: crearArbolMenu() \n",
                            new Object[]{usuario.getIdPersona().getUsuario(), MenuView.class.getSimpleName()});
        }
    }

    private void getHijo(DefaultSubMenu opPadre, Long idOpcion) {
        List<OpcionMenuUsuarioDto> lst = catalogoRepo.findAllOpcionMenuByUsuarioAndApp(usuario.getIdUsuario(), idOpcion);

        for (OpcionMenuUsuarioDto object : lst) {

            params.clear();
            params.add(new Filtro(Filtro.EQUALS, "opcIdOpcMenu.idOpcMenu", object.getIdOpcMenu()));

            List lstTemp = catalogoRepo.findAllOpcionMenuByUsuarioAndApp(usuario.getIdUsuario(), object.getIdOpcMenu());

            if (lstTemp.isEmpty()) {
                DefaultMenuItem itemMenu = new DefaultMenuItem();

                itemMenu.setValue(" " + object.getNombreOpcion());
                itemMenu.setOutcome(object.getNombrePanel());
                itemMenu.setIcon(object.getIcono());
                itemMenu.setAjax(false);
                itemMenu.setId("item" + object.getIdOpcMenu());
                opPadre.getElements().add(itemMenu);
            } else {
                DefaultSubMenu subMenu = new DefaultSubMenu();
                subMenu.setIcon(object.getIcono());
                subMenu.setLabel(object.getNombrePanel());
                subMenu.setId("sub" + object.getIdOpcMenu());
                getHijo(subMenu, object.getIdOpcMenu());
                opPadre.getElements().add(subMenu);
            }
        }
    }
}
