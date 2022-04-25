/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.utils;

import java.io.File;
import java.math.BigDecimal;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author misanchez
 */
public class VarSession {

    //public static final String urlBAse = "";
    public static final String PATH_REPORTES = File.separator + "resources" + File.separator + "reportes" + File.separator;
    public static final String PATH_IMAGENES = File.separator + "resources" + File.separator + "imagenes" + File.separator;

    public static Object getVariableSession(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().get(key);
    }

    public static String getVariableSessionUsuario() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().get("Usuario").toString();
    }

    public static int getVariableSessionED() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getExternalContext().getSessionMap().containsKey("estadoEdicion")) {
            return Integer.parseInt(context.getExternalContext().getSessionMap().get("estadoEdicion").toString());
        } else {
            return 0;
        }
    }

    public static void setVariableSessionED(String valor) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("estadoEdicion", valor);
    }

    public static void setVariableSession(String key, Object value) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put(key, value);
    }

    public static Boolean isVariableSession(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().containsKey(key);
    }

    public static void removeVariableSession(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().remove(key);
    }

    public static void crearCookie(String nombre, String valor) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        Cookie cookie = new Cookie(nombre, valor);
        cookie.setMaxAge(1 * 365 * 24 * 60 * 60);
        cookie.setPath(request.getContextPath());
        
        ((HttpServletResponse) context.getExternalContext().getResponse()).addCookie(cookie);
    }

    public static Boolean isCookie(String name) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getRequestCookieMap().containsKey(name);
    }

    public static String getCookieValue(String name) {
        FacesContext context = FacesContext.getCurrentInstance();
        return ((Cookie) context.getExternalContext().getRequestCookieMap().get(name)).getValue();
    }
}
