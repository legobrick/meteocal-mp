/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.gui;

import it.polimi.deib.se2.mp.weathercal.boundary.UserManager;
import it.polimi.deib.se2.mp.weathercal.entity.User;
import it.polimi.deib.se2.mp.weathercal.util.EventConsistencyChecker;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

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
    private User reverted;
    
    public User getReverted(){
        return reverted;
    }

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
      if(this.searched==null){
      System.out.println("è nullo");
      }
      else{  Query q = em.createNamedQuery("User.findByEmail");
        q.setParameter("email", this.searched);
        System.out.println("ricerca");
        if (q.getResultList().size() > 0) {
            User searchus = (User) q.getResultList().get(0);

            if (searchus.getCalendarCollection().iterator().next().getIsPublic()) {

                FacesContext.getCurrentInstance().getExternalContext().redirect("searched_user_page.xhtml?id=" + searchus.getCalendarCollection().iterator().next().getId() + "&name=" + searchus.getFirstName() + "&surname=" + searchus.getLastName() + "&mail=" + searchus.getEmail());
                FacesContext context = FacesContext.getCurrentInstance();
                HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
                ServletContext sContext = request.getSession().getServletContext();
                sContext.setAttribute("selectedEvent", searchus.getCalendarCollection().iterator().next().getId());
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Forbidden", "The user has a private calendar");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "User Not Found", "The email insert doesn't match any result");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
        
      }
    }

    public void editAction() {
        Map<String, String> params
                = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String action = params.get("action");
        //calid = Long.parseLong(action);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Forbidden", "" + action);
        RequestContext.getCurrentInstance().showMessageInDialog(message);
        //  RequestContext.getCurrentInstance().update("schedule1");

        RequestContext.getCurrentInstance().update("f:schedule1");
    }

    
    public void reverse(ComponentSystemEvent event) throws AbortProcessingException {
        if(FacesContext.getCurrentInstance().isPostback() || this.searched == null)
            throwError(FacesContext.getCurrentInstance(), "The value is not a valid User ID:" + searched);

        Query q = em.createNamedQuery("User.findByEmail");
        q.setParameter("email", this.searched);
        if (q.getResultList().isEmpty())
            throwError(FacesContext.getCurrentInstance(), "The value is not a valid User ID:" + searched);
        reverted = (User) q.getResultList().get(0);
    }
    
    private void throwError(FacesContext context, String errorText){
        try {
            context.getExternalContext().responseSendError(
                    403, errorText);
        } catch (IOException ex) {
            Logger.getLogger(EventConsistencyChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void onItemSelect(SelectEvent event) {
        System.out.println("Item Selected"+ event.getObject().toString());
    }
}
