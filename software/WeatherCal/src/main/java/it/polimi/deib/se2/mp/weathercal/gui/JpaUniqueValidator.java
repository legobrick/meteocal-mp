/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.gui;

import it.polimi.deib.se2.mp.weathercal.util.JpaUniqueSupport;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import static org.omnifaces.util.Utils.isBlank;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author paolo
 */
@FacesValidator("jpaUniqueValidator")
public class JpaUniqueValidator implements Validator {

    @Inject
    private JpaUniqueSupport jpaUniqueSupport;
    
    private static final boolean debug = true;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (entity == null) {
            entity = (Serializable) component.getAttributes().get("entity");
        }

        if (property == null) {
            property = (String) component.getAttributes().get("property");
        }
        
        if (entity == null && isBlank(property)) {
            if(debug) Logger.getLogger(JpaUniqueValidator.class.getName()).log(Level.SEVERE, "tutto vuoto");
            return;
        }

        if(property == null)
            throw new IllegalStateException("Missing 'property' attribute");

        if (!jpaUniqueSupport.isUnique(entity, property, value)) {
            
            FacesMessage fm;
            String customMsg = (String) component.getAttributes().get("message");
            try {
                fm = new FacesMessage(customMsg == null?
                        BeanUtils.getProperty(entity, property) + " already exists.":
                        customMsg
                );
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                Logger.getLogger(JpaUniqueValidator.class.getName()).log(Level.SEVERE, null, ex);
                fm = new FacesMessage(property + " already exists.");
            }
            fm.setSeverity(SEVERITY_ERROR);
            throw new ValidatorException(fm);
        }
            if(debug) Logger.getLogger(JpaUniqueValidator.class.getName()).log(Level.SEVERE, "validazione OK");
    }

    private Serializable entity;
    private String property;

    public void setEntity(Serializable entity) {
        this.entity = entity;
    }

    public Serializable getEntity() {
        return entity;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}