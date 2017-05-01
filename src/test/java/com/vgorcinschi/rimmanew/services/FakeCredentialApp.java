package com.vgorcinschi.rimmanew.services;

import java.util.HashSet;
import com.vgorcinschi.rimmanew.rest.services.CredentialResourceService;
import com.vgorcinschi.rimmanew.rest.services.helpers.*;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author vgorcinschi
 */
@ApplicationPath("/fake")
public class FakeCredentialApp extends Application  {

    public FakeCredentialApp() {
    }
    
     @Override
    public Set<Object> getSingletons() {
        return super.getSingletons(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Class<?>> getClasses() {
        HashSet<Class<?>> set = new HashSet<>();
        set.add(CredentialResourceService.class);
        set.add(BadRequestExceptionMapper.class);
        set.add(InternalServerErrorExceptionMapper.class);
        set.add(NotFoundExceptionMapper.class);
        set.add(SqlDateConverterProvider.class);
        return set;
    }
}
