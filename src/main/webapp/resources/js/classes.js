/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function Appointment(rawData) {
    this.id = ko.observable(rawData.id);
    this.date = ko.observable(new Date(rawData.date));
    this.time = ko.observable(rawData.time);
    this.type = ko.observable(rawData.type);
    this.clientName = ko.observable(rawData.clientName);
    this.email = ko.observable(rawData.email);
    this.message = ko.observable(rawData.message);
}