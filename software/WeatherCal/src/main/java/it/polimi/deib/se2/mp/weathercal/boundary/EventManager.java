/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.boundary;

import it.polimi.deib.se2.mp.weathercal.entity.Event;
import it.polimi.deib.se2.mp.weathercal.entity.TimeZoneResponse;
import java.io.DataOutput;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.zone.ZoneRules;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
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
public class EventManager {
    
    private final static String GOOGLE_TIMEZONE_API = "https://maps.googleapis.com/maps/api/timezone/json?"
            + "location={latitude},{longitude}&timestamp={ts}";
    private Map<String, Object> params;

    @PersistenceContext
    EntityManager em;
    
    private Client client;
    
    public EventManager() {
        client = ClientBuilder.newClient();
    }
    
    private TimeZoneResponse fetchTimezone(String lat, String lng, String ts) {
        params = new HashMap<String, Object>(){{
            put("latitude", lat);
            put("longitude", lng);
            put("timestamp", ts);
        }};
        return client.target(GOOGLE_TIMEZONE_API)
                .resolveTemplates(params)
                .request(MediaType.APPLICATION_JSON)
                .get(TimeZoneResponse.class);
    }
    
    public ZoneId getTimezone(Event e, LocalDateTime ts) {
        TimeZoneResponse tz = fetchTimezone(
                e.getPlaceLatitude().toPlainString(),
                e.getPlaceLongitude().toPlainString(),
                Long.toString(ts.toEpochSecond(ZoneOffset.UTC))
        );
        return ZoneOffset.of(
                Float.toString((tz.getDstOffset() + tz.getRawOffset()) / 3600)
        );
    }

    public void save(Event event) {
        
        em.persist(event);
//        em.flush();
//        Query q = em.createNamedQuery("Groups.findByName");
//        Groups g = (Groups) q
//                    .setParameter("name", Groups.USERS)
//                    .getSingleResult();
//        if(!user.getGroupsCollection().contains(g)){
//            user.getGroupsCollection().add(g);
//            em.merge(user);
//        }
    }
    
    public List<Event> tGetAll(){
        Query q = em.createNamedQuery("Event.findAll");
        return q.getResultList();
    }
    
}
