/* global ko */
(function (sch, $, ko) {
    sch.specdaysModel = function (dataService) {
        var self = this;
        self.specdays = ko.observableArray([]);
        self.entrySpecDay = ko.observable(null);
        self.first = ko.observable();
        self.next = ko.observable();
        self.last = ko.observable();
        self.previous = ko.observable();
        self.dateSortAscending = ko.observable(false);
        self.showFilters = ko.observable(false);
        self.toJSON = function () {
            var copy = ko.toJS(self);
            return copy;
        };
        //initial load
        dataService.getSpecDays(self.toJSON(), function (data) {
            self.specdays(data.specdays);
            ko.utils.arrayForEach(self.specdays(), function (s) {
                console.log(s.date());
            });
            //TODO!!!
//            self.showButtons(data);
        });
        self.filters = ko.observable({
            date: ko.observable().extend({
                required: false,
                date: true,
                minLength: 10
            }).extend({rateLimit: 530}),
            limit: ko.observable().extend({
                digit: true,
                max: 100,
                min: 1
            }).extend({rateLimit: 600}),
            offset: ko.observable().extend({
                digit: true,
                max: 100,
                min: 0
            }).extend({rateLimit: 600})
        });
        self.newSpecDay = function () {
            self.entrySpecDay(new sch.EntryAppointment());
        };
        self.cleanUp = function () {
            $("#specdaysTable thead tr th").each(function () {
                $(this).attr('class', 'hand-on-hover');
            });
            $("#specdaysTable thead tr th i").each(function () {
                $(this).removeAttr('class');
            });
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
        self.revertShowFilters = function () {
            self.showFilters(!self.showFilters());
        };
    };
})(window.sch = window.sch || {}, jQuery, ko);