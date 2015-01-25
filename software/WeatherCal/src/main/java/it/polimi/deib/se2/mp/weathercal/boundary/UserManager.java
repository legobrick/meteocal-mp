/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.boundary;

import it.polimi.deib.se2.mp.weathercal.entity.CalendarEntity;
import it.polimi.deib.se2.mp.weathercal.entity.Groups;
import it.polimi.deib.se2.mp.weathercal.entity.User;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author paolo
 */
@Stateless
public class UserManager extends AbstractFacade<User>{
    
    @Inject
    Logger logger;
    @PersistenceContext
    EntityManager em;

    @Inject
    Principal principal;

    public UserManager() {
        super(User.class);
    }
    
    @Override
    protected Logger getLogger() {
        return logger;
    }
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public User edit(User entity) {
        User retVal = super.edit(entity); //To change body of generated methods, choose Tools | Templates.
        em.flush();
        return retVal;
    }

    @Override
    public void remove(User entity) {
        super.remove(entity); //To change body of generated methods, choose Tools | Templates.
        em.flush();
    }

    public void save(User user) {
        
        super.create(user); //To change body of generated methods, choose Tools | Templates.
        em.flush();
       
        em.flush();
        //set user's group
        Query q = em.createNamedQuery("Groups.findByName");
        Groups g = (Groups) q
                    .setParameter("name", Groups.USERS)
                    .getSingleResult();
        Collection userGroups = user.getGroupsCollection();
        if(!userGroups.contains(g)){
            userGroups.add(g);
            em.merge(user);
        }
        Collection userCalendars = user.getCalendarCollection();
        if(userCalendars.size() < 1){
            //create user's default calendar
            CalendarEntity c = new CalendarEntity();
            c.setUserCollection(new ArrayList<User>(){{
                add(user);
            }});
            userCalendars.add(c);
            em.persist(c);
            em.flush();
        }
    }

    public void unregister() {
        em.remove(getLoggedUser());
    }
    
     public Long getCalByEmail(String mail) {
         Query q = em.createNamedQuery("User.findByEmail");
            q.setParameter("email",mail);
            User u= (User)q.getResultList().iterator().next();
            return u.getCalendarCollection().iterator().next().getId();
    }
     
    public User getLoggedUser() {
        return em.find(User.class, principal.getName());
    }
}
