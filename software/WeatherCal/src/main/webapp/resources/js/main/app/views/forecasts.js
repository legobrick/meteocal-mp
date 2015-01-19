/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(["marionette", "views/forecast"],function(Marionette, ForecastView){
	return Marionette.CollectionView.extend({
		itemView: ForecastView,
		tagName: 'ul',
		onRender: function(){
			this.$el.find("ul").hide();
		},
		events : {
		      'click .hitarea' : 'openCloseFolder'
	    },
	    openCloseFolder: function(evt){
    		vent.trigger('hitarea:click',evt);
	    }
	});
});