/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 *
 * @author vgorcinschi
 */
public class Localizer {

    public static String getLocalizedLabel(String toConvert) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot = context.getViewRoot();
        Locale locale = viewRoot.getLocale();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Application app = context.getApplication();
        String bundleName = app.getMessageBundle();
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, loader);
        return (bundle==null?null:bundle.getString(toConvert));
    }
}
