/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.gui;

import it.polimi.deib.se2.mp.weathercal.boundary.EventManager;
import it.polimi.deib.se2.mp.weathercal.boundary.UserManager;
import it.polimi.deib.se2.mp.weathercal.entity.Calendar;
import it.polimi.deib.se2.mp.weathercal.entity.Event;
import it.polimi.deib.se2.mp.weathercal.entity.Participation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author paolo
 */
@ManagedBean
@Named
@RequestScoped
public class EventManagerBean {

    @PersistenceContext
    EntityManager em;
    
    @EJB
    UserManager um;
    /**
     * Creates a new instance of EventManagerBean
     */
    Event e1=new Event();
    public EventManagerBean() {
    }
    
    public String createNew() throws IOException{
        return "create_event?faces-redirect=true";
//        FacesContext.getCurrentInstance().getExternalContext().redirect("create_event");
    }
    

     public List<Event> allEventByAvailability(String availability){
    
 List<Event> allUserEv=new  <Event>ArrayList();
    Query q = em.createNamedQuery("Participation.findByIdCalendarandAvailability");
    q.setParameter("idCalendar",this.userCalendarId());
    q.setParameter("av",availability);
    int size=q.getResultList().size();
   for(int n=0;n<size;n++){
      Participation part=(Participation)q.getResultList().get(n);
       long idEv=part.getParticipationPK().getIdEvent();
      
     Query p = em.createNamedQuery("Event.findById");
     p.setParameter("id",idEv);
     Event e=(Event)p.getResultList().get(0);
      
      allUserEv.add(e);
      
   }
   
    return allUserEv;
   
    /*Calendar cal=(Calendar) q.getResultList().get(0);
    long calId =cal.getId();
    
    
    return calId;*/
    }
     public String descr(){
     return this.e1.getDescription();
     }
    public long userCalendarId(){
    
    Calendar cal=(Calendar)um.getLoggedUser().getCalendarCollection().iterator().next();
    return cal.getId();
    }
}


