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
    init: function() {
        return { controlsDescendantBindings: true };
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
    this.date = ko.observable(new Date(date));
    this.time = ko.observable(time);
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
    self.next= ko.observable();
    self.last = ko.observable();
    $.ajax({
        url: self.serviceURL,
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