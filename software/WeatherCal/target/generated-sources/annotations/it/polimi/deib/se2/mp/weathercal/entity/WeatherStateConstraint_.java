package it.polimi.deib.se2.mp.weathercal.entity;

import it.polimi.deib.se2.mp.weathercal.entity.Event;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-12-22T17:37:16")
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-12-25T23:32:09")
>>>>>>> aggiunte istruzioni per creare realm da command line e ri-modificata form per login con username (tanto è unique...)
@StaticMetamodel(WeatherStateConstraint.class)
public class WeatherStateConstraint_ { 

    public static volatile SingularAttribute<WeatherStateConstraint, String> weatherState;
    public static volatile SingularAttribute<WeatherStateConstraint, Event> idEvent;
    public static volatile SingularAttribute<WeatherStateConstraint, Long> id;

}