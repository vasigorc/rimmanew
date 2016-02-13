/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.vgorcinschi.rimmanew.ejbs.AppointmentRepository;
import com.vgorcinschi.rimmanew.ejbs.OutsideContainerJpaTests;
import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.rest.services.AppointmentResourceService;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vgorcinschi
 */
public class JaxbAppointmentListWrapperBuilderTests {

    private final AppointmentRepository repository;
    private final AppointmentResourceService service;

    public JaxbAppointmentListWrapperBuilderTests() {
        OutsideContainerJpaTests tests = new OutsideContainerJpaTests();
        this.repository = tests.getRepository();
        this.service = new AppointmentResourceService();
        this.service.setRepository(repository);
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCurrentLoadReturned() {
        List<Appointment> list = repository.getAll();
        List<Appointment> current = list.stream().skip(20).limit(20)
                .collect(toList());
        JaxbAppointmentListWrapper response = 
                new JaxbAppointmentListWrapperBuilder(20, list.size(), 
                        20, current).compose();
        assertEquals(current.size(), response.getCurrent().size());
        System.out.println(response.getFirst().toASCIIString());
    }
}
