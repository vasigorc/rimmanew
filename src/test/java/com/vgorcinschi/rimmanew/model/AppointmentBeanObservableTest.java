/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.verify;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vgorcinschi
 */
public class AppointmentBeanObservableTest {

    private AppointmentFormBean form;
    private VGObserver mockObserver;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Before
    public void setUp() {
        form = new AppointmentFormBean();
        mockObserver = createMock("observer", VGObserver.class);
        form.registerVGObserver(mockObserver);
    }

    @After
    public void tearDown() {
        verify(mockObserver);
    }

    @Test
    public void testIocForVGObserved() throws ParseException {        
        Date aDate = sdf.parse("2015-09-15");
        
        mockObserver.update(aDate);
        EasyMock.expectLastCall().anyTimes();
        replay(mockObserver);
        
        form.setSelectedDate(aDate);
    }
}
