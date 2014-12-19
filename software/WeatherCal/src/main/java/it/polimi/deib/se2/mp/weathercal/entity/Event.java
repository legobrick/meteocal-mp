/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "event", catalog = "weathercal", schema = "development")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
    @NamedQuery(name = "Event.findById", query = "SELECT e FROM Event e WHERE e.id = :id"),
    @NamedQuery(name = "Event.findByPlaceLatitude", query = "SELECT e FROM Event e WHERE e.placeLatitude = :placeLatitude"),
    @NamedQuery(name = "Event.findByPlaceLongitude", query = "SELECT e FROM Event e WHERE e.placeLongitude = :placeLongitude"),
    @NamedQuery(name = "Event.findByIsPublic", query = "SELECT e FROM Event e WHERE e.isPublic = :isPublic"),
    @NamedQuery(name = "Event.findByPlaceDescription", query = "SELECT e FROM Event e WHERE e.placeDescription = :placeDescription"),
    @NamedQuery(name = "Event.findByIsOutdoor", query = "SELECT e FROM Event e WHERE e.isOutdoor = :isOutdoor"),
    @NamedQuery(name = "Event.findByWhenT", query = "SELECT e FROM Event e WHERE e.whenT = :whenT"),
    @NamedQuery(name = "Event.findByName", query = "SELECT e FROM Event e WHERE e.name = :name"),
    @NamedQuery(name = "Event.findByDescription", query = "SELECT e FROM Event e WHERE e.description = :description")})
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "place_latitude", precision = 9, scale = 6)
    private BigDecimal placeLatitude;
    @Column(name = "place_longitude", precision = 9, scale = 6)
    private BigDecimal placeLongitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_public", nullable = false)
    private boolean isPublic;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "place_description", nullable = false, length = 500)
    private String placeDescription;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_outdoor", nullable = false)
    private boolean isOutdoor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "when_t", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date whenT;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "name", nullable = false, length = 300)
    private String name;
    @Size(max = 2147483647)
    @Column(name = "description", length = 2147483647)
    private String description;

    public Event() {
    }

    public Event(Long id) {
        this.id = id;
    }

    public Event(Long id, boolean isPublic, String placeDescription, boolean isOutdoor, Date whenT, String name) {
        this.id = id;
        this.isPublic = isPublic;
        this.placeDescription = placeDescription;
        this.isOutdoor = isOutdoor;
        this.whenT = whenT;
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

    public Date getWhenT() {
        return whenT;
    }

    public void setWhenT(Date whenT) {
        this.whenT = whenT;
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
    
}
