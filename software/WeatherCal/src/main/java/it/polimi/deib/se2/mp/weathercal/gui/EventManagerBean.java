/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.gui;

import it.polimi.deib.se2.mp.weathercal.boundary.AbstractFacade;
import it.polimi.deib.se2.mp.weathercal.boundary.EventManager;
import it.polimi.deib.se2.mp.weathercal.boundary.UserManager;
import it.polimi.deib.se2.mp.weathercal.entity.CalendarEntity;
import it.polimi.deib.se2.mp.weathercal.entity.Event;
import it.polimi.deib.se2.mp.weathercal.entity.Owner;
import it.polimi.deib.se2.mp.weathercal.entity.Participation;
import it.polimi.deib.se2.mp.weathercal.entity.User;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Transient;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author paolo
 */
@Named
@ViewScoped
//@RequestScoped

public class EventManagerBean implements Serializable {
    
    @Inject
    private transient Logger logger;

    @PersistenceContext
    EntityManager em;
    
    AbstractFacade ef = new AbstractFacade(Event.class) {

        @Override
        protected Logger getLogger() {
            return Logger.getLogger("cielcio");
        }

        @Override
        protected EntityManager getEntityManager() {
            return em;
        }
    };

    @EJB
    UserManager um;
    @EJB
    EventManager evm;

    private boolean lettoNonLetto;
    private Event selectEvent;
    private String dateSt;
    private String dateEn;
    private ScheduleModel lazyModel2;
    private ScheduleModel lazySearch;
    private ScheduleEvent event = new DefaultScheduleEvent();
    
    @Inject
    private Logger logger;

    public EventManagerBean() {

    }

    public String getDateSt() {
        return dateSt;
    }

    public String getDateEn() {
        return dateEn;
    }

    public ScheduleModel getLazyModel2() {
        return lazyModel2;
    }

    public void setLazyModel2(ScheduleModel eventModel) {
        this.lazyModel2 = eventModel;
    }

    public ScheduleModel getLazySearch() {
        return lazySearch;
    }

    public void setLazySearch(ScheduleModel eventModel) {
        this.lazySearch = eventModel;
    }

    @PostConstruct
    public void init() {

        if (um.getLoggedUser() != null) {

            this.lazyModel2 = lazySchedule(userCalendarId(), true, "output");
            this.lazySearch = searchedCal();

        }
    }

    public ScheduleModel lazySchedule(Long calId, boolean soloEventiPubblici, String output) {
        ScheduleModel lazyModel = new LazyScheduleModel() {
            @Override
            public void loadEvents(Date start, Date end) {
                int n;

                for (n = 0; n < allEventByNotAvailability("si", calId, false).size(); n++) {
                    Event e = allEventByNotAvailability("si", calId, false).get(n);
                    Instant startInstant = e.getStart().atZone(ZoneId.systemDefault()).toInstant();
                    Date startDate = Date.from(startInstant);

                    Instant endInstant = e.getEnd().atZone(ZoneId.systemDefault()).toInstant();
                    Date endDate = Date.from(endInstant);
                    String colore;
                    Long id = e.getId();
                    if (isOwner(id,um.getLoggedUser(), ef)) {
                        colore = "empowner";
                    } else {

                        Query q = em.createNamedQuery("Participation.findByIdCalendarandIdEvent");
                        q.setParameter("idCalendar", calId);//devo passare il parametro
                        q.setParameter("idEvent", id);
                        if (q.getResultList().size() > 0) {
                            Participation part = (Participation) q.getResultList().get(0);
                            if (part.getAvailability().equals("si")) {
                                colore = "empyes";
                            } else if (part.getAvailability().equals("forse")) {
                                colore = "empmaybe";
                            } else if (part.getAvailability().equals("letto")) {
                                colore = "empletto";
                            } else {
                                colore = "empno";
                            }

                        } else {
                            colore = "empno";
                        }
                    }
                    //colore="empno";
                    Instant instant = Instant.ofEpochMilli(start.getTime());
                    LocalDateTime startCalDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    Instant instant2 = Instant.ofEpochMilli(end.getTime());
                    LocalDateTime endCalDate = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());

                    if (e.getStart().isAfter(startCalDate) && e.getStart().isBefore(endCalDate)) {
                        System.out.println("evento " + e.getName() + " " + colore);
                        DefaultScheduleEvent evento = new DefaultScheduleEvent(e.getDescription(), startDate, endDate, colore);
                        evento.setData(e);
                        addEvent(evento);
                    }

                }

            }
        };

        return lazyModel;
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

