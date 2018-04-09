package com.vgorcinschi.rimmanew.cdi;

import com.vgorcinschi.rimmanew.annotations.Production;
import com.vgorcinschi.rimmanew.ejbs.CompanyProperties;
import com.vgorcinschi.rimmanew.ejbs.CompanyPropertiesImpl;
import java.util.Locale;
import java.util.Set;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Ideally we would be able to create an outside container, arquillian based,
 * test to test various aspects of the UserPreferencies CDI bean
 *
 * @author vgorcinschi
 */
@RunWith(Arquillian.class)
public class UserPreferenciesTests {

    public UserPreferenciesTests() {
    }

    //humble attempt to mock glassfish only with the UserPreferencies bean inside
    @Deployment
    public static JavaArchive createDeployment() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
                .addClass(UserPreferencies.class)
                .addClasses(CompanyProperties.class,
                        CompanyPropertiesImpl.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        return jar;
    }

    @Inject
    UserPreferencies preferencies;

    @Inject
    @Production
    CompanyProperties companyProperties;

    @Test
    public void localeIsNotNull() {
        assertNotNull(preferencies.getCurrentLocale().getCountry());
    }

    @Test
    public void defaultLocaleTest() {
        assertTrue("The default locale should be \"fr\"",
                "fr".equalsIgnoreCase(preferencies.getCurrentLocale().getLanguage()));
    }

    @Test
    public void companyPropetiesIsNotNull() {
        assertNotNull(companyProperties);
    }
    
    @Test
    public void atLeastOneLanguageSupportIsLoadedSuccessfully(){
        Set<String> languages = companyProperties.getLanguages();
        languages.stream().forEach(System.out::println);
        assertTrue(!languages.isEmpty());
    }
    
    @Test
    public void cannotUseUnsupportedLocale(){
        preferencies.setCurrentLocale(new Locale("kr"));
        assertTrue("Current locale should remain \"fr\" - korean "
                + "language is not supported",
                "fr".equalsIgnoreCase(preferencies.getCurrentLocale().getLanguage()));
    }
    
    @Test
    public void canSwitchToASupportedLocale(){
        preferencies.setCurrentLocale(new Locale("ru"));
        assertTrue("Current locale should become \"ru\" - if russian is supported",
                "ru".equalsIgnoreCase(preferencies.getCurrentLocale().getLanguage()));        
    }
}