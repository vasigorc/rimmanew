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
        updateAppointment: function (data, appointmentId, callback) {
            serviceURL = sch.restServiceRoot + "/appointments/" + appointmentId;
            this.basicPut(data, serviceURL, callback);
        },
        deleteAppointment: function (appointmentId, callback) {
            serviceURL = sch.restServiceRoot + "/appointments/" + appointmentId;
            this.basicDelete(serviceURL, appointmentId, callback);
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
                success: function (data) {
                    callback("updated", null);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    var err = xhr.responseText;
                    callback("failed", err);
                }
            });
        }, basicDelete: function (url, appId, callback) {
            $.ajax({
                url: url,
                type: 'delete',
                dataType: 'json',
                contentType: 'application/json',
                success: function (data) {
                    callback("deleted", null, appId);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    var err = xhr.responseText;
                    callback("failed", err, appId);
                }
            });
        }
    };
    sch.SpecdaysService = {
        getSpecDays: function (copy, callback) {
            serviceURL = sch.restServiceRoot + "/specialdays";
            if (copy.filters.date !== undefined && copy.filters.date.length > 0) {
                serviceURL = serviceURL.concat("/" + copy.filters.date);
//                call query for only one employee
                this.queryOne(copy, serviceURL, callback);
            } else {
                serviceURL = serviceURL.concat("?");
//               if we're here - we are looking for multiple appointments
//               and thus will be calling a different method
                if (copy.filters.limit !== undefined && copy.filters.limit.length > 0) {
                    serviceURL = serviceURL.concat("&size=" + copy.filters.limit);
                }
                if (copy.filters.offset !== undefined && copy.filters.offset.length > 0) {
                    serviceURL = serviceURL.concat("&offset=" + copy.filters.offset);
                }
                //same method is called regardless of whether size & offset are set
                this.queryMultiple(copy, serviceURL, callback);
            }
        }, queryMultiple: function (copy, url, callback) {
            $.ajax({
                url: url,
                type: 'get',
                data: null,
                dataType: 'json',
                success: function (data) {
                    var specdays = data.specialDays.current;

                    copy.next = data.specialDays.next;
                    copy.first = data.specialDays.first;
                    copy.last = data.specialDays.last;
                    copy.previous = data.specialDays.previous;

                    copy.specdays = $.map(specdays, function (item) {
                        return new sch.SpecialDay(item.id, item.date, item.startAt,
                                item.endAt, item.breakStart || "", item.breakEnd || "",
                                item.duration, item.blocked, item.message || "");
                    });
                    callback("success", copy, null);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    var err = xhr.responseText;
                    callback("failure", copy, err);
                }
            });
        }, queryOne: function (copy, url, callback) {
            $.ajax({
                url: url,
                type: 'get',
                data: null,
                dataType: 'json',
                success: function (data) {
                    console.log(data);
                    copy.specdays = [];
                    var item = data.dayWithSpecialSchedule;
                    copy.specdays.push(new sch.SpecialDay(item.id, item.date, item.startAt,
                            item.endAt, item.breakStart || "", item.breakEnd || "",
                            item.duration, item.blocked, item.message || ""));

                    callback("success", copy, null);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    var err = xhr.responseText;
                    callback("failure", copy, err);
                }
            });
        }, getNext: function (copy, callback) {
            var serviceURL = copy.next;
            this.queryMultiple(copy, serviceURL, callback);
        },
        getPrevious: function (copy, callback) {
            var serviceURL = copy.previous;
            this.queryMultiple(copy, serviceURL, callback);
        },
        getLast: function (copy, callback) {
            var serviceURL = copy.last;
            this.queryMultiple(copy, serviceURL, callback);
        },
        getFirst: function (copy, callback) {
            var serviceURL = copy.first;
            this.queryMultiple(copy, serviceURL, callback);
        }
    };
})(window.sch = window.sch || {}, jQuery, ko);