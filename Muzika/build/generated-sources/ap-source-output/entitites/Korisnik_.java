package entitites;

import entitites.Alarm;
import entitites.Istorijapustanja;
import entitites.Obaveza;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-21T21:17:27")
@StaticMetamodel(Korisnik.class)
public class Korisnik_ { 

    public static volatile SingularAttribute<Korisnik, String> ime;
    public static volatile SingularAttribute<Korisnik, String> prezime;
    public static volatile SingularAttribute<Korisnik, Integer> idK;
    public static volatile SingularAttribute<Korisnik, String> password;
    public static volatile SingularAttribute<Korisnik, Integer> idMesto;
    public static volatile ListAttribute<Korisnik, Alarm> alarmList;
    public static volatile ListAttribute<Korisnik, Obaveza> obavezaList;
    public static volatile ListAttribute<Korisnik, Istorijapustanja> istorijapustanjaList;
    public static volatile SingularAttribute<Korisnik, String> username;

}