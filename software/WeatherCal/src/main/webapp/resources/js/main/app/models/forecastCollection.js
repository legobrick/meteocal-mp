/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(["backbone", "models/forecastModel"],function(Backbone, Forecast){
	return Backbone.Collection.extend({
        model: Forecast
    });
});