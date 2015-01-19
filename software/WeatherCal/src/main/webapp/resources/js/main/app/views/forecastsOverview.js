/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(["marionette", "text!templates/forecast_overview_template.html", "views/forecast"],
function(Marionette, forecastOverviewTmpl, ForecastView){
	return Marionette.ItemView.extend({
        constructor: function() {
            Marionette.View.apply(this, arguments);
            this.days = new ForecastView({ collection: this.model.forecasts});
        },
        modelEvents: {
        },
		template: _.template(forecastOverviewTmpl),
		tagName: 'div',
        className: 'ui-grid-row',
		onRender: function(){
            this.$el.find("#forecast_days").replaceWith(this.days.render().el);
            this.model.on('sync', this.updateCity, this);
		},
        updateCity: function(){
            var html = Marionette.Renderer.render(this.getTemplate(), this.mixinTemplateHelpers(this.serializeData()), this);
            this.$el.find("#place").replaceWith($(html).find("#place"));
        },
		events:{
			'dblclick span' : 'openProject'
		}
	});
});