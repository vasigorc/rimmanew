/* global ko */
(function (sch, $, ko) {
    sch.specdaysModel = function (dataService) {
        var self = this;
        self.name = ko.observable("name");
        self.specdays = ko.observableArray([]);
        self.entrySpecDay = ko.observable(null);
        self.first = ko.observable();
        self.next = ko.observable();
        self.last = ko.observable();
        self.previous = ko.observable();
        self.dateSortAscending = ko.observable(false);
        self.showFilters = ko.observable(false);
        self.filters = ko.observable({
            date: ko.observable().extend({
                required: false,
                date: true,
                minLength: 10
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
            }).extend({rateLimit: 1000})
        });
        self.cancelSpecDay = function () {
            //remove failure alert if activated
            $('#sd-failureAlert').css("display", "none");
            self.entrySpecDay(null);
            self.updateView();
        };
        self.toJSON = function () {
            var copy = ko.toJS(self);
            return copy;
        };
        //initial load
        dataService.getSpecDays(self.toJSON(), function (result, data, errorMsg) {
            self.specdays(data.specdays);
//            ko.utils.arrayForEach(self.specdays(), function (s) {
//                console.log(s.date());
//            });   
            self.showButtons(data);
        });
        self.newSpecDay = function () {
            self.entrySpecDay(new sch.EntrySpecialDay());
        };
        self.saveSpecDay = function () {
        };
        self.cleanUp = function () {
            $("#specdaysTable thead tr th").each(function () {
                $(this).attr('class', 'hand-on-hover');
            });
            $("#specdaysTable thead tr th i").each(function () {
                $(this).removeAttr('class');
            });
        };
        self.showButtons = function (data) {
            if (data.next !== undefined) {
                self.next(data.next);
                $("#sd-step-forward").removeClass("move-button-invisible");
            } else {
                $("#sd-step-forward").addClass("move-button-invisible");
            }
            if (data.last !== undefined) {
                self.last(data.last);
                $("#sd-fast-forward").removeClass("move-button-invisible");
            } else {
                $("#sd-fast-forward").addClass("move-button-invisible");
            }
            if (data.previous !== undefined) {
                self.previous(data.previous);
                $("#sd-step-backward").removeClass("move-button-invisible");
            } else {
                $("#sd-step-backward").addClass("move-button-invisible");
            }
            if (data.first !== undefined) {
                self.first(data.first);
                $("#sd-fast-backward").removeClass("move-button-invisible");
            } else {
                $("#sd-fast-backward").addClass("move-button-invisible");
            }
        };
        self.sortByDate = function () {
            self.specdays(self.specdays.sort(function (a, b) {
                first = new Date(a.date);
                second = new Date(b.date);
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
            $("#sdDateCri").parent().addClass("selected-tab");
            if (self.dateSortAscending()) {
                $("#sdDateCri").addClass("glyphicon glyphicon-chevron-up");
            } else {
                $("#sdDateCri").addClass("glyphicon glyphicon-chevron-down");
            }
        };
        self.updateView = function () {
            //remove failure alerts (if any) on another try
            $('#sd-failureAlert').css("display", "none");
            dataService.getSpecDays(self.toJSON(), function (result, data, errorMsg) {
                if (result === "success") {
                    self.specdays(data.specdays);
                } else {
                    $('#sd-requestErrorMsg').text(" " + errorMsg);
                    $('#sd-failureAlert').css("display", "block");
                }
                self.showButtons(data);
            });
        };
        self.getNext = function () {
            dataService.getNext(self.toJSON(), function (data) {
                self.specdays(data.specdays);
                self.showButtons(data);
            });
        };
        self.getPrevious = function () {
            dataService.getPrevious(self.toJSON(), function (data) {
                self.specdays(data.specdays);
                self.showButtons(data);
            });
        };
        self.getLast = function () {
            dataService.getLast(self.toJSON(), function (data) {
                self.specdays(data.specdays);
                self.showButtons(data);
            });
        };
        self.getFirst = function () {
            dataService.getFirst(self.toJSON(), function (data) {
                self.specdays(data.specdays);
                self.showButtons(data);
            });
        };
        self.revertShowFilters = function () {
            self.showFilters(!self.showFilters());
        };
    };
})(window.sch = window.sch || {}, jQuery, ko);