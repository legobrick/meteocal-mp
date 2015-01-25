/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.gui;

import it.polimi.deib.se2.mp.weathercal.boundary.EventManager;
import it.polimi.deib.se2.mp.weathercal.boundary.OwnerManager;
import it.polimi.deib.se2.mp.weathercal.boundary.ParticipationManager;
import it.polimi.deib.se2.mp.weathercal.boundary.UserManager;
import it.polimi.deib.se2.mp.weathercal.boundary.WeatherConstraintManager;
import it.polimi.deib.se2.mp.weathercal.boundary.WeatherStateConstraintManager;
import it.polimi.deib.se2.mp.weathercal.entity.CalendarEntity;
import it.polimi.deib.se2.mp.weathercal.entity.Event;
import it.polimi.deib.se2.mp.weathercal.entity.LocalizedEvent;
import it.polimi.deib.se2.mp.weathercal.entity.Owner;
import it.polimi.deib.se2.mp.weathercal.entity.OwnerPK;
import it.polimi.deib.se2.mp.weathercal.entity.Participation;
import it.polimi.deib.se2.mp.weathercal.entity.ParticipationPK;
import it.polimi.deib.se2.mp.weathercal.entity.User;
import it.polimi.deib.se2.mp.weathercal.entity.WeatherConstraint;
import it.polimi.deib.se2.mp.weathercal.entity.WeatherStateConstraint;
import it.polimi.deib.se2.mp.weathercal.entity.WeatherStateConstraint.State;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

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
    OwnerManager ownerM;
    
    @EJB
    ParticipationManager participationM;
    
    @EJB
    UserManager um;

    @EJB
    WeatherStateConstraintManager wscManager;

    @EJB
    WeatherConstraintManager wcManager;
    private Event event;
    private LocalizedEvent lEvent;
    private WeatherConstraint weatherC;
    private boolean hasConstraint = false;
    private final String centerGeoMap = "45.47803760760912, 9.229593882060279";
    private final String zoomGeoMap = "16";
    private String createButtonText = "Create";
    private String pageTitle = "New Event";
    private Long editId;
     private List<User> invitedUsers=new <User>ArrayList();
    private List<State> states;

    /**
     * Creates a new instance of EventBean
     */
    public EventBean() {
        this.wscManager = new WeatherStateConstraintManager();
        this.wcManager = new WeatherConstraintManager();
        this.ownerM = new OwnerManager();
        this.participationM = new ParticipationManager();
    }

    @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id") == null) {
            event = new Event();
            lEvent = new LocalizedEvent();
            weatherC = new WeatherConstraint();
        } else {
            event = manager.find(Long.parseLong(
                    FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id")
            ));
            lEvent = LocalizedEvent.from(event, null);
            weatherC = event.getValueConstraints().isEmpty() ? new WeatherConstraint() : event.getValueConstraints().iterator().next();
        }
    }

    public List<User> getInvitedUsers() {
        Collection<Participation> oldParts = participationM.findByEvent(event);
        List<User> invitedUsers = new ArrayList<>();
        Long idc;
        boolean match;
        for(Participation p: oldParts){
            idc = p.getCalendar().getId();
            match = false;
            for(Owner o: event.getOwners()){
                match = idc.equals(o.getCalendar().getId());
                if(match) break;
            }
            if(!match) invitedUsers.add(p.getCalendar().getUserCollection().iterator().next());
        }
        return invitedUsers;
    }

    public void setInvitedUsers(List<User> inUs) {
        if(inUs == null) inUs = new ArrayList<>();
        Collection<Participation> oldParts = event.getParticipation();
        List<CalendarEntity> newCals = new ArrayList<>();
        Pattern userRegex = Pattern.compile("(" + Matcher.quoteReplacement("it.polimi.deib.se2.mp.entity.User[ email=") + ")(.+)(" + Matcher.quoteReplacement(" ]") + ")");
        for(Object u: inUs){
            if(u instanceof String){
                if(userRegex.matcher((String)u).matches())
                    u = ((String) u)
                            .replaceAll(Matcher.quoteReplacement("it.polimi.deib.se2.mp.entity.User[ email="), "")
                            .replaceAll(Matcher.quoteReplacement(" ]"), "");
                u = um.find(u);
            }
            for(CalendarEntity uc: ((User) u).getCalendarCollection())
                if(!newCals.contains(uc)) newCals.add(uc);
        }
        List<CalendarEntity> ownerCals = new ArrayList<>();
        for(Owner o: event.getOwners())
            ownerCals.add(o.getCalendar());
        List<Participation> toRemove = new ArrayList<>();
        List<CalendarEntity> retainCals = new ArrayList<>();
        for (Participation p : oldParts) {
            if (!(newCals.contains(p.getCalendar()) || ownerCals.contains(p.getCalendar()))) {
                toRemove.add(p);
                p.getCalendar().getParticipationCollection().remove(p);
                //participationM.remove(p);
            } else {
                retainCals.add(p.getCalendar());
            }
        }
        oldParts.removeAll(toRemove);
        newCals.removeAll(retainCals);
        ownerCals.removeAll(retainCals);
        Participation p;
        for (CalendarEntity cal : newCals) {
            p = new Participation(new ParticipationPK(cal.getId(), event.getId()), "nonletto");
            p.setCalendar(cal);
            p.setEvent(event);
            //manager.createParticipation(p);
            oldParts.add(p);
            cal.getParticipationCollection().add(p);
        }
        for (CalendarEntity cal : ownerCals) {
            p = new Participation(new ParticipationPK(cal.getId(), event.getId()), "si");
            p.setCalendar(cal);
            p.setEvent(event);
            //manager.createParticipation(p);
            oldParts.add(p);
            cal.getParticipationCollection().add(p);
        }
        event.setParticipation(oldParts);
    }

    public String getCenterGeoMap() {
        return centerGeoMap;
    }

    public String goToIndex() {
        return "/index?faces-redirect=true";
    }

    /**
     * @return the event
     */
    public LocalizedEvent getEvent() {
        return lEvent;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(LocalizedEvent event) {
        this.lEvent = event;
    }

    private String startDate;
    private String startTime;
    private ZoneOffset startZone;
    private String endDate;
    private String endTime;
    private ZoneOffset endZone;

    public String save() {
        //startZone = manager.getTimezone(manager.getTimezoneOffset(event, LocalDateTime.of(startDate, startTime)) - userTimezone);
        //endZone = manager.getTimezone(manager.getTimezoneOffset(event, LocalDateTime.of(endDate, endTime)) - userTimezone);
//        System.out.println("========================================");
//        System.out.println("Start date: "+startDate+"\tendDate: "+endDate);
//        System.out.println("Start date: "+startTime+"\tendDate: "+endTime);
//        System.out.println("========================================");
//        System.out.println("Start datetime: "+ZonedDateTime.of(startDate, startTime, ZoneId.ofOffset("UTC", manager.getTimezone(userTimezone))).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
//        System.out.println("Start enddatetime:" +ZonedDateTime.of(endDate, endTime, ZoneId.ofOffset("UTC", manager.getTimezone(userTimezone))).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
//        System.out.println("========================================");

        lEvent.setLocalDatesTogether(
                new Date(
                        Integer.parseInt(startDate.substring(startDate.lastIndexOf("/") + 1)) - 1900,
                        Integer.parseInt(startDate.substring(startDate.indexOf("/") + 1, startDate.lastIndexOf("/"))) - 1,
                        Integer.parseInt(startDate.substring(0, startDate.indexOf("/"))),
                        Integer.parseInt(startTime.substring(0, startTime.indexOf(":"))),
                        Integer.parseInt(startTime.substring(startTime.indexOf(":") + 1)), 0
                ),
                new Date(
                        Integer.parseInt(endDate.substring(endDate.lastIndexOf("/") + 1)) - 1900,
                        Integer.parseInt(endDate.substring(endDate.indexOf("/") + 1, endDate.lastIndexOf("/"))) - 1,
                        Integer.parseInt(endDate.substring(0, endDate.indexOf("/"))),
                        Integer.parseInt(endTime.substring(0, endTime.indexOf(":"))),
                        Integer.parseInt(endTime.substring(endTime.indexOf(":") + 1)), 0
                )
        );
        event = lEvent.toServerEvent();
        if (hasConstraint) {
            weatherC.setEvent(event);
            this.setValueConstraints(weatherC);
        } else {
            weatherC.setEvent(null);
            this.setValueConstraints(null);
        }
        Collection<WeatherStateConstraint> wsc = event.getStateConstraints();
        if (wsc == null) {
            wsc = new ArrayList();
            event.setStateConstraints(wsc);
        }
        Collection<WeatherStateConstraint> toRemove = new ArrayList<>();
        Collection<State> yetPresent = new ArrayList<>();
        for (WeatherStateConstraint w : wsc) {
            if (!states.contains(w.toState())) {
                toRemove.add(w);
                wscManager.remove(w);
            } else {
                yetPresent.add(w.toState());
            }
        }
        wsc.removeAll(toRemove);
        final Collection<WeatherStateConstraint> wscSubset = wsc;
        for (State s : states) {
            if (yetPresent.contains(s)) {
                continue;
            }
            WeatherStateConstraint w;
            w = new WeatherStateConstraint(event, s);
            w.setEvent(event);
            wscSubset.add(w);
        }
        System.out.println("informazione evento nome:" + event.getName() + " posto " + event.getPlaceDescription() + " ");
        if (editId == null) {
            manager.create(event);
            
            CalendarEntity c = um.getLoggedUser().getCalendarCollection().iterator().next();
            Participation p = new Participation(c.getId(), event.getId(), pageTitle);
            //participationM.create(p);
            event.getParticipation().add(p);
            c.getParticipationCollection().add(p);

            Owner o=new Owner();
            o.setEvent(event);
            o.setCalendar(c);
            OwnerPK ok=new OwnerPK();
            ok.setIdCalendar(c.getId());
            o.setOwnerPK(ok);
            ok.setIdEvent(event.getId());
            //ownerM.create(o);
            c.getOwnerCollection().add(o);
            event.getOwners().add(o);
            //manager.creatOwner(um.getLoggedUser(), event);
            //this.saveParticipation();
        } else {
            event.setId(editId);
            //saveParticipation();
        }
        manager.edit(event);
        RequestContext.getCurrentInstance().showMessageInDialog(
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Event submitted", "Event successfully " + (editId == null ? "created." : "modified."))
        );

        return "index?faces-redirect=true";
    }

    public String deleteEvent() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        ServletContext sContext = request.getSession().getServletContext();
        editId = (Long) sContext.getAttribute("pressedEv");
        manager.removeById(editId);
        sContext.removeAttribute("pressedEv");
        RequestContext.getCurrentInstance().showMessageInDialog(
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Event submitted", "Event successfully deleted.")
        );
        return "index?faces-redirect=true";
    }

