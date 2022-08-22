/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.web.converter;

/**
 *
 * @author misanchez
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.utils.DescriptorDto;

/**
 *
 * @author misanchez
 */
@Named
@FacesConverter(value = "descriptorDtoConverter", managed = true)
public class DescriptorDtoConverter implements Converter {

    @Inject
    private CatalogoRepo catalogoRepo;

    @Override
    public DescriptorDto getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                for (DescriptorDto tec : catalogoRepo.getLstTecnicosPaquete()) {
                    if (tec.getAtributo().equals(value)) {
                        return tec;
                    }
                }
                return null;
                //return catalogoRepo.getLstTecnicosPaquete().stream().filter(tec -> tec.getAtributo().equals(value)).findAny().get();
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid country."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        return object.toString();
    }
}
