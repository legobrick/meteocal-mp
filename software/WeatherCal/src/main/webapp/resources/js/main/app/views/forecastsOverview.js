/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(["marionette", "text!templates/forecast_overview_template.html", "views/forecast", "views/forecastDetail", "text!templates/forecast_error_template.html"],
function(Marionette, forecastOverviewTmpl, ForecastView, ForecastDetail, ErrorTmpl){
	return Marionette.ItemView.extend({
        constructor: function() {
            Marionette.View.apply(this, arguments);
            this.detail = new ForecastDetail();
            this.days = new ForecastView({ collection: this.model.forecasts });
            this.days.on("detail:open", this.openDetail, this);
        },
        modelEvents: {
            "weather:error": 'showError'
        },
		template: _.template(forecastOverviewTmpl),
		tagName: 'div',
        className: 'ui-grid-row',
		onRender: function(){
            var $el = this.$el;
            $el.find("#forecast_days").replaceWith(this.days.render().el);
            this.model.on('sync', this.updateCity, this);
            $el.find("#forecast_detail").fadeOut(100, function(){
                $el.find("#forecast_detail").html();
            });
		},
        updateCity: function(){
            var html = Marionette.Renderer.render(this.getTemplate(), this.mixinTemplateHelpers(this.serializeData()), this);
            this.$el.find("#forecast_days").show();
            this.$el.find("#place").replaceWith($(html).find("#place"));
            this.$el.find("#forecast_detail").fadeOut(100);
        },
        openDetail: function(model){
            this.detail.model = model;
            var $fd = this.$el.find("#forecast_detail");
            var detail = this.detail;
            $fd.fadeOut(100, function(){
                $fd.replaceWith(detail.render().$el.fadeOut(100)).fadeIn(100);
            });
        },
        showError: function(){
            var $new = Marionette.Renderer.render(_.template(ErrorTmpl), null, this);
            var $el = this.$el;
            $el.find("#place").fadeOut(100, function(){
                $el.find("#place").html($new);
                $el.find("#place").fadeIn(100);
            });
            $el.find("#forecast_days").fadeOut(100, function(){
                $el.find("#forecast_days div").detach();
            });
            $el.find("#forecast_detail div").fadeOut(100, function(){
                $el.find("#forecast_detail div").detach();
            });
        }
	});
});