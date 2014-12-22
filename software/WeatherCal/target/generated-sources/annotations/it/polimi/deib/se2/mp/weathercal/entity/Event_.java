package it.polimi.deib.se2.mp.weathercal.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-12-22T17:37:16")
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-12-25T23:32:09")
>>>>>>> aggiunte istruzioni per creare realm da command line e ri-modificata form per login con username (tanto è unique...)
@StaticMetamodel(Event.class)
public class Event_ { 

    public static volatile SingularAttribute<Event, BigDecimal> placeLongitude;
    public static volatile SingularAttribute<Event, Date> whenT;
    public static volatile SingularAttribute<Event, String> name;
    public static volatile SingularAttribute<Event, Boolean> isPublic;
    public static volatile SingularAttribute<Event, String> placeDescription;
    public static volatile SingularAttribute<Event, String> description;
    public static volatile SingularAttribute<Event, Long> id;
    public static volatile SingularAttribute<Event, BigDecimal> placeLatitude;
    public static volatile SingularAttribute<Event, Boolean> isOutdoor;

}