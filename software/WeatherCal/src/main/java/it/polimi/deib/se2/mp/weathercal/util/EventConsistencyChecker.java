/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.util;

import it.polimi.deib.se2.mp.weathercal.boundary.EventManager;
import it.polimi.deib.se2.mp.weathercal.entity.Event;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author paolo
 */
@Named
@RequestScoped
public class EventConsistencyChecker implements Converter {
    
    @Inject
    Logger logger;
    
    @EJB
    EventManager em;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        if(value.contains("id=")){
            value = value.substring(value.indexOf("id=") + 3);
            int i;
            for(i=0; Character.isDigit(value.charAt(i)); i++);
            value = value.substring(0, i);
        }

        if (!value.matches("[0-9]+"))
            throwError(context, "The value is not a valid Event ID: " + value);
        Event e = null;
        try {
            long id = Long.valueOf(value);
            e = em.find(id);
        } catch (NumberFormatException ex){
            value += "\n" + ex.getMessage();
        }
        
        if(e == null)
            throwError(context, "The value is not a valid Event ID: " + value);
        return e.getId();
    }
    
    private void throwError(FacesContext context, String errorText){
        try {
            context.getExternalContext().responseSendError(
                    403, errorText);
        } catch (IOException ex) {
            Logger.getLogger(EventConsistencyChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value == null? null: value.toString();// getAsObject(context, component, ((Event) value).getId().toString()).toString();
    }
}
