/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.entity;

import java.io.Serializable;

/**
 *
 * @author paolo
 */
public class TimeZoneResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String STATUS_OK = "OK";
    public static final String STATUS_INVALID_REQUEST = "INVALID_REQUEST";
    public static final String STATUS_OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
    public static final String STATUS_REQUEST_DENIED = "REQUEST_DENIED";
    public static final String STATUS_UNKNOWN_ERROR = "UNKNOWN_ERROR";
    public static final String STATUS_ZERO_RESULTS = "ZERO_RESULTS";

    private int dstOffset;
    private int rawOffset;
    private String timeZoneId;
    private String timeZoneName;
    private String status;
    private String error_message;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (timeZoneId != null ? timeZoneId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TimeZoneResponse)) {
            return false;
        }
        TimeZoneResponse other = (TimeZoneResponse) object;
        if ((this.timeZoneId == null && other.timeZoneId != null) || (this.timeZoneId != null && !this.timeZoneId.equals(other.timeZoneId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.polimi.deib.se2.mp.weathercal.entity.TimeZoneResponse[ id=" + timeZoneId + " ]";
    }

    /**
     * @return the dstOffset
     */
    public int getDstOffset() {
        return dstOffset;
    }

    /**
     * @param dstOffset the dstOffset to set
     */
    public void setDstOffset(int dstOffset) {
        this.dstOffset = dstOffset;
    }

    /**
     * @return the rawOffset
     */
    public int getRawOffset() {
        return rawOffset;
    }

    /**
     * @param rawOffset the rawOffset to set
     */
    public void setRawOffset(int rawOffset) {
        this.rawOffset = rawOffset;
    }

    /**
     * @return the timeZoneId
     */
    public String getTimeZoneId() {
        return timeZoneId;
    }

    /**
     * @param timeZoneId the timeZoneId to set
     */
    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    /**
     * @return the timeZoneName
     */
    public String getTimeZoneName() {
        return timeZoneName;
    }

    /**
     * @param timeZoneName the timeZoneName to set
     */
    public void setTimeZoneName(String timeZoneName) {
        this.timeZoneName = timeZoneName;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the error_message
     */
    public String getError_message() {
        return error_message;
    }

    /**
     * @param error_message the error_message to set
     */
    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
    
}
