/* global ko */

(function (sch, $, ko) {
    sch.Appointment = function (id, date, time, type, clientName, email, message
            , past, noShow) {
        this.id = ko.observable(id);
        this.date = ko.observable(date);
        this.time = ko.observable(time.substring(0, time.length - 3));
        this.type = ko.observable(type);
        this.clientName = ko.observable(clientName);
        this.email = ko.observable(email);
        this.message = ko.observable(message);
        this.past = ko.observable(past);
        this.noShow = ko.observable(noShow);
        var self = this;
        self.displayEdits = ko.observable(false);
        self.toggleEdits = function () {
            self.displayEdits(!self.displayEdits());
        };
    };
    sch.EntryAppointment = function (init) {
        if (init === undefined) {
            sch.Appointment.call(this, "", "", "", "", "", "", "", false, false);
        } else {
            sch.Appointment.call(this, init.id, init.date, init.time, init.type,
                    init.clientName, init.email, init.message || "", init.past, init.noShow);
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

    sch.SpecialDay = function (id, date, startAt, endAt, breakStart, breakEnd
            , duration, blocked, message) {
        this.id = ko.observable(id);
        this.date = ko.observable(date);
        this.startAt = ko.observable(startAt);
        this.endAt = ko.observable(endAt);
        this.breakStart = ko.observable(breakStart);
        this.breakEnd = ko.observable(breakEnd);
        this.duration = ko.observable(duration);
        this.blocked = ko.observable(blocked);
        this.message = ko.observable(message);
    };
    sch.EntrySpecialDay = function (init) {
        if (init === undefined) {
            sch.SpecialDay.call(this, "", "", "", "", "", "", "", false, "");
        } else {
            sch.SpecialDay.call(this, init.id, init.date, init.startAt, init.endAt,
                    init.breakStart, init.breakEnd, init.duration, init.blocked || "",
                    init.message || "");
        }
        this.date.extend({
            required: true,
            date: true,
            minLength: 10
        }).extend({rateLimit: 1000});
        this.startAt.extend({
            required: true,
            pattern: '^([01]?[0-9]|2[0-3]):[0-5][0-9]'
        }).extend({rateLimit: 1000});
        this.endAt.extend({
            required: true,
            pattern: '^([01]?[0-9]|2[0-3]):[0-5][0-9]'
        }).extend({rateLimit: 1000});
        this.breakStart.extend({
            pattern: '^([01]?[0-9]|2[0-3]):[0-5][0-9]'
        }).extend({rateLimit: 1000});
        this.breakEnd.extend({
            pattern: '^([01]?[0-9]|2[0-3]):[0-5][0-9]'
        }).extend({rateLimit: 1000});
        this.duration.extend({
            required: true,
            digit: true,
            max: 120,
            min: 15
        }).extend({rateLimit: 1000});
        this.blocked.extend({
            required: true
        });
        this.allowConflicts = ko.observable(false);
    };
    sch.EntrySpecialDay.prototype = Object.create(sch.SpecialDay.prototype);
    sch.EntrySpecialDay.prototype.constructor = sch.EntrySpecialDay;
})(window.sch = window.sch || {}, jQuery, ko);