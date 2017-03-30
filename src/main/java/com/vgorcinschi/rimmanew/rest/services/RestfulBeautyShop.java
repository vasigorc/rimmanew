/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services;

import com.vgorcinschi.rimmanew.rest.services.helpers.BadRequestExceptionMapper;
import com.vgorcinschi.rimmanew.rest.services.helpers.InternalServerErrorExceptionMapper;
import com.vgorcinschi.rimmanew.rest.services.helpers.NotFoundExceptionMapper;
import com.vgorcinschi.rimmanew.rest.services.helpers.SqlDateConverterProvider;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author vgorcinschi
 */
@ApplicationPath("/rest")
public class RestfulBeautyShop extends Application {

    public RestfulBeautyShop() {
    }

    @Override
    public Set<Object> getSingletons() {
        return super.getSingletons(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Class<?>> getClasses() {
        HashSet<Class<?>> set = new HashSet<>();
        set.add(AppointmentResourceService.class);
        set.add(SpecialDayResourceService.class);
        set.add(GroupsResourceService.class);
        set.add(BadRequestExceptionMapper.class);
        set.add(InternalServerErrorExceptionMapper.class);
        set.add(NotFoundExceptionMapper.class);
        set.add(SqlDateConverterProvider.class);
        return set;
    }
}
