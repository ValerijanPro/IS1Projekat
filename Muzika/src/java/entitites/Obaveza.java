/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitites;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Valja
 */
@Entity
@Table(name = "obaveza")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Obaveza.findAll", query = "SELECT o FROM Obaveza o"),
    @NamedQuery(name = "Obaveza.findByIdOba", query = "SELECT o FROM Obaveza o WHERE o.idOba = :idOba"),
    @NamedQuery(name = "Obaveza.findByPocetak", query = "SELECT o FROM Obaveza o WHERE o.pocetak = :pocetak"),
    @NamedQuery(name = "Obaveza.findByTrajanje", query = "SELECT o FROM Obaveza o WHERE o.trajanje = :trajanje"),
    @NamedQuery(name = "Obaveza.findByDestinacija", query = "SELECT o FROM Obaveza o WHERE o.destinacija = :destinacija")})
public class Obaveza implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idOba")
    private Integer idOba;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pocetak")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pocetak;
    @Basic(optional = false)
    @NotNull
    @Column(name = "trajanje")
    @Temporal(TemporalType.TIMESTAMP)
    private Date trajanje;
    @Size(max = 45)
    @Column(name = "destinacija")
    private String destinacija;
    @JoinColumn(name = "idK", referencedColumnName = "idK")
    @ManyToOne(optional = false)
    private Korisnik idK;

    public Obaveza() {
    }

    public Obaveza(Integer idOba) {
        this.idOba = idOba;
    }

    public Obaveza(Integer idOba, Date pocetak, Date trajanje) {
        this.idOba = idOba;
        this.pocetak = pocetak;
        this.trajanje = trajanje;
    }

    public Integer getIdOba() {
        return idOba;
    }

    public void setIdOba(Integer idOba) {
        this.idOba = idOba;
    }

    public Date getPocetak() {
        return pocetak;
    }

    public void setPocetak(Date pocetak) {
        this.pocetak = pocetak;
    }

    public Date getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(Date trajanje) {
        this.trajanje = trajanje;
    }

    public String getDestinacija() {
        return destinacija;
    }

    public void setDestinacija(String destinacija) {
        this.destinacija = destinacija;
    }

    public Korisnik getIdK() {
        return idK;
    }

    public void setIdK(Korisnik idK) {
        this.idK = idK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOba != null ? idOba.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Obaveza)) {
            return false;
        }
        Obaveza other = (Obaveza) object;
        if ((this.idOba == null && other.idOba != null) || (this.idOba != null && !this.idOba.equals(other.idOba))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitites.Obaveza[ idOba=" + idOba + " ]";
    }
    
}
