/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author paolo
 */
@Entity
@Table(name = "participation", schema = "development")
@XmlRootElement
@NamedQueries({
     @NamedQuery(name = "Participation.findByIdCalendarandNotAvailability", query = "SELECT p FROM Participation p WHERE p.participationPK.idCalendar = :idCalendar and p.availability!=:av "),
      @NamedQuery(name = "Participation.findByIdCalendarandAvailability", query = "SELECT p FROM Participation p WHERE p.participationPK.idCalendar = :idCalendar and p.availability=:av "),
    @NamedQuery(name = "Participation.findByIdCalendarandIdEvent", query = "SELECT p FROM Participation p WHERE p.participationPK.idCalendar = :idCalendar and p.participationPK.idEvent = :idEvent"),
    @NamedQuery(name = "Participation.findAll", query = "SELECT p FROM Participation p"),
    @NamedQuery(name = "Participation.findByIdCalendar", query = "SELECT p FROM Participation p WHERE p.participationPK.idCalendar = :idCalendar"),
    @NamedQuery(name = "Participation.findByIdEvent", query = "SELECT p FROM Participation p WHERE p.participationPK.idEvent = :idEvent"),
    @NamedQuery(name = "Participation.findByAvailability", query = "SELECT p FROM Participation p WHERE p.availability = :availability"),
    @NamedQuery(name = "Participation.findByNotification", query = "SELECT p FROM Participation p WHERE p.notification = :notification")})
public class Participation implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ParticipationPK participationPK;
    @Basic(optional = false)
    @NotNull(message = "May not be empty")
    @Size(min = 1, max = 50)
    @Column(name = "availability", nullable = false, length = 50)
    private String availability;
    @Column(name = "notification")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notification;
    @JoinColumn(name = "id_calendar", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private CalendarEntity calendar;
    @JoinColumn(name = "id_event", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    private Event event;

    public Participation() {
    }

    public Participation(ParticipationPK participationPK) {
        this.participationPK = participationPK;
    }

    public Participation(ParticipationPK participationPK, String availability) {
        this.participationPK = participationPK;
        this.availability = availability;
    }

    public Participation(long idCalendar, long idEvent) {
        this.participationPK = new ParticipationPK(idCalendar, idEvent);
    }

    public Participation(long idCalendar, long idEvent, String availability) {
        this.participationPK = new ParticipationPK(idCalendar, idEvent);
        this.availability = availability;
    }

    public ParticipationPK getParticipationPK() {
        return participationPK;
    }

    public void setParticipationPK(ParticipationPK participationPK) {
        this.participationPK = participationPK;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public Date getNotification() {
        return notification;
    }

    public void setNotification(Date notification) {
        this.notification = notification;
    }

    public CalendarEntity getCalendar() {
        return calendar;
    }

    public void setCalendar(CalendarEntity calendar) {
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
        hash += (participationPK != null ? participationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Participation)) {
            return false;
        }
        Participation other = (Participation) object;
        if ((this.participationPK == null && other.participationPK != null) || (this.participationPK != null && !this.participationPK.equals(other.participationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.deib.se2.mp.entity.Participation[ participationPK=" + participationPK + " ]";
    }
    
}
