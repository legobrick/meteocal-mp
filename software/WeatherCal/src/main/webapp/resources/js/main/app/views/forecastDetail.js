/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(["marionette", "text!templates/weather_detail.html"],function(Marionette, WeatherDetailTmpl){
	return Marionette.ItemView.extend({
        template: _.template(WeatherDetailTmpl),
		tagName: 'div',
        id: 'forecast_detail',
        className: 'ui-grid-col-12 forecast_detail_outer'
	});
});