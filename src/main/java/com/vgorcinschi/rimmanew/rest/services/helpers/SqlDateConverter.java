/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.vgorcinschi.rimmanew.rest.services.helpers.querycandidates.AppointmentsQueryCandidatesTriage;
import java.sql.Date;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ext.ParamConverter;

/**
 *
 * @author vgorcinschi
 */
public class SqlDateConverter implements ParamConverter<Date> {

    private CompletableFuture<AppointmentsQueryCandidatesTriage> future = null;

    public SqlDateConverter() {
    }

    public SqlDateConverter(CompletableFuture<AppointmentsQueryCandidatesTriage> future) {
        this.future = future;
    }

    @Override
    public Date fromString(String value) {
        if (!value.matches("\\d{4}-\\d{2}-\\d{2}")) {
            if (future != null) {
                future.cancel(true);
                System.out.println("Completable Future cancelled: "+future.isCancelled());
            }            
            throw new BadRequestException(value + " wasn't recognized as "
                    + "a valid date on the server side. Please follow this "
                    + "pattern: yyyy-mm-dd", new Throwable(value + " wasn't recognized as "
                            + "a valid date on the server side. Please follow this "
                            + "pattern: yyyy-mm-dd"));
        }
        return Date.valueOf(value);
    }

    @Override
    public String toString(Date value) {
        return value.toLocalDate().toString();
    }

}
