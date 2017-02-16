/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.ejbs;

import com.vgorcinschi.rimmanew.annotations.InMemoryRepository;
import com.vgorcinschi.rimmanew.entities.Appointment;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlDate;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToSqlTime;
import java.sql.Date;
import static java.sql.Date.valueOf;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import static java.util.Comparator.comparing;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import javax.ejb.Singleton;

/**
 *
 * @author vgorcinschi
 */
@Singleton
@InMemoryRepository
public class InMemoryAppointmentRepository implements AppointmentRepository {

    private static final Map<Long, Appointment> database = new Hashtable<>();
    private volatile long appointmentIdSequence = 6L;

    static {
        database.put(1L, new Appointment(1, localToSqlDate(LocalDate.now()
                .plusDays(5)), localToSqlTime(LocalTime.of(11, 00)),
                "waxing", "Danielle Labrave", "ahdjdsa@hakdfs.ds", "A tantot"));
        database.put(2L, new Appointment(2, valueOf(LocalDate.of(2015, 12, 14)), localToSqlTime(LocalTime.of(14, 00)),
                "manicure", "Aglaia Ivanovna", "cratita@mail.md", "Vin, vin"));
        database.put(3L, new Appointment(3,
                localToSqlDate(LocalDate.now().plusDays(10)), localToSqlTime(LocalTime.of(13, 00)),
                "massage", "Tamara Fedorovna", "casserole@yahoo.qc", "J'y viendrais"));
        database.put(4L, new Appointment(4, localToSqlDate(LocalDate.now()
                .plusDays(5)), localToSqlTime(LocalTime.of(10, 00)),
                "massage", "Isabelle Legrand", "", "Telephonez moi SVP"));
        database.put(5L, new Appointment(5, localToSqlDate(LocalDate.now()),localToSqlTime(LocalTime.of(9, 00)),
                "massage","Nastasia Filipovna", "","Idu, idu"));
        database.put(6L, new Appointment(7, localToSqlDate(LocalDate.now()),localToSqlTime(LocalTime.of(16, 00)),
                "manicure","Mme Lefebvre", "","J'arrive"));
    }

    public InMemoryAppointmentRepository() {
    }

    @Override
    public void add(Appointment appointment) {
        appointment.setId(getNextAppointmentId());
        database.put(appointment.getId(), appointment);
    }

    @Override
    public void update(Appointment appointment) {
        database.put(appointment.getId(), appointment);
    }

    @Override
    public Appointment get(long id) {
        return database.get(id);
    }

    @Override
    public List<Appointment> getAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public List<Appointment> getByName(String name) {
        return database.values().stream().filter(a -> a.getClientName().equalsIgnoreCase(name)).collect(toList());
    }

    @Override
    public List<Appointment> getByDate(Date date) {
        return database.values().stream().filter(a -> a.getDate().equals(date)).sorted(comparing(Appointment::getDate)).collect(toList());
    }

    @Override
    public List<Appointment> getByType(String type) {
        return database.values().stream().filter(a -> a.getType().equalsIgnoreCase(type)).collect(toList());
    }

    @Override
    public Appointment getByDateAndTime(Date date, Time time) {
        return database.values().stream().filter(a -> a.getDate().equals(date) && a.getTime().equals(time)).findAny().get();
    }

    @Override
    public List<Appointment> getByDateAndType(Date date, String type) {
        return database.values().stream().filter(a -> a.getDate().equals(date)
                && a.getType().equalsIgnoreCase(type)).sorted(comparing(Appointment::getDate)).collect(toList());
    }

    @Override
    public void deleteOne(Appointment appointment) {
        if (database.values().stream().anyMatch(a -> a.getId() == appointment.getId())) {
            database.remove(appointment.getId());
        }
    }

    /**
     *
     * @param date - we must delete all appointments preceding this date We need
     * the intermediary collection of target appointments because trying to
     * remove values of a collection (Set or EntrySet) while iterating through
     * it throws java.util.ConcurrentModificationException The same thing occurs
     * with the returned iterator
     */
    @Override
    public void deleteAllBefore(Date date) {
        List<Appointment> targetAppointments = database.values().stream().filter(app
                -> app.getDate().before(date)).collect(toList());
        targetAppointments.stream().forEach(app -> deleteOne(app));
    }

    private synchronized long getNextAppointmentId() {
        return this.appointmentIdSequence++;
    }
}
