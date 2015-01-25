/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.boundary;

import it.polimi.deib.se2.mp.weathercal.entity.Owner;
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
public class OwnerManager extends AbstractFacade<Owner>{
    
    @Inject
    Logger logger;
    @PersistenceContext
    EntityManager em;

    public OwnerManager() {
        super(Owner.class);
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
    public Owner edit(Owner entity) {
        Owner retVal = super.edit(entity); //To change body of generated methods, choose Tools | Templates.
        em.flush();
        return retVal;
    }

    @Override
    public void create(Owner entity) {
        super.create(entity); //To change body of generated methods, choose Tools | Templates.
        em.flush();
    }

    @Override
    public void remove(Owner entity) {
        entity.setEvent(null);
        entity.setCalendar(null);
        super.remove(entity); //To change body of generated methods, choose Tools | Templates.
        em.flush();
    }
}
