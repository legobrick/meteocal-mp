/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define([
    'marionette',
    'models/locationModel',
    'views/forecastsOverview'],
  function(Marionette, LocationModel, ForecastOverview){
    "use strict";

    var app = new Marionette.Application();

    app.addRegions({
        weather:    '#backboneForecast'
    });

    app.addInitializer(function(){
        app.weather.show(new ForecastOverview({ model: new LocationModel()}));
    });
    return app;
  }
);