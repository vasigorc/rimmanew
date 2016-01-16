/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.Appointment;
import com.vgorcinschi.rimmanew.entities.DivizableDay;
import com.vgorcinschi.rimmanew.model.AppointmentWrapper;
import com.vgorcinschi.rimmanew.util.EntityManagerFactoryProvider;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.getAvailabilitiesPerWorkingDay;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlDate;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlTime;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.noBreakInSchedule;
import java.sql.Date;
import static java.sql.Date.valueOf;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import static java.time.LocalTime.of;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
        if (count < 1) {
            appointmentService.save(dummy);
        }
        assertEquals(appointmentService.findByName("Nastasia Filipovna").get(0)
                .getClientName(),
                "Nastasia Filipovna");
    }

    @Test
    public void testFindAll() {
        List<AppointmentWrapper> allApps = appointmentService.findAll();
        System.out.println(allApps.size());
        assertEquals("Should return the oldest appointment from the DB",
                allApps.get(allApps.size() - 1).getClientMessage(),
                "Риммочка, позвони мне пожалуйста - 514-789-8900");
    }

    @Test
    public void testFindById() {
        assertNotNull("For brevity we will try to return the appointment "
                + "with id 1", appointmentService.findById(1));
    }

    @Test
    public void testFindByDate() {
        assertEquals("We will agaub retrieve the appointment with the id 1, only"
                + " that this time by its date. We are testing that the two "
                + "ids match", 1, appointmentService.findByDate(localToSqlDate(LocalDate.of(2015, 7, 8))).get(0)
                .getEntity().getId());
    }

    @Test
    public void testFindByType() {
        assertEquals("We will retrieve only entities with type waxing. "
                + "Their count should be equal to one (today at least).",
                1, appointmentService.findByType("waxing").size());
    }

    @Test
    public void testFindByDateAndTime() {
        assertEquals("Daria Petrovna", appointmentService
                .findByDateAndTime(localToSqlDate(LocalDate.of(2015, 7, 8)),
                        localToSqlTime(LocalTime.of(11, 00))).getClientName());
    }

    @Test
    public void testFindByDateAndType() {
        assertEquals(1, appointmentService
                .findByDateAndType(localToSqlDate(LocalDate.of(2015, 12, 30)),
                        "waxing").size());
    }

    @Test
    public void testDeleteOne() {
        Appointment dummy2 = new Appointment(2,
                valueOf(LocalDate.of(2015, 12, 14)), localToSqlTime(LocalTime.of(14, 00)),
                "manicure", "Aglaia Ivanovna", "cratita@mail.md", "Vin, vin");
        appointmentService.save(dummy2);
        appointmentService.deleteOne(appointmentService
                .findByName("Aglaia Ivanovna").get(0).getEntity());
        assertEquals(appointmentService
                .findByName("Aglaia Ivanovna").size(), 0);
    }

    @Test
    public void testBeforeADate() {
        Appointment dummy2 = new Appointment(2,
                valueOf(LocalDate.of(2015, 07, 07)), localToSqlTime(LocalTime.of(14, 00)),
                "manicure", "Aglaia Ivanovna", "cratita@mail.md", "Vin, vin");
        appointmentService.save(dummy2);
        appointmentService.deleteAllBefore(valueOf(LocalDate.of(2015, 07, 07)));
        assertEquals(appointmentService
                .findByDate(valueOf(LocalDate.of(2015, 07, 07))).size(), 0);
    }

    @Test
    public void testGetAvailabilitiesPerWorkingDay() {
        
        List<LocalTime> testList = getAvailabilitiesPerWorkingDay
                .apply(new DivizableDayStub(), 
                        repository.getByDate(localToSqlDate(LocalDate.of(2015, 12, 30))), 
                        noBreakInSchedule);
        System.out.println(testList);
        assertEquals("The returned list should not include "
                + "localtimes of saved appointments", testList.contains(
                        appointmentService.findByDateAndTime(localToSqlDate(
                                LocalDate.of(2015, 12, 30)), 
                                localToSqlTime(of(16, 0))).getTime()), false);
    }

    //stub class for the repository using driver on the test classpath
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
            try {
                trans.begin();
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
            try {
                trans.begin();
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
            EntityManager em = entityManagerFactory.createEntityManager();
            try {
                TypedQuery<Appointment> query
                        = em.createNamedQuery("findAllAppointments", Appointment.class);
                return query.getResultList();
            } catch (Exception e) {
                return null;
            } finally {
                em.close();
            }
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
            EntityManager em = entityManagerFactory.createEntityManager();
            try {
                TypedQuery<Appointment> query
                        = em.createQuery("SELECT a FROM Appointment a "
                                + "WHERE a.date = :date", Appointment.class)
                        .setParameter("date", date);
                return query.getResultList();
            } catch (NoResultException e) {
                return null;
            } finally {
                em.close();
            }
        }

        @Override
        public List<Appointment> getByType(String type) {
            EntityManager em = entityManagerFactory.createEntityManager();
            try {
                TypedQuery<Appointment> query
                        = em.createQuery("SELECT a FROM Appointment a "
                                + "WHERE a.type = :type", Appointment.class)
                        .setParameter("type", type);
                return query.getResultList();
            } catch (NoResultException e) {
                return null;
            } finally {
                em.close();
            }
        }

        @Override
        public Appointment getByDateAndTime(Date date, Time time) {
            EntityManager em = entityManagerFactory.createEntityManager();
            try {
                TypedQuery<Appointment> query
                        = em.createQuery("SELECT a FROM Appointment a "
                                + "WHERE a.date = :date AND a.time ="
                                + " :time", Appointment.class)
                        .setParameter("date", date).setParameter("time", time);
                return query.getSingleResult();
            } catch (Exception e) {
                return null;
            } finally {
                em.close();
            }
        }

        @Override
        public List<Appointment> getByDateAndType(Date date, String type) {
            EntityManager em = entityManagerFactory.createEntityManager();
            try {
                TypedQuery<Appointment> query
                        = em.createQuery("SELECT a FROM Appointment a WHERE a.date "
                                + "= :date AND a.type= :type", Appointment.class)
                        .setParameter("date", date)
                        .setParameter("type", type);
                return query.getResultList();
            } catch (Exception e) {
                return null;
            } finally {
                em.close();
            }
        }

        @Override
        public void deleteOne(Appointment appointment) {
            EntityManager em = entityManagerFactory.createEntityManager();
            EntityTransaction trans = em.getTransaction();
            try {
                trans.begin();
                em.remove(em.merge(appointment));
                trans.commit();
            } catch (Exception e) {
                trans.rollback();
            } finally {
                em.close();
            }
        }

        @Override
        public void deleteAllBefore(Date date) {
            EntityManager em = entityManagerFactory.createEntityManager();
            EntityTransaction trans = em.getTransaction();
            try {
                trans.begin();
                Query query = em.createQuery("DELETE FROM Appointment a WHERE "
                        + "a.date <= :date").setParameter("date", date);
                query.executeUpdate();
                trans.commit();
            } catch (Exception e) {
                trans.rollback();
            } finally {
                em.close();
            }
        }
    }

    public class DivizableDayStub implements DivizableDay {

        @Override
        public LocalTime getStartAt() {
            return of(10, 0);
        }

        @Override
        public LocalTime getEndAt() {
            return of(17, 0);
        }

        @Override
        public Duration getDuration() {
            return Duration.ofMinutes(30);
        }

        @Override
        public LocalTime getBreakStart() {
            return of(12, 0);
        }

        @Override
        public LocalTime getBreakEnd() {
            return of(13, 0);
        }

    }
}
