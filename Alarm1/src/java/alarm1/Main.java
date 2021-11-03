/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarm1;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import entities.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import niti.obican;
import niti.periodican;
import prozori.ProzorGreska;
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
        
        emf=Persistence.createEntityManagerFactory("Alarm1PU");
        
        JMSContext context=connectionFactory.createContext();
        JMSConsumer consumer=context.createConsumer(topic);
        
        while(true){
            Message msg=consumer.receive();
            if(msg instanceof ObjectMessage){
                try {
                    EntityManager em=emf.createEntityManager();
                    
                //System.out.print("Primljena poruka "+ msg.getStringProperty("Naziv"));
                
              
                    ObjectMessage txt=(ObjectMessage)msg;
                    System.out.println("Primljena poruka");
                    
                    //ovde se pravi novi alarm
                    System.out.println("asd "+txt.getIntProperty("user"));
                    Korisnik user=em.createQuery("SELECT k FROM Korisnik k WHERE k.idK=:p",Korisnik.class).setParameter("p", txt.getIntProperty("user")).getResultList().get(0);
                    Date datum=(Date)txt.getObject();
                    
                    
                    //setuj ovo sto je poslato
                    sacuvaj(user,datum,context);
                    
                   
                    // stavi da je default pesma lexington
                } catch (JMSException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
               
                
            }
            else if (msg instanceof TextMessage){
                String tekstPoruke=((TextMessage) msg).getText();
                if(tekstPoruke.equals("melodija")){
                    //podesavanja melodija alarma
                     EntityManager em=emf.createEntityManager();
                    
                
                
              
                    TextMessage txt=(TextMessage)msg;
                    //System.out.println("Primljena poruka");
                    
                   
                   
                    //Korisnik user=em.createQuery("SELECT k FROM Korisnik k WHERE k.idK=:p",Korisnik.class).setParameter("p", txt.getIntProperty("user")).getResultList().get(0);
                    String naziv=txt.getStringProperty("naziv");
                    int idA=txt.getIntProperty("idA");
                    promeniMelodiju(idA,naziv);
                }
                else if(tekstPoruke.equals("periodican")){
                      EntityManager em=emf.createEntityManager();
                    
                
                
              
                    TextMessage txt=(TextMessage)msg;
                    //System.out.println("Primljena poruka");
                    
                   
                   
                    Korisnik user=em.createQuery("SELECT k FROM Korisnik k WHERE k.idK=:p",Korisnik.class).setParameter("p", txt.getIntProperty("user")).getResultList().get(0);
                    try {
                        Date vreme=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").parse(txt.getStringProperty("vreme"));
                        Date period=new SimpleDateFormat("HH:mm:ss").parse(txt.getStringProperty("period"));
                       
                            sacuvajPeriod(user,vreme,context,period);
                            
                            
                        
                        
                    } catch (ParseException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   
                }
                
            }
        }
        //if(emf!=null) emf.close();
    }
    public static void sacuvaj(Korisnik user, Date datum,JMSContext context){
         EntityManager em=null;
         em=emf.createEntityManager();
         Alarm a=new Alarm();
         a.setIdAlarm(em.createQuery("select max(o.idAlarm) from Alarm o", Integer.class).getSingleResult()+ 1);
         a.setIdK(user);
         a.setVremeZvonjenja(datum);
         Pesma podrazumevana=(Pesma)em.createQuery("SELECT p FROM Pesma p WHERE p.idPesme=1").getResultList().get(0);
         a.setIdPesme(podrazumevana);
         
       
         
         em.getTransaction().begin();
         em.persist(a);
         em.getTransaction().commit();
         obican nit=new obican(a,context);
         nit.start();
         System.out.println("zapocet alarm ");
         
    }
    private static void sacuvajPeriod(Korisnik user, Date datum, JMSContext context, Date period) {
         EntityManager em=null;
         em=emf.createEntityManager();
         Alarm a=new Alarm();
         a.setIdAlarm(em.createQuery("select max(o.idAlarm) from Alarm o", Integer.class).getSingleResult()+ 1);
         a.setIdK(user);
         a.setVremeZvonjenja(datum);
         a.setPeriod(period);
         Pesma podrazumevana=(Pesma)em.createQuery("SELECT p FROM Pesma p WHERE p.idPesme=1").getResultList().get(0);
         a.setIdPesme(podrazumevana);
         
       
         
         em.getTransaction().begin();
         em.persist(a);
         em.getTransaction().commit();
         periodican nit=new periodican(a,context);
         nit.start();
         System.out.println("zapocet alarm ");
    }
    private static boolean postojiPesma(String naziv){
        EntityManager em=null;
        em=emf.createEntityManager();
        List resultList = em.createQuery("SELECT p FROM Pesma p where p.naziv=:n").setParameter("n", naziv).getResultList();
        if(resultList.size()>0) return true;
        return false;
    }
    private static void promeniMelodiju(int idA, String naziv) {
        EntityManager em=null;
         em=emf.createEntityManager();
        Alarm alarm = em.find(Alarm.class, idA);
        
        if(!postojiPesma(naziv)){
            ProzorGreska prozor= new ProzorGreska();
            prozor.postaviTekst("Ne postoji pesma sa ovim imenom");
            prozor.setVisible(true);
            return;
        }
        Pesma pesma=(Pesma)em.createQuery("SELECT p FROM Pesma p where p.naziv=:n").setParameter("n", naziv).getResultList().get(0);
        alarm.setIdPesme(pesma);
        
        em.getTransaction().begin();
        em.flush();
        em.refresh(alarm);
        em.persist(alarm);
        em.getTransaction().commit();
        
    }

    
    
}
