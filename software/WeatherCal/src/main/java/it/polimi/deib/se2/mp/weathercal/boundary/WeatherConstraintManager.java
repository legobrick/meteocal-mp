/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.boundary;

import it.polimi.deib.se2.mp.weathercal.entity.WeatherConstraint;
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
public class WeatherConstraintManager extends AbstractFacade<WeatherConstraint>{
    
    @Inject
    Logger logger;
    @PersistenceContext
    EntityManager em;

    public WeatherConstraintManager() {
        super(WeatherConstraint.class);
    }
    @Override
    protected Logger getLogger() {
        return logger;
    }
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
