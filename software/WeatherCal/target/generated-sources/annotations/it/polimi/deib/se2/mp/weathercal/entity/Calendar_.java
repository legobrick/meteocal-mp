package it.polimi.deib.se2.mp.weathercal.entity;

import it.polimi.deib.se2.mp.weathercal.entity.Owner;
import it.polimi.deib.se2.mp.weathercal.entity.Participation;
import it.polimi.deib.se2.mp.weathercal.entity.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-12-27T16:08:52")
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-12-27T21:43:46")
>>>>>>> aggiunti filtri redirezionamento utente loggato/anonimo + rinominate pagine index -> login e user_page -> index
@StaticMetamodel(Calendar.class)
public class Calendar_ { 

    public static volatile CollectionAttribute<Calendar, Participation> participationCollection;
    public static volatile CollectionAttribute<Calendar, User> userCollection;
    public static volatile SingularAttribute<Calendar, Boolean> isPublic;
    public static volatile SingularAttribute<Calendar, Long> id;
    public static volatile CollectionAttribute<Calendar, Owner> ownerCollection;

}