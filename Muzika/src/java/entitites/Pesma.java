/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitites;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Valja
 */
@Entity
@Table(name = "pesma")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pesma.findAll", query = "SELECT p FROM Pesma p"),
    @NamedQuery(name = "Pesma.findByIdPesme", query = "SELECT p FROM Pesma p WHERE p.idPesme = :idPesme"),
    @NamedQuery(name = "Pesma.findByNaziv", query = "SELECT p FROM Pesma p WHERE p.naziv = :naziv"),
    @NamedQuery(name = "Pesma.findByUrl", query = "SELECT p FROM Pesma p WHERE p.url = :url")})
public class Pesma implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idPesme")
    private Integer idPesme;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "naziv")
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "url")
    private String url;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pesma")
    private List<Istorijapustanja> istorijapustanjaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPesme")
    private List<Alarm> alarmList;

    public Pesma() {
    }

    public Pesma(Integer idPesme) {
        this.idPesme = idPesme;
    }

    public Pesma(Integer idPesme, String naziv, String url) {
        this.idPesme = idPesme;
        this.naziv = naziv;
        this.url = url;
    }

    public Integer getIdPesme() {
        return idPesme;
    }

    public void setIdPesme(Integer idPesme) {
        this.idPesme = idPesme;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @XmlTransient
    public List<Istorijapustanja> getIstorijapustanjaList() {
        return istorijapustanjaList;
    }

    public void setIstorijapustanjaList(List<Istorijapustanja> istorijapustanjaList) {
        this.istorijapustanjaList = istorijapustanjaList;
    }

    @XmlTransient
    public List<Alarm> getAlarmList() {
        return alarmList;
    }

    public void setAlarmList(List<Alarm> alarmList) {
        this.alarmList = alarmList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPesme != null ? idPesme.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pesma)) {
            return false;
        }
        Pesma other = (Pesma) object;
        if ((this.idPesme == null && other.idPesme != null) || (this.idPesme != null && !this.idPesme.equals(other.idPesme))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitites.Pesma[ idPesme=" + idPesme + " ]";
    }
    
}
