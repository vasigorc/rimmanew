/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.entities.SpecialDay;
import com.vgorcinschi.rimmanew.helpers.SpecialDayBuilder;
import com.vgorcinschi.rimmanew.util.EntityManagerFactoryProvider;
import com.vgorcinschi.rimmanew.util.Java8Toolkit;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import static java.time.LocalTime.of;
import java.time.Month;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author vgorcinschi
 */
public class AvailabilitiesFacadeTests {

    private EntityManagerFactory emFactory;
    private SpecialDayRepository sdRepo;
    private AppointmentRepository aRepo;
    private SpecialDay dummy;
    private NormalSchedule normalSchedule;
    //primary unit under test
    private AvailabilitiesFacade facade;

    public AvailabilitiesFacadeTests() {
        this.emFactory = EntityManagerFactoryProvider.getUniqueInstance();
        this.sdRepo = new SpecialDayRepositoryStub(emFactory);
        this.aRepo = new OutsideContainerJpaTests().repository;
        //initialize and set the normal schedule
        this.normalSchedule = new DefaultNormalSchedule();
        this.normalSchedule.setStartAt(of(9, 0));
        this.normalSchedule.setEndAt(of(17, 0));
        this.normalSchedule.setBreakStart(of(12, 0));
        this.normalSchedule.setBreakEnd(of(13, 0));
        this.normalSchedule.setDuration(Duration.ofMinutes(60));
        //we will choose the production impl and pass to it the above configured
        //ressources
        this.facade = new AvailabilitiesFacadeImpl(aRepo, sdRepo, normalSchedule);
    }

    @Before
    public void setUp() {
        dummy = new SpecialDayBuilder(LocalDate.of(2016, 01, 10))
                .setStartAt(of(12, 0)).setEndAt(of(16, 0))
                .setDuration(Duration.ofMinutes(45)).build();
    }

    @After
    public void cleanUp() {
        EntityManager em = emFactory.createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Query query = em.createQuery("DELETE FROM SpecialDay", SpecialDay.class);
            query.executeUpdate();
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    public void persistASpecialDayTest() {
        Assert.assertTrue(sdRepo.setSpecialDay(dummy));
    }

    @Test
    public void retrieveSpecialDayTest() {
        sdRepo.setSpecialDay(dummy);
        Assert.assertNotNull(sdRepo.getSpecialDay(LocalDate.of(2016, 01, 10)));
    }

    @Test
    public void updateASpecialDayTest() {
        sdRepo.setSpecialDay(dummy);
        SpecialDay newDummy = sdRepo.getSpecialDay(LocalDate.of(2016, 01, 10));
        newDummy.setMessage("I am updated!");
        sdRepo.updateSpecialDay(newDummy);
        Assert.assertEquals("I am updated!",
                sdRepo.getSpecialDay(LocalDate.of(2016, 01, 10)).getMessage());
    }

    @Test
    public void deleteASpecialDayTest() {
        sdRepo.setSpecialDay(dummy);
        SpecialDay newDummy = sdRepo.getSpecialDay(LocalDate.of(2016, 01, 10));
        sdRepo.deleteSpecialDay(newDummy);
        Assert.assertNull(sdRepo.getSpecialDay(LocalDate.of(2016, 01, 10)));
    }

    @Test
    public void getAvailsNormalScheduleTest() {
        List<LocalTime> avails = facade
                .searchAvailabilities(LocalDate.of(2016, Month.JANUARY, 11)).get()
                .getSlots().get();
        Assert.assertFalse(avails.isEmpty());
        System.out.println(avails);
    }

    @Test
    public void getAvailsScpecialScheduleTest() {
        sdRepo.setSpecialDay(dummy);
        List<LocalTime> avails = facade
                .searchAvailabilities(LocalDate.of(2016, 01, 10)).get().getSlots()
                .get();
        Assert.assertFalse(avails.isEmpty());
        System.out.println(avails);
    }
    
    @Test
    public void testAClosedDayScheduleExample(){
        //a new 'local' dummy for Closed Day
        dummy = new SpecialDayBuilder(LocalDate.of(2016, 01, 10))
                .setIsBlocked(true).build();
        sdRepo.setSpecialDay(dummy);
        Assert.assertTrue(facade.searchAvailabilities(LocalDate.of(2016, 01, 10))
        .get().isBlocked());        
    }

    public class SpecialDayRepositoryStub implements SpecialDayRepository {

        private EntityManagerFactory emf;

        public SpecialDayRepositoryStub(EntityManagerFactory emf) {
            this.emf = emf;
        }

        public SpecialDayRepositoryStub() {
        }

        @Override
        public boolean setSpecialDay(SpecialDay sd) {
            EntityManager em = emf.createEntityManager();
            EntityTransaction trans = em.getTransaction();
            try {
                trans.begin();
                //em.joinTransaction();//<-doesn't work with this line
                em.persist(sd);
                trans.commit();
                return true;
            } catch (Exception e) {
                trans.rollback();
                return false;
            } finally {
                em.close();
            }
        }

        @Override
        public SpecialDay getSpecialDay(LocalDate ld) {
            EntityManager em = emf.createEntityManager();
            try {
                TypedQuery<SpecialDay> query
                        = em.createQuery("SELECT s FROM SpecialDay s"
                                + " WHERE s.date = :date", SpecialDay.class)
                        .setParameter("date", Java8Toolkit.localToSqlDate(ld));
                return query.getSingleResult();
            } catch (Exception e) {
                return null;
            } finally {
                em.close();
            }
        }

        @Override
        public boolean updateSpecialDay(SpecialDay sd) {
            EntityManager em = emf.createEntityManager();
            EntityTransaction trans = em.getTransaction();
            try {
                trans.begin();
                em.merge(sd);
                trans.commit();
                return true;
            } catch (Exception e) {
                trans.rollback();
                return false;
            } finally {
                em.close();
            }
        }

        @Override
        public boolean deleteSpecialDay(SpecialDay sd) {
            EntityManager em = emf.createEntityManager();
            EntityTransaction trans = em.getTransaction();
            try {
                trans.begin();
                em.remove(em.merge(sd));
                trans.commit();
                return true;
            } catch (Exception e) {
                trans.rollback();
                return false;
            } finally {
                em.close();
            }
        }

        @Override
        public List<SpecialDay> getAll() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
