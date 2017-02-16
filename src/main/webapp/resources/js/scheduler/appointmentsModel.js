/* global ko */
(function (sch, $, ko) {
    //appointments part
    sch.appointmentsModel = function (dataService) {
        var self = this;
        self.appointments = ko.observableArray([]);
        self.entryAppointment = ko.observable(null);
        self.first = ko.observable();
        self.next = ko.observable();
        self.last = ko.observable();
        self.previous = ko.observable();
        self.dateSortAscending = ko.observable(false);
        self.nameSortAscending = ko.observable(false);
        self.typeSortAscending = ko.observable(false);
        self.showFilters = ko.observable(false);
        self.deleteCandidateId = '';
        //this is the container for all model inputs        
        self.filters = ko.observable({
            clientName: ko.observable('').extend({
                required: false,
                minLength: 4,
                maxLength: 20
            }).extend({rateLimit: 1000}),
            typeOptions: ['massage', 'waxing', 'pedicure', 'manicure'],
            type: ko.observable('').extend({
                required: false
            }).extend({rateLimit: 1000}),
            date: ko.observable().extend({
                required: false,
                date: true,
                minLength: 10
            }).extend({rateLimit: 1000}),
            time: ko.observable().extend({
                required: false,
                pattern: '^([01]?[0-9]|2[0-3]):[0-5][0-9]'
            }).extend({rateLimit: 1000}),
            limit: ko.observable().extend({
                digit: true,
                max: 100,
                min: 1
            }).extend({rateLimit: 1000}),
            offset: ko.observable().extend({
                digit: true,
                max: 100,
                min: 0
            }).extend({rateLimit: 1000}),
            past: ko.observable(false)
        });
        self.newAppointment = function () {
            self.entryAppointment(new sch.EntryAppointment());
        };
        self.editAppointment = function (appointment) {
            appointment.toggleEdits();
            self.entryAppointment(ko.toJS(appointment));
        },
        self.saveAppointment = function () {
            //remove failure alert if activated
            $('#failureAlert').css("display", "none");
            var appCandidate = ko.toJS(self.entryAppointment());
            delete appCandidate.displayEdits;
            delete appCandidate.toggleEdits;
            delete appCandidate.self;
            if (appCandidate.id === "" || null) {
                dataService.createAppointment(ko.toJSON(appCandidate)
                        , function (action, errorMsg) {
                            //1. remove the entry form 2. show the modal 3. update
                            //the table
                            if (action === "saved") {
                                $('#persistOpsTitle').text(sch.appointmentLabels.get("success"));
                                $('#persistOpsOutcome')
                                        .text(sch.appointmentLabels.get("saved") + self.entryAppointment().clientName());
                                self.entryAppointment(null);
                                $('#successAlert').css("display", "block").delay(3000).fadeOut();
                                self.updateView();
                            } else if (action === "failed") {
                                $('#requestErrorMsg').text(" " + errorMsg);
                                $('#failureAlert').css("display", "block");
                            }
                        });
            } else {
                dataService.updateAppointment(ko.toJSON(appCandidate),
                        appCandidate.id, function (action, errorMsg) {
                            //1. remove the entry form 2. show the modal 3. update
                            //the table
                            if (action === "updated") {
                                $('#persistOpsTitle').text(sch.appointmentLabels.get("success"));
                                $('#persistOpsOutcome')
                                        .text(sch.appointmentLabels.get("updated") + self.entryAppointment().clientName);
                                self.entryAppointment(null);
                                $('#successAlert').css("display", "block").delay(3000).fadeOut();
                                self.updateView();
                            } else if (action === "failed") {
                                $('#requestErrorMsg').text(" " + errorMsg);
                                $('#failureAlert').css("display", "block");
                            }
                        });
            }
        };
        self.deleteAppointment = function () {
            $('#failureAlert').css("display", "none");
            dataService.deleteAppointment(self.deleteCandidateId, function (action,
                    errorMsg, appId) {
                if (action === "deleted") {
                    $('#persistOpsOutcome').text(sch.appointmentLabels.get("deleted1")
                            + appId + sch.appointmentLabels.get("deleted2"));
                    $('#successAlert').css("display", "block").delay(3000).fadeOut();
                    self.updateView();
                } else if (action === "failed") {
                    $('#requestErrorMsg').text(" " + errorMsg);
                    $('#failureAlert').css("display", "block");
                }
            });
        };
        self.popDelete = function (appointment) {
            $("#deleteDialog").dialog("open");
            self.deleteCandidateId = ko.toJS(appointment).id;
        };
        self.cancelAppointment = function () {
            //remove failure alert if activated
            $('#failureAlert').css("display", "none");
            self.entryAppointment(null);
            self.updateView();
        };
        self.toJSON = function () {
            var copy = ko.toJS(self);
            delete copy.dateSortAscending;
            delete copy.nameSortAscending;
            delete copy.typeSortAscending;
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
            if (data.next !== undefined) {
                self.next(data.next);
                $("#step-forward").removeClass("move-button-invisible");
            } else {
                $("#step-forward").addClass("move-button-invisible");
            }
            if (data.last !== undefined) {
                self.last(data.last);
                $("#fast-forward").removeClass("move-button-invisible");
            } else {
                $("#fast-forward").addClass("move-button-invisible");
            }
            if (data.previous !== undefined) {
                self.previous(data.previous);
                $("#step-backward").removeClass("move-button-invisible");
            } else {
                $("#step-backward").addClass("move-button-invisible");
            }
            if (data.first !== undefined) {
                self.first(data.first);
                $("#fast-backward").removeClass("move-button-invisible");
            } else {
                $("#fast-backward").addClass("move-button-invisible");
            }
        };
        self.sortByDate = function () {
            self.appointments(self.appointments.sort(function (a, b) {
                first = new Date(a);
                second = new Date(b);
                if (first === second)
                    return 0;
                if (self.dateSortAscending()) {
                    return first < second ? -1 : 1;
                } else {
                    return first > second ? -1 : 1;
                }
            }));
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
            self.appointments(self.appointments.sort(function (a, b) {
                if (a.clientName() == b.clientName())
                    return 0;
                if (self.nameSortAscending()) {
                    return a.clientName() > b.clientName() ? -1 : 1;
                } else {
                    return a.clientName() < b.clientName() ? -1 : 1;
                }
            }));
            self.nameSortAscending(!self.nameSortAscending());
            self.cleanUp();
            $("#appNameCri").parent().addClass("selected-tab");
            if (self.nameSortAscending())
                $("#appNameCri").addClass("glyphicon glyphicon-chevron-up");
            else
                $("#appNameCri").addClass("glyphicon glyphicon-chevron-down");
        };
        self.sortByType = function () {
            self.appointments(self.appointments.sort(function (a, b) {
                if (a.type() == b.type())
                    return 0;
                if (self.typeSortAscending()) {
                    return a.type() > b.type() ? -1 : 1;
                } else {
                    return a.type() < b.type() ? -1 : 1;
                }
            }));
            self.typeSortAscending(!self.typeSortAscending());
            self.cleanUp();
            $("#appTypeCri").parent().addClass("selected-tab");
            if (self.typeSortAscending())
                $("#appTypeCri").addClass("glyphicon glyphicon-chevron-up");
            else
                $("#appTypeCri").addClass("glyphicon glyphicon-chevron-down");
        };        
        //INITIAL LOAD!!!
        dataService.getAppointments(self.toJSON(), function (data) {
            self.appointments(data.appointments);
            self.showButtons(data);
        });
        self.updateView = function () {
            dataService.getAppointments(self.toJSON(), function (data) {
                self.appointments(data.appointments);
                self.showButtons(data);
            });
        };
        self.getNext = function () {
            dataService.getNext(self.toJSON(), function (data) {
                self.appointments(data.appointments);
                self.showButtons(data);
            });
        };
        self.getPrevious = function () {
            dataService.getPrevious(self.toJSON(), function (data) {
                self.appointments(data.appointments);
                self.showButtons(data);
            });
        };
        self.getLast = function () {
            dataService.getLast(self.toJSON(), function (data) {
                self.appointments(data.appointments);
                self.showButtons(data);
            });
        };
        self.getFirst = function () {
            dataService.getFirst(self.toJSON(), function (data) {
                self.appointments(data.appointments);
                self.showButtons(data);
            });
        };
        self.revertShowFilters = function () {
            self.showFilters(!self.showFilters());
        };
    };    
})(window.sch = window.sch || {}, jQuery, ko);