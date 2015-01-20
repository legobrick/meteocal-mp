/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(["marionette", "text!templates/forecast.html"],
function(Marionette, forecastTemplate){
	return Marionette.ItemView.extend({
		template: _.template(forecastTemplate),
		tagName: 'div',
        className: 'ui-grid-row',
        id: 'forecast_days',
        collectionEvents: {
            update: 'replaceEl'
        },
        replaceEl: function(){
            this.render();
        },
        render : function(){
            if(this.collection.size() == 0)
                this.$el.attr('id', '');
            else
                this.$el.attr('id', this.id);
            var $forecastDetail = this.$el.find("#forecast_detail");
            $forecastDetail.fadeOut("fast", function(){
                $forecastDetail.html();
            });
            return Marionette.ItemView.prototype.render.apply(this, arguments);
        },
		openDetail: function(e){
            var model = this.collection.get($(e.currentTarget).data("id"));
            this.trigger("detail:open", model);
		},
		events:{
			'click .has_detail' : 'openDetail'
		}
	});
});
