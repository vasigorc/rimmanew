package com.vgorcinschi.rimmanew.cdi;

import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.ejbs.CompanyProperties;
import com.vgorcinschi.rimmanew.util.Localizer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import static java.util.Optional.ofNullable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author vgorcinschi
 */
@Named
@SessionScoped
public class UserPreferencies implements Serializable {

    /**
     * gives access to application-wide supported languages
     */
    @Inject
    @Production
    private CompanyProperties companyProperties;

    private Locale currentLocale;
    private final ArrayList<Locale> availableLocales = new ArrayList();

    public UserPreferencies() {
        //locale is initiated to French but may be overriden in the
        //post-constructed method
        currentLocale = new Locale("fr");
    }

    @PostConstruct
    public void initialize() {
        /**
         * if a user specific locale is provided in the browser we should be
         * setting it here
         */
        if (FacesContext.getCurrentInstance() != null) {
            //get Option of ExternalContext: user's browser
            Optional<ExternalContext> optContext = ofNullable(FacesContext.getCurrentInstance()
                    .getExternalContext());
            if (optContext.isPresent()) {
                Optional<Locale> userLocale = ofNullable(optContext.get().getRequestLocale());
                if (userLocale.isPresent()
                        && isLocaleSupported(userLocale.get())) {
                    currentLocale = userLocale.get();
                }
            }
        }
        //import available locales for this session
        companyProperties.getLanguages().stream()
                .forEach(language -> availableLocales.add(new Locale(language)));
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public void setCurrentLocale(Locale currentLocale) {
        //if is required to protect from manual modifications
        //of POST requests
        if (isLocaleSupported(currentLocale)) {
            this.currentLocale = currentLocale;
            //update the actual jsf view unless this is an outside container unit test
            Optional<UIViewRoot> viewRoot = ofNullable(Localizer.getCurrentViewRoot());
            if(viewRoot.isPresent()){
                viewRoot.get().setLocale(this.currentLocale);
            }
        }
    }
    
    public void changeLocale(ActionEvent event){
        //obtain the language code from the component attribute
        UIComponent component = event.getComponent();
        //component's attributes map
        Map<String, Object> attrs = component.getAttributes();
        Locale languageCode = (Locale) attrs.get("languageCode");
        setCurrentLocale(languageCode);
    }

    public boolean isLocaleSupported(Locale locale) {
        return companyProperties.getLanguages().stream()
                .anyMatch(l -> l.equalsIgnoreCase(locale.getLanguage()));
    }

    public ArrayList<Locale> getAvailableLocales() {
        return availableLocales;
    }
}