//    public void saveParticipation() {
//        Collection<Participation> oldParts = event.getParticipation();
//        List<CalendarEntity> newCals = new ArrayList<>();
//        Pattern userRegex = Pattern.compile("(" + Matcher.quoteReplacement("it.polimi.deib.se2.mp.entity.User[ email=") + ")(.+)(" + Matcher.quoteReplacement(" ]") + ")");
//        for(Object u: this.invitedUsers){
//            if(u instanceof String){
//                if(userRegex.matcher((String)u).matches())
//                    u = ((String) u)
//                            .replaceAll(Matcher.quoteReplacement("it.polimi.deib.se2.mp.entity.User[ email="), "")
//                            .replaceAll(Matcher.quoteReplacement(" ]"), "");
//                u = um.find(u);
//            }
//            for(CalendarEntity uc: ((User) u).getCalendarCollection())
//                if(!newCals.contains(uc)) newCals.add(uc);
//        }
//        List<CalendarEntity> ownerCals = new ArrayList<>();
//        for(Owner o: event.getOwners())
//            ownerCals.add(o.getCalendar());
//        List<Participation> toRemove = new ArrayList<>();
//        List<CalendarEntity> retainCals = new ArrayList<>();
//        for (Participation p : oldParts) {
//            if (!(newCals.contains(p.getCalendar()) || ownerCals.contains(p.getCalendar()))) {
//                toRemove.add(p);
//                p.getCalendar().getParticipationCollection().remove(p);
//                //manager.removeParticipation(p);
//            } else {
//                retainCals.add(p.getCalendar());
//            }
//        }
//        oldParts.removeAll(toRemove);
//        newCals.removeAll(retainCals);
//        ownerCals.removeAll(retainCals);
//        Participation p;
//        for (CalendarEntity cal : newCals) {
//            p = new Participation(new ParticipationPK(cal.getId(), event.getId()), "nonletto");
//            p.setCalendar(cal);
//            p.setEvent(event);
//            //manager.createParticipation(p);
//            oldParts.add(p);
//            cal.getParticipationCollection().add(p);
//        }
//        for (CalendarEntity cal : ownerCals) {
//            p = new Participation(new ParticipationPK(cal.getId(), event.getId()), "si");
//            p.setCalendar(cal);
//            p.setEvent(event);
//            //manager.createParticipation(p);
//            oldParts.add(p);
//            cal.getParticipationCollection().add(p);
//        }
//        event.setParticipation(oldParts);
//    }
    
    public void loadParticipation(){
    }

    public void addUser(Event evt, CalendarEntity cal, String av) {
        ParticipationPK p = new ParticipationPK();
        p.setIdCalendar(cal.getId());
        p.setIdEvent(evt.getId());
        Participation prova = new Participation();
        prova.setParticipationPK(p);
        prova.setAvailability(av);
        manager.invitaUser(prova);
        cal.getParticipationCollection().add(prova);
        evt.getParticipation().add(prova);
    }
  
    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
        if (wcs == null) {
            if (valueConstraint != null) {
                event.setValueConstraints(new ArrayList<WeatherConstraint>() {
                    {
                        add(valueConstraint);
                    }
                });
            }
        } else {
            if (valueConstraint == null) {
                for (WeatherConstraint wc : wcs) {
                    wcManager.remove(wc); //porcodio
                }
                wcs.clear();
            } else {
                //remove the deleted
                List<WeatherConstraint> toRemove = new ArrayList<>();
                for (WeatherConstraint wc : wcs) {
                    if (!wc.equals(valueConstraint)) {
                        toRemove.add(wc);
                        wcManager.remove(wc);
                    }
                }
                wcs.removeAll(toRemove);
                //add the missing
                if (!wcs.contains(valueConstraint)) {
                    wcs.add(valueConstraint);
                }
            }
        }
    }

    /**
     * @param editingEvent the editingEvent to set
     */
    private void setEditingEvent(Event editingEvent) {
        if (editingEvent == null) {
            return;
        }
        createButtonText = "Modify";
        pageTitle = "Edit Event";
        RequestContext requestContextInstance = RequestContext.getCurrentInstance();
        Collection<WeatherConstraint> wcs = event.getValueConstraints();
        hasConstraint = !wcs.isEmpty();
        weatherC = hasConstraint ? wcs.iterator().next() : new WeatherConstraint();
        LocalDateTime eventStart = event.getStart();
        ZonedDateTime startZdt = ZonedDateTime.of(eventStart, ZoneId.of("UTC"));//.withZoneSameInstant(
//                manager.getTimezone(manager.getTimezoneOffset(
//                                event, LocalDateTime.of(eventStart.toLocalDate(), eventStart.toLocalTime()))
//                )
//        );
        requestContextInstance.execute(String.format("startDT='%s'", startZdt.toLocalDateTime().toString()));
        LocalDateTime eventEnd = event.getEnd();
        ZonedDateTime endZdt = ZonedDateTime.of(eventEnd, ZoneId.of("UTC"));//.withZoneSameInstant(
//                manager.getTimezone(manager.getTimezoneOffset(
//                                event, LocalDateTime.of(eventEnd.toLocalDate(), eventEnd.toLocalTime()))
//                )
//        );
        requestContextInstance.execute(String.format("endDT='%s'", endZdt.toLocalDateTime().toString()));
        states = new ArrayList<>();
        event.getStateConstraints().iterator().forEachRemaining(
                st -> states.add(st.toState())
        );
        lEvent = LocalizedEvent.from(event, ZoneId.of("UTC"));
        //loadParticipation();
        startDate = "01/01/1970";
        startTime = "00:00";
        endDate = "01/01/1970";
        endTime = "00:00";
    }

    /**
     * @return the createButtonText
     */
    public String getCreateButtonText() {
        return createButtonText;
    }

    /**
     * @return the pageTitle
     */
    public String getPageTitle() {
        return pageTitle;
    }

    /**
     * @return the editId
     */
    public Long getEditId() {
        return editId;
    }

    /**
     * @param editId the editId to set
     */
    public void setEditId(Long editId) {
        this.editId = editId;
        if (editId == null) {
            return;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        ServletContext sContext = request.getSession().getServletContext();
        sContext.setAttribute("pressedEv", this.editId);
        event = manager.find(editId);
        weatherC = event.getValueConstraints().isEmpty() ? new WeatherConstraint() : event.getValueConstraints().iterator().next();
        setEditingEvent(event);
    }
}
