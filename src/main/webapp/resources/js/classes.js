/* global ko */

(function (sch, $, ko) {
    sch.Appointment = function (id, date, time, type, clientName, email, message) {
        this.id = ko.observable(id);
        this.date = ko.observable(date);
        this.time = ko.observable(time.substring(0, time.length - 3));
        this.type = ko.observable(type);
        this.clientName = ko.observable(clientName);
        this.email = ko.observable(email);
        this.message = ko.observable(message);
        var self = this;
        self.displayEdits = ko.observable(false);
        self.toggleEdits = function () {
            self.displayEdits(!self.displayEdits());
        };
    };
    sch.EntryAppointment = function (init) {
        if (init === undefined) {
            sch.Appointment.call(this, "", "", "", "", "", "", "");
        }
        this.date.extend({
            required: true,
            date: true,
            minLength: 10
        }).extend({rateLimit: 530});
        this.time.extend({
            required: true,
            pattern: '^([01]?[0-9]|2[0-3]):[0-5][0-9]'
        }).extend({rateLimit: 500});
        this.type.extend({
            required: true
        }).extend({rateLimit: 500});
        this.clientName.extend({
            required: true,
            minLength: 4,
            maxLength: 20
        }).extend({rateLimit: 500});
        this.email.extend({
            required: true,
            email: true
        }).extend({rateLimit: 550});
        this.message.extend({
            maxLength: 250
        }).extend({rateLimit: 500});
    };
    sch.EntryAppointment.prototype = Object.create(sch.Appointment.prototype);
    sch.EntryAppointment.prototype.constructor = sch.EntryAppointment;
})(window.sch = window.sch || {}, jQuery, ko);