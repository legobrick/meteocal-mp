/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.gui;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author paolo
 */
@Named
@RequestScoped
public class EventManagerBean {

    /**
     * Creates a new instance of EventManagerBean
     */
    public EventManagerBean() {
    }
    
    public String createNew() throws IOException{
        return "create_event?faces-redirect=true";
//        FacesContext.getCurrentInstance().getExternalContext().redirect("create_event");
    }
    
}
