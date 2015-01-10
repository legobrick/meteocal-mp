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
import it.polimi.deib.se2.mp.weathercal.entity.Participation;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
@RequestScoped
public class EventManagerBean {

    @PersistenceContext
    EntityManager em;

    @EJB
    UserManager um;
    /**
     * Creates a new instance of EventManagerBean
     */
    private ScheduleEvent event = new DefaultScheduleEvent();

    public EventManagerBean() {
    }

    public String createNew() throws IOException {
        return "create_event?faces-redirect=true";
//        FacesContext.getCurrentInstance().getExternalContext().redirect("create_event");
    }

    public ScheduleModel eventiDelMese() {
        ScheduleModel eventi = new DefaultScheduleModel();
        int n;
        for (n = 0; n < this.allEventByAvailability("si").size(); n++) {

            Instant startInstant = this.allEventByAvailability("si").get(n).getStart().atZone(ZoneId.systemDefault()).toInstant();
            Date startDate = Date.from(startInstant);

            Instant endInstant = this.allEventByAvailability("si").get(n).getEnd().atZone(ZoneId.systemDefault()).toInstant();
            Date endDate = Date.from(endInstant);

            eventi.addEvent(new DefaultScheduleEvent(this.allEventByAvailability("si").get(n).getDescription(), startDate, endDate));

        }
        return eventi;
    }

    private Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);

        return calendar;
    }

    private Date today1Pm() {
        Calendar t = (Calendar) today();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 1);

        return t.getTime();
    }

    private Date today3Pm() {
        Calendar t = (Calendar) today();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 3);

        return t.getTime();
    }

    public List<Event> allEventByAvailability(String availability) {

        List<Event> allUserEv = new <Event>ArrayList();
        Query q = em.createNamedQuery("Participation.findByIdCalendarandAvailability");
        q.setParameter("idCalendar", this.userCalendarId());
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

                allUserEv.add(e);

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

}
