/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
require(['weather_app','backbone'],function(app, Backbone){
  "use strict";
  PF('new_event_button').jq.appendTo(PF('myschedule').jq.find('td').eq(0).append('<span class="fc-header-space"></span>')).show().find('.fa').css('padding-top', '.15em');
  app.start();
});

