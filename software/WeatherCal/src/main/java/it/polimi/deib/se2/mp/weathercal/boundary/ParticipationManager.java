/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.boundary;

import it.polimi.deib.se2.mp.weathercal.entity.Event;
import it.polimi.deib.se2.mp.weathercal.entity.Participation;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author paolo
 */
@Stateless
public class ParticipationManager extends AbstractFacade<Participation>{
    
    @Inject
    Logger logger;
    @PersistenceContext
    EntityManager em;

    public ParticipationManager() {
        super(Participation.class);
    }
    @Override
    protected Logger getLogger() {
        return logger;
    }
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Participation edit(Participation entity) {
        Participation retVal = super.edit(entity); //To change body of generated methods, choose Tools | Templates.
        em.flush();
        return retVal;
    }

    @Override
    public void create(Participation entity) {
        
        super.create(entity); //To change body of generated methods, choose Tools | Templates.
        em.flush();
    }

    @Override
    public void remove(Participation entity) {
        entity.setEvent(null);
        entity.setCalendar(null);
        super.remove(entity); //To change body of generated methods, choose Tools | Templates.
        em.flush();
    }
    
    public List<Participation> findByEvent(Event e){
        return em.createNamedQuery("Participation.findByIdEvent").setParameter("idEvent", e.getId()).getResultList();
    }
}
