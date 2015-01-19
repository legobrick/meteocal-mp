/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
require(['weather_app','backbone', 'create_event_app'],function(app, Backbone, CEA){
  "use strict";
  app.start();
  var cea = new CEA();
  cea.start();
});