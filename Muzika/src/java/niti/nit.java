/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package niti;

import entitites.Istorijapustanja;
import entitites.Korisnik;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import prozori.Istorija;

/**
 *
 * @author Valja
 */
public class nit extends Thread {
    
    
    private Korisnik user;
    private static EntityManagerFactory emf;
    public nit(Korisnik u){
         user=u;
    }
    
    public void run(){
        emf=Persistence.createEntityManagerFactory("MuzikaPU");
        EntityManager em=emf.createEntityManager();
        
         List<Istorijapustanja> resultList = em.createNamedQuery("Istorijapustanja.findByIdK",Istorijapustanja.class).setParameter("idK",user.getIdK()).getResultList();
         Istorija is=new Istorija();
                    
                    
                    
        for(Istorijapustanja i:resultList){
            is.dodajPesmu(i.getPesma().getNaziv());
            is.dodajVreme(i.getIstorijapustanjaPK().getVremePustanja().toString());
             //s.add(i.getPesma().getNaziv());
        }
                    
                     
        is.promeni();
                    
        is.setVisible(true);
    }
}
