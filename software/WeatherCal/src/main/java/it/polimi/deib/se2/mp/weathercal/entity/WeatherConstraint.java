/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author paolo
 */
@Entity
@Table(name = "weather_constraint", schema = "development")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WeatherConstraint.findAll", query = "SELECT w FROM WeatherConstraint w"),
    @NamedQuery(name = "WeatherConstraint.findById", query = "SELECT w FROM WeatherConstraint w WHERE w.id = :id"), 
    @NamedQuery(name = "WeatherConstraint.findByTemperature", query = "SELECT w FROM WeatherConstraint w WHERE w.temperature = :temperature"),
    @NamedQuery(name = "WeatherConstraint.findByIsTemperatureLowerThan", query = "SELECT w FROM WeatherConstraint w WHERE w.isTemperatureLowerThan = :isTemperatureLowerThan")})
public class WeatherConstraint implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Max(value=100)  @Min(value=-80)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull(message = "May not be empty")
    @Column(name = "temperature", nullable = false, precision = 5, scale = 2)
    private BigDecimal temperature = new BigDecimal(20);
    @Basic(optional = false)
    @NotNull(message = "May not be empty")
    @Column(name = "is_temperature_lower_than", nullable = false)
    private boolean isTemperatureLowerThan;
    @JoinColumn(name = "id_event", referencedColumnName = "id", nullable = false)
    @NotNull(message = "May not be empty")
    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Event event;

    public WeatherConstraint() {
    }

    public WeatherConstraint(Long id) {
        this.id = id;
    }

    public WeatherConstraint(Long id, BigDecimal temperature, boolean isTemperatureLowerThan) {
        this.id = id;
        this.temperature = temperature;
        this.isTemperatureLowerThan = isTemperatureLowerThan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTemperature() {
        return temperature.setScale(0, RoundingMode.HALF_UP);
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public boolean getIsTemperatureLowerThan() {
        return isTemperatureLowerThan;
    }

    public void setIsTemperatureLowerThan(boolean isTemperatureLowerThan) {
        this.isTemperatureLowerThan = isTemperatureLowerThan;
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
        if (!(object instanceof WeatherConstraint)) {
            return false;
        }
        WeatherConstraint other = (WeatherConstraint) object;
        return (other.id != null && this.id != null && this.id.equals(other.id))
            || (other.isTemperatureLowerThan == this.isTemperatureLowerThan
                && ((this.event == null && other.event == null)
                    || (this.event != null && this.event.equals(other.event)))
                && this.temperature.equals(other.temperature
            )
        );
    }

    @Override
    public String toString() {
        return "it.polimi.deib.se2.mp.entity.WeatherConstraint[ id=" + id + " ]";
    }
    
}
