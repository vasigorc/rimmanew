(function (sch, $, ko) {
    sch.appointmentLabels = new Map();

    sch.currentLocale = $('#pageLocale').val();
    if (sch.currentLocale.includes("fr")) {
        ko.validation.locale('fr-FR');
        $.datepicker.setDefaults($.datepicker.regional[ "fr-CA" ]);
        sch.appointmentLabels.set("success", "Success!");
        sch.appointmentLabels.set("updated", " Le rendez-vous a été mis à jour pour ");
        sch.appointmentLabels.set("saved", " Le rendez-vous a été sauveagardé pour ");
        sch.appointmentLabels.set("sd-saved", "La journée spéciale a été sauveagardé pour ");
        sch.appointmentLabels.set("deleted1", " Le rendez-vous avec id: ");
        sch.appointmentLabels.set("deleted2", " a été effacé.");
        sch.appointmentLabels.set("confirm-delete", "Oui");
    } else if (sch.currentLocale.includes("ru")) {
        ko.validation.locale('ru-RU');
        $.datepicker.setDefaults($.datepicker.regional[ "ru" ]);
        sch.appointmentLabels.set("success", "Сохранено!");
        sch.appointmentLabels.set("updated", " Запись обнавлена для ");
        sch.appointmentLabels.set("saved", " Запись сохранена для ");
        sch.appointmentLabels.set("sd-saved", "Расписание сохранено на ");
        sch.appointmentLabels.set("deleted1", " Запись № ");
        sch.appointmentLabels.set("deleted2", " успешно удалена.");
        sch.appointmentLabels.set("confirm-delete", "Да");
    } else {
        ko.validation.locale('en-US');
        $.datepicker.setDefaults($.datepicker.regional[ "en-GB" ]);
        sch.appointmentLabels.set("success", "Success!");
        sch.appointmentLabels.set("updated", " The appointment was updated for ");
        sch.appointmentLabels.set("saved", " The appointment was saved for ");
        sch.appointmentLabels.set("sd-saved", "The special day saved for ");
        sch.appointmentLabels.set("deleted1", " The appointment with id ");
        sch.appointmentLabels.set("deleted2", " was deleted.");
        sch.appointmentLabels.set("confirm-delete", "Yes");
    }

    sch.sdLabels = new Map();
    if (sch.currentLocale.includes("fr")) {
        sch.sdLabels.set("deleted1", "L'horraire spéciale du ");
        sch.sdLabels.set("deleted2", " a été retiré.");
        sch.sdLabels.set("updated", "L'horraire a été mis à jour pour ");
    } else if (sch.currentLocale.includes("ru")) {
        sch.sdLabels.set("deleted1", "Специальное расписание на ");
        sch.sdLabels.set("deleted2", " было удалено.");
        sch.sdLabels.set("updated", "Специальное расписание сохранено на ");
    } else {
        sch.sdLabels.set("deleted1", "Special schedule for ");
        sch.sdLabels.set("deleted2", " was deleted.");
        sch.sdLabels.set("updated", "Special schedule saved for ");
    }
})(window.sch = window.sch || {}, jQuery, ko);