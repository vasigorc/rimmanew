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
//this has to be taken from the hidden field in the page <- from companyPropertiesBean
var restServiceRoot = "http://localhost:8080/RimmaNew/rest";

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
        $(element).toggle(ko.unwrap(value)); // Use "unwrapObservable" so we can handle values that may or may not be observable
    },
    update: function (element, valueAccessor) {
        // Whenever the value subsequently changes, slowly fade the element in or out
        var value = valueAccessor();
        ko.unwrap(value) ? $(element).fadeIn() : $(element).fadeOut();
    }
};

function Appointment(id, date, time, type, clientName, email, message) {
    this.id = ko.observable(id);
    this.date = ko.observable(date);
    this.time = ko.observable(time.substring(0, time.length - 3));
    this.type = ko.observable(type);
    this.clientName = ko.observable(clientName);
    this.email = ko.observable(email);
    this.message = ko.observable(message);
}
//appointments part
//Initial load
function appointmentsModel() {
    var self = this;
    self.viewName = "Appointments' management";
    self.serviceURL = restServiceRoot + "/appointments";
    self.appointments = ko.observableArray([]);
    self.next = ko.observable();
    self.last = ko.observable();
    self.dateSortAscending = ko.observable(false);
    self.nameSortAscending = ko.observable(false);
    self.typeSortAscending = ko.observable(false);
    self.cleanUp = function () {
        $("#appointmentsTable thead tr th").each(function () {
            $(this).attr('class', 'hand-on-hover');
        });
        $("#appointmentsTable thead tr th i").each(function () {
            $(this).removeAttr('class');
        });
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
        if(self.typeSortAscending())
            $("#appTypeCri").addClass("glyphicon glyphicon-chevron-up");
        else
            $("#appTypeCri").addClass("glyphicon glyphicon-chevron-down");
    };
    $.ajax({
        url: self.serviceURL+"?past=false",
        type: 'get',
        data: null,
        dataType: 'json',
        success: function (data) {
            var appointmentsArray = data.appointments.current;
            self.next = data.appointments.next;
            self.last = data.appointments.last;
            var mappedAppointments = $.map(appointmentsArray, function (item) {
                return new Appointment(item.id, item.date, item.time, item.type, item.clientName,
                        item.email, item.message);
            });
            self.appointments(mappedAppointments);
        },
        error: function (xhr, ajaxOptions, thrownError) {
            var err = xhr.responseText;
            alert(err);
        }
    });
}

ko.applyBindings(scopeModel);
ko.applyBindings(new appointmentsModel(), document.getElementById("appointmentsModel"));