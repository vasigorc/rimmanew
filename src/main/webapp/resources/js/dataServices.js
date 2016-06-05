/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/* global ko */

(function (sch, $, ko) {
    //this has to be taken from the hidden field in the page <- from companyPropertiesBean
    sch.restServiceRoot = "http://localhost:8080/RimmaNew/rest";
    //dealing with appointments
    sch.AppointmentsService = {
        getAppointments: function (copy, callback) {
            serviceURL = sch.restServiceRoot + "/appointments";
            var typedApps = [];
            $.ajax({
                url: serviceURL + "?past=false",
                type: 'get',
                data: null,
                dataType: 'json',
                success: function (data) {

                    var typedApps = data.appointments.current;

                    copy.next = data.appointments.next;
                    copy.first = data.appointments.first;
                    copy.last = data.appointments.last;
                    copy.previous = data.appointments.previous;

                    copy.appointments = $.map(typedApps, function (item) {
                        return new sch.Appointment(item.id, item.date, item.time, item.type, item.clientName,
                                item.email, item.message);
                    });
                    callback(copy);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    var err = xhr.responseText;
                    alert(err);
                }
            });
        },
        getNext: function (copy, callback) {
            $.ajax({
                url: copy.next,
                type: 'get',
                data: null,
                dataType: 'json',
                success: function (data) {

                    var typedApps = data.appointments.current;

                    copy.next = data.appointments.next;
                    copy.first = data.appointments.first;
                    copy.last = data.appointments.last;
                    copy.previous = data.appointments.previous;

                    copy.appointments = $.map(typedApps, function (item) {
                        return new sch.Appointment(item.id, item.date, item.time, item.type, item.clientName,
                                item.email, item.message);
                    });
                    callback(copy);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    var err = xhr.responseText;
                    alert(err);
                }
            });
        },
        getPrevious: function (copy, callback) {
            $.ajax({
                url: copy.previous,
                type: 'get',
                data: null,
                dataType: 'json',
                success: function (data) {

                    var typedApps = data.appointments.current;

                    copy.next = data.appointments.next;
                    copy.first = data.appointments.first;
                    copy.last = data.appointments.last;
                    copy.previous = data.appointments.previous;

                    copy.appointments = $.map(typedApps, function (item) {
                        return new sch.Appointment(item.id, item.date, item.time, item.type, item.clientName,
                                item.email, item.message);
                    });
                    callback(copy);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    var err = xhr.responseText;
                    alert(err);
                }
            });
        },
        getLast: function (copy, callback) {
            $.ajax({
                url: copy.last,
                type: 'get',
                data: null,
                dataType: 'json',
                success: function (data) {

                    var typedApps = data.appointments.current;

                    copy.next = data.appointments.next;
                    copy.first = data.appointments.first;
                    copy.last = data.appointments.last;
                    copy.previous = data.appointments.previous;

                    copy.appointments = $.map(typedApps, function (item) {
                        return new sch.Appointment(item.id, item.date, item.time, item.type, item.clientName,
                                item.email, item.message);
                    });
                    callback(copy);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    var err = xhr.responseText;
                    alert(err);
                }
            });
        },
        getFirst: function (copy, callback) {
            $.ajax({
                url: copy.first,
                type: 'get',
                data: null,
                dataType: 'json',
                success: function (data) {

                    var typedApps = data.appointments.current;

                    copy.next = data.appointments.next;
                    copy.first = data.appointments.first;
                    copy.last = data.appointments.last;
                    copy.previous = data.appointments.previous;

                    copy.appointments = $.map(typedApps, function (item) {
                        return new sch.Appointment(item.id, item.date, item.time, item.type, item.clientName,
                                item.email, item.message);
                    });
                    callback(copy);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    var err = xhr.responseText;
                    alert(err);
                }
            });
        }
    };
})(window.sch = window.sch || {}, jQuery, ko);

