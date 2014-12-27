package it.polimi.deib.se2.mp.weathercal.entity;

import it.polimi.deib.se2.mp.weathercal.entity.Event;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-12-27T16:08:52")
@StaticMetamodel(WeatherConstraint.class)
public class WeatherConstraint_ { 

    public static volatile SingularAttribute<WeatherConstraint, Boolean> isTemperatureLowerThan;
    public static volatile SingularAttribute<WeatherConstraint, BigDecimal> temperature;
    public static volatile SingularAttribute<WeatherConstraint, Event> idEvent;
    public static volatile SingularAttribute<WeatherConstraint, Long> id;

}