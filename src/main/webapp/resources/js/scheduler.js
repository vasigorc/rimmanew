jQuery(function () {
    $("#radioControls input").on("click", function (e) {
        $("#radioControls input").each(function () {
            //here self will be the input field and label - label field
            var self = this;
            var label = $("label[for='" + $(self).attr('id') + "']");
            if (self.id === e.target.id) {
                $(self).attr("checked", "");
                label.css("color", "white");
            } else {
                $(self).removeAttr("checked");
                label.css("color", "#4C4545");
            }
        });
    });
});
//knockout part
var scopeSelector = {
    //scopeValues: ["Appointments", "Special Days"],
    selectedScope: ko.observable("Appointments")
};
// Here's a custom Knockout binding that makes elements shown/hidden via jQuery's fadeIn()/fadeOut() methods
// Could be stored in a separate utility library
ko.bindingHandlers.fadeVisible = {
    init: function(element, valueAccessor) {
        // Initially set the element to be instantly visible/hidden depending on the value
        var value = valueAccessor();
        $(element).toggle(ko.unwrap(value)); // Use "unwrapObservable" so we can handle values that may or may not be observable
    },
    update: function(element, valueAccessor) {
        // Whenever the value subsequently changes, slowly fade the element in or out
        var value = valueAccessor();
        ko.unwrap(value) ? $(element).fadeIn() : $(element).fadeOut();
    }
};
ko.applyBindings(scopeSelector);