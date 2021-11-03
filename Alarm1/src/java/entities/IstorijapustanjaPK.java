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
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Valja
 */
@Embeddable
public class IstorijapustanjaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "idPesme")
    private int idPesme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vremePustanja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vremePustanja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idK")
    private int idK;

    public IstorijapustanjaPK() {
    }

    public IstorijapustanjaPK(int idPesme, Date vremePustanja, int idK) {
        this.idPesme = idPesme;
        this.vremePustanja = vremePustanja;
        this.idK = idK;
    }

    public int getIdPesme() {
        return idPesme;
    }

    public void setIdPesme(int idPesme) {
        this.idPesme = idPesme;
    }

    public Date getVremePustanja() {
        return vremePustanja;
    }

    public void setVremePustanja(Date vremePustanja) {
        this.vremePustanja = vremePustanja;
    }

    public int getIdK() {
        return idK;
    }

    public void setIdK(int idK) {
        this.idK = idK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idPesme;
        hash += (vremePustanja != null ? vremePustanja.hashCode() : 0);
        hash += (int) idK;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IstorijapustanjaPK)) {
            return false;
        }
        IstorijapustanjaPK other = (IstorijapustanjaPK) object;
        if (this.idPesme != other.idPesme) {
            return false;
        }
        if ((this.vremePustanja == null && other.vremePustanja != null) || (this.vremePustanja != null && !this.vremePustanja.equals(other.vremePustanja))) {
            return false;
        }
        if (this.idK != other.idK) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.IstorijapustanjaPK[ idPesme=" + idPesme + ", vremePustanja=" + vremePustanja + ", idK=" + idK + " ]";
    }
    
}
