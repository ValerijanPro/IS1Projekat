package entitites;

import entitites.Korisnik;
import entitites.Pesma;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-21T21:17:27")
@StaticMetamodel(Alarm.class)
public class Alarm_ { 

    public static volatile SingularAttribute<Alarm, Korisnik> idK;
    public static volatile SingularAttribute<Alarm, Date> period;
    public static volatile SingularAttribute<Alarm, Pesma> idPesme;
    public static volatile SingularAttribute<Alarm, Date> vremeZvonjenja;
    public static volatile SingularAttribute<Alarm, Integer> idAlarm;

}