/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.entity;

import java.io.Serializable;
import javax.management.InvalidAttributeValueException;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "weather_state_constraint", schema = "development")
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
    @NotNull(message = "May not be empty")
    @Size(min = 1, max = 50)
    @Column(name = "weather_state", nullable = false, length = 50)
    private String weatherState;
    @JoinColumn(name = "id_event", referencedColumnName = "id", nullable = false)
    @NotNull(message = "May not be empty")
    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Event event;
    
    public static enum State {
        SUN("sun"), RAIN("rain"), FOG("fog"), CLOUD("cloud"), SNOW("snow"), WIND("wind");
        private final String value;
        private State(String value) {
                this.value = value;
        }
        public static State getFor(String value){
            for(State s : values()){
                if( s.toString().equals(value)){
                    return s;
                }
            }
            return null;
        }
        @Override
        public String toString() {
            return value;
        }
    }
    private static boolean contains(String test) {
        for (State c : State.values()) {
            if (c.toString().equals(test)) {
                return true;
            }
        }
        return false;
    }

    public WeatherStateConstraint() {
    }

    public WeatherStateConstraint(Long id) {
        this.id = id;
    }

    public WeatherStateConstraint(Event event, State weatherState) {
        this.event = event;
        this.weatherState = weatherState.toString();
    }

    public WeatherStateConstraint(Event event, String weatherState) throws InvalidAttributeValueException {
        this.event = event;
        if(!contains(weatherState))
            throw new InvalidAttributeValueException("weatherState must be a String in the State enum");
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

    public void setWeatherState(String weatherState) throws InvalidAttributeValueException {
        if(!contains(weatherState))
            throw new InvalidAttributeValueException("weatherState must be a String in the State enum");
        this.weatherState = weatherState;
    }

    public void setWeatherState(State weatherState) {
        this.weatherState = weatherState.toString();
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
        return (other.id != null && this.id != null && this.id.equals(other.id)) ||
                ((this.event == null && other.event == null) ||
                (this.event != null && this.event.equals(other.event)) && 
                (this.weatherState == null && other.weatherState == null) ||
                (this.weatherState != null && this.weatherState.equals(other.weatherState)));
    }

    @Override
    public String toString() {
        return getWeatherState();
    }
    public State toState() {
        return State.getFor(weatherState);
    }
    
}
