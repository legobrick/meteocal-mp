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
public class LocalizedEvent extends Event {
    private Date startD;
    private Date endD;
    private ZoneId timezone;
    

    
    public LocalizedEvent() {
    }

    public LocalizedEvent(Long id) {
        super(id);
    }

    public LocalizedEvent(Long id, boolean isPublic, String placeDescription, boolean isOutdoor, Date start, Date end,
            ZoneId timezone, String name, BigDecimal placeLatitude, BigDecimal placeLongitude, String description,
            Collection<WeatherConstraint> valueConstraints, Collection<WeatherStateConstraint> stateConstraints,
            Collection<Owner> owners, Collection<Participation> participation) {
        super(id, isPublic, placeDescription, isOutdoor, null, null, name);
        setPlaceLatitude(placeLatitude);
        setPlaceLongitude(placeLongitude);
        setDescription(description);
        setValueConstraints(valueConstraints);
        setStateConstraints(stateConstraints);
        setOwners(owners);
        setParticipation(participation);
        this.startD = start;
        this.endD = end;
        this.timezone = timezone;
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
        Event e = new Event(getId(), getIsPublic(), getPlaceDescription(), getIsOutdoor(), utilDateToLocalDateTime(startD), utilDateToLocalDateTime(endD), getName());
        e.setPlaceLatitude(getPlaceLatitude());
        e.setPlaceLongitude(getPlaceLongitude());
        e.setDescription(getDescription());
        e.setValueConstraints(getValueConstraints());
        e.setStateConstraints(getStateConstraints());
        e.setOwners(getOwners());
        e.setParticipation(getParticipation());
        return e;
    }
    public Event toServerEvent(){
        Event e = new Event(getId(), getIsPublic(), getPlaceDescription(), getIsOutdoor(), utilDateToLocalDateTime(startD, timezone), utilDateToLocalDateTime(endD, timezone), getName());
        e.setPlaceLatitude(getPlaceLatitude());
        e.setPlaceLongitude(getPlaceLongitude());
        e.setDescription(getDescription());
        e.setValueConstraints(getValueConstraints());
        e.setStateConstraints(getStateConstraints());
        e.setOwners(getOwners());
        e.setParticipation(getParticipation());
        return e;
    }

    /**
     * @return the startD
     */
    public Date getStartD() {
        return startD;
    }

    /**
     * @param startD the startD to set
     */
    public void setStartD(Date startD) {
        if( endD != null && endD.before(startD)) throw new ValidationException("Start date must be before the end date");
        this.startD = startD;
    }

    /**
     * @return the endD
     */
    public Date getEndD() {
        return endD;
    }

    /**
     * @param endD the endD to set
     */
    public void setEndD(Date endD) {
        if( startD != null && startD.after(endD)) throw new ValidationException("End date must be after the start date");
        this.endD = endD;
    }
    
    public void setUtcDatesTogether(Date start, Date end){
        if( start != null && start.after(end)) throw new ValidationException("End date must be after the start date");
        if( end != null && end.before(start)) throw new ValidationException("Start date must be before the end date");
        this.startD = start;
        this.endD = end;
    }
    
    private Date localToUtc(Date input){
        return Date.from(input.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime().atZone(timezone).toInstant());
    }
    
    public void setLocalDatesTogether(Date start, Date end){
        start = localToUtc(start); end = localToUtc(end);
        if( start != null && start.after(end)) throw new ValidationException("End date must be after the start date");
        if( end != null && end.before(start)) throw new ValidationException("Start date must be before the end date");
        this.startD = start;
        this.endD = end;
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

    /**
     * @return the timezone
     */
    public int getMinutesTimezone() throws Exception {
        return 0;
    }

    /**
     * @param timezone the timezone to set
     */
    public void setMinutesTimezone(int timezone) {
        this.timezone = it.polimi.deib.se2.mp.weathercal.util.UtilTimeConverter.getTimezone(timezone);
    }
}
