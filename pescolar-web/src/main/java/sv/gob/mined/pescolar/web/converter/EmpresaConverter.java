package sv.gob.mined.pescolar.web.converter;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.repository.CatalogoGlobal;

/**
 *
 * @author misanchez
 */
@Named
@FacesConverter(value = "empresaConverter", managed = true)
public class EmpresaConverter implements Converter<Empresa>, Serializable {

    @Inject
    private CatalogoGlobal cgRepo;

    @Override
    public Empresa getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                return cgRepo.getEmpresasAsMap().get(Long.parseLong(value));
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid country."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Empresa value) {
        if (value != null) {
            return String.valueOf(value.getId());
        } else {
            return null;
        }
    }

}
