/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import static com.vgorcinschi.rimmanew.util.Java8Toolkit.getNextSuitableDate;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.isAWeekEnd;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.localToUtilDate;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.nextNotWeekEnd;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import static java.time.LocalDate.of;
import static java.time.Month.DECEMBER;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.function.Function;
import java.util.function.Predicate;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.instanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vgorcinschi
 */
public class DateConvertersTest {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Date aDate;
    private LocalDate shouldBeMonday;

    public DateConvertersTest() {
    }

    @Before
    public void setUp() throws ParseException {
        aDate = sdf.parse("2015-09-15");
        shouldBeMonday = getNextSuitableDate(of(2015, 12, 19),isAWeekEnd, nextNotWeekEnd);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void utilToSqlConversionTest(){        
        assertEquals("The two strings representing today's date"
                + " should be equal", DateConverters.utilToSql(aDate).toString(), "2015-09-15");
    }
    
    @Test
    public void isSqlClass(){
        assertThat(DateConverters.utilToSql(aDate), Matchers.instanceOf(java.sql.Date.class));
    }
    
    @Test
    public void testGetNextDayAfterWeekend(){    
        assertEquals("Provied that nineteenth is a Sat, the method"
                + "should return 21st which is the next Monday", of(2015, DECEMBER, 21), shouldBeMonday);
    }
    
    @Test
    public void testThatReturnedValueIsUtilDate(){
        assertThat(localToUtilDate(shouldBeMonday), instanceOf(java.util.Date.class));
        System.out.println(localToUtilDate(shouldBeMonday));
    }
}
