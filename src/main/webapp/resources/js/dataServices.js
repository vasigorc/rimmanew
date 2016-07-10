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
            serviceURL = serviceURL.concat("?past=" + copy.filters.past);
            if (copy.filters.clientName.length > 3) {
                serviceURL = serviceURL.concat("&name=" + copy.filters.clientName);
            }
            if (copy.filters.type !== undefined && copy.filters.type.length > 0) {
                serviceURL = serviceURL.concat("&type=" + copy.filters.type);
            }
            if (copy.filters.date !== undefined && copy.filters.date.length > 0) {
                serviceURL = serviceURL.concat("&date=" + copy.filters.date);
            }
            if (copy.filters.time !== undefined && copy.filters.time.length > 0) {
                serviceURL = serviceURL.concat("&time=" + copy.filters.time);
            }
            if (copy.filters.limit !== undefined && copy.filters.limit.length > 0) {
                serviceURL = serviceURL.concat("&size=" + copy.filters.limit);
            }
            if (copy.filters.offset !== undefined && copy.filters.offset.length > 0) {
                serviceURL = serviceURL.concat("&offset=" + copy.filters.offset);
            }
            this.basicQuery(copy, serviceURL, callback);
        },
        getNext: function (copy, callback) {
            var serviceURL = copy.next.concat("&past=" + copy.filters.past);
            this.basicQuery(copy, serviceURL, callback);
        },
        getPrevious: function (copy, callback) {
            var serviceURL = copy.previous.concat("&past=" + copy.filters.past);
            this.basicQuery(copy, serviceURL, callback);
        },
        getLast: function (copy, callback) {
            var serviceURL = copy.last.concat("&past=" + copy.filters.past);
            this.basicQuery(copy, serviceURL, callback);
        },
        getFirst: function (copy, callback) {
            var serviceURL = copy.first.concat("&past=" + copy.filters.past);
            this.basicQuery(copy, serviceURL, callback);
        },
        createAppointment: function (data, callback) {
            serviceURL = sch.restServiceRoot + "/appointments";
            this.basicPost(data, serviceURL, callback);
        },
        updateAppointment: function(data, appointmentId, callback){
            serviceURL = sch.restServiceRoot + "/appointments/"+appointmentId;
            this.basicPut(data, serviceURL, callback);
        },
        basicQuery: function (copy, url, callback) {
            $.ajax({
                url: url,
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
                                item.email, item.message || "", item.past, item.noShow);
                    });
                    callback(copy);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    var err = xhr.responseText;
                    alert(err);
                }
            });
        },
        basicPost: function (data, url, callback) {
            $.ajax({
                url: url,
                type: 'post',
                dataType: 'json',
                data: data,
                contentType: 'application/json',
                beforeSend: function () {
//                    $('#ajaxResponse').html("<img src='245.gif' />");
                },
                success: function (data) {
                    callback("saved", null);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    var err = xhr.responseText;
                    callback("failed", err);
                }
            });
        }, basicPut: function (data, url, callback) {
            $.ajax({
                url: url,
                type: 'put',
                dataType: 'json',
                data: data,
                contentType: 'application/json',
                beforeSend: function () {
//                    $('#ajaxResponse').html("<img src='245.gif' />");
                },
                success: function (data) {
                    callback("updated", null);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    var err = xhr.responseText;
                    callback("failed", err);
                }
            });
        }
    };
})(window.sch = window.sch || {}, jQuery, ko);