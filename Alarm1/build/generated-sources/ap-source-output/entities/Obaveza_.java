package entities;

import entities.Korisnik;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-21T21:12:53")
@StaticMetamodel(Obaveza.class)
public class Obaveza_ { 

    public static volatile SingularAttribute<Obaveza, Korisnik> idK;
    public static volatile SingularAttribute<Obaveza, Integer> idOba;
    public static volatile SingularAttribute<Obaveza, Date> trajanje;
    public static volatile SingularAttribute<Obaveza, String> destinacija;
    public static volatile SingularAttribute<Obaveza, Date> pocetak;

}