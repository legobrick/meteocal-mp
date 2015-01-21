/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(["backbone", "moment"],function(Backbone, moment){
	return Backbone.Model.extend({
        idAttribute: 'dt',
        parse : function( data, xhr ) {
            var when = moment.unix(data.dt);
            return {
                dt: data.dt,
                dow: when.format("ddd"),
                doy: when.format("D/M"),
                temp: _.each(data.temp, function(v, k, temps){
                    temps[k] = Math.round(parseFloat( v - 273.15));
                }), //°K
                pressure: Math.round(parseFloat(data.pressure)), //hPa
                humidity: data.humidity, //%
                weather: _.extend(data.weather[0], {
                        icon: "http://openweathermap.org/img/w/" + data.weather[0].icon + ".png",
                        description: data.weather[0].description[0].toUpperCase() + data.weather[0].description.substring(1)
                    }
                ),
                speed: data.speed, //km/h
                deg: data.deg, //°
                clouds: data.clouds, //%
                rain: data.rain, //mm
                snow: data.snow //mm
            };
        }
    });
});