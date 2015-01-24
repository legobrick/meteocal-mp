/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.gui;

import it.polimi.deib.se2.mp.weathercal.boundary.EventManager;
import it.polimi.deib.se2.mp.weathercal.boundary.UserManager;
import it.polimi.deib.se2.mp.weathercal.entity.Event;
import it.polimi.deib.se2.mp.weathercal.entity.ForecastResponse;
import it.polimi.deib.se2.mp.weathercal.entity.Owner;
import it.polimi.deib.se2.mp.weathercal.entity.Participation;
import java.io.BufferedReader;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import it.polimi.deib.se2.mp.weathercal.entity.WeatherConstraint;
import it.polimi.deib.se2.mp.weathercal.entity.WeatherStateConstraint;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import static javax.servlet.SessionTrackingMode.URL;
import static org.omnifaces.util.Ajax.data;
import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;
import sun.net.www.http.HttpClient;

/**
 *
 * @author miglie
 */
@Named
@ViewScoped
@RequestScoped
@SessionScoped
public class WheaterChecker implements Serializable {

    @PersistenceContext
    EntityManager em;

    @EJB
    UserManager um;

    ForecastResponse fr = new ForecastResponse();

    public static String getWeather(float lat, float lon, int cnt) {

        HttpURLConnection con = null;
        InputStream is = null;

        try {
            con = (HttpURLConnection) (new URL("http://api.openweathermap.org/data/2.5/forecast/?lat=" + lat + "&lon=" + lon + "&cnt=" + cnt + "")).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\r\n");
            }

            is.close();
            con.disconnect();
            return buffer.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
            }
        }

        return null;
    }
    public static String getWeather16(float lat, float lon, int cnt) {

        HttpURLConnection con = null;
        InputStream is = null;

        try {
            con = (HttpURLConnection) (new URL("http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + lat + "&lon=" + lon + "&cnt=" + cnt + "")).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\r\n");
            }

            is.close();
            con.disconnect();
            return buffer.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
            }
        }

        return null;
    }

    public void checkParticipant() {
        List<Event> checkEv = this.getEventToCheck(false, 1);
        System.out.println("In check + eventi " + checkEv.size());
    }

    public void check() throws JSONException {

        List<Event> checkEv = this.getEventToCheck(true, 5);

        double nexttemp = 0;
        if (!checkEv.isEmpty()) {
            for (int i = 0; i < checkEv.size(); i++) {
                String attention = "";
                Event e = checkEv.get(i);
                fr.weatherJson(e);
                
                nexttemp = fr.getTemperatura();
                float state = fr.getWeatherState();
                System.out.println("In check " + nexttemp + state + fr.getState());
                if (!e.getValueConstraints().isEmpty()) {
                    WeatherConstraint temperatura = e.getValueConstraints().iterator().next();
                    if (temperatura.getIsTemperatureLowerThan() == true && nexttemp < temperatura.getTemperature().floatValue()) {

                    } else if (temperatura.getIsTemperatureLowerThan() == true && nexttemp > temperatura.getTemperature().floatValue()) {
                        attention = "Temperature is higher then the desired one";

                    } else if (temperatura.getIsTemperatureLowerThan() == false && nexttemp > temperatura.getTemperature().floatValue()) {

                    } else {
                        attention = "The temperature is lower then the desired one";
                    }

                }
               boolean weather=false;
                if (!e.getStateConstraints().isEmpty()) {
                    Collection<WeatherStateConstraint> collState = e.getStateConstraints();
                    System.out.println("vincoli: " + e.getStateConstraints().size());
                    for (WeatherStateConstraint sta : collState) {
                        if (!sta.getWeatherState().equals(fr.getState().toString())) {
                            System.out.println("Diverse" + sta.toString());
                        } else {
                            weather=true;
                            System.out.println("Uguale");
                        }
                    }
                   
                    if (!weather){
                    attention=attention+". Also the state is different from the one you specified.";
                    }
                    
                    /* for (int z = 0; z < collState.size(); z++) {
                        WeatherStateConstraint sta = collState.iterator().next();
                        if (!sta.getWeatherState().equals(fr.getState().toString())) {
                            System.out.println("Diverse" + sta.toString());
                        } else {
                            System.out.println("Uguale");
                        }

                        //   WeatherStateConstraint vincoli=checkEv.get(i).getStateConstraints().iterator().next();
                    }*/
                    if (attention.equals("")) {
                    } else {
                         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
                         String giorno;
                         if(fr.weatherDailyNext(e).format(formatter)==null){
                         giorno=" Sorry no closest day matches with your desired constraint.";
                         }
                         else giorno=fr.weatherDailyNext(e).format(formatter);
                         
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Attenzione", "The weather forecast for the event " + e.getName() + " scheduled for " + e.getStart().getYear() + "/" + e.getStart().getMonthValue() + "/" + e.getStart().getDayOfMonth() + ". " + attention + " Do you want to postpone the event? "+giorno);
                        RequestContext.getCurrentInstance().showMessageInDialog(message);
                        
                          }
                // WeatherConstraint temperatura=checkEv.get(i).getValueConstraints().iterator().next();
                    // WeatherStateConstraint vincoli=checkEv.get(i).getStateConstraints().iterator().next();
                }
            }
        }
    }

    public List<Event> getEventToCheck(boolean participantOrOwner, int deltadays) {

        List<Event> eventToCheck = new <Event>ArrayList();

        Date currDate = new Date();
        Instant instant = Instant.ofEpochMilli(currDate.getTime());
        LocalDateTime currLocal = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        if (participantOrOwner) {
            Query p = em.createNamedQuery("Owner.findByIdCalendar");
            p.setParameter("idCalendar", um.getLoggedUser().getCalendarCollection().iterator().next().getId());
            List<Owner> eventOwned = p.getResultList();
            if (eventOwned.isEmpty()) {
            } else {
                for (int i = 0; i < p.getResultList().size(); i++) {
                    Query q = em.createNamedQuery("Event.findById");
                    q.setParameter("id", eventOwned.get(i).getOwnerPK().getIdEvent());
                    Event e = (Event) q.getResultList().get(0);
                    if (e.getStart().isAfter(currLocal) && e.getStart().isBefore(currLocal.plusDays(deltadays))) {
                        eventToCheck.add(e);
                    }
                }
            }
        } else {
            Query p = em.createNamedQuery("Participation.findByIdCalendarandAvailability");
            p.setParameter("idCalendar", um.getLoggedUser().getCalendarCollection().iterator().next().getId());
            p.setParameter("av", "si");
            List<Participation> eventParticipant = p.getResultList();
            if (eventParticipant.isEmpty()) {
            } else {
                for (int i = 0; i < p.getResultList().size(); i++) {
                    Query q = em.createNamedQuery("Event.findById");
                    q.setParameter("id", eventParticipant.get(i).getParticipationPK().getIdEvent());
                    Event e = (Event) q.getResultList().get(0);
                    if (e.getStart().isAfter(currLocal) && e.getStart().isBefore(currLocal.plusDays(deltadays))) {
                        eventToCheck.add(e);
                    }
                }
            }
        }
        return eventToCheck;
    }

}
