/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.entity;

import it.polimi.deib.se2.mp.weathercal.util.LocalDateTimePersistenceConverter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author paolo
 */
@Entity
@Table(name = "event", schema = "development")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
    @NamedQuery(name = "Event.findById", query = "SELECT e FROM Event e WHERE e.id = :id"),
    @NamedQuery(name = "Event.findByPlaceLatitude", query = "SELECT e FROM Event e WHERE e.placeLatitude = :placeLatitude"),
    @NamedQuery(name = "Event.findByPlaceLongitude", query = "SELECT e FROM Event e WHERE e.placeLongitude = :placeLongitude"),
    @NamedQuery(name = "Event.findByIsPublic", query = "SELECT e FROM Event e WHERE e.isPublic = :isPublic"),
    @NamedQuery(name = "Event.findByPlaceDescription", query = "SELECT e FROM Event e WHERE e.placeDescription = :placeDescription"),
    @NamedQuery(name = "Event.findByIsOutdoor", query = "SELECT e FROM Event e WHERE e.isOutdoor = :isOutdoor"),
    @NamedQuery(name = "Event.findByStart", query = "SELECT e FROM Event e WHERE e.start = :start"),
    @NamedQuery(name = "Event.findByEnd", query = "SELECT e FROM Event e WHERE e.end = :end"),
    @NamedQuery(name = "Event.findByName", query = "SELECT e FROM Event e WHERE e.name = :name"),
    @NamedQuery(name = "Event.findByDescription", query = "SELECT e FROM Event e WHERE e.description = :description"),
    @NamedQuery(name = "Event.deleteById", query="DELETE FROM Event e WHERE e.id = :id")})
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "place_latitude", precision = 9, scale = 6, nullable = false)
    @NotNull(message = "May not be empty")
    private BigDecimal placeLatitude;
    @Column(name = "place_longitude", precision = 9, scale = 6, nullable = false)
    @NotNull(message = "May not be empty")
    private BigDecimal placeLongitude;
    @Basic(optional = false)
    @NotNull(message = "May not be empty")
    @Column(name = "is_public", nullable = false)
    private boolean isPublic = false;
    @Basic(optional = false)
    @NotNull(message = "May not be empty")
    @Size(min = 1, max = 500)
    @Column(name = "place_description", nullable = false, length = 500)
    private String placeDescription;
    @Basic(optional = false)
    @NotNull(message = "May not be empty")
    @Column(name = "is_outdoor", nullable = false)
    private boolean isOutdoor = true;
    @Basic(optional = false)
    @NotNull(message = "May not be empty")
    @Column(name = "start_t", nullable = false,
        columnDefinition= "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime start;
    @Basic(optional = false)
    @NotNull(message = "May not be empty")
    @Column(name = "end_t", nullable = false,
        columnDefinition= "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime end;
    @Basic(optional = false)
    @NotNull(message = "May not be empty")
    @Size(min = 1, max = 300)
    @Column(name = "name", nullable = false, length = 300)
    private String name;
    @Lob
    @Column(name = "description", nullable=false)
    private String description;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<WeatherConstraint> valueConstraints;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<WeatherStateConstraint> stateConstraints;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Owner> owners;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Participation> participation;
    

    
    public Event() {
    }

    public Event(Long id) {
        this.id = id;
    }

    public Event(Long id, boolean isPublic, String placeDescription, boolean isOutdoor, LocalDateTime start, LocalDateTime end, String name) {
        if( start != null && end != null && start.isAfter(end)) throw new ValidationException("End date must be after the start date");
        this.id = id;
        this.isPublic = isPublic;
        this.placeDescription = placeDescription;
        this.isOutdoor = isOutdoor;
        this.start = start;
        this.end = end;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public BigDecimal getPlaceLatitude() {
        return placeLatitude;
    }

    public void setPlaceLatitude(BigDecimal placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public BigDecimal getPlaceLongitude() {
        return placeLongitude;
    }

    public void setPlaceLongitude(BigDecimal placeLongitude) {
        this.placeLongitude = placeLongitude;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getPlaceDescription() {
        return placeDescription;
    }

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }

    public boolean getIsOutdoor() {
        return isOutdoor;
    }

    public void setIsOutdoor(boolean isOutdoor) {
        this.isOutdoor = isOutdoor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.deib.se2.mp.entity.Event[ id=" + id + " ]";
    }

    /**
     * @return the start
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(LocalDateTime start) {
        if( end != null && end.isBefore(start)) throw new ValidationException("Start date must be before the end date");
        this.start = start;
    }

    /**
     * @return the end
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(LocalDateTime end) {
        if( start != null && start.isAfter(end)) throw new ValidationException("End date must be after the start date");
        this.end = end;
    }
    
    public void setDatesTogether(LocalDateTime start, LocalDateTime end){
        if( start != null && start.isAfter(end)) throw new ValidationException("End date must be after the start date");
        if( end != null && end.isBefore(start)) throw new ValidationException("Start date must be before the end date");
        this.start = start;
        this.end = end;
    }

    /**
     * @return the valueConstraints
     */
    public Collection<WeatherConstraint> getValueConstraints() {
        return valueConstraints == null? new ArrayList<>(): valueConstraints;
    }

    /**
     * @return the stateConstraints
     */
    public Collection<WeatherStateConstraint> getStateConstraints() {
        return stateConstraints == null? new ArrayList<>(): stateConstraints;
    }

    /**
     * @param stateConstraints the stateConstraints to set
     */
    public void setStateConstraints(Collection<WeatherStateConstraint> stateConstraints) {
        this.stateConstraints = stateConstraints;
    }

    /**
     * @param valueConstraints the valueConstraints to set
     */
    public void setValueConstraints(Collection<WeatherConstraint> valueConstraints) {
        if(valueConstraints != null && valueConstraints.size() > 1) throw new InvalidParameterException("Invalid length, must be equals to 1. To be implemented.");
        this.valueConstraints = valueConstraints;
    }

    /**
     * @return the owners
     */
    public Collection<Owner> getOwners() {
        return owners == null? new ArrayList<>(): owners;
    }

    /**
     * @param owners the owners to set
     */
    public void setOwners(Collection<Owner> owners) {
        this.owners = owners;
    }

    /**
     * @return the participation
     */
    public Collection<Participation> getParticipation() {
        return participation == null? new ArrayList<>(): participation;
    }

    /**
     * @param participation the participation to set
     */
    public void setParticipation(Collection<Participation> participation) {
        this.participation = participation;
    }
    
}
