/* global ko */
(function (sch, $, ko) {
    jQuery(function () {
        $("#radioControls input").on("click", function (e) {
            $("#radioControls input").each(function () {
                //here self will be the input field and label - label field
                var self = this;
                var label = $("label[for='" + $(self).attr('id') + "']");
                if (self.id === e.target.id) {
                    $(self).attr("checked", "");
                    label.css("color", "white");
                } else {
                    $(self).removeAttr("checked");
                    label.css("color", "#4C4545");
                }
            });
        });
        //the warning modal before deleting an appointment
        $("#deleteDialog").dialog({
            autoOpen: false,
            closeOnEscape: true,
            modal: true,
            position: {my: "center", at: "center", of: window},
            buttons: [
                {
                    text: sch.appointmentLabels.get("confirm-delete"),
                    click: function () {
                        appmodel.deleteAppointment();
                        $(this).dialog("close");
                    }
                }
            ],
            hide: {effect: "fadeOut", duration: 750},
            fluid: true,
            resizable: true,
            height: 'auto',
            width: 'auto', // overcomes width:'auto' and maxWidth bug
            maxWidth: 600,
            open: function () {
                $('.ui-widget-overlay').addClass('custom-overlay');
            },
            close: function () {
                $('.ui-widget-overlay').removeClass('custom-overlay');
            }
        });
        //create two datepickers for specdays and applications scopes
        //we must create one instead of two (using jquery wildcard selector)
        //as the minDate parameter must be different for the two
        $("input[id^='datepicker-sd']").datepicker({
            dateFormat: "yy-mm-dd",
            gotoCurrent: true,
            maxDate: "+6m",
            minDate: new Date()
        });
        $("#datepicker-app").datepicker({
            dateFormat: "yy-mm-dd",
            gotoCurrent: true,
            maxDate: "+6m",
            minDate: "-2y"
        });
    });
//initialize the validation module
    ko.validation.init({
        registerExtenders: true,
        messagesOnModified: true,
        decorateInputElement: true,
        insertMessages: false,
        parseInputAttributes: true,
        messageTemplate: null,
        errorClass: "has-error"
    }, true);

    ko.bindingHandlers.stopBinding = {
        init: function () {
            return {controlsDescendantBindings: true};
        }
    };

//KO 2.1, now allows you to add containerless support for custom bindings
    ko.virtualElements.allowedBindings.stopBinding = true;

    var scopeModel = {
        selectedScope: ko.observable()
    };
// Here's a custom Knockout binding that makes elements shown/hidden via jQuery's fadeIn()/fadeOut() methods
// Could be stored in a separate utility library
    ko.bindingHandlers.fadeVisible = {
        init: function (element, valueAccessor) {
            // Initially set the element to be instantly visible/hidden depending on the value
            var value = valueAccessor();
            $(element).toggle(ko.unwrap(value)); // Use "unwrapObservable" so we 
            //can handle values that may or may not be observable
        },
        update: function (element, valueAccessor) {
            // Whenever the value subsequently changes, slowly fade the element in or out
            var value = valueAccessor();
            ko.unwrap(value) ? $(element).fadeIn() : $(element).fadeOut();
        }
    };

    ko.bindingHandlers.slideVisible = {
        update: function (element, valueAccessor, allBindings) {
            // First get the latest data that we're bound to
            var value = valueAccessor();

            // Next, whether or not the supplied model property is observable, get its current value
            var valueUnwrapped = ko.unwrap(value);

            // Grab some more data from another binding property
            var duration = allBindings.get('slideDuration') || 400; // 400ms is default duration unless otherwise specified

            // Now manipulate the DOM element
            if (valueUnwrapped == true)
                $(element).slideDown(duration); // Make the element visible
            else
                $(element).slideUp(duration);   // Make the element invisible
        }
    };

    ko.bindingHandlers.placeholder = {
        init: function (element, valueAccessor, allBindingsAccessor) {
            var underlyingObservable = valueAccessor();
            ko.applyBindingsToNode(element, {attr: {placeholder: underlyingObservable}});
        }
    };
    //bind the switch of model
    ko.applyBindings(scopeModel);

    //bind the application model
    var appmodel = new sch.appointmentsModel(sch.AppointmentsService);
    ko.applyBindings(appmodel, document.getElementById("appointmentsModel"));

    //bind the special day model
    var sdmodel = new sch.specdaysModel(sch.SpecdaysService);
    ko.applyBindings(sdmodel, document.getElementById("specdaysModel"));
})(window.sch = window.sch || {}, jQuery, ko);