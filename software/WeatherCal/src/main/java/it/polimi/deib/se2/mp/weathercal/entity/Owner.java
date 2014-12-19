/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author paolo
 */
@Entity
@Table(name = "owner", catalog = "weathercal", schema = "development")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Owner.findAll", query = "SELECT o FROM Owner o"),
    @NamedQuery(name = "Owner.findByIdCalendar", query = "SELECT o FROM Owner o WHERE o.ownerPK.idCalendar = :idCalendar"),
    @NamedQuery(name = "Owner.findByIdEvent", query = "SELECT o FROM Owner o WHERE o.ownerPK.idEvent = :idEvent"),
    @NamedQuery(name = "Owner.findByNotification", query = "SELECT o FROM Owner o WHERE o.notification = :notification")})
public class Owner implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OwnerPK ownerPK;
    @Column(name = "notification")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notification;
    @JoinColumn(name = "id_calendar", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Calendar calendar;
    @JoinColumn(name = "id_event", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Event event;

    public Owner() {
    }

    public Owner(OwnerPK ownerPK) {
        this.ownerPK = ownerPK;
    }

    public Owner(long idCalendar, long idEvent) {
        this.ownerPK = new OwnerPK(idCalendar, idEvent);
    }

    public OwnerPK getOwnerPK() {
        return ownerPK;
    }

    public void setOwnerPK(OwnerPK ownerPK) {
        this.ownerPK = ownerPK;
    }

    public Date getNotification() {
        return notification;
    }

    public void setNotification(Date notification) {
        this.notification = notification;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ownerPK != null ? ownerPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Owner)) {
            return false;
        }
        Owner other = (Owner) object;
        if ((this.ownerPK == null && other.ownerPK != null) || (this.ownerPK != null && !this.ownerPK.equals(other.ownerPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.deib.se2.mp.entity.Owner[ ownerPK=" + ownerPK + " ]";
    }
    
}
