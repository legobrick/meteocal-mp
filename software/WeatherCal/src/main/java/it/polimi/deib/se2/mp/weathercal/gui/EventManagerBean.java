/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.gui;

import it.polimi.deib.se2.mp.weathercal.boundary.EventManager;
import it.polimi.deib.se2.mp.weathercal.boundary.UserManager;
import it.polimi.deib.se2.mp.weathercal.entity.CalendarEntity;
import java.util.Calendar;
import it.polimi.deib.se2.mp.weathercal.entity.Event;
import it.polimi.deib.se2.mp.weathercal.entity.Owner;
import it.polimi.deib.se2.mp.weathercal.entity.Participation;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author paolo
 */
@ManagedBean
@Named
@SessionScoped
@RequestScoped
public class EventManagerBean {

    @PersistenceContext
    EntityManager em;

    @EJB
    UserManager um;

    @ManagedProperty("#{param.cal}")
    Long calId;

    private Event selectEvent;

    /**
     * Creates a new instance of EventManagerBean
     */
    private ScheduleEvent event = new DefaultScheduleEvent();

    public EventManagerBean() {
    }

    public Event getSelectEvent() {
        return selectEvent;
    }

    public void setSelectEvent(Event sel) {
        this.selectEvent = sel;
    }

    public String createNew() throws IOException {
        return "create_event?faces-redirect=true";
//        FacesContext.getCurrentInstance().getExternalContext().redirect("create_event");
    }

    public Long param() {
        String cal = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cal");
        System.out.println("questo è il strings s " + cal);
        return this.calId;

    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public void editAction() {

        String cal = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cal");
        System.out.println("questo è il strings s " + cal);
          //...

    }

    public ScheduleModel loggedEventiMese() {
        return this.eventiDelMese(this.userCalendarId());
    }

    public ScheduleModel eventiDelMese(Long calId) {
        String cal = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cal");
        /*  Long id=Long.parseLong(cal);
         calId=id;*/
 
     //  calId=this.calId;
         
         // calId=Long.parseLong(action);
        // FacesContext context=FacesContext.getCurrentInstance();
        // HttpServletRequest request=(HttpServletRequest) context.getExternalContext().getRequest();
        // calId=(Long)request.getAttribute("calid");
        ScheduleModel eventi = new DefaultScheduleModel();
        int n;
        for (n = 0; n < this.allEventByAvailability("si", calId, false).size(); n++) {

            Instant startInstant = this.allEventByAvailability("si", calId, false).get(n).getStart().atZone(ZoneId.systemDefault()).toInstant();
            Date startDate = Date.from(startInstant);

            Instant endInstant = this.allEventByAvailability("si", calId, false).get(n).getEnd().atZone(ZoneId.systemDefault()).toInstant();
            Date endDate = Date.from(endInstant);

            eventi.addEvent(new DefaultScheduleEvent(this.allEventByAvailability("si", calId, false).get(n).getDescription(), startDate, endDate));

        }
        return eventi;
    }

    public List<Event> loggedEventUsr(String availability) {
        return this.allEventByAvailability(availability, this.userCalendarId(), false);
    }

    public List<Event> allEventByAvailability(String availability, Long CalId, boolean lista) {

        Date currDate = new Date();
        Instant instant = Instant.ofEpochMilli(currDate.getTime());
        LocalDateTime currLocal = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        List<Event> allUserEv = new <Event>ArrayList();
        Query q = em.createNamedQuery("Participation.findByIdCalendarandAvailability");
        q.setParameter("idCalendar", CalId);
        q.setParameter("av", availability);
        int size = q.getResultList().size();
        if (size == 0) {

        } else {
            for (int n = 0; n < size; n++) {
                Participation part = (Participation) q.getResultList().get(n);
                long idEv = part.getParticipationPK().getIdEvent();

                Query p = em.createNamedQuery("Event.findById");
                p.setParameter("id", idEv);
                Event e = (Event) p.getResultList().get(0);

                if (lista) {
                    if (e.getStart().isAfter(currLocal)) {
                        allUserEv.add(e);//carico nella scroll solo gli evnti dopo la data di oggi
                    }

                } else {
                    allUserEv.add(e);
                }

            }
        }
        return allUserEv;

        /*CalendarEntity cal=(CalendarEntity) q.getResultList().get(0);
         long calId =cal.getId();
    
    
         return calId;*/
    }

    public long userCalendarId() {
        if (um.getLoggedUser().getCalendarCollection().size() == 0) {
            return 0;
        } else {
            CalendarEntity cal = (CalendarEntity) um.getLoggedUser().getCalendarCollection().iterator().next();
            return cal.getId();
        }

    }

    public void onEventSelect(SelectEvent selectEvent) {
        this.selectEvent = (Event) selectEvent.getObject();
    }

    public void owner() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String action = params.get("action");
        Long idEv = Long.parseLong(action);
        Query p = em.createNamedQuery("Owner.findByIdEvent");
        p.setParameter("idEvent", idEv);
        Owner owncal = (Owner) p.getResultList().get(0);
        Long owncalid = owncal.getOwnerPK().getIdCalendar();
        if (owncalid == this.userCalendarId()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "SI", "è tuo");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } else  {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "NO", "non è tuo");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

        System.out.println("sssss" + action);
    }
}
