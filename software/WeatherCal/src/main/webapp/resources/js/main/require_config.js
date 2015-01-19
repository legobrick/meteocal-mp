/*
 * require_config.js
 *
 * Questo file contiene la configurazione base di RequireJS
 *
 * WARNING: non gestire con RequireJS la libreria JQuery
 * ed evitare l'inserimento come AMD di librerie che si basano su di essa
 *
 * Coded by:
 *   - Paolo Polidori - <polyp91@gmail.com.
 */
var require = {
    baseUrl: resourcesUrl + "/js/main/app",
    paths: {
        /* ==== DEPLOY ==== */
        /* ----- LIBS ----- */
//        moment:         '../lib/moment-with-locales.min',
//        underscore:     '../lib/underscore.min',
//        backbone:       '../lib/backbone.min'
//        marionette:     '../lib/backbone.marionette.min'


//        underscore:             'external_libs/underscore',
//        EventGateway:           'internal_libs/eventGateway',
//        director:               'external_libs/director/build/director',
        //text:         'text',
        /* ----- RISORSE ----- */
//        res:                    'application_data',
//        vent:                   'messagingPool',
        /* ----- VIEWMODELS ----- */
//        mvs:                    'objs'

        text:           '../lib/text',
        jquery:         '../lib/jquery',

        /* ===== DEBUG ==== */
        /* ----- LIBS ----- */
        moment:         '../lib/moment-with-locales',
        underscore:     '../lib/underscore',
        backbone:       '../lib/backbone',
        marionette:     '../lib/backbone.marionette'
    },
//    deps: ['knockout', 'mapping'],
//    callback: function (knockout, mapping) {
//        knockout.mapping = mapping;
//    },
    shim: {
        backbone:{
            deps: ['underscore'],
            exports: 'Backbone'
        },
        underscore:{
            exports: '_'
        },
        marionette:{
            deps : ['backbone'],
            exports : 'Marionette'
       },
//        EventGateway: {
//            deps:       ['underscore'],
//            exports:    'EventGateway'
//        },
//        director: {
//            exports:    'Router'
//        }
    }
};
