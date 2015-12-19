/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery.noConflict();
jQuery(function (va){
    va(document).delegate("*[id$='tryAppButton']","click",function(){
        va("#calendarForm").submit();
    });
});