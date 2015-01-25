/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.boundary;

import it.polimi.deib.se2.mp.weathercal.entity.CalendarEntity;
import it.polimi.deib.se2.mp.weathercal.entity.Event;
import it.polimi.deib.se2.mp.weathercal.entity.Owner;
import it.polimi.deib.se2.mp.weathercal.entity.OwnerPK;
import it.polimi.deib.se2.mp.weathercal.entity.Participation;
import it.polimi.deib.se2.mp.weathercal.entity.TimeZoneResponse;
import it.polimi.deib.se2.mp.weathercal.entity.User;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author paolo
 */
@Stateless
public class EventManager extends AbstractFacade<Event>{
    
    private final static String GOOGLE_TIMEZONE_API = "https://maps.googleapis.com/maps/api/timezone/json?"
            + "location={latitude},{longitude}&timestamp={ts}";
    private Map<String, Object> params;

    @PersistenceContext
    EntityManager em;

    @EJB
    UserManager um;

    @Inject
    private Logger logger;
    
    private Client client;
    
    public EventManager() {
        super(Event.class);
        client = ClientBuilder.newClient();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    private TimeZoneResponse fetchTimezone(String lat, String lng, String ts) {
        params = new HashMap<String, Object>(){{
            put("latitude", lat);
            put("longitude", lng);
            put("ts", ts);
        }};
        return client.target(GOOGLE_TIMEZONE_API)
                .resolveTemplates(params)
                .request(MediaType.APPLICATION_JSON)
                .get(TimeZoneResponse.class);
    }
    
    /**
     *
     * Calculates the offset from the UTC for the locality set for the Event e at the inputed LocalDateTime ts.
     * 
     * @param e the interested Event
     * @param ts the inputed LocalDateTime
     * @return the minutes of the offset
     */
    public int getTimezoneOffset(Event e, LocalDateTime ts){
        TimeZoneResponse tz = fetchTimezone(
                e.getPlaceLatitude().toPlainString(),
                e.getPlaceLongitude().toPlainString(),
                Long.toString(ts.toEpochSecond(ZoneOffset.UTC))
        );
        return (tz.getDstOffset() + tz.getRawOffset()) / 60;
    }
    
    @Override
    public void create(Event event) {
        System.out.print("iltitolo "+event.getPlaceDescription());
        super.create(event);
        em.flush();
        em.refresh(event);
    }
    public void creatOwner(User u,Event e){
        Owner ok=new Owner();
        OwnerPK o=new OwnerPK();
        o.setIdEvent(e.getId());
        o.setIdCalendar(u.getCalendarCollection().iterator().next().getId());
        ok.setOwnerPK(o);
        e.getOwners().add(ok);
        em.persist(ok);
        em.flush();
           // o.setEvent(event);
            //o.setCalendar(c);
        
    }
    
    public void changeAvailability(String av,Participation changepart){
        changepart.setAvailability(av);
        em.merge(changepart);
        em.flush();
      //  em.merge(changepart);
        System.out.println("sssss" + changepart.getAvailability());
    }
    
    public void removeParticipation(Participation p){
        em.remove(p);
        em.flush();
    }
    
    public void createParticipation(Participation p){
        em.persist(p);
        em.flush();
    }
    
    public void changCalVisibility(boolean visibility,CalendarEntity cal){
    cal.setIsPublic(visibility);
    em.merge(cal);
    em.flush();
    }
    
    public void invitaUser(Participation p){
    em.merge(p);
    em.flush();
    }
    
    public List<Participation> getParticipationsForCalendarToEvent(CalendarEntity c, Event e){
        Query q = em.createNamedQuery("Participation.findByIdCalendarandIdEvent");
        q.setParameter("idCalendar", c.getId());
        q.setParameter("idEvent", e.getId());
        return q.getResultList();
    }
    
    public List<Participation> getParticipationsForUserToEvent(User u, Event e){
        return u.getCalendarCollection().size() == 0? new ArrayList():
            getParticipationsForCalendarToEvent(u.getCalendarCollection().iterator().next(), e);
    }
    
    public List<Event> tGetAll(){
        Query q = em.createNamedQuery("Event.findAll");
        return q.getResultList();
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    public Event edit(Event entity) {
        Event retVal = super.edit(entity); //To change body of generated methods, choose Tools | Templates.
        em.flush();
        return retVal;
    }

    @Override
    public void remove(Event entity) {
        super.remove(entity); //To change body of generated methods, choose Tools | Templates.
        em.flush();
    }
    public void removeById(long id){
     this.remove(find(id));
     em.flush();
    }
}
