/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['moment', 'underscore'], function (moment, _) {
    "use strict";
    var App = function(){};
    App.prototype = {
        start: function(){
            this.setUserTZ().initGMap().initDatePair().initTempSelector();
        },
        exportPF: function(widgetVars){
            _.each(widgetVars, function(v){
                this["PF" + v] = PF(v);
                this["$" + v] = PF(v).getJQ();
            }, this);
            return this;
        },
        setUserTZ: function(){
            moment().format();
            this.exportPF(['userTimezone']).$userTimezone.val(moment().utcOffset());
            return this;
        },
        timer: 0,
        _map: null,
        get map(){
            this._map === null && (this._map = PF('geoMap').map);
            return this._map;
        },
        set map(arg){},
        delay: function(callback, ms) {
            clearTimeout (this.timer);
            this.timer = setTimeout(callback, ms);
            return this;
        },
        geocode: function(){
            var me = this;
            this.myGeocoder.geocode({
                address: this.$address.val()
            }, function(results, status){
                if (status === google.maps.GeocoderStatus.OK)
                {
                    me.$latitude.val(results[0].geometry.location.lat());
                    me.$longitude.val(results[0].geometry.location.lng());
                    var bounds = new google.maps.LatLngBounds(
                        results[0].geometry.viewport.getSouthWest(), 
                        results[0].geometry.viewport.getNorthEast()
                    );
                    me.map.__proto__.fitBounds.call(me.map, bounds);
                    me.marker.setValues({
                        position: results[0].geometry.location,
                        map: me.map,
                        title: results[0].formatted_address,
                        draggable: false,
                        animation: google.maps.Animation.BOUNCE
                    });
                    me.delay(function(){
                        me.marker.setAnimation(null);
                    }, 500);
                }
            });
            return this;
        },
        initGMap: function(){
            var me = this;
            var geocode = function(){
                me.geocode.apply(me, arguments);
            };
            this.myGeocoder = new google.maps.Geocoder();
            this.marker = new google.maps.Marker();
            this.exportPF(['address', 'latitude', 'longitude', 'go']);
            this.$address.keyup(function() {
                me.delay(geocode, 8000);
            });
            this.$go.on('click', geocode);
            return this;
        },
        initDatePair: function(){
            this.exportPF(['startTime', 'startDate', 'endTime', 'endDate']);
            var $time = this.$startTime.add(this.$endTime);
            var $date = this.$startDate.add(this.$endDate);
            // initialize input widgets first
            $time.timepicker({
                show2400: true,
                'showDuration': true,
                'timeFormat': 'H:i'
            });

            $date.datepicker({
                dateFormat: 'dd/mm/yy'
            });

            // initialize datepair
            $('#form-part-2').datepair({
                parseDate: function(input){
                    return $(input).datepicker( "getDate" );
                },
                updateDate: function(input, dateObj){
                    return $(input).datepicker( "setDate", dateObj );
                }
            });
            return this;
        },
        initTempSelector: function(){
            this.exportPF(['tempSelector', 'tempDir', 'tempVal']);
            var enabled = this.$tempSelector.hasClass('ui-state-active');
            var $tempDir = this.PFtempDir;
            this.$tempDir.off('click.inputSwitch').on('click.inputSwitch', function(e) {
                $tempDir.toggle();
            });
            $tempDir.input.off('keydown.inputSwitch').off('keyup.inputSwitch').off('change.inputSwitch')
                .on('keydown.inputSwitch', function(e) {
                    var keyCode = $.ui.keyCode;
                    if(e.which === keyCode.SPACE && enabled) {
                        e.preventDefault();
                    }
                })
                .on('keyup.inputSwitch', function(e) {
                    var keyCode = $.ui.keyCode;
                    if(e.which === keyCode.SPACE && enabled) {
                        $tempDir.toggle();

                        e.preventDefault();
                    }
                })
                .on('change.inputSwitch', function(e) {
                    if(enabled){
                        if($tempDir.input.prop('checked'))
                            $tempDir._checkUI();
                        else
                            $tempDir._uncheckUI();
                    }
                });
                    
            var $tempVal = this.PFtempVal;

            this.$tempVal.children('.ui-spinner-button').off('mouseup.spinner').off('mousedown.spinner')
            .on('mouseup.spinner', function() {
                clearInterval($tempVal.timer);
                $(this).removeClass('ui-state-active').addClass('ui-state-hover');
                if(enabled) $tempVal.input.trigger('change');
            })
            .on('mousedown.spinner', function(e) {
                var element = $(this),
                dir = element.hasClass('ui-spinner-up') ? 1 : -1;

                element.removeClass('ui-state-hover').addClass('ui-state-active');
                
                if($tempVal.input.is(':not(:focus)')) {
                    $tempVal.input.focus();
                }

                if(enabled) $tempVal.repeat(null, dir);

                //keep focused
                e.preventDefault();
            });

            $tempVal.input.on('keydown.spinner', function (e) {
                if(!enabled) return;
                var keyCode = $.ui.keyCode;

                switch(e.which) {
                    case keyCode.UP:
                        $tempVal.spin(1);
                    break;

                    case keyCode.DOWN:
                        $tempVal.spin(-1);
                    break;

                    default:
                        //do nothing
                    break;
                }
            })
            .on('keyup.spinner', function (e) { 
                if(!enabled) return;
                $tempVal.updateValue();

                var keyCode = $.ui.keyCode;
                if(e.which === keyCode.UP||e.which === keyCode.DOWN) {
                    $tempVal.input.trigger('change');
                }
            })
            .on('blur.spinner', function(e) {
                $tempVal.format();
            })
            .on('mousewheel.spinner', function(event, delta) {
                if($tempVal.input.is(':focus') && enabled) {
                    if(delta > 0)
                        $tempVal.spin(1);
                    else
                        $tempVal.spin(-1);

                    return false;
                }
            });
            var activationClassSetter = function(e){
                if($(this).hasClass('ui-state-active')){ //clicked, activate
                    e.data.$tempDir.removeClass('ui-state-disabled').find('input').removeAttr('disabled');
                    e.data.$tempVal.removeClass('ui-state-disabled').find('input').removeAttr('disabled')
                            .prop('role', 'spinner').prop('aria-disabled', 'false').prop('aria-readonly', 'false');
                    enabled = true;
                } else { //deactivate
                    e.data.$tempDir.addClass('ui-state-disabled').find('input').attr('disabled', 'disabled');
                    e.data.$tempVal.addClass('ui-state-disabled').find('input').attr('disabled', 'disabled')
                            .prop('role', 'textbox').prop('aria-disabled', 'true');
                    enabled = false;
                }
            };
            activationClassSetter({ data: this });
            this.$tempSelector.on('click', this, activationClassSetter);
            return this;
        }
    };
    return App;
});
