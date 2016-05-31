/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/* global ko */

(function (sch, $, ko) {
    sch.restServiceRoot = "http://localhost:8080/RimmaNew/rest";
    //dealing with appointments
    sch.AppointmentsService = {
        getAppointments: function (callback) {
            serviceURL = sch.restServiceRoot + "/appointments";
            var typedApps = [];
            $.ajax({
                url: serviceURL + "?past=false",
                type: 'get',
                data: null,
                dataType: 'json',
                success: function (data) {
                    
                    var typedApps = data.appointments.current;
                    if (data.appointments.next != null)
                        sch.appointmentsModel.next = data.appointments.next;
                    if (data.appointments.last != null)
                        sch.appointmentsModel.last = data.appointments.last;
                    
                    var mappedAppointments = $.map(typedApps, function (item) {
                        return new sch.Appointment(item.id, item.date, item.time, item.type, item.clientName,
                                item.email, item.message);
                    });
                    callback(mappedAppointments);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    var err = xhr.responseText;
                    alert(err);
                }
            });
        }
    };
})(window.sch = window.sch || {}, jQuery, ko);

