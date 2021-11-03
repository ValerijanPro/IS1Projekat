/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarm;

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
import java.util.Date;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
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
    
    
    public static void main(String[] args) {
        
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("my_persistence_unit");
        
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
                    sacuvaj(user,datum);
                   
                   
                    // stavi da je default pesma lexington
                } catch (JMSException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
    }
    public static void sacuvaj(Korisnik user, Date datum){
         EntityManager em=null;
         Alarm a=new Alarm();
         a.setIdK(user);
         a.setVremeZvonjenja(datum);
         Pesma podrazumevana=(Pesma)em.createQuery("SELECT p from Pesma p where p.idPesme=1").getSingleResult();
         a.setIdPesme(podrazumevana);
         em.getTransaction().begin();
         em.persist(a);
         em.getTransaction().commit();
    }
    
}
