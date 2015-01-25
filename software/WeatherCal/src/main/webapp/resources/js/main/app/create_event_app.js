/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['moment', 'underscore'], function (moment, _) {
    "use strict";
    var App = function(){};
    var latLngChecker = /^(\-?1?\d{1,2}(\.(\d)+)?)$/;
    var error = '<div class="ui-messages ui-widget nontext-center" aria-live="polite" data-global="false" data-summary="data-summary" data-detail="data-detail" data-severity="all,error" data-redisplay="true" style="position: relative;margin-top: auto;margin-bottom: auto;width: 40%;height: 60%;"><div class="ui-messages-error ui-corner-all text-center" style="height: 100%;"><span class="ui-messages-error-icon" style="position: relative;top: 43%;width: 48px;height: 48px;background-size: cover;background-position: initial;margin-left: 10px;"></span><ul style="position: relative;top: 45%;font-size: 1.4em;"><li><span class="ui-messages-error-summary">Error!</span><span class="ui-messages-error-detail">Missing internet connection.</span></li></ul></div></div>';
    App.prototype = {
        start: function(){
            this.setUserTZ().initTempSelector().initGMap().initDatePair();
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
            this.exportPF(['startTime', 'startDate', 'endTime', 'endDate', 'userTimezone']);
            this.$userTimezone.val(moment().utcOffset());
            if(!_.isNull(startDT)){
               var start = moment.utc(startDT).local(moment().utcOffset());
               this.$startDate.val(start.format("DD/MM/YYYY"));
               this.$startTime.val(start.format("HH:mm"));
            }
            if(!_.isNull(endDT)){
               var end = moment.utc(endDT).local(moment().utcOffset());
               this.$endDate.val(end.format("DD/MM/YYYY"));
               this.$endTime.val(end.format("HH:mm"));
            }
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
        geoCallback : function(results, status){
            if (status === google.maps.GeocoderStatus.OK)
            {
                this.$latitude.val(results[0].geometry.location.lat());
                this.$longitude.val(results[0].geometry.location.lng());
                var bounds = new google.maps.LatLngBounds(
                    results[0].geometry.viewport.getSouthWest(), 
                    results[0].geometry.viewport.getNorthEast()
                );
                this.map.__proto__.fitBounds.call(this.map, bounds);
                this.marker.setValues({
                    position: results[0].geometry.location,
                    map: this.map,
                    title: results[0].formatted_address,
                    draggable: false,
                    animation: google.maps.Animation.BOUNCE
                });
                var me = this;
                this.delay(function(){
                    me.marker.setAnimation(null);
                }, 500);
            }
        },
        geocode: function(e){
            if(e){
                e.stopPropagation();
                e.preventDefault();
            }
            var me = this;
            this.myGeocoder.geocode({
                address: this.$address.val()
            }, function(){ me.geoCallback.apply(me, arguments);});
            return this;
        },
        initGMap: function(){
            var me = this;
            if(google === undefined){
                PF('geoMap').jq.replaceWith(error);
                PF('create_submit').jq.attr('disabled', 'disabled');
                return false;
            }
            var geocode = function(){
                me.geocode.apply(me, arguments);
            };
            this.myGeocoder = new google.maps.Geocoder();
            this.marker = new google.maps.Marker();
            this.exportPF(['address', 'latitude', 'longitude', 'go']);
            if(latLngChecker.test(this.$latitude.val()) && latLngChecker.test(this.$longitude.val())){
                var oldLat = this.$latitude.val();
                var oldLng = this.$longitude.val();
                this.geocode({stopPropagation:function(){},preventDefault:function(){}});
                if(oldLat != this.$latitude.val() || oldLng != this.$longitude.val())
                    console.error("New position is different from the preceding one: why?!\nOLD: " + oldLat + ", "
                        + oldLng + "\nNEW: " + this.$latitude.val() + ", " + this.$longitude.val());
            }
            this.$address.keyup(function() {
                me.delay(geocode, 8000);
            });
            this.$go.on('click', geocode);
            return this;
        },
        initDatePair: function(){
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
            activationClassSetter.call(this.$tempSelector, { data: this });
            this.$tempSelector.on('click', this, activationClassSetter);
            return this;
        }
    };
    return App;
});
