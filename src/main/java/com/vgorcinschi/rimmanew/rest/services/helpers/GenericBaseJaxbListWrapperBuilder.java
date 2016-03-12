/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.services.helpers;

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
public abstract class GenericBaseJaxbListWrapperBuilder<R> implements JaxbListWrapperBuilder {

    protected JaxbListWrapper response = null;
    protected String pathToAppend;
    //remainder - how many appointments left in db after offset and current load
    protected final int requestSize, listSize, requestOffset, remainder;
    protected final List<R> current;
    //this var delivers us from evaluating separetely next and last when later
    //are equal
    protected final boolean nextIsLast;

    public GenericBaseJaxbListWrapperBuilder(int requestSize, int listSize, 
            int requestOffset, List<R> current) {
        this.requestSize = requestSize;
        this.listSize = listSize;
        this.requestOffset = requestOffset;
        this.remainder = listSize - (current.size() + requestOffset);
        this.current = current;        
        this.nextIsLast = (listSize - (requestSize + requestOffset) <= requestSize);
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

    @Override
    public void setFirstURI() {
        Map<String, String> first = new HashMap<>();
        first.put("offset", valueOf(0));
        first.put("size", valueOf(requestSize));
        first.put("path", pathToAppend);
        response.setFirst(uriGenerator.apply(appsUriBuilder, first));
    }

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
        last.put("path", pathToAppend);
        response.setLast(uriGenerator.apply(appsUriBuilder, last));
    }

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
            next.put("path", pathToAppend);
            response.setNext(uriGenerator.apply(appsUriBuilder, next));
        }
    }

    @Override
    public void setPreviousURI() {
        if (requestOffset == 0 || requestOffset <= requestSize) {
            response.setPrevious(response.getFirst());
        } else {
            Map<String, String> previous = new HashMap<>();
            previous.put("offset", valueOf(requestOffset - requestSize));
            previous.put("size", valueOf(requestSize));
            previous.put("path", pathToAppend);
            response.setPrevious(uriGenerator.apply(appsUriBuilder, previous));
        }
    }
}
