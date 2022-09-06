package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.math.BigDecimal;
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
import sv.gob.mined.pescolar.utils.db.Filtro;
import sv.gob.mined.pescolar.utils.JsfUtil;
import sv.gob.mined.pescolar.utils.enums.TipoOperador;

/**
 *
 * @author misanchez
 */
@SuppressWarnings("serial")
@Named
@SessionScoped
public class MenuView implements Serializable {

    private Boolean showModuloContrataciones = false;
    private Boolean showModuloProveedores = false;
    private Boolean showModuloConsultas = false;
    private Boolean showModuloModificativas = false;
    private Boolean showModuloCreditos = false;
    private Boolean showModuloRecepcion = false;
    private Boolean showModuloPagos = false;
    private Boolean showModuloLiquidacion = false;
    private Boolean showModuloMantenimiento = false;

    private Long idOpcionMenu;
    private Usuario usuario;
    private List<BigDecimal> lstModulos = new ArrayList();
    private MenuModel model;
    private List<OpcionMenuUsuarioDto> lstOpcionMenu = new ArrayList();
    private List<Filtro> params = new ArrayList();

    @Inject
    private SecurityContext securityContext;

    @Inject
    private CatalogoRepo catalogoRepo;

    @PostConstruct
    public void init() {
        loadingUsuarioAndModulos();
    }

    public Boolean getShowModuloContrataciones() {
        return showModuloContrataciones;
    }

    public Boolean getShowModuloProveedores() {
        return showModuloProveedores;
    }

    public Boolean getShowModuloConsultas() {
        return showModuloConsultas;
    }

    public Boolean getShowModuloModificativas() {
        return showModuloModificativas;
    }

    public Boolean getShowModuloCreditos() {
        return showModuloCreditos;
    }

    public Boolean getShowModuloRecepcion() {
        return showModuloRecepcion;
    }

    public Boolean getShowModuloPagos() {
        return showModuloPagos;
    }

    public Boolean getShowModuloLiquidacion() {
        return showModuloLiquidacion;
    }

    public Boolean getShowModuloMantenimiento() {
        return showModuloMantenimiento;
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
            params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idOpcMenu", idOpcionMenu).build());
            params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idUsuario", usuario.getIdUsuario()).build());

            lstOpcionMenu = catalogoRepo.findAllOpcionMenuByUsuarioAndApp(usuario.getIdUsuario(), idOpcionMenu);

            crearArbolMenu();
        } else if (usuario.getIdTipoUsuario().getIdTipoUsuario() == 9l) { //se verifica que el usuario se uno de tipo proveedor
            lstOpcionMenu = catalogoRepo.findAllOpcionMenuByUsuarioAndApp(usuario.getIdUsuario(), 55l);
            crearArbolMenu();
        }
    }

    public void limpiarMenu() {
        model.getElements().clear();
    }

    private void crearArbolMenu() {
        DefaultMenuModel menu = new DefaultMenuModel();

        try {
            //<p:menuitem id="om_dashboard" value="Home" icon="pi pi-home" action="/app/principal" />

            DefaultMenuItem itemMenu = DefaultMenuItem.builder()
                    .value(" Principal")
                    .icon("pi pi-home")
                    .command("#{guestPreferences.ocultarMenuRedirect}")
                    .ajax(false)
                    .build();

            menu.getElements().add(itemMenu);

            for (OpcionMenuUsuarioDto opc : lstOpcionMenu) {
                if (opc.getOpcIdOpcMenu() != null) {
                    itemMenu = DefaultMenuItem.builder()
                            .value(" " + opc.getNombreOpcion())
                            .outcome(opc.getNombrePanel())
                            .icon(opc.getIcono())
                            .ajax(false)
                            .id("item" + opc.getIdOpcMenu().toString())
                            .build();

                    menu.getElements().add(itemMenu);
                } else {
                    DefaultSubMenu subMenu = DefaultSubMenu.builder()
                            .icon(opc.getIcono())
                            .label(opc.getNombreOpcion())
                            .id("sub" + opc.getIdOpcMenu().toString())
                            .build();
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
            params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "opcIdOpcMenu.idOpcMenu", object.getIdOpcMenu()).build());

            List lstTemp = catalogoRepo.findAllOpcionMenuByUsuarioAndApp(usuario.getIdUsuario(), object.getIdOpcMenu());

            if (lstTemp.isEmpty()) {
                DefaultMenuItem itemMenu = DefaultMenuItem.builder()
                        .value(" " + object.getNombreOpcion())
                        .outcome(object.getNombrePanel())
                        .icon(object.getIcono())
                        .ajax(false)
                        .id("item" + object.getIdOpcMenu())
                        .build();
                opPadre.getElements().add(itemMenu);
            } else {
                DefaultSubMenu subMenu = DefaultSubMenu.builder()
                        .icon(object.getIcono())
                        .label(object.getNombrePanel())
                        .id("sub" + object.getIdOpcMenu())
                        .build();

                getHijo(subMenu, object.getIdOpcMenu());
                opPadre.getElements().add(subMenu);
            }
        }
    }

    private void loadingUsuarioAndModulos() {
        params = new ArrayList();
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idPersona.usuario", securityContext.getCallerPrincipal().getName()).build());
        usuario = ((Usuario) catalogoRepo.findListByParam(Usuario.class, params).get(0));

        if (usuario.getIdTipoUsuario().getIdTipoUsuario() == 9l) {
        } else {
            lstModulos = catalogoRepo.findAllModuloByUsuario(usuario.getIdUsuario());
            for (BigDecimal idModulo : lstModulos) {
                switch (idModulo.intValue()) {
                    case 1:
                        showModuloContrataciones = true;
                        break;
                    case 2:
                        showModuloConsultas = true;
                        break;
                    case 3:
                        showModuloProveedores = true;
                        break;
                    case 4:
                        showModuloModificativas = true;
                        break;
                    case 5:
                        showModuloCreditos = true;
                        break;
                    case 6:
                        showModuloRecepcion = true;
                        break;
                    case 7:
                        showModuloPagos = true;
                        break;
                    case 8:
                        showModuloMantenimiento = true;
                        break;
                    case 10:
                        showModuloLiquidacion = true;
                        break;
                }
            }
        }
    }
}
