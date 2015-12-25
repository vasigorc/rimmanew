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
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author vgorcinschi
 */
public class OutsideContainerJpaTests {

    EntityManagerFactory entityManagerFactory;
    AppointmentService appointmentService;
    AppointmentRepository repository;
    //field to control the execution of some queries
    int count = 1;

    public OutsideContainerJpaTests() {
        this.entityManagerFactory = EntityManagerFactoryProvider.getUniqueInstance();
        this.repository = new JpaAppointmentRepositoryStub(entityManagerFactory);
        this.appointmentService = new OutsideContainerAppointmentService(repository);
    }

    @Before
    public void setUp() {
        count++;
    }

    @Test
    public void updateAnExistingEntity() {
        Appointment updatedSix = appointmentService.findById(6).getEntity();
        System.out.println(updatedSix.getId());
        updatedSix.setMessage("Actually I may not be coming");
        appointmentService.save(updatedSix);
        Assert.assertEquals("We expect the message srting to be updated for"
                + " the same entity instance", "Actually I may not be coming",
                appointmentService.findById(6).getClientMessage());
    }

    @Test
    public void addANewEntityInstance() {
        Appointment dummy = new Appointment();
        dummy.setClientName("Nastasia Filipovna");
        dummy.setDate(localToSqlDate(LocalDate.now()));
        dummy.setEmail("nastasiaFilippovna@yahoo.co.uk");
        dummy.setMessage("Idu, Idu");
        dummy.setType("massage");
        dummy.setTime(localToSqlTime(LocalTime.of(9, 00)));
        if (count < 1) 
            appointmentService.save(dummy);        
        assertEquals(appointmentService.findByName("Nastasia Filipovna").get(0)
                .getClientName(),
                "Nastasia Filipovna");
    }

    public class JpaAppointmentRepositoryStub implements AppointmentRepository {

        private EntityManagerFactory emf;

        public JpaAppointmentRepositoryStub(EntityManagerFactory emf) {
            this.emf = emf;
        }

        public JpaAppointmentRepositoryStub() {
        }

        @Override
        public void add(Appointment appointment) {
            EntityManager em = entityManagerFactory.createEntityManager();
            EntityTransaction trans = em.getTransaction();
            trans.begin();
            try {
                em.joinTransaction();
                em.persist(appointment);
                trans.commit();
            } catch (Exception e) {
                trans.rollback();
            } finally {
                em.close();
            }
        }

        @Override
        public void update(Appointment appointment) {
            EntityManager em = entityManagerFactory.createEntityManager();
            EntityTransaction trans = em.getTransaction();
            trans.begin();
            try {
                em.merge(appointment);
                trans.commit();
            } catch (Exception e) {
                trans.rollback();
            } finally {
                em.close();
            }
        }

        @Override
        public Appointment get(long id) {
            EntityManager em = entityManagerFactory.createEntityManager();
            try {
                Appointment app = em.find(Appointment.class, id);
                return app;
            } catch (NoResultException e) {
                return null;
            } finally {
                em.close();
            }
        }

        @Override
        public List<Appointment> getAll() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<Appointment> getByName(String name) {
            EntityManager em = entityManagerFactory.createEntityManager();
            try {
                TypedQuery<Appointment> query = em.createQuery("SELECT a FROM "
                        + "Appointment a WHERE LOWER(a.clientName) LIKE :custName",
                        Appointment.class).setParameter("custName", name.toLowerCase());
                return query.getResultList();
            } catch (NoResultException e) {
                return null;
            } finally {
                em.close();
            }
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
