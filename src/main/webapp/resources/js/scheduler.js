jQuery(function () {
    $("#radioControls input").on("click", function (e) {
        $("#radioControls input").each(function () {
            var self = this;
            console.log(e.target.id);
            if (self.id === e.target.id) {
                $(self).attr("checked","");
            } else {
                $(self).removeAttr("checked");
            }
        });
    });
});