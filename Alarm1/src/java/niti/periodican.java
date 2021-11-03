/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package niti;

import entities.Alarm;
import entities.Pesma;
import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Valja
 */
public class periodican extends Thread {
     
    private Alarm alarm;
    private JMSContext context;
    private  EntityManagerFactory emf;
      
    public periodican(Alarm a, JMSContext c){
        alarm=a;
        context=c;
    }
    public void run(){
        try {
            emf=Persistence.createEntityManagerFactory("Alarm1PU");
            Date tren=java.util.Calendar.getInstance().getTime();
            Date kad=alarm.getVremeZvonjenja();
            Date T=alarm.getPeriod();
            T.setYear(java.util.Calendar.getInstance().getTime().getYear());
            
            T.setMonth(java.util.Calendar.getInstance().getTime().getMonth());
            T.setDate(java.util.Calendar.getInstance().getTime().getDate());
            
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String sada=formatter.format(tren);
            
            String kada=formatter.format(kad);
            while(true){
                    while(!sada.equals(kada)){
                        tren=java.util.Calendar.getInstance().getTime();
                        sada=formatter.format(tren);

                        //kada = kada + T;
                    }
                    T.setYear(java.util.Calendar.getInstance().getTime().getYear());
                    Calendar c=java.util.Calendar.getInstance();
                    c.setTime(kad);
                    c.add(Calendar.HOUR, T.getHours());
                    c.add(Calendar.MINUTE,T.getMinutes());
                    c.add(Calendar.SECOND, T.getSeconds());
                    
                    //long sum = kad.getTime() + T.getTime();

                    kad = c.getTime();
                    kada=formatter.format(kad);
                    
                    System.out.println("sada je : "+sada);
                    //PUSTI MUZIKU OVDE
                    String putanja=alarm.getIdPesme().getUrl();
                    System.out.println("PUSTAM MUZIKU: ");
                    if(Desktop.isDesktopSupported()){
                        Desktop.getDesktop().browse(new URI(putanja));
                    }

                    //OBRISI ALARM IZ BAZE
                EntityManager em=emf.createEntityManager();
                if (!em.contains(alarm)) {
                    alarm = em.merge(alarm);
                }
                alarm=em.find(Alarm.class, alarm.getIdAlarm());
                //System.out.println("ID ALARMA KOJI SE BRISE= "+alarm.getIdAlarm());
                 em.getTransaction().begin();
                  em.remove(alarm);
                 em.getTransaction().commit();
                 //dodaj novi alarm
                 Alarm a=new Alarm();
                a.setIdK(alarm.getIdK());
                a.setVremeZvonjenja(kad);
                a.setIdAlarm(em.createQuery("select max(o.idAlarm) from Alarm o", Integer.class).getSingleResult()+ 1);
                //a.setPeriod(T);
                
                Pesma podrazumevana=(Pesma)em.createQuery("SELECT p FROM Pesma p WHERE p.idPesme=1").getResultList().get(0);
                a.setIdPesme(podrazumevana);
                
                em.getTransaction().begin();
                em.persist(a);
                em.getTransaction().commit();
                alarm=a;
                //em.createQuery("DELETE FROM Alarm a where a.idAlarm=:p").setParameter("p", alarm.getIdAlarm());
                //if(emf!=null) emf.close();
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(obican.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(obican.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(obican.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
}
