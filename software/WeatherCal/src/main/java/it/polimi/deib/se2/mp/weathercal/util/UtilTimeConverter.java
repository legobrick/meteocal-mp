/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 *
 * @author paolo
 */
public class UtilTimeConverter {
    public static LocalDate utilDateToLocalDate(Date input){
        return input == null? null: LocalDate.ofEpochDay(input.getTime()/86400000);
    }
    public static Date localDateToUtilDate(LocalDate input){
        return input == null? null: Date.from(input.atStartOfDay(ZoneId.of("UTC")).toInstant());
    }

    public static LocalTime utilDateToLocalTime(Date input){
        return input == null? null: input.toInstant().atZone(ZoneId.of("UTC")).toLocalTime();
    }
    public static Date localTimeToUtilDate(LocalTime input){
        if(input == null) return null;
        ZonedDateTime zdt = input.atDate(LocalDate.now()).atZone(ZoneId.of("UTC"));
        Instant i = zdt.toInstant();
        return Date.from(i);
    }
    
    public static Date localDateTimeToUtilDate(LocalDateTime input){
        return input == null? null: Date.from(input.atZone(ZoneId.of("UTC")).toInstant());
    }
    public static LocalDateTime utilDateToLocalDateTime(Date input){
        return input == null? null: input.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime();
    }
    public static LocalDateTime utilDateToLocalDateTime(Date input, ZoneId zone){
        return input == null? null: input.toInstant().atZone(zone).toLocalDateTime();
    }
}
