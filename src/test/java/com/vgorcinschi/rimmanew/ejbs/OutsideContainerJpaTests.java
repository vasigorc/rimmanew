/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.model.AppointmentWrapper;
import com.vgorcinschi.rimmanew.util.EntityManagerFactoryProvider;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlDate;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlTime;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author vgorcinschi
 */
public class OutsideContainerJpaTests {

    EntityManagerFactory entityManagerFactory;
    EntityManager entityManager;
    AppointmentService appointmentService;
    AppointmentRepository repository;

    public OutsideContainerJpaTests() {
        this.entityManagerFactory = EntityManagerFactoryProvider.getUniqueInstance();
        this.entityManager = entityManagerFactory.createEntityManager();
        this.repository = new JpaAppointmentRepositoryStub(entityManager);
        this.appointmentService = new OutsideContainerAppointmentService(repository);
    }

    @Before
    public void setUp() {
        Appointment dummy = new Appointment();
        dummy.setClientName("Danielle Labrave");
        dummy.setDate(localToSqlDate(LocalDate.now()
                .plusDays(5)));
        dummy.setEmail("ahdjdsa@hakdfs.ds");
        dummy.setTime(localToSqlTime(LocalTime.of(11, 00)));
        dummy.setType("waxing");
        dummy.setMessage("A tantot");
        appointmentService.save(dummy);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getAnAppointmentTest() {
        AppointmentWrapper aw = appointmentService.findById(2);
        assertNotNull(aw);
        System.out.println(aw.getClientName());
    }

    public class JpaAppointmentRepositoryStub implements AppointmentRepository {

        private final EntityManager em;

        public JpaAppointmentRepositoryStub(EntityManager em) {
            this.em = em;
        }

        @Override
        public void add(Appointment appointment) {
            em.persist(appointment);
        }

        @Override
        public void update(Appointment appointment) {
            em.merge(appointment);
        }

        @Override
        public Appointment get(long id) {
            try {
                return this.em.createQuery("SELECT a FROM Appointment a WHERE a.id = :id",
                        Appointment.class).setParameter("id", id).getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
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
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
}
