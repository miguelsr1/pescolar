package sv.gob.mined.pescolar.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class JsfUtil {

    private static FacesMessage msg;
    private static final DecimalFormat FORMAT_DECIMAL = new DecimalFormat("#,##0.00");
    private static final DecimalFormat FORMAT_ENTERO = new DecimalFormat("#,##0");
    public static final DateFormat FORMAT_DATE = new SimpleDateFormat("dd/MM/yyyy");

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("Bundle");

    public static String getValorFromBundleByKey(String key) {
        return RESOURCE_BUNDLE.getString(key);
    }

    public static String getPathReportes() {
        if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
            return JsfUtil.getValorFromBundleByKey("path_reportes_win");
        } else {
            return JsfUtil.getValorFromBundleByKey("path_reportes_linux");
        }
    }

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static void mensajeUpdate() {
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Informaci??n", "Actualizaci??n exitosa.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void mensajeInsert() {
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Informaci??n", "Registro almacenado satisfactoriamente");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void mensajeAlerta(String mensaje) {
        msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "" + mensaje + "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void mensajeError(String mensaje) {
        msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "" + mensaje + "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void mensajeInformacion(String mensaje) {
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Informaci??n", "" + mensaje + "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void redireccionar(String url) {
        ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();

        configurableNavigationHandler.performNavigation(url);
    }

    /*public static <T extends Object> T getValuePK(Object t) {
        T value = null;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(javax.persistence.Id.class) != null) {
                try {
                    Class[] sinArgumentos = new Class[0];
                    Object[] sinParametros = new Object[0];
                    Method getter = new PropertyDescriptor(field.getName(), t.getClass()).getReadMethod();
                    value = (T) t.getClass().getMethod(getter.getName(), sinArgumentos).invoke(t, sinParametros);
                    break;
                } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                    Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return value;
    }*/

    public static Object newInstanceValuePK(Class t, Object o) {
        Object value = null;
        Field[] fields = t.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(javax.persistence.Id.class) != null) {
                try {
                    if (Integer.class.isAssignableFrom(field.getType())) {
                        value = Integer.parseInt(o.toString());
                    } else if (BigDecimal.class.isAssignableFrom(field.getType())) {
                        value = new BigDecimal(o.toString());
                    } else if (BigInteger.class.isAssignableFrom(field.getType())) {
                        value = new BigInteger(o.toString());
                    } else if (String.class.isAssignableFrom(field.getType())) {
                        value = o.toString();
                    }

                    break;
                } catch (NumberFormatException ex) {
                    Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return value;
    }

    public static String getNombreDepartamentoByCodigo(String codigo) {
        String nombre = "";
        switch (codigo) {
            case "00":
                nombre = "TODO EL PAIS";
                break;
            case "01":
                nombre = "AHUACHAPAN";
                break;
            case "02":
                nombre = "SANTA ANA";
                break;
            case "03":
                nombre = "SONSONATE";
                break;
            case "04":
                nombre = "CHALATENANGO";
                break;
            case "05":
                nombre = "LA LIBERTAD";
                break;
            case "06":
                nombre = "SAN SALVADOR";
                break;
            case "07":
                nombre = "CUSCATLAN";
                break;
            case "08":
                nombre = "LA PAZ";
                break;
            case "09":
                nombre = "CABA??AS";
                break;
            case "10":
                nombre = "SAN VICENTE";
                break;
            case "11":
                nombre = "USULUTAN";
                break;
            case "12":
                nombre = "SAN MIGUEL";
                break;
            case "13":
                nombre = "MORAZAN";
                break;
            case "14":
                nombre = "LA UNION";
                break;
        }
        return nombre;
    }

    public static String getNombreRubroById(BigDecimal idRubro) {
        switch (idRubro.intValue()) {
            case 1:
            case 4:
            case 5:
                return "SERVICIOS DE CONFECCION DE UNIFORMES";
            case 2:
                return "SUMINISTRO DE PAQUETES DE UTILES ESCOLARES";
            default:
                return "PRODUCCION DE ZAPATOS";
        }
    }

    public static HashMap getNombreRubroRpt(int idRubro, HashMap param, Boolean sobredemanda) {
        switch (idRubro) {
            case 1:
                param.put("descripcionRubro", (sobredemanda ? "SOBREDEMANDA DE " : "") + "SERVICIOS DE CONFECCION DE UNIFORMES");
                break;
            case 4:
                param.put("descripcionRubro", (sobredemanda ? "SOBREDEMANDA DE " : "") + "SERVICIOS DE CONFECCION DE PRIMER UNIFORME");
                break;
            case 5:
                param.put("descripcionRubro", (sobredemanda ? "SOBREDEMANDA DE " : "") + "SERVICIOS DE CONFECCION DE SEGUNDO UNIFORME");
                break;
            case 2:
                param.put("descripcionRubro", (sobredemanda ? "SOBREDEMANDA DE " : "") + "SUMINISTRO DE PAQUETES DE UTILES ESCOLARES");
                break;
            case 3:
                param.put("descripcionRubro", (sobredemanda ? "SOBREDEMANDA DE " : "") + "PRODUCCION DE ZAPATOS");
                break;
        }
        return param;
    }

    public static String getFormatoNum(Object obj, Boolean entero) {
        if (entero) {
            if (obj == null) {
                return "0";
            } else {
                return FORMAT_ENTERO.format(obj);
            }
        } else {
            if (obj == null) {
                return "0.00";
            } else {
                return FORMAT_DECIMAL.format(obj);
            }
        }
    }

    public static String formatearNumero(int posisiones, String valor, Boolean numInt) {
        valor = valor.replaceAll("[.]", "");
        Formatter fmt = new Formatter();
        if (numInt) {
            fmt.format("%0" + posisiones + "d", Integer.parseInt(valor));
        } else {
            fmt.format("%-" + posisiones + "s", valor);
        }
        return fmt.toString();
    }

    public static String formatoNumeroF910(int posisiones, BigDecimal valor, Boolean numInt) {
        String formato = FORMAT_DECIMAL.format(valor.doubleValue()).replaceAll("[.]", "").replaceAll("[,]", "");
        Formatter fmt = new Formatter();
        if (numInt) {
            fmt.format("%0" + posisiones + "d", Integer.parseInt(formato));
        } else {
            fmt.format("%-" + posisiones + "s", valor);
        }
        return fmt.toString();
    }

    public static String getFechaString(Date date) {
        return FORMAT_DATE.format(date);
    }

    public static Boolean isExisteParametroUrl(String nombreParamentro) {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        return params.containsKey(nombreParamentro);
    }

    public static String getParametroUrl(String nombreParamentro) {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        return params.get(nombreParamentro);
    }


    public static Object getControllerByName(String name) {
        return FacesContext.getCurrentInstance().getApplication().getELResolver().
                getValue(FacesContext.getCurrentInstance().getELContext(), null, name);

    }

    public static void redirectToIndex() {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(externalContext.getRequestContextPath());
        } catch (IOException ex) {
            Logger.getLogger(JsfUtil.class.getName()).log(Level.INFO, "Error redireccionando al Index", ex);
        }
    }

}
