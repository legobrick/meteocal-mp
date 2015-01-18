/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.gui;

import it.polimi.deib.se2.mp.weathercal.boundary.EventManager;
import it.polimi.deib.se2.mp.weathercal.boundary.WeatherConstraintManager;
import it.polimi.deib.se2.mp.weathercal.boundary.WeatherStateConstraintManager;
import it.polimi.deib.se2.mp.weathercal.entity.Event;
import it.polimi.deib.se2.mp.weathercal.entity.WeatherConstraint;
import it.polimi.deib.se2.mp.weathercal.entity.WeatherStateConstraint;
import it.polimi.deib.se2.mp.weathercal.entity.WeatherStateConstraint.State;
import it.polimi.deib.se2.mp.weathercal.util.UtilTimeConverter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.inject.Inject;

/**
 *
 * @author paolo
 */
@RequestScoped
@Named
public class EventBean {
    
    @Inject
    private Logger logger;
    
    @EJB
    EventManager manager;
    
    @EJB
    WeatherStateConstraintManager wscManager;
    
    @EJB
    WeatherConstraintManager wcManager;
     
    private Event event;
    private WeatherConstraint weatherC;
    private boolean hasConstraint = false;
    private final String centerGeoMap = "45.47803760760912, 9.229593882060279";
    private final String zoomGeoMap = "16";
    
    private String userTimezone;
    
    private List<State> states;

    /**
     * Creates a new instance of EventBean
     */
    public EventBean() {
        this.wscManager = new WeatherStateConstraintManager();
        this.wcManager = new WeatherConstraintManager();
    }
     
    @PostConstruct
    public void init() {
        event = new Event();
        weatherC = new WeatherConstraint();
    }
    
    public String getCenterGeoMap() {
        return centerGeoMap;
    }
    public String goToIndex(){
        return "/index?faces-redirect=true";
    }

    /**
     * @return the event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(Event event) {
        this.event = event;
    }
    
    private LocalDate startDate;
    private LocalTime startTime;
    private ZoneOffset startZone;
    private LocalDate endDate;
    private LocalTime endTime;
    private ZoneOffset endZone;
    
    public String save() {
        int userTimezone = Integer.valueOf(this.userTimezone);
        startZone = manager.getTimezone(manager.getTimezoneOffset(event, LocalDateTime.of(startDate, startTime)) - userTimezone);
        event.setStart(ZonedDateTime.of(startDate, startTime, startZone).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
        endZone = manager.getTimezone(manager.getTimezoneOffset(event, LocalDateTime.of(endDate, endTime)) - userTimezone);
        event.setEnd(ZonedDateTime.of(endDate, endTime, endZone).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
        if(hasConstraint){
            this.setValueConstraints(weatherC);
            weatherC.setEvent(event);
        }
        Collection<WeatherStateConstraint> wsc = event.getStateConstraints();
        if(wsc == null){
            wsc = new ArrayList();
            event.setStateConstraints(wsc);
        }
        for(WeatherStateConstraint w: wsc){
            if(!states.contains(w.toState())){
                wsc.remove(w);
                wscManager.remove(w);
            }
        }
        for(State s: states){
            WeatherStateConstraint w = new WeatherStateConstraint(event, s);
            w.setEvent(event);
            wsc.add(w);
        }
        manager.create(event);
        return "";
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return UtilTimeConverter.localDateToUtilDate(startDate);
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = UtilTimeConverter.utilDateToLocalDate(startDate);
    }

    /**
     * @return the startTime
     */
    public Date getStartTime() {
        return UtilTimeConverter.localTimeToUtilDate(startTime);
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime) {
        this.startTime = UtilTimeConverter.utilDateToLocalTime(startTime);
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return UtilTimeConverter.localDateToUtilDate(endDate);
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = UtilTimeConverter.utilDateToLocalDate(endDate);
    }

    /**
     * @return the endTime
     */
    public Date getEndTime() {
        return UtilTimeConverter.localTimeToUtilDate(endTime);
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Date endTime) {
        this.endTime = UtilTimeConverter.utilDateToLocalTime(endTime);
    }

    /**
     * @return the states
     */
    public List<State> getStates() {
        return states;
    }

    /**
     * @param states the states to set
     */
    public void setStates(List<State> states) {
        this.states = states;
    }

    /**
     * @return the userTimezone
     */
    public String getUserTimezone() {
        return userTimezone;
    }

    /**
     * @param userTimezone the userTimezone to set
     */
    public void setUserTimezone(String userTimezone) {
        this.userTimezone = userTimezone;
    }

    /**
     * @return the zoomGeoMap
     */
    public String getZoomGeoMap() {
        return zoomGeoMap;
    }

    /**
     * @return the weatherC
     */
    public WeatherConstraint getWeatherC() {
        return weatherC;
    }

    /**
     * @param weatherC the weatherC to set
     */
    public void setWeatherC(WeatherConstraint weatherC) {
        this.weatherC = weatherC;
    }

    /**
     * @return the hasConstraint
     */
    public boolean getHasConstraint() {
        return hasConstraint;
    }

    /**
     * @param hasConstraint the hasConstraint to set
     */
    public void setHasConstraint(boolean hasConstraint) {
        this.hasConstraint = hasConstraint;
    }
    
    public void setValueConstraints(WeatherConstraint valueConstraint) {
        Collection<WeatherConstraint> wcs = event.getValueConstraints();
        if(wcs == null)
            event.setValueConstraints(new ArrayList<WeatherConstraint>(){{
                add(valueConstraint);
            }});
        else {
            if(!wcs.contains(valueConstraint)){
                wcs.stream().forEach((wc) -> {
                    wcManager.remove(wc);
                });
                wcs.clear();
                wcs.add(valueConstraint);
            }
        }
    }
}