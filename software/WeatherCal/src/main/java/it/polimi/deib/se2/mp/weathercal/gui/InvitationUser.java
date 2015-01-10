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
public class InvitationUser {

    @PersistenceContext
    EntityManager em;

    @EJB
    UserManager um;

    /**
     * Creates a new instance of EventBean
     */
    public List<User> completeUser(String query) {

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
