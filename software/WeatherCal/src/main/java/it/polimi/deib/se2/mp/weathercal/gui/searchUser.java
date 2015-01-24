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
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Named;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author paolo
 */
@ManagedBean
@Named
@RequestScoped
@SessionScoped
public class searchUser {

    @PersistenceContext
    EntityManager em;

    @EJB
    UserManager um;

    EventManagerBean emb;

    private Long calid;
    private String searched;
    

    public void setSearched(String email) {
        this.searched = email;
    }

    public String getSearched() {
        return this.searched;
    }

    /**
     * Creates a new instance of EventBean
     */
    public void searchUser() throws IOException {

        Query q = em.createNamedQuery("User.findByEmail");
        q.setParameter("email", this.searched);
        System.out.println("ricerca");
        if (q.getResultList().size()>0){
        User searchus = (User) q.getResultList().get(0);

        if (searchus.getCalendarCollection().iterator().next().getIsPublic()) {

            FacesContext.getCurrentInstance().getExternalContext().redirect("searched_user_page.xhtml?id=" + searchus.getCalendarCollection().iterator().next().getId() + "&name=" + searchus.getFirstName() + "&surname=" + searchus.getLastName() + "&mail=" + searchus.getEmail());
           FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        ServletContext sContext = request.getSession().getServletContext();
        sContext.setAttribute("selectedEvent",searchus.getCalendarCollection().iterator().next().getId());
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Forbidden", "The user has a private calendar");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
        }
        else{
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "User Not Found", "The email insert doesn't match any result");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void editAction() {
        Map<String, String> params
                = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
         String action =params.get("action");
        //calid = Long.parseLong(action);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Forbidden", ""+action);
                RequestContext.getCurrentInstance().showMessageInDialog(message);
        //  RequestContext.getCurrentInstance().update("schedule1");
         
        RequestContext.getCurrentInstance().update("f:schedule1");
    }

  
}
