/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.model.AppointmentFormBean;
import com.vgorcinschi.rimmanew.util.Java8Toolkit;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlTime;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import static org.hamcrest.Matchers.instanceOf;
import org.junit.After;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author vgorcinschi
 */
public class AppointmentCDIMockTests {

    private Date dummy = Java8Toolkit.localToSqlDate(LocalDate.now().plusDays(10));
    private AppointmentFormBean form;
    private AppointmentService service;

    private class AppointmentRepositoryStub implements AppointmentRepository {

        public AppointmentRepositoryStub() {
        }

        @Override
        public void add(Appointment appointment) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void update(Appointment appointment) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Appointment get(long id) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<Appointment> getAll() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<Appointment> getByName(String name) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<Appointment> getByDate(Date date) {
            List<Appointment> oneApp = new LinkedList<>();
            oneApp.add(new Appointment(3, dummy, localToSqlTime(LocalTime.of(11, 30)),
                    "massage", "Tamara Fedorovna", "casserole@yahoo.qc", "J'y viendrais"));
            return oneApp;
        }

        @Override
        public List<Appointment> getByType(String type) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Appointment getByDateAndTime(Date date, Time time) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<Appointment> getByDateAndType(Date date, String type) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void deleteOne(Appointment appointment) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void deleteAllBefore(Date date) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    public AppointmentCDIMockTests() {
        form = new AppointmentFormBean();
    }

    @Before
    public void setUp() {
        service = new DefaultAppointmentService(new AppointmentRepositoryStub());
        form.setService(service);
        form.setSelectedDate(dummy);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testRepositoryStubFromBean() {
        assertTrue(form.getBookedAlready().size() > 0);
        System.out.println(form.getAvailabilities());
    }

    @Test
    public void testFormBeanAvailabilities() {
        assertThat(form.getAvailabilities().get(0), instanceOf(LocalTime.class));
    }
}
