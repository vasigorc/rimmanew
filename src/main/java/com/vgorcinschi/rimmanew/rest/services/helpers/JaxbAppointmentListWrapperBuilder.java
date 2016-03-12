/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.vgorcinschi.rimmanew.entities.Appointment;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.appsUriBuilder;
import static com.vgorcinschi.rimmanew.util.Java8Toolkit.uriGenerator;
import static java.lang.String.valueOf;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vgorcinschi
 */
public class JaxbAppointmentListWrapperBuilder
        extends GenericBaseJaxbListWrapperBuilder<Appointment> {

    private final Map<String, Object> additionalParams;

    public JaxbAppointmentListWrapperBuilder(int requestSize, int listSize,
            int requestOffset, List<Appointment> current) {
        super(requestSize, listSize, requestOffset, current);
        this.pathToAppend = "appointments";
        this.response = new JaxbAppointmentListWrapper((List<Appointment>)current);
        this.additionalParams = new HashMap<>();
    }

    public JaxbAppointmentListWrapperBuilder(int requestSize, int listSize,
            int requestOffset, List<Appointment> current,
            Map<String, Object> additionalParams) {
        super(requestSize, listSize, requestOffset, current);
        this.pathToAppend = "appointments";
        this.response = new JaxbAppointmentListWrapper((List<Appointment>)current);
        if (additionalParams.size() > 0) {
            this.additionalParams = additionalParams;
        } else {
            this.additionalParams = new HashMap<>();
        }
    }

    @Override
    public GenericBaseJaxbListWrapper compose() {
        this.response.setReturnedSize(current.size());
        if (!current.isEmpty() && requestOffset != 0) {
            setFirstURI();
            setPreviousURI();
        }
        if (remainder > 1) {
            setLastURI();
            setNextURI();
        }
        return (GenericBaseJaxbListWrapper) response;
    }

    //setting the first URI for response
    @Override
    public void setFirstURI() {
        Map<String, String> first = new HashMap<>();
        first.put("offset", valueOf(0));
        first.put("size", valueOf(requestSize));
        first.put("path", "appointments");
        if (!additionalParams.isEmpty()) {
            additionalParams.forEach((k, v) -> {
                first.put(k, v.toString());
            });
        }
        response.setFirst(uriGenerator.apply(appsUriBuilder, first));
    }

    //setting the last URI for response
    @Override
    public void setLastURI() {
        Map<String, String> last = new HashMap<>();
        if (remainder > requestSize) {
            last.put("size", valueOf(requestSize));
            last.put("offset", valueOf(listSize - requestSize));
        } else {
            last.put("size", valueOf(remainder));
            last.put("offset", valueOf(listSize - remainder));
        }
        if (!additionalParams.isEmpty()) {
            additionalParams.forEach((k, v) -> {
                last.put(k, v.toString());
            });
        }
        last.put("path", "appointments");
        response.setLast(uriGenerator.apply(appsUriBuilder, last));
    }

    //setting the next URI for response
    @Override
    public void setNextURI() {
        /*
         if next equals to last - we will simply still steal it from
         last. Else we will evaluate it.
         */
        if (nextIsLast) {
            response.setNext(response.getLast());
        } else {
            Map<String, String> next = new HashMap<>();
            next.put("offset", valueOf(current.size() + requestOffset));
            if (remainder > requestSize) {
                next.put("size", valueOf(requestSize));
            } else {
                next.put("size", valueOf(remainder));
            }
            if (!additionalParams.isEmpty()) {
                additionalParams.forEach((k, v) -> {
                    next.put(k, v.toString());
                });
            }
            next.put("path", "appointments");
            response.setNext(uriGenerator.apply(appsUriBuilder, next));
        }
    }

    //setting the previous URI for response
    @Override
    public void setPreviousURI() {
        if (requestOffset == 0 || requestOffset <= requestSize) {
            response.setPrevious(response.getFirst());
        } else {
            Map<String, String> previous = new HashMap<>();
            previous.put("offset", valueOf(requestOffset - requestSize));
            previous.put("size", valueOf(requestSize));
            if (!additionalParams.isEmpty()) {
                additionalParams.forEach((k, v) -> {
                    previous.put(k, v.toString());
                });
            }
            previous.put("path", "appointments");
            response.setPrevious(uriGenerator.apply(appsUriBuilder, previous));
        }
    }
}
