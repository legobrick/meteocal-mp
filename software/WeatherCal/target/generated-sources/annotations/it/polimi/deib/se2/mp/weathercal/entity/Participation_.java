package it.polimi.deib.se2.mp.weathercal.entity;

import it.polimi.deib.se2.mp.weathercal.entity.Calendar;
import it.polimi.deib.se2.mp.weathercal.entity.Event;
import it.polimi.deib.se2.mp.weathercal.entity.ParticipationPK;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-12-27T16:08:52")
@StaticMetamodel(Participation.class)
public class Participation_ { 

    public static volatile SingularAttribute<Participation, Calendar> calendar;
    public static volatile SingularAttribute<Participation, Date> notification;
    public static volatile SingularAttribute<Participation, String> availability;
    public static volatile SingularAttribute<Participation, Event> event;
    public static volatile SingularAttribute<Participation, ParticipationPK> participationPK;

}