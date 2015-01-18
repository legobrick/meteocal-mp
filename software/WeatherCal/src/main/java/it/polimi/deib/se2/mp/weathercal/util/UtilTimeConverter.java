/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author paolo
 */
public class UtilTimeConverter {
    public static LocalDate utilDateToLocalDate(Date input){
        return input == null? null: input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    public static Date localDateToUtilDate(LocalDate input){
        return input == null? null: Date.from(input.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalTime utilDateToLocalTime(Date input){
        return input == null? null: input.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }
    public static Date localTimeToUtilDate(LocalTime input){
        return input == null? null: Date.from(input.atDate(LocalDate.MIN).atZone(ZoneId.systemDefault()).toInstant());
    }
}
