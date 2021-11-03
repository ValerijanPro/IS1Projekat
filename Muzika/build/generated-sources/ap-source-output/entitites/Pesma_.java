package entitites;

import entitites.Alarm;
import entitites.Istorijapustanja;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-21T21:17:27")
@StaticMetamodel(Pesma.class)
public class Pesma_ { 

    public static volatile SingularAttribute<Pesma, Integer> idPesme;
    public static volatile SingularAttribute<Pesma, String> naziv;
    public static volatile ListAttribute<Pesma, Alarm> alarmList;
    public static volatile SingularAttribute<Pesma, String> url;
    public static volatile ListAttribute<Pesma, Istorijapustanja> istorijapustanjaList;

}