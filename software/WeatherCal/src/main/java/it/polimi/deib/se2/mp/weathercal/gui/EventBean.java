/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.gui;

import it.polimi.deib.se2.mp.weathercal.entity.Event;
import javax.inject.Named;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author paolo
 */
@ManagedBean
@Named
public class EventBean {
     
    private Event event;
    private String centerGeoMap = "45.47803760760912, 9.229593882060279";

    /**
     * Creates a new instance of EventBean
     */
    public EventBean() {
    }
     
    @PostConstruct
    public void init() {
        event = new Event();
    }
 
    public String getCenterGeoMap() {
        return centerGeoMap;
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
    
}