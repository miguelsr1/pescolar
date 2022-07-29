/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.convert;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

import sv.gob.mined.pescolar.model.view.VwCatalogoEntidadEducativa;
import sv.gob.mined.pescolar.web.RepositorioAplicacionView;

/**
 *
 * @author misanchez
 */
@Named
@FacesConverter(value = "entidadEducativaConvert", managed = true)
public class EntidadEducativaConvert implements Converter<VwCatalogoEntidadEducativa> {

    @Inject
    private RepositorioAplicacionView repositorioAplicacionView;

    @Override
    public VwCatalogoEntidadEducativa getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                return repositorioAplicacionView.findEntidadByCodigoEntidad(value);
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Entidad Educativa."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, VwCatalogoEntidadEducativa value) {
        if (value != null) {
            return String.valueOf(value.getCodigoEntidad());
        } else {
            return null;
        }
    }

}
