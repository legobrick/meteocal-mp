/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author paolo
 */
@Entity
@Table(name = "weather_state_constraint", catalog = "weathercal", schema = "development")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WeatherStateConstraint.findAll", query = "SELECT w FROM WeatherStateConstraint w"),
    @NamedQuery(name = "WeatherStateConstraint.findById", query = "SELECT w FROM WeatherStateConstraint w WHERE w.id = :id"),
    @NamedQuery(name = "WeatherStateConstraint.findByWeatherState", query = "SELECT w FROM WeatherStateConstraint w WHERE w.weatherState = :weatherState")})
public class WeatherStateConstraint implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "weather_state", nullable = false, length = 50)
    private String weatherState;
    @JoinColumn(name = "id_event", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Event idEvent;

    public WeatherStateConstraint() {
    }

    public WeatherStateConstraint(Long id) {
        this.id = id;
    }

    public WeatherStateConstraint(Long id, String weatherState) {
        this.id = id;
        this.weatherState = weatherState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWeatherState() {
        return weatherState;
    }

    public void setWeatherState(String weatherState) {
        this.weatherState = weatherState;
    }

    public Event getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Event idEvent) {
        this.idEvent = idEvent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WeatherStateConstraint)) {
            return false;
        }
        WeatherStateConstraint other = (WeatherStateConstraint) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.deib.se2.mp.entity.WeatherStateConstraint[ id=" + id + " ]";
    }
    
}
