/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.boundary;

import it.polimi.deib.se2.mp.weathercal.entity.Groups;
import it.polimi.deib.se2.mp.weathercal.entity.User;
import it.polimi.deib.se2.mp.weathercal.entity.Event;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
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
public class CalendarEventManager {

    @PersistenceContext
    EntityManager em;

    @Inject
    Principal principal;
    
    @EJB
    UserManager um;


    public void save(User user) {
        em.persist(user);
        em.flush();
        Query q = em.createNamedQuery("Groups.findByName");
        Groups g = (Groups) q
                    .setParameter("name", Groups.USERS)
                    .getSingleResult();
        if(!user.getGroupsCollection().contains(g)){
            user.getGroupsCollection().add(g);
            em.merge(user);
        }
    }

    public void unregister() {
        em.remove(getLoggedUser());
    }

    public User getLoggedUser() {
        return em.find(User.class, principal.getName());
    }
   
}
