/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.gui;

import it.polimi.deib.se2.mp.weathercal.boundary.UserManager;
import it.polimi.deib.se2.mp.weathercal.entity.Event;
import it.polimi.deib.se2.mp.weathercal.entity.User;
import it.polimi.deib.se2.mp.weathercal.util.EventConsistencyChecker;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
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

    public Object getSearched() {
        return searched == null? null: um.find(searched);
    }

    /**
     * Creates a new instance of EventBean
     * @throws java.io.IOException
     */
    public void searchUser() throws IOException {
        System.out.println("DIODIODIDIDIODIOD" + this.searched);
      if(this.searched==null){
      System.out.println("è nullo");
      FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Input", "" + "No user matches with your input");
        RequestContext.getCurrentInstance().showMessageInDialog(message);
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
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "User Not Found", "The searched email doesn\\'t match any result");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
        
      }
    }

    public void editAction(SelectEvent se) throws IOException {
        Object bound = se.getObject();
        Pattern userRegex = Pattern.compile("(" + Matcher.quoteReplacement("it.polimi.deib.se2.mp.entity.User[ email=") + ")(.+)(" + Matcher.quoteReplacement(" ]") + ")");
        if(bound instanceof String){
            System.out.println("lamadonna" + bound);
            if(userRegex.matcher((String)bound).matches())
                this.searched = ((String) bound)
                        .replaceAll(Matcher.quoteReplacement("it.polimi.deib.se2.mp.entity.User[ email="), "")
                        .replaceAll(Matcher.quoteReplacement(" ]"), "");
            else
                this.searched = ((String) bound);
        } else
            this.searched = um.find(bound).getEmail();
        searchUser();
    }

    
    public void reverse(ComponentSystemEvent event) throws AbortProcessingException {
        if(FacesContext.getCurrentInstance().isPostback() || this.searched == null){}
        else{ 

        Query q = em.createNamedQuery("User.findByEmail");
        q.setParameter("email", this.searched);
        if (q.getResultList().isEmpty()){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Forbidden", "The user searched is not a valid user");
        RequestContext.getCurrentInstance().showMessageInDialog(message);
         ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
            try {
                context.redirect("index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(searchUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{reverted = (User) q.getResultList().get(0);
        if (!reverted.getCalendarCollection().iterator().next().getIsPublic()||reverted.getEmail().equals(um.getLoggedUser().getEmail())){
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
            try {
                context.redirect("index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(searchUser.class.getName()).log(Level.SEVERE, null, ex);
            }
         FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Forbidden", "" + "private");
        RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
        }
        }
    }
    
    private void throwError(FacesContext context, String errorText){
        try {
            context.getExternalContext().responseSendError(
                    403, errorText);
        } catch (IOException ex) {
            Logger.getLogger(EventConsistencyChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
}
