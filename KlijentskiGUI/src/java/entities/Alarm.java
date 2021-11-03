/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Valja
 */
@Entity
@Table(name = "alarm")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alarm.findAll", query = "SELECT a FROM Alarm a"),
    @NamedQuery(name = "Alarm.findByIdAlarm", query = "SELECT a FROM Alarm a WHERE a.idAlarm = :idAlarm"),
    @NamedQuery(name = "Alarm.findByVremeZvonjenja", query = "SELECT a FROM Alarm a WHERE a.vremeZvonjenja = :vremeZvonjenja"),
    @NamedQuery(name = "Alarm.findByPeriod", query = "SELECT a FROM Alarm a WHERE a.period = :period")})
public class Alarm implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idAlarm")
    private Integer idAlarm;
    @Column(name = "vremeZvonjenja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vremeZvonjenja;
    @Column(name = "period")
    @Temporal(TemporalType.TIMESTAMP)
    private Date period;
    @JoinColumn(name = "idK", referencedColumnName = "idK")
    @ManyToOne(optional = false)
    private Korisnik idK;
    @JoinColumn(name = "idPesme", referencedColumnName = "idPesme")
    @ManyToOne(optional = false)
    private Pesma idPesme;

    public Alarm() {
    }

    public Alarm(Integer idAlarm) {
        this.idAlarm = idAlarm;
    }

    public Integer getIdAlarm() {
        return idAlarm;
    }

    public void setIdAlarm(Integer idAlarm) {
        this.idAlarm = idAlarm;
    }

    public Date getVremeZvonjenja() {
        return vremeZvonjenja;
    }

    public void setVremeZvonjenja(Date vremeZvonjenja) {
        this.vremeZvonjenja = vremeZvonjenja;
    }

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public Korisnik getIdK() {
        return idK;
    }

    public void setIdK(Korisnik idK) {
        this.idK = idK;
    }

    public Pesma getIdPesme() {
        return idPesme;
    }

    public void setIdPesme(Pesma idPesme) {
        this.idPesme = idPesme;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAlarm != null ? idAlarm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alarm)) {
            return false;
        }
        Alarm other = (Alarm) object;
        if ((this.idAlarm == null && other.idAlarm != null) || (this.idAlarm != null && !this.idAlarm.equals(other.idAlarm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Alarm[ idAlarm=" + idAlarm + " ]";
    }
    
}
