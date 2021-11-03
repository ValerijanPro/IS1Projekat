/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitites;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Valja
 */
@Entity
@Table(name = "istorijapustanja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Istorijapustanja.findAll", query = "SELECT i FROM Istorijapustanja i"),
    @NamedQuery(name = "Istorijapustanja.findByIdPesme", query = "SELECT i FROM Istorijapustanja i WHERE i.istorijapustanjaPK.idPesme = :idPesme"),
    @NamedQuery(name = "Istorijapustanja.findByVremePustanja", query = "SELECT i FROM Istorijapustanja i WHERE i.istorijapustanjaPK.vremePustanja = :vremePustanja"),
    @NamedQuery(name = "Istorijapustanja.findByIdK", query = "SELECT i FROM Istorijapustanja i WHERE i.istorijapustanjaPK.idK = :idK")})
public class Istorijapustanja implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected IstorijapustanjaPK istorijapustanjaPK;
    @JoinColumn(name = "idK", referencedColumnName = "idK", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Korisnik korisnik;
    @JoinColumn(name = "idPesme", referencedColumnName = "idPesme", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Pesma pesma;

    public Istorijapustanja() {
    }

    public Istorijapustanja(IstorijapustanjaPK istorijapustanjaPK) {
        this.istorijapustanjaPK = istorijapustanjaPK;
    }

    public Istorijapustanja(int idPesme, Date vremePustanja, int idK) {
        this.istorijapustanjaPK = new IstorijapustanjaPK(idPesme, vremePustanja, idK);
    }

    public IstorijapustanjaPK getIstorijapustanjaPK() {
        return istorijapustanjaPK;
    }

    public void setIstorijapustanjaPK(IstorijapustanjaPK istorijapustanjaPK) {
        this.istorijapustanjaPK = istorijapustanjaPK;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public Pesma getPesma() {
        return pesma;
    }

    public void setPesma(Pesma pesma) {
        this.pesma = pesma;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (istorijapustanjaPK != null ? istorijapustanjaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Istorijapustanja)) {
            return false;
        }
        Istorijapustanja other = (Istorijapustanja) object;
        if ((this.istorijapustanjaPK == null && other.istorijapustanjaPK != null) || (this.istorijapustanjaPK != null && !this.istorijapustanjaPK.equals(other.istorijapustanjaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitites.Istorijapustanja[ istorijapustanjaPK=" + istorijapustanjaPK + " ]";
    }
    
}
