/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.helpers;

import java.sql.Timestamp;
import java.time.Instant;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * code copied from Professional Java For Web Applications 2014
 *
 * @author nicholas williams
 */
@Converter
public class InstantConverter implements AttributeConverter<Instant, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(Instant instant) {
        return instant == null ? null : new Timestamp(instant.toEpochMilli());
    }

    @Override
    public Instant convertToEntityAttribute(Timestamp timestamp) {
        return timestamp == null ? null : Instant.ofEpochMilli(timestamp.getTime());
    }

}
