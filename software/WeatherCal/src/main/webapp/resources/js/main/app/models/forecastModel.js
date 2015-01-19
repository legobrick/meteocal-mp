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
                dow: when.format("ddd"),
                doy: when.format("D/M"),
                temp: data.temp, //°C
                pressure: data.pressure, //hPa
                humidity: data.humidity, //%
                weather: _.extend(data.weather[0], {
                        icon: "http://openweathermap.org/img/w/" + data.weather.icon + ".png"
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