        return this.lazySchedule(this.userCalendarId(), false, "ciao");
    }

    public List<Event> nonLetti(String availability, boolean lista) {

        return this.allEventByAvailability(availability, this.userCalendarId(), lista);

    }

    public List<Event> loggedEventUsr(String availability, boolean lista) {

        return this.allEventByNotAvailability(availability, this.userCalendarId(), lista);
    }

    public List<Event> notificationUser(String av) {
        List<Event> allUserEv = new <Event>ArrayList();
        Query q = em.createNamedQuery("Participation.findByIdCalendarandAvailability");
        q.setParameter("idCalendar", this.userCalendarId());
        q.setParameter("av", av);
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
    }

    public ScheduleModel searchedCal() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        ServletContext sContext = request.getSession().getServletContext();
        Long id = (Long) sContext.getAttribute("selectedEvent");
        if (id == null) {
            return null;
        } else {
            return this.lazyScheduleSearch(id, true, "c");
        }
    }

    public ScheduleModel lazyScheduleSearch(Long calId, boolean soloEventiPubblici, String output) {

        ScheduleModel lazyModel2 = new LazyScheduleModel() {
            @Override
            public void loadEvents(Date start, Date end) {
                int n;

                for (n = 0; n < allEventByAvailability("si", calId, false).size(); n++) {
                    Event e = allEventByAvailability("si", calId, false).get(n);
                    Instant startInstant = e.getStart().atZone(ZoneId.systemDefault()).toInstant();
                    Date startDate = Date.from(startInstant);

                    Instant endInstant = e.getEnd().atZone(ZoneId.systemDefault()).toInstant();
                    Date endDate = Date.from(endInstant);

                    //colore="empno";
                    Instant instant = Instant.ofEpochMilli(start.getTime());
                    LocalDateTime startCalDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    Instant instant2 = Instant.ofEpochMilli(end.getTime());
                    LocalDateTime endCalDate = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());

                    if (e.getStart().isAfter(startCalDate) && e.getStart().isBefore(endCalDate)) {

                        DefaultScheduleEvent evento = new DefaultScheduleEvent(e.getDescription(), startDate, endDate, "empowner");
                        if (soloEventiPubblici == true) {

                            addEvent(evento);
                            evento.setData(e);

                        } else {
                            addEvent(evento);
                        }
                    }

                }

            }
        };

        return lazyModel2;
    }

    public List<Event> allEventByNotAvailability(String availability, Long CalId, boolean lista) {
        Date currDate = new Date();
        Instant instant = Instant.ofEpochMilli(currDate.getTime());
        LocalDateTime currLocal = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        List<Event> allUserEv = new <Event>ArrayList();
        Query q = em.createNamedQuery("Participation.findByIdCalendarandNotAvailability");
        q.setParameter("idCalendar", CalId);
        q.setParameter("av", "nonletto");
        int size = q.getResultList().size();
        if (size == 0) {

        } else {
            for (int n = 0; n < size; n++) {
                Participation part = (Participation) q.getResultList().get(n);
                long idEv = part.getParticipationPK().getIdEvent();

                Query p = em.createNamedQuery("Event.findById");
                p.setParameter("id", idEv);
                Event e = (Event) p.getResultList().get(0);

                if (lista) {//sapere se caricare o no gli eventi passati
                    if (e.getStart().isAfter(currLocal)) {
                        allUserEv.add(e);//carico nella scroll solo gli evnti dopo la data di oggi

                    }

                } else {

                    allUserEv.add(e);
                }
            }
        }
        return allUserEv;
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

                if (lista) {//sapere se caricare o no gli eventi passati
                    if (e.getStart().isAfter(currLocal)) {
                        allUserEv.add(e);//carico nella scroll solo gli evnti dopo la data di oggi

                    }

                } else {

                    allUserEv.add(e);
                }
            }
        }
        return allUserEv;
    }

    public long userCalendarId() {
        if (um.getLoggedUser().getCalendarCollection().isEmpty()) {
            return 0;
        } else {
            CalendarEntity cal = (CalendarEntity) um.getLoggedUser().getCalendarCollection().iterator().next();
            return cal.getId();
        }

    }

    public void onEventSelectSearch(SelectEvent selectEventt) throws IOException {
        event = (ScheduleEvent) selectEventt.getObject();
        Event e = (Event) event.getData();

        if (e.getIsPublic()) {
            this.selectEvent = e;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            this.dateSt = e.getStart().format(formatter);
            this.dateEn = e.getEnd().format(formatter);
        } else {
            this.selectEvent = new Event();
            this.selectEvent.setDescription("Is Private");
            this.selectEvent.setPlaceDescription("Is Private");
            this.dateEn = "Is Private";
            this.dateSt = "Is Private";
        }

        System.out.println("non esiste" + e.getName() + " " + lazyModel2.getEventCount());
    }

    public void onEventSelect(SelectEvent selectEventt) throws IOException {
        event = (ScheduleEvent) selectEventt.getObject();
        Event e = (Event) event.getData();
        this.selectEvent = e;
        if (selectEventt.getObject() == null) {
            System.out.println("non nullo");

        }
        System.out.println("non esiste" + e.getName() + " " + lazyModel2.getEventCount());
    }

    public void onDateSelect(SelectEvent e) {
        Date date = (Date) e.getObject();
        event = new DefaultScheduleEvent("", date, date);
        System.out.println("date");
    }

    public static boolean isOwner(Long idEv,User user, AbstractFacade af) {
        Collection<Owner> owners = ((Event) af.find(idEv)).getOwners();
        if (owners.isEmpty()) 
            return false;
        Owner owncal = (Owner) owners.iterator().next();
        Long owncalid = owncal.getOwnerPK().getIdCalendar();
        if (owncal.getCalendar().equals(user.getCalendarCollection().iterator().next())) {
            return true;
        } else {
            return false;
        }
    }
    
    
    
    public String editEvent(){
        Long idEv = Long.valueOf(
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        logger.log(Level.FINER, "Opening edit page for event " + idEv);
        if (EventManagerBean.isOwner(idEv, um.getLoggedUser(), ef)){
//            return "/create_event?faces-redirect=true&amp;includeViewParams=true";
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(String.format("create_event.xhtml?evt=%s", idEv));
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
          return "";
        }
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "NO", "You are not allowed to edit this event, since you are not an owner.");
        RequestContext.getCurrentInstance().showMessageInDialog(message);
        return "";
    }

    public void owner() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String action = params.get("action");
        Long idEv = Long.parseLong(action);

        if (this.isOwner(idEv,um.getLoggedUser(), ef)) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "SI", "è tuo");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "NO", "non è tuo");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

        System.out.println("sssss" + action);
    }

    public void changeAvailability(String availability) {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String action = params.get("action");
        Long idEv = Long.parseLong(action);
        Query p = em.createNamedQuery("Participation.findByIdCalendarandIdEvent");
        p.setParameter("idEvent", idEv);
        p.setParameter("idCalendar", this.userCalendarId());

        Participation changepart = (Participation) p.getResultList().get(0);
        evm.changeAvailability(availability, changepart);
        RequestContext.getCurrentInstance().update("form:next_event");
        //  em.merge(changepart);
        //System.out.println("sssss" + changepart.getAvailability());
    }

    public String numberOfNot(){
         Query q = em.createNamedQuery("Participation.findByIdCalendarandAvailability");
        q.setParameter("idCalendar", this.userCalendarId());
        q.setParameter("av", "nonletto");
        int size = q.getResultList().size();
        if (size>0) return "Notifications("+size+")";
        else return "Notifications";
    }
    public void eventoLetto() {
        Query q = em.createNamedQuery("Participation.findByIdCalendarandAvailability");
        q.setParameter("idCalendar", this.userCalendarId());
        q.setParameter("av", "nonletto");
        int size = q.getResultList().size();

        if (size == 0) {

        }
        else{
        for (int n = 0; n < size; n++) {
            Participation part = (Participation) q.getResultList().get(0);
            System.out.println("Numeri non letti:" + size + part.getParticipationPK().getIdEvent());
            evm.changeAvailability("letto", part);
        }
            
        }
    }

    public void addEvent(ActionEvent actionEvent) {

    }

    public void changeCalendarVisibility() {
        CalendarEntity cal = (CalendarEntity) um.getLoggedUser().getCalendarCollection().iterator().next();
        if (!cal.getIsPublic()) {
            System.out.println("part true");
            evm.changCalVisibility(true, cal);
        } else {
            evm.changCalVisibility(false, cal);
            System.out.println("vis false");
        }
        RequestContext.getCurrentInstance().update("pub:pubButton");
    }

    public String calVisibility() {
        CalendarEntity cal = (CalendarEntity) um.getLoggedUser().getCalendarCollection().iterator().next();
        if (!cal.getIsPublic()) {
            return "ui-icon-close";
        } else {
            return "ui-icon-check";
        }
    }
    public String calVisibilityTit() {
        CalendarEntity cal = (CalendarEntity) um.getLoggedUser().getCalendarCollection().iterator().next();
        if (!cal.getIsPublic()) {
            return "Private";
        } else {
            return "Public";
        }
    }

    public boolean isLettoNonLetto() {
        return lettoNonLetto;
    }

    public void setLettoNonLetto(boolean lettoNonLetto) {
        this.lettoNonLetto = lettoNonLetto;
    }

    public void changeNotificationToRead() {
         
        if (!lettoNonLetto) {
            System.out.println("cambaito");
            this.eventoLetto();
            RequestContext.getCurrentInstance().update("h:mySchedule");
            RequestContext.getCurrentInstance().update("header-form:notification_event");
            RequestContext.getCurrentInstance().update("header-form:notification-button");
            RequestContext.getCurrentInstance().update("f:new-event");
            RequestContext.getCurrentInstance().update("form:next_event");
        }
    }
}
