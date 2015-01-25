/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author paolo
 */
@Entity
@Table(name = "calendar", schema = "development")
@XmlRootElement
@NamedQueries({
   // @NamedQuery(name = "CalendarEntity.findByEmail", query = "SELECT c FROM user_has_calendar c where c.id_calendar =:id"),
    @NamedQuery(name = "CalendarEntity.findAll", query = "SELECT c FROM CalendarEntity c"),
    @NamedQuery(name = "CalendarEntity.findById", query = "SELECT c FROM CalendarEntity c WHERE c.id = :id"),
    @NamedQuery(name = "CalendarEntity.findByIsPublic", query = "SELECT c FROM CalendarEntity c WHERE c.isPublic = :isPublic")})
public class CalendarEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic(optional = false)
    @NotNull(message = "May not be empty")
    @Column(name = "is_public", nullable = false)
    private boolean isPublic = false;
    @JoinTable(schema = "development", name = "user_has_calendar", joinColumns = {
        @JoinColumn(name = "id_calendar", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "id_user", referencedColumnName = "email", nullable = false)})
    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<User> userCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "calendar")
    private Collection<Owner> ownerCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "calendar")
    private Collection<Participation> participationCollection;

    public CalendarEntity() {
    }

    public CalendarEntity(Long id) {
        this.id = id;
    }

    public CalendarEntity(Long id, boolean isPublic) {
        this.id = id;
        this.isPublic = isPublic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    @XmlTransient
    public Collection<Owner> getOwnerCollection() {
        return ownerCollection;
    }

    public void setOwnerCollection(Collection<Owner> ownerCollection) {
        this.ownerCollection = ownerCollection;
    }

    @XmlTransient
    public Collection<Participation> getParticipationCollection() {
        return participationCollection;
    }

    public void setParticipationCollection(Collection<Participation> participationCollection) {
        this.participationCollection = participationCollection;
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
        if (!(object instanceof CalendarEntity)) {
            return false;
        }
        CalendarEntity other = (CalendarEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.deib.se2.mp.entity.Calendar[ id=" + id + " ]";
    }
    
}
