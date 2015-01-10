/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.gui;

import it.polimi.deib.se2.mp.weathercal.boundary.EventManager;
import it.polimi.deib.se2.mp.weathercal.boundary.UserManager;
import it.polimi.deib.se2.mp.weathercal.entity.Event;
import it.polimi.deib.se2.mp.weathercal.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.primefaces.context.RequestContext;

/**
 *
 * @author paolo
 */
@ManagedBean
@Named
@RequestScoped
public class searchUser {

    @PersistenceContext
    EntityManager em;

    @EJB
    UserManager um;

    
    private String searched;
    
    public void setSearched(String email){
        this.searched=email;
    }
    public String getSearched(){
return this.searched;
}
    /**
     * Creates a new instance of EventBean
     */
    public void searchUser() {

        Query q = em.createNamedQuery("User.findByEmail");
         q.setParameter("email",this.searched);
         User searchus= (User)q.getResultList().get(0);
         
    
        
if (searchus.getCalendarCollection().iterator().next().getIsPublic()){
 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Yes", "");
RequestContext.getCurrentInstance().showMessageInDialog(message);
}
else{
FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Forbidden", "The user has a private calendar");
RequestContext.getCurrentInstance().showMessageInDialog(message);
}
        
    }

}