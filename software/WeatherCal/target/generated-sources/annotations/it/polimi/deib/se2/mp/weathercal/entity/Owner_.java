package it.polimi.deib.se2.mp.weathercal.entity;

import it.polimi.deib.se2.mp.weathercal.entity.Calendar;
import it.polimi.deib.se2.mp.weathercal.entity.Event;
import it.polimi.deib.se2.mp.weathercal.entity.OwnerPK;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< Updated upstream
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-12-27T23:03:05")
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-12-25T20:51:50")
>>>>>>> Stashed changes
@StaticMetamodel(Owner.class)
public class Owner_ { 

    public static volatile SingularAttribute<Owner, Calendar> calendar;
    public static volatile SingularAttribute<Owner, Date> notification;
    public static volatile SingularAttribute<Owner, OwnerPK> ownerPK;
    public static volatile SingularAttribute<Owner, Event> event;

}