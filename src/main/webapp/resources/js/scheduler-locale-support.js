(function (sch, $, ko) {
    sch.appointmentLabels = new Map();

    sch.currentLocale = $('#pageLocale').val();
    if (sch.currentLocale.includes("fr")) {
        ko.validation.locale('fr-FR');
        sch.appointmentLabels.set("success", "Success!");
        sch.appointmentLabels.set("updated"," Le rendez-vous a été mis à jour pour ");
        sch.appointmentLabels.set("saved"," Le rendez-vous a été sauveagrdé à jour pour ");
        sch.appointmentLabels.set("deleted1"," Le rendez-vous avec id: ");
        sch.appointmentLabels.set("deleted2", " a été effacé.");
    } else if (sch.currentLocale.includes("ru")) {
        ko.validation.locale('ru-RU');
        sch.appointmentLabels.set("success", "Сохранено!");
        sch.appointmentLabels.set("updated"," Запись обнавлена для ");
        sch.appointmentLabels.set("saved"," Запись сохранена для ");
        sch.appointmentLabels.set("deleted1"," Запись № ");
        sch.appointmentLabels.set("deleted2", " успешно удалена.");
    } else {
        ko.validation.locale('en-US');
        sch.appointmentLabels.set("success", "Success!");
        sch.appointmentLabels.set("updated"," The appointment was updated for ");
        sch.appointmentLabels.set("updated"," The appointment was saved for ");
        sch.appointmentLabels.set("deleted1"," The appointment with id ");
        sch.appointmentLabels.set("deleted2", " was deleted.");
    }
})(window.sch = window.sch || {}, jQuery, ko);