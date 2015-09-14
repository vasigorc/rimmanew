/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public DateConvertersTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void utilToSqlConversionTest() throws ParseException {
        Date aDate = sdf.parse("2015-09-15");
        assertEquals("The two strings representing today's date"
                + " should be equal", DateConverters.utilToSql(aDate).toString(), "2015-09-15");
    }
}
