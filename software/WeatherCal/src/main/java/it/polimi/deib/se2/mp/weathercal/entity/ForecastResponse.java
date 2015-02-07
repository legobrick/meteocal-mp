/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.entity;

import it.polimi.deib.se2.mp.weathercal.boundary.EventManager;
import it.polimi.deib.se2.mp.weathercal.entity.WeatherStateConstraint.State;
import static it.polimi.deib.se2.mp.weathercal.gui.WheaterChecker.getWeather;
import static it.polimi.deib.se2.mp.weathercal.gui.WheaterChecker.getWeather16;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import javax.ejb.EJB;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 *
 * @author paolo
 */
public class ForecastResponse implements Serializable {

    private double temperatura;
    private float weatherState;
    private State state;
    private LocalDateTime closestSunny;
   @EJB
    EventManager em=new EventManager();
   
    public double getTemperatura() {
        return temperatura;
    }

    public float getWeatherState() {

        return weatherState;
    }

    public LocalDateTime getClosestSunny() {
        return closestSunny;
    }

    public State getState() {

        return converter(this.weatherState);
    }

    public State converter(float stato) {
        if ((stato > 499 && stato < 532) || (stato > 299 && stato < 322) || (stato > 199 && stato < 231)) {
            return State.RAIN;
        } else if ((stato > 700 && stato < 772)) {
            return State.FOG;
        } else if ((stato > 801 && stato < 805)) {
            return State.CLOUD;
        } else if ((stato > 599 && stato < 623)) {
            return State.SNOW;
        } else if ((stato > 951 && stato < 963)) {
            return State.WIND;
        } else if (stato == 801 || stato == 800 || stato == 904 || stato == 951) {
            return State.SUN;
        }
        return State.SUN;
    }

    public LocalDateTime weatherDailyNext(Event e) throws JSONException {
        float lat = e.getPlaceLatitude().floatValue();
        float lon = e.getPlaceLongitude().floatValue();
        String jas;
        jas = getWeather16(lat, lon, 16);
        JSONObject jObj = new JSONObject(jas);
        String list = jObj.getString("list");
        JSONArray object2 = new JSONArray(list);
        for (int i = 0; i < object2.length(); i++) {
            JSONObject object3 = object2.getJSONObject(i);
            String date = object3.getString("dt");
            JSONArray jArray1 = object2.getJSONObject(i).getJSONArray("weather");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.ofEpochSecond(Long.valueOf(date), 1, ZoneOffset.UTC);
            ZonedDateTime zd= e.getStart().atZone(em.getTimezone(em.getTimezoneOffset(e,e.getStart())));
           if (dateTime.isAfter(zd.toLocalDateTime())) {
                int ora = zd.getHour();
                String intervallogiorno = "day";
                if (ora >= 18) {
                    intervallogiorno = "eve";
                } else if (ora < 6) {
                    intervallogiorno = "night";
                } else if (ora >= 6 && ora < 12) {
                    intervallogiorno = "morn";
                } else if (ora >= 12 && ora < 18) {
                    intervallogiorno = "day";
                }

                JSONObject jArray2 = object2.getJSONObject(i).getJSONObject("temp");
                double temp = Double.valueOf(jArray2.getString(intervallogiorno)) - 273.15;
                System.out.println("lora:"+zd.getHour()+"vecchia"+e.getStart().getHour()+" temp"+temp);
                
                WeatherConstraint temperatura = e.getValueConstraints().iterator().next();
                boolean stateok = false;
                if (!e.getStateConstraints().isEmpty()) {
                    Collection<WeatherStateConstraint> collState = e.getStateConstraints();
                    System.out.println("vincoli: " + e.getStateConstraints().size());
                    for (WeatherStateConstraint sta : collState) {
                        State t = this.converter(Float.valueOf(jArray1.getJSONObject(0).getString("id")));
                        if (!sta.getWeatherState().equals(t.toString())) {
                            System.out.println("Diverse" + t.toString());
                        } else {
                            stateok = true;
                            System.out.println("Uguale");
                        }
                    }
                //qui fa controllo incorciato tra temperatura e stato
                if (temperatura.getIsTemperatureLowerThan()==true && temp < temperatura.getTemperature().doubleValue() && stateok) {
                    System.out.println(dateTime + " " + jArray1.getJSONObject(0).getString("id") + " " + temp + " Condizioni: " + jArray1.getJSONObject(0).getString("main") + " " + e.getStart() + " " + " " + date + " " + dateTime.getDayOfMonth());
                    return dateTime;
                } else if (temperatura.getIsTemperatureLowerThan()==false && temp > temperatura.getTemperature().doubleValue() && stateok) {
                    System.out.println("No " + jArray1.getJSONObject(0).getString("id") + " " + temp + " Condizioni: " + jArray1.getJSONObject(0).getString("main") + " " + e.getStart() + " " + " " + date + " " + dateTime.getDayOfMonth());
                    return dateTime;
                }
                
                }
                //controllo solo sulla temperatura
                else{
                    if (temperatura.getIsTemperatureLowerThan()==true && temp < temperatura.getTemperature().doubleValue() ) {
                    System.out.println(dateTime + " " + jArray1.getJSONObject(0).getString("id") + " " + temp + " Condizioni: " + jArray1.getJSONObject(0).getString("main") + " " + e.getStart() + " " + " " + date + " " + dateTime.getDayOfMonth());
                    return dateTime;
                } else if (temperatura.getIsTemperatureLowerThan()==false && temp > temperatura.getTemperature().doubleValue() ) {
                    System.out.println("No " + jArray1.getJSONObject(0).getString("id") + " " + temp + " Condizioni: " + jArray1.getJSONObject(0).getString("main") + " " + e.getStart() + " " + " " + date + " " + dateTime.getDayOfMonth());
                    return dateTime;
                }
                
                }

                
              //   JSONObject jArray3 =object2.getJSONObject(i).getJSONObject("weather");

            }
        }
        return null;
    }

    public void weatherJson(Event e) throws JSONException {

        float lat = e.getPlaceLatitude().floatValue();
        float lon = e.getPlaceLongitude().floatValue();
        String jas;
        jas = getWeather(lat, lon, 5);

        JSONObject jObj = new JSONObject(jas);
        String list = jObj.getString("list");
        JSONArray object2 = new JSONArray(list);

        for (int i = 0; i < object2.length(); i++) {

            JSONObject object3 = object2.getJSONObject(i);
            String date = object3.getString("dt_txt");
            JSONArray jArray1 = object2.getJSONObject(i).getJSONArray("weather");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
            JSONObject jArray2 = object2.getJSONObject(i).getJSONObject("main");

            if (dateTime.plusHours(3).isAfter(e.getStart()) && dateTime.isBefore(e.getStart()) /*&& e.getStart().isBefore(dateTime.plusHours(3)*/) {
                System.out.println(jArray1.getJSONObject(0).getString("id") + " " + jArray2.getString("temp") + " " + e.getStart() + " " + " " + date + " " + dateTime.getDayOfMonth());
                this.temperatura = Float.valueOf(jArray2.getString("temp")) - 273.15;
                this.weatherState = Float.valueOf(jArray1.getJSONObject(0).getString("id"));
            }
        }
    }
}
