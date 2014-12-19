/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.boundary;

import it.polimi.deib.se2.mp.weathercal.entity.Groups;
import it.polimi.deib.se2.mp.weathercal.entity.User;
import java.security.Principal;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
        if(!user.getGroupsCollection().contains(
                em.createNamedQuery("findByName")
                    .setParameter("name", Groups.USERS)
                    .getSingleResult()
        )){
            user.getGroupsCollection().add(
                (Groups) em.createNamedQuery("findByName")
                    .setParameter("name", Groups.USERS)
                    .getSingleResult());
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
