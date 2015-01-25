/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.gui;

import it.polimi.deib.se2.mp.weathercal.boundary.UserManager;
import it.polimi.deib.se2.mp.weathercal.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author paolo
 */
@Named
@RequestScoped
public class InvitationUser {

    @PersistenceContext
    EntityManager em;

    @EJB
    UserManager um;

    /**
     * Creates a new instance of EventBean
     * @param query
     * @return 
     */
    public List<User> completeUser(String query) {

        Query q = em.createNamedQuery("User.findAll");

        List<User> allUser = q.getResultList();

        List<User> filteredUser = new ArrayList<>();
        
        for (int i = 0; i < allUser.size(); i++) {
            User us = allUser.get(i);
            if (!us.getEmail().equals(um.getLoggedUser().getEmail())) {
                if (us.getEmail().toLowerCase().contains(query.toLowerCase()) ||
                        us.getUsername().toLowerCase().contains(query.toLowerCase())||
                        (us.getFirstName() + " " + us.getLastName()).toLowerCase().contains(query.toLowerCase())) {
                    filteredUser.add(us);
                }
            }
        }

        return filteredUser;
    }
    
    public List<User> completeUsers(String query) {

        Query q = em.createNamedQuery("User.findAll");

        List<User> allUser = q.getResultList();

        List<User> filteredUser = new ArrayList<User>();
        
        for (int i = 0; i < allUser.size(); i++) {
            User us = allUser.get(i);
            if (us.getEmail().equals(um.getLoggedUser().getEmail())) {

            } else {
                if (us.getEmail().toLowerCase().startsWith(query)) {
                    filteredUser.add(us);
                }
            }
        }

        return filteredUser;
    }

}
