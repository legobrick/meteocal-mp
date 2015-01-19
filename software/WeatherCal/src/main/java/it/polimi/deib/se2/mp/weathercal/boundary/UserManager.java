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
public class UserManager {

    @PersistenceContext
    EntityManager em;

    @Inject
    Principal principal;

    public void save(User user) {
        em.persist(user);
       
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
            em.merge(user);
            em.flush();
        }
    }

    public void unregister() {
        em.remove(getLoggedUser());
    }

    public User getLoggedUser() {
        return em.find(User.class, principal.getName());
    }
    
}
