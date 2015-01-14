/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.gui;

import it.polimi.deib.se2.mp.weathercal.boundary.UserManager;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import sun.net.www.http.HttpClient;

/**
 *
 * @author miglie
 */
@ManagedBean
@ApplicationScoped
public class WheaterChecker{

@PostConstruct
  public void getWeatherJson(){
       // HttpClient webIstemci = new DefaultHttpClient();
  //      HttpGet webdenGetir = new HttpGet("http://api.openweathermap.org/data/2.5/weather?q=London,uk");
  }
 
 
}
