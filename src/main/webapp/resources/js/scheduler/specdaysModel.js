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
//            self.showButtons(data);
        });
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
    };
})(window.sch = window.sch || {}, jQuery, ko);