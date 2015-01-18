/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author paolo
 */
@Embeddable
public class ParticipationPK implements Serializable {
    @Basic(optional = false)
    @NotNull(message = "May not be empty")
    @Column(name = "id_calendar", nullable = false)
    private long idCalendar;
    @Basic(optional = false)
    @NotNull(message = "May not be empty")
    @Column(name = "id_event", nullable = false)
    private long idEvent;

    public ParticipationPK() {
    }

    public ParticipationPK(long idCalendar, long idEvent) {
        this.idCalendar = idCalendar;
        this.idEvent = idEvent;
    }

    public long getIdCalendar() {
        return idCalendar;
    }

    public void setIdCalendar(long idCalendar) {
        this.idCalendar = idCalendar;
    }

    public long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(long idEvent) {
        this.idEvent = idEvent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idCalendar;
        hash += (int) idEvent;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParticipationPK)) {
            return false;
        }
        ParticipationPK other = (ParticipationPK) object;
        if (this.idCalendar != other.idCalendar) {
            return false;
        }
        if (this.idEvent != other.idEvent) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.deib.se2.mp.entity.ParticipationPK[ idCalendar=" + idCalendar + ", idEvent=" + idEvent + " ]";
    }
    
}
