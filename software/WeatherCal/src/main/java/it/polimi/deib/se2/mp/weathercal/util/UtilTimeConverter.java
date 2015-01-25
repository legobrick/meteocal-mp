/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.util;

import java.time.Instant;
import java.time.LocalDate;
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
        return input == null? null: LocalTime.ofSecondOfDay(((long)input.getTime()/1000) % 86400);
    }
    public static Date localTimeToUtilDate(LocalTime input){
        if(input == null) return null;
        ZonedDateTime zdt = input.atDate(LocalDate.now()).atZone(ZoneId.of("UTC"));
        Instant i = zdt.toInstant();
        return Date.from(i);
    }
}
