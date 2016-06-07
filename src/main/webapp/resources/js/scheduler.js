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
    });


//knockout part
//initialize the validation module
    ko.validation.init({
        registerExtenders: true,
        messagesOnModified: true,
        decorateInputElement: true,
        insertMessages: true,
        parseInputAttributes: true,
        messageTemplate: null,
        errorClass: "has-error"
    }, true);

    sch.currentLocale = $('#pageLocale').val();
    if (sch.currentLocale.includes("fr")) {
        ko.validation.locale('fr-FR');
    } else if (sch.currentLocale.includes("ru")) {
        ko.validation.locale('ru-RU');
    } else {
        ko.validation.locale('en-US');
    }

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

//appointments part
    sch.appointmentsModel = function (dataService) {
        var self = this;
        self.viewName = "Appointments' management";
        self.appointments = ko.observableArray([]);
        self.first = ko.observable();
        self.next = ko.observable();
        self.last = ko.observable();
        self.previous = ko.observable();
        self.dateSortAscending = ko.observable(false);
        self.nameSortAscending = ko.observable(false);
        self.typeSortAscending = ko.observable(false);
        //this is the container for all model inputs
        self.filters = ko.observable({
            clientName: ko.observable('').extend({
                required: false,
                minLength: 4,
                maxLength: 20
            }),
            typeOptions:['massage', 'waxing', 'pedicure', 'manicure'],
            type: ko.observable('').extend({
                required: false
            })
        });
        self.toJSON = function () {
            var copy = ko.toJS(self);
            delete copy.dateSortAscending;
            delete copy.nameSortAscending;
            delete copy.typeSortAscending;
            delete copy.viewName;
            return copy;
        };
        self.cleanUp = function () {
            $("#appointmentsTable thead tr th").each(function () {
                $(this).attr('class', 'hand-on-hover');
            });
            $("#appointmentsTable thead tr th i").each(function () {
                $(this).removeAttr('class');
            });
        };
        self.showButtons = function (data) {
            if (data.next != null) {
                self.next(data.next);
                $("#step-forward").removeClass("move-button-invisible");
            } else {
                $("#step-forward").addClass("move-button-invisible");
            }
            if (data.last != null) {
                self.last(data.last);
                $("#fast-forward").removeClass("move-button-invisible");
            } else {
                $("#fast-forward").addClass("move-button-invisible");
            }
            if (data.previous != null) {
                self.previous(data.previous);
                $("#step-backward").removeClass("move-button-invisible");
            } else {
                $("#step-backward").addClass("move-button-invisible");
            }
            if (data.first != null) {
                console.log(data.first);
                self.first(data.first);
                $("#fast-backward").removeClass("move-button-invisible");
            } else {
                $("#fast-backward").addClass("move-button-invisible");
            }
        };
        self.sortByDate = function () {
            self.appointments.sort(function (a, b) {
                first = new Date(a);
                second = new Date(b);
                return first > second ? -1 : 1;
            });
            self.dateSortAscending(!self.dateSortAscending());
            self.cleanUp();
            $("#appDateCri").parent().addClass("selected-tab");
            if (self.dateSortAscending()) {
                $("#appDateCri").addClass("glyphicon glyphicon-chevron-up");
            } else {
                $("#appDateCri").addClass("glyphicon glyphicon-chevron-down");
            }
        };
        self.sortByName = function () {
            self.appointments.sort(function (a, b) {
                return a > b ? -1 : 1;
            });
            self.nameSortAscending(!self.nameSortAscending());
            self.cleanUp();
            $("#appNameCri").parent().addClass("selected-tab");
            if (self.nameSortAscending())
                $("#appNameCri").addClass("glyphicon glyphicon-chevron-up");
            else
                $("#appNameCri").addClass("glyphicon glyphicon-chevron-down");
        };
        self.sortByType = function () {
            self.appointments.sort(function (a, b) {
                return a > b ? -1 : 1;
            });
            self.typeSortAscending(!self.typeSortAscending());
            self.cleanUp();
            $("#appTypeCri").parent().addClass("selected-tab");
            if (self.typeSortAscending())
                $("#appTypeCri").addClass("glyphicon glyphicon-chevron-up");
            else
                $("#appTypeCri").addClass("glyphicon glyphicon-chevron-down");
        };
        dataService.getAppointments(self.toJSON, function (data) {
            self.appointments(data.appointments);
            self.showButtons(data);
        });
        self.getNext = function () {
            dataService.getNext(self.toJSON, function (data) {
                self.appointments(data.appointments);
                self.showButtons(data);
            });
        };
        self.getPrevious = function () {
            dataService.getPrevious(self.toJSON, function (data) {
                self.appointments(data.appointments);
                self.showButtons(data);
            });
        };
        self.getLast = function () {
            dataService.getLast(self.toJSON, function (data) {
                self.appointments(data.appointments);
                self.showButtons(data);
            });
        };
        self.getFirst = function () {
            dataService.getFirst(self.toJSON, function (data) {
                self.appointments(data.appointments);
                self.showButtons(data);
            });
        };
    };
    ko.applyBindings(scopeModel);
    ko.applyBindings(new sch.appointmentsModel(sch.AppointmentsService), document.getElementById("appointmentsModel"));
})(window.sch = window.sch || {}, jQuery, ko);