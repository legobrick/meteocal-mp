/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.entity;

import static it.polimi.deib.se2.mp.weathercal.util.UtilTimeConverter.localDateTimeToUtilDate;
import static it.polimi.deib.se2.mp.weathercal.util.UtilTimeConverter.utilDateToLocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import javax.validation.ValidationException;

/**
 *
 * @author paolo
 */
public class LocalizedEvent implements Serializable {
    private Long id;
    private BigDecimal placeLatitude;
    private BigDecimal placeLongitude;
    private boolean isPublic = false;
    private String placeDescription;
    private boolean isOutdoor = true;
    private Date start;
    private Date end;
    private ZoneId timezone;
    private String name;
    private String description;
    private Collection<WeatherConstraint> valueConstraints;
    private Collection<WeatherStateConstraint> stateConstraints;
    private Collection<Owner> owners;
    private Collection<Participation> participation;
    

    
    public LocalizedEvent() {
    }

    public LocalizedEvent(Long id) {
        this.id = id;
    }

    public LocalizedEvent(Long id, boolean isPublic, String placeDescription, boolean isOutdoor, Date start, Date end,
            ZoneId timezone, String name, BigDecimal placeLatitude, BigDecimal placeLongitude, String description,
            Collection<WeatherConstraint> valueConstraints, Collection<WeatherStateConstraint> stateConstraints,
            Collection<Owner> owners, Collection<Participation> participation) {
        this.id = id;
        this.isPublic = isPublic;
        this.placeDescription = placeDescription;
        this.isOutdoor = isOutdoor;
        this.start = start;
        this.end = end;
        this.timezone = timezone;
        this.name = name;
        
        this.placeLatitude = placeLatitude;
        this.placeLongitude = placeLongitude;
        this.description = description;
        this.valueConstraints = valueConstraints;
        this.stateConstraints = stateConstraints;
        this.owners = owners;
        this.participation = participation;
    }
    
    public static LocalizedEvent from(Event e, ZoneId userTz){
        LocalizedEvent le = new LocalizedEvent(
                e.getId(), e.getIsPublic(), e.getPlaceDescription(), e.getIsOutdoor(),
                localDateTimeToUtilDate(e.getStart()), localDateTimeToUtilDate(e.getEnd()), userTz, e.getName(),
                e.getPlaceLatitude(), e.getPlaceLongitude(), e.getDescription(), e.getValueConstraints(),
                e.getStateConstraints(), e.getOwners(), e.getParticipation()
        );
        return le;
    }
    
    public Event toClientEvent(){
        return new Event(id, isPublic, placeDescription, isOutdoor, utilDateToLocalDateTime(start), utilDateToLocalDateTime(end), name);
    }
    public Event toServerEvent(){
        return new Event(id, isPublic, placeDescription, isOutdoor, utilDateToLocalDateTime(start, timezone), utilDateToLocalDateTime(end, timezone), name);
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
        if (!(object instanceof LocalizedEvent)) {
            return false;
        }
        LocalizedEvent other = (LocalizedEvent) object;
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
    public Date getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(Date start) {
        if( end != null && end.before(start)) throw new ValidationException("Start date must be before the end date");
        this.start = start;
    }

    /**
     * @return the end
     */
    public Date getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(Date end) {
        if( start != null && start.after(end)) throw new ValidationException("End date must be after the start date");
        this.end = end;
    }
    
    public void setDatesTogether(Date start, Date end){
        if( start != null && start.after(end)) throw new ValidationException("End date must be after the start date");
        if( end != null && end.before(start)) throw new ValidationException("Start date must be before the end date");
        this.start = start;
        this.end = end;
    }

    /**
     * @return the valueConstraints
     */
    public Collection<WeatherConstraint> getValueConstraints() {
        return valueConstraints;
    }

    /**
     * @return the stateConstraints
     */
    public Collection<WeatherStateConstraint> getStateConstraints() {
        return stateConstraints;
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
        return owners;
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
        return participation;
    }

    /**
     * @param participation the participation to set
     */
    public void setParticipation(Collection<Participation> participation) {
        this.participation = participation;
    }

    /**
     * @return the timezone
     */
    public ZoneId getTimezone() {
        return timezone;
    }

    /**
     * @param timezone the timezone to set
     */
    public void setTimezone(ZoneId timezone) {
        this.timezone = timezone;
    }
    
}
