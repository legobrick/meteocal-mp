/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.util;

import it.polimi.deib.se2.mp.weathercal.entity.WeatherStateConstraint;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.inject.Named;
import org.omnifaces.converter.GenericEnumConverter;
import static org.omnifaces.util.Faces.getViewAttribute;
import org.omnifaces.util.Messages;

/**
 *
 * @author paolo
 */
@Named
@RequestScoped
public class MyEnumConverter extends GenericEnumConverter{
    
        private static final String ATTRIBUTE_ENUM_TYPE = "GenericEnumConverter.%s";

        @Override
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
                if (submittedValue == null || submittedValue.isEmpty() || submittedValue.equals("-")) {
                        return null;
                }

                try {
                        return WeatherStateConstraint.State.valueOf(submittedValue);
                }
                catch (IllegalArgumentException e) {
                        throw new ConverterException(Messages.createError("Vaffanculo"));
                }
        }
}
