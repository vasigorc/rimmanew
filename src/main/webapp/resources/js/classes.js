/* global ko */

(function (sch, $, ko) {
    sch.Appointment = function(id, date, time, type, clientName, email, message) {
        this.id = ko.observable(id);
        this.date = ko.observable(date);
        this.time = ko.observable(time.substring(0, time.length - 3));
        this.type = ko.observable(type);
        this.clientName = ko.observable(clientName);
        this.email = ko.observable(email);
        this.message = ko.observable(message);
    };
})(window.sch = window.sch || {}, jQuery, ko);