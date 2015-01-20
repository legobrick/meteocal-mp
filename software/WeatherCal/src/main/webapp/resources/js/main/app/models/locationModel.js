/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(["backbone", "models/forecastCollection"],function(Backbone, Forecasts){
	return Backbone.Model.extend({
        initialize: function(){
            this.forecasts = new Forecasts();
            var me = this;
            navigator.geolocation.watchPosition(
                    function(){me.setPosition.apply(me, arguments);},
                    function(){me.setErrorState.apply(me, arguments);},
                    {timeout:900000, maximumAge: 0}
            ); //every 15mins
        },
        latitude: 0,
        longitude: 0,
        age: Infinity,
        defaults: {
                city_name: "",
                country: ""
        },
        setPosition: function(pos){
            var crd = pos.coords;
            if(crd.latitude == this.latitude && crd.longitude == this.longitude) return;
            this.latitude = crd.latitude;
            this.longitude = crd.longitude;
            this.age = 0;
            this.fetch();
        },
        setErrorState : function(){
            this.trigger("weather:error");
        },
        url: "http://api.openweathermap.org/data/2.5/forecast/daily",
        parse : function( data, xhr ) {
            return {
                city_name: data.city.name,
                country: data.city.country
            };
        },
        fetch: function(options){
            options = _.extend(options || {}, {
                crossDomain:    true,
                dataType:       'jsonp',
                data: {
                    cnt:    6,
                    lat:    this.latitude,
                    lon:    this.longitude
                }
            });
            var trigger = this.trigger;
            options.error = function(){
                trigger("weather:error");
            };
            var success = options.success;
            var model = this;
            options.success = function(model, resp, options) {
                model.forecasts.set(resp.list, options);
                model.forecasts.trigger("update");
                if (success) success(model, resp, options);
            };
            return Backbone.Model.prototype.fetch.call(this, options);
        }
    });
});

