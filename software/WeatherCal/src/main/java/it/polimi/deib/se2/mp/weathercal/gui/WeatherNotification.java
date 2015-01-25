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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
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
import org.primefaces.json.JSONException;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author paolo
 */
@Named
@SessionScoped
public class WeatherNotification {

    WheaterChecker wc;
    private Long calid;
    private String searched;

    @PostConstruct
    public void init() {
        this.wc = new WheaterChecker();
    }

    /**
     * Creates a new instance of EventBean
     *
     * @throws java.io.IOException
     * @throws org.primefaces.json.JSONException
     */
    public void triggerNotificaiton() throws IOException, JSONException {

        wc.check();
        wc.checkParticipant();
        /* FacesContext context = FacesContext.getCurrentInstance();
         HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
         ServletContext sContext = request.getSession().getServletContext();
         LocalDateTime lastNot = (LocalDateTime) sContext.getAttribute("lastNotification");

         if (lastNot == null) {
         System.out.println("creata data");
         Date currDate = new Date();
         Instant instant = Instant.ofEpochMilli(currDate.getTime());
         LocalDateTime currLocal = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
         sContext.setAttribute("lastNotification", currLocal);
         } else {
         Date currDate = new Date();
         Instant instant = Instant.ofEpochMilli(currDate.getTime());
         LocalDateTime currLocal = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
         if (currLocal.isAfter(lastNot.plusMinutes(1))) {
         System.out.println("passati 5 minuti");
               
         wc.check();
         wc.checkParticipant();
         //   sContext.setAttribute("lastNotification", currLocal);
         } else {
         System.out.println("non passati 5 minuti");

         }
         }*/

    }

}
