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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author vgorcinschi
 */
@RunWith(Parameterized.class)
public class JaxbAppointmentListWrapperBuilderTests {

    private final AppointmentRepository repository;
    private final AppointmentResourceService service;
    private final int[] paramsArray;

    public JaxbAppointmentListWrapperBuilderTests(int[] params) {
        OutsideContainerJpaTests tests = new OutsideContainerJpaTests();
        this.repository = tests.getRepository();
        this.service = new AppointmentResourceService();
        this.service.setRepository(repository);
        this.paramsArray = params;
    }

    @Parameters
    public static Collection<int[]> getTestParameters() {
        LinkedList<int[]> params = new LinkedList<>();
        int[] array1 = {3, 3};
        params.add(array1);
        int[] array2 = {5, 5};
        params.add(array2);
        int[] array3 = {10, 10};
        params.add(array3);
        int[] array4 = {10, 5};
        params.add(array4);
        int[] array5 = {2, 7};
        params.add(array5);
        int[] array6 = {23, 1};
        params.add(array6);
        return params;
    }

    @Test
    public void testCurrentLoadReturned() {
        List<Appointment> list = repository.getAll();
        List<Appointment> current = list.stream().skip(paramsArray[0]).limit(paramsArray[1])
                .collect(toList());
        JaxbAppointmentListWrapper response
                = new JaxbAppointmentListWrapperBuilder(paramsArray[1], list.size(),
                        paramsArray[0], current).compose();
        assertNotEquals(list.size(), response.getReturnedSize());
        assertEquals(current.size(), response.getReturnedSize());
        System.out.println("\nFirst: " + response.getFirst().toASCIIString());
        System.out.println("Last: " + response.getLast());
        System.out.println("Next: " + response.getNext());
        System.out.println("Previous: " + response.getPrevious());
    }
}
