/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muzika;

import prozori.Istorija;
import entitites.Istorijapustanja;
import entitites.IstorijapustanjaPK;
import entitites.Korisnik;
import entitites.Pesma;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import niti.nit;
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
        
        emf=Persistence.createEntityManagerFactory("MuzikaPU");
        
        JMSContext context=connectionFactory.createContext();
        JMSConsumer consumer=context.createConsumer(topic);
        int k=2;
        while(true){
            k+=2;
            if(k%2==1) break;
            Message msg=consumer.receive();
            
            if (msg instanceof TextMessage){
                String tekstPoruke=((TextMessage) msg).getText();
                if(tekstPoruke.equals("pustanje")){
                    
                     EntityManager em=emf.createEntityManager();
                    
                
                
                 
                    TextMessage txt=(TextMessage)msg;
                    System.out.println("Primljena poruka");
                    
                   
                   
                    Korisnik user=em.createQuery("SELECT k FROM Korisnik k WHERE k.idK=:p",Korisnik.class).setParameter("p", txt.getIntProperty("user")).getResultList().get(0);
                    String naziv=txt.getStringProperty("naziv");
                    pustiPesmu(naziv,user);
                   
                }
                else if(tekstPoruke.equals("istorija")){
                      EntityManager em=emf.createEntityManager();
                    
                
                
              
                    TextMessage txt=(TextMessage)msg;
                    System.out.println("Primljena poruka");
                    
                    
                    Korisnik user=em.createQuery("SELECT k FROM Korisnik k WHERE k.idK=:p",Korisnik.class).setParameter("p", txt.getIntProperty("user")).getResultList().get(0);
                   
                    nit nit1=new nit(user);
                    nit1.start();
                    
                    
                    
                    //is.repaint();
                    //is.revalidate();
                    //is.setVisible(true);
                   
                    
                }
                      
                
                
            }
        }
        
        System.out.println("KAKO");
        //if(emf!=null) emf.close();
    }
    private static boolean postojiPesma(String naziv){
        EntityManager em=null;
        em=emf.createEntityManager();
        List resultList = em.createQuery("SELECT p FROM Pesma p where p.naziv=:n").setParameter("n", naziv).getResultList();
        if(resultList.size()>0) return true;
        return false;
    }
    private static void pustiPesmu(String naziv, Korisnik user) {
        EntityManager em=null;
        em=emf.createEntityManager();
        if(!postojiPesma(naziv)){
            ProzorGreska prozor= new ProzorGreska();
            prozor.postaviTekst("Ne postoji pesma sa ovim imenom");
            prozor.setVisible(true);
            return;
        }
        Pesma pesma=(Pesma)em.createQuery("SELECT p FROM Pesma p where p.naziv=:n").setParameter("n", naziv).getResultList().get(0);
        //TODO: ako lose ime pesme?
        //if(pesma==null) {System.out.println("lose ime pesme");return;}
        IstorijapustanjaPK i=new IstorijapustanjaPK(pesma.getIdPesme(),new Date(System.currentTimeMillis()),user.getIdK());
        
        Istorijapustanja ip=new Istorijapustanja(i);
        
        em.getTransaction().begin();
        em.persist(ip);
        em.getTransaction().commit();
        
        String putanja=pesma.getUrl();
        System.out.println("PUSTAM MUZIKU: ");
        if(Desktop.isDesktopSupported()){
            try {
                Desktop.getDesktop().browse(new URI(putanja));
                
                
            } catch (URISyntaxException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }
   
    
}
