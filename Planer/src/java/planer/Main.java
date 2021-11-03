/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planer;

import entities.Korisnik;
import entities.Mesto;
import entities.Obaveza;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.swing.JOptionPane;
import prozori.ProzorGreska;
import prozori.listanje;

/**
 *
 * @author Valja
 */
public class Main {

    @PersistenceContext
   
    @Resource(lookup="myConnFactory")
    private static ConnectionFactory connectionFactory;
     
    @Resource(lookup="MyTopic")
    private static Topic topic;
    /**
     * @param args the command line arguments
     */
    private static EntityManagerFactory emf;
    
    public static void main(String[] args) throws JMSException {
        
        emf=Persistence.createEntityManagerFactory("PlanerPU");
        
        JMSContext context=connectionFactory.createContext();
        JMSConsumer consumer=context.createConsumer(topic);
       
        while(true){
           
            Message msg=consumer.receive();
            
            if (msg instanceof TextMessage){
                String tekstPoruke=((TextMessage) msg).getText();
                if(tekstPoruke.equals("dodavanje")){
                    
                     EntityManager em=emf.createEntityManager();
                    
                
                
                 
                    TextMessage txt=(TextMessage)msg;
                    System.out.println("Primljena poruka");
                    
                   
                   
                    Korisnik user=em.createQuery("SELECT k FROM Korisnik k WHERE k.idK=:p",Korisnik.class).setParameter("p", txt.getIntProperty("user")).getResultList().get(0);
                    
                    try {
                        Date vremePocetka=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").parse(txt.getStringProperty("vreme"));
                        Date trajanje=new SimpleDateFormat("HH:mm:ss").parse(txt.getStringProperty("trajanje"));
                        String destinacija=txt.getStringProperty("destinacija");
                        sacuvajObavezu(user,vremePocetka,trajanje,destinacija);
                            
                            
                        
                        
                    } catch (ParseException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   
                } //if dodavanje
                else if(tekstPoruke.equals("listanje")){
                    EntityManager em=emf.createEntityManager();
                    
                    TextMessage txt=(TextMessage)msg;
                    System.out.println("Primljena poruka");
                    
                   
                   
                    Korisnik user=em.createQuery("SELECT k FROM Korisnik k WHERE k.idK=:p",Korisnik.class).setParameter("p", txt.getIntProperty("user")).getResultList().get(0);
                
                    listanje l=new listanje();
                    
                    List<Obaveza> resultList = em.createQuery("SELECT o FROM Obaveza o WHERE o.idK.idK = :d").setParameter("d",user.getIdK()).getResultList();
                     SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm:ss");
                     
                    l.setKorisnik(user.getIdK());
                    for(Obaveza i:resultList){
                        l.dodajObavezu(i.getIdOba().toString(), i.getPocetak().toString(), formatter1.format(i.getTrajanje()), i.getDestinacija(),i.getIdOba());
                        
                        
                    }
                    l.promeni();
                    l.setVisible(true);
                    
                
                }
                else if(tekstPoruke.equals("brisanje")){
                    EntityManager em=emf.createEntityManager();
                    
                    TextMessage txt=(TextMessage)msg;
                    System.out.println("Primljena poruka");
                    
                   
                   
                    Korisnik user=em.createQuery("SELECT k FROM Korisnik k WHERE k.idK=:p",Korisnik.class).setParameter("p", txt.getIntProperty("user")).getResultList().get(0);
                    int id=txt.getIntProperty("id");
                    Obaveza pronadjena = em.find(Obaveza.class, id);
                    
                    if (!em.contains(pronadjena)) {
                        pronadjena = em.merge(pronadjena);
                    }
                    
        
                    em.getTransaction().begin();
                    em.remove(pronadjena);
                    em.getTransaction().commit();
                }
                else if(tekstPoruke.equals("promenaP")){
                    EntityManager em=emf.createEntityManager();
                    
                    TextMessage txt=(TextMessage)msg;
                    System.out.println("Primljena poruka");
                    
                   
                   
                    Korisnik user=em.createQuery("SELECT k FROM Korisnik k WHERE k.idK=:p",Korisnik.class).setParameter("p", txt.getIntProperty("user")).getResultList().get(0);
                    int id=txt.getIntProperty("id");
                    try {
                        Date poc=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").parse(txt.getStringProperty("vreme"));
                        Obaveza obaveza = em.find(Obaveza.class, id);
                        if (!em.contains(obaveza)) {
                            obaveza = em.merge(obaveza);
                        }
                        obaveza.setPocetak(poc);
                        if(mozeDodavanje(obaveza,user)){
                            em.getTransaction().begin();
                            em.persist(obaveza);
                            em.getTransaction().commit();
                        }
                        else{
                            em.detach(obaveza);
                        }
                        
                        
                    } catch (ParseException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                    
                }
                else if(tekstPoruke.equals("promenaT")){
                     EntityManager em=emf.createEntityManager();
                    
                    TextMessage txt=(TextMessage)msg;
                    System.out.println("Primljena poruka");
                    
                   
                   
                    Korisnik user=em.createQuery("SELECT k FROM Korisnik k WHERE k.idK=:p",Korisnik.class).setParameter("p", txt.getIntProperty("user")).getResultList().get(0);
                    int id=txt.getIntProperty("id");
                    try {
                        Date tra=new SimpleDateFormat("HH:mm:ss").parse(txt.getStringProperty("trajanje"));
                        Obaveza obaveza = em.find(Obaveza.class, id);
                        if (!em.contains(obaveza)) {
                            obaveza = em.merge(obaveza);
                        }
                        
                        obaveza.setTrajanje(tra);
                        if(mozeDodavanje(obaveza,user)){
                            em.getTransaction().begin();
                            em.persist(obaveza);
                            em.getTransaction().commit();
                        }
                        else{
                            em.detach(obaveza);
                        }
                        
                        
                        
                    } catch (ParseException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else if(tekstPoruke.equals("promenaD")){
                     EntityManager em=emf.createEntityManager();
                    
                    TextMessage txt=(TextMessage)msg;
                    System.out.println("Primljena poruka");
                    
                   
                   
                    Korisnik user=em.createQuery("SELECT k FROM Korisnik k WHERE k.idK=:p",Korisnik.class).setParameter("p", txt.getIntProperty("user")).getResultList().get(0);
                    int id=txt.getIntProperty("id");
                    String destinacija=txt.getStringProperty("destinacija");
                       
                        Obaveza obaveza = em.find(Obaveza.class, id);
                        if (!em.contains(obaveza)) {
                            obaveza = em.merge(obaveza);
                        }
                        if(postojiDestinacija(destinacija)){
                            obaveza.setDestinacija(destinacija);
                        }
                        else{
                            ProzorGreska prozor= new ProzorGreska();
                            prozor.postaviTekst("Ne postoji unesena destinacija");
                            prozor.setVisible(true);
                            continue;
                        }
                        em.getTransaction().begin();
                        em.persist(obaveza);
                        em.getTransaction().commit();
                        
                        
                   
                }
                else if (tekstPoruke.equals("izracunavanje")){
                    EntityManager em=emf.createEntityManager();
                    
                    TextMessage txt=(TextMessage)msg;
                    System.out.println("Primljena poruka");
                    
                   
                   
                    Korisnik user=em.createQuery("SELECT k FROM Korisnik k WHERE k.idK=:p",Korisnik.class).setParameter("p", txt.getIntProperty("user")).getResultList().get(0);
                    
                    String poc=txt.getStringProperty("poc");
                    String kraj=txt.getStringProperty("kraj");
                    
                    if(poc.equals("default")) //poc="Beograd"; //TODO: TRENUTNA OBAVEZA
                    
                    {
                        Date sada=Calendar.getInstance().getTime();
                        List<Obaveza>obaveze=new ArrayList<>();
                        obaveze=em.createQuery("SELECT o from Obaveza o where o.idK.idK=:k").setParameter("k", user.getIdK()).getResultList();
                        Obaveza tren=null;
                        for(Obaveza o:obaveze){
                            
                            Calendar c = Calendar.getInstance();
                            c.setTime(o.getPocetak());
                            c.add(Calendar.HOUR,o.getTrajanje().getHours());
                            c.add(Calendar.MINUTE,o.getTrajanje().getMinutes());
                            c.add(Calendar.SECOND,o.getTrajanje().getSeconds());
                            Date kr=c.getTime();
                            //long sum = o.getPocetak().getTime()+o.getTrajanje().getTime();

                            //Date kr = new Date(sum);
                            
                            if(o.getPocetak().before(sada) && (kr.after(sada))){
                                tren=o;
                            }
                            
                            
                        }
                        if(tren!=null){
                                if( !postojiDestinacija(kraj)){
                                    ProzorGreska prozor= new ProzorGreska();
                                    prozor.postaviTekst("Ne postoji unesena krajnja destinacija");
                                    prozor.setVisible(true);
                                    continue;
                                }
                                Mesto pocetno=(Mesto)em.createQuery("SELECT m FROM Mesto m WHERE m.naziv=:naz").setParameter("naz", tren.getDestinacija()).getResultList().get(0);
                                Mesto krajnje=(Mesto)em.createQuery("SELECT m FROM Mesto m WHERE m.naziv=:naz").setParameter("naz", kraj).getResultList().get(0);
                                double rastojanje=distance(pocetno.getY(),krajnje.getY(),pocetno.getX(),krajnje.getX(),0.0,0.0);
                                //rastojanje=Math.sqrt( Math.pow((pocetno.getX()-krajnje.getX()),2)  + Math.pow((pocetno.getY()-krajnje.getY()),2) );
                                rastojanje=distance1(pocetno.getX(),krajnje.getX(),pocetno.getY(),krajnje.getY());
                                JMSProducer producer=context.createProducer();
                                final double brzina=60; // 60km/ h putujemo
                                rastojanje=rastojanje / brzina;
                                TextMessage poruka=context.createTextMessage("racunanje");

                                int ras=(int)rastojanje;
                                double min=rastojanje-(double) ras;
                                int minuta=(int)(min*100);


                                poruka.setIntProperty("sati", (int)Math.floor(rastojanje));
                                poruka.setIntProperty("minuta", minuta);
                                producer.send(topic, poruka);
                            }
                        else{
                                //TODO: NEMA TRENUTNE
                                
                            obaveze.sort(new Comparator<Obaveza>(){
                                @Override
                                public int compare(Obaveza o1, Obaveza o2) {

                                    //1. datum - 2. datum
                                    Date poc1;
                                    Date poc2;
                                    poc1=o1.getPocetak();
                                    poc2=o2.getPocetak();
                                    return -poc1.compareTo(poc2);

                                }

                            });
                            System.out.println("asd");
                            Obaveza preth=null;
                             for(Obaveza o:obaveze){
                                 if(o.getPocetak().before(sada)){
                                     preth=o;
                                     break;
                                 }
                             }
                             if(preth!=null){

                                 //JOptionPane.showMessageDialog(null, "NEMA PRETHODNE OBAVEZE",null, JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                                 //int exit=JOptionPane.showConfirmDialog(prozor, "NEMA PRETHODNE OBAVEZE" , null, JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                                 //if (exit == JOptionPane.YES_OPTION){
                                 //    System.out.println("KLIKNUO JE LADNO !");
                                 //}
                                 if( !postojiDestinacija(kraj)){
                                    ProzorGreska prozor= new ProzorGreska();
                                    prozor.postaviTekst("Ne postoji unesena krajnja destinacija");
                                    prozor.setVisible(true);
                                    continue;
                                }
                                Mesto pocetno=(Mesto)em.createQuery("SELECT m FROM Mesto m WHERE m.naziv=:naz").setParameter("naz", preth.getDestinacija()).getResultList().get(0);
                                Mesto krajnje=(Mesto)em.createQuery("SELECT m FROM Mesto m WHERE m.naziv=:naz").setParameter("naz", kraj).getResultList().get(0);
                                double rastojanje=distance(pocetno.getY(),krajnje.getY(),pocetno.getX(),krajnje.getX(),0.0,0.0);
                                //rastojanje=Math.sqrt( Math.pow((pocetno.getX()-krajnje.getX()),2)  + Math.pow((pocetno.getY()-krajnje.getY()),2) );
                                rastojanje=distance1(pocetno.getX(),krajnje.getX(),pocetno.getY(),krajnje.getY());
                                JMSProducer producer=context.createProducer();
                                final double brzina=60; // 60km/ h putujemo
                                rastojanje=rastojanje / brzina;
                                TextMessage poruka=context.createTextMessage("racunanje");

                                int ras=(int)rastojanje;
                                double min=rastojanje-(double) ras;
                                int minuta=(int)(min*100);


                                poruka.setIntProperty("sati", (int)Math.floor(rastojanje));
                                poruka.setIntProperty("minuta", minuta);
                                producer.send(topic, poruka);
                             }
                             else{
                                 //TODO: GRESKA, NEMA PRETHODNE OBAVEZE
                                 ProzorGreska prozor= new ProzorGreska();
                                 prozor.postaviTekst("NEMA PRETHODNE OBAVEZE");
                                 prozor.setVisible(true);
                             }
                                
                        }
                       //nadji trenutnu obavezu (pocetak >...)
                        
                        //ako nema trenutne
                        
                        //uzmem listu obaveza svoju; 
                        //sort listu obaveza po vremenu pocetka
                        
                        //nadjem prvu obavezu koja je manja ili = trenutnom vremenu
                        //ako nema: greska
                        
                       // Obaveza tren;
                        
                       //tren=em.createQuery("SELECT o from Obaveza o where o.idK.idK=:k").setParameter("k", user.getIdK());
                    }
                    else{
                        
                        if( !postojiDestinacija(kraj)){
                                    ProzorGreska prozor= new ProzorGreska();
                                    prozor.postaviTekst("Ne postoji unesena krajnja destinacija");
                                    prozor.setVisible(true);
                                    continue;
                         }
                        if( !postojiDestinacija(poc)){
                                    ProzorGreska prozor= new ProzorGreska();
                                    prozor.postaviTekst("Ne postoji unesena pocetna destinacija");
                                    prozor.setVisible(true);
                                    continue;
                         }
                        Mesto pocetno=(Mesto)em.createQuery("SELECT m FROM Mesto m WHERE m.naziv=:naz").setParameter("naz", poc).getResultList().get(0);
                        Mesto krajnje=(Mesto)em.createQuery("SELECT m FROM Mesto m WHERE m.naziv=:naz").setParameter("naz", kraj).getResultList().get(0);
                        double rastojanje=distance(pocetno.getY(),krajnje.getY(),pocetno.getX(),krajnje.getX(),0.0,0.0);
                        //rastojanje=Math.sqrt( Math.pow((pocetno.getX()-krajnje.getX()),2)  + Math.pow((pocetno.getY()-krajnje.getY()),2) );
                        rastojanje=distance1(pocetno.getX(),krajnje.getX(),pocetno.getY(),krajnje.getY());
                        JMSProducer producer=context.createProducer();
                        final double brzina=60; // 60km/ h putujemo
                        rastojanje=rastojanje / brzina;
                        TextMessage poruka=context.createTextMessage("racunanje");

                        int ras=(int)rastojanje;
                        double min=rastojanje-(double) ras;
                        int minuta=(int)(min*100);


                        poruka.setIntProperty("sati", (int)Math.floor(rastojanje));
                        poruka.setIntProperty("minuta", minuta);
                        producer.send(topic, poruka);
                    }
                    
                    //Topic topic=(Topic) context.
                }
               
                      
                
                
            }
        }
        
        
        //if(emf!=null) emf.close();
    }
    public static double distance1(double lat1, 
                     double lat2, double lon1, 
                                  double lon2) 
    { 
  
        // The math module contains a function 
        // named toRadians which converts from 
        // degrees to radians. 
        lon1 = Math.toRadians(lon1); 
        lon2 = Math.toRadians(lon2); 
        lat1 = Math.toRadians(lat1); 
        lat2 = Math.toRadians(lat2); 
  
        // Haversine formula  
        double dlon = lon2 - lon1;  
        double dlat = lat2 - lat1; 
        double a = Math.pow(Math.sin(dlat / 2), 2) 
                 + Math.cos(lat1) * Math.cos(lat2) 
                 * Math.pow(Math.sin(dlon / 2),2); 
              
        double c = 2 * Math.asin(Math.sqrt(a)); 
  
        // Radius of earth in kilometers. Use 3956  
        // for miles 
        double r = 6371; 
  
        // calculate the result 
        return(c * r); 
    }  
    private static double distance(double lat1, double lat2, double lon1,
        double lon2, double el1, double el2) {

    final int R = 6371; // Radius of the earth

    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = R * c * 1000; // convert to meters

    double height = el1 - el2;

    distance = Math.pow(distance, 2) + Math.pow(height, 2);

    return Math.sqrt(distance);
}
    
    private static boolean mozeDodavanje(Obaveza o, Korisnik user){
        EntityManager em=null;
        em=emf.createEntityManager();
        List<Obaveza>obaveze=new ArrayList<>();
        obaveze=em.createQuery("SELECT o from Obaveza o where o.idK.idK=:k and o.idOba!=:p").setParameter("k", user.getIdK()).setParameter("p", o.getIdOba()).getResultList();
        int preklapajuSe=0;
        Obaveza preklapajuca=null;
        for(Obaveza ob:obaveze){
            if(preklapaju(o,ob)) {
                preklapajuSe=1;
                preklapajuca=ob;
            }
        }
        if(preklapajuSe==0) return true;
        ProzorGreska prozor= new ProzorGreska();
        prozor.postaviTekst("Nova obaveza se preklapa sa obavezom sa id="+preklapajuca.getIdOba());
        prozor.setVisible(true);
        return false;
    }
//    private static void proveriIPostaviDestinaciju(Obaveza o,String d){
//        EntityManager em=null;
//        em=emf.createEntityManager();
//        List resultList = em.createQuery("SELECT m from Mesto m where m.naziv=:p").setParameter("p", d).getResultList();
//        if(resultList.size()>0) {
//            o.setDestinacija(d);
//        }
//        else{
//            ProzorGreska prozor= new ProzorGreska();
//            prozor.postaviTekst("Ne postoji unesena destinacija");
//            prozor.setVisible(true);
//        }
//        
//    }
    private static boolean postojiDestinacija(String d){
        EntityManager em=null;
        em=emf.createEntityManager();
        List resultList = em.createQuery("SELECT m from Mesto m where m.naziv=:p").setParameter("p", d).getResultList();
        if(resultList.size()>0) return true;
        return false;
    }
    
    private static void sacuvajObavezu(Korisnik user, Date vremePocetka, Date trajanje, String destinacija) {
        EntityManager em=null;
        em=emf.createEntityManager();
        Obaveza o=new Obaveza();
        o.setIdOba(em.createQuery("select max(o.idOba) from Obaveza o", Integer.class).getSingleResult() + 1);
        o.setPocetak(vremePocetka);
        o.setTrajanje(trajanje);
        if(destinacija.equals("default")){
            Mesto mesto = em.find(Mesto.class,user.getIdMesto());
            destinacija=mesto.getNaziv();
            //kod kuce korisnika
        }
        o.setIdK(user);
        
        //TODO: PROVERA DA LI POSTOJI DESTINACIJA
        if(postojiDestinacija(destinacija)){
            o.setDestinacija(destinacija);
        }
        else{
            ProzorGreska prozor= new ProzorGreska();
           prozor.postaviTekst("Ne postoji unesena destinacija");
            prozor.setVisible(true);
            return;
        }
        
       
       
        if(mozeDodavanje(o,user)){
            //za svaki slucaj opet
            Obaveza neSmeDaPostoji=null;
            List lista = em.createQuery("SELECT o from Obaveza o where o.idOba=:a").setParameter("a", o.getIdOba()).getResultList();
            if(lista.size()!=0) neSmeDaPostoji=(Obaveza)lista.get(0);
            if(neSmeDaPostoji==null){
                em.getTransaction().begin();
                em.persist(o);
                em.getTransaction().commit();
            }
        }
       
            
        
            
            
        
       
            // poruka o preklapanju
           
        
        
         
        
    }

    private static boolean preklapaju(Obaveza ob, Obaveza o) {
        //p1 k1 plavi
        //p2 k2 crvena
        Date p1,p2,k1,k2;
        p1=ob.getPocetak();
        p2=o.getPocetak();
        k1=saberi(ob.getPocetak(),ob.getTrajanje());
        k2=saberi(o.getPocetak(),o.getTrajanje());
        
        if( ((p1.before(p2)||p1.equals(p2)) && (k1.after(p2) && k1.before(k2))) 
            ||
            ((k1.after(k2) || k1.equals(k2)) && (p1.after(p2) && p1.before(k2)))
            ||    
            (p1.before(p2) && (k1.after(k2)))    
                ||
            (p1.after(p2) && k1.before(k2))    
                ||
                (p1.equals(p2) && k1.equals(k2))
            ){
            return true;
        } 
        
        
        return false;
    }
    private static Date saberi(Date d1, Date d2){
        Calendar c = Calendar.getInstance();
        c.setTime(d1);
        c.add(Calendar.HOUR,d2.getHours());
        c.add(Calendar.MINUTE,d2.getMinutes());
        c.add(Calendar.SECOND,d2.getSeconds());
        return c.getTime();
    }
    
}
