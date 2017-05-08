package com.vgorcinschi.rimmanew.rest.services.helpers;

import com.vgorcinschi.rimmanew.entities.Credential;
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
public class JaxbCredentialListWrapperBuilder extends 
        GenericBaseJaxbListWrapperBuilder<Credential>{
    
    private Map<String, Object> additionalParams;
    
    public JaxbCredentialListWrapperBuilder(int requestSize, int listSize, int requestOffset, List<Credential> current) {
        super(requestSize, listSize, requestOffset, current);
        this.pathToAppend = "credential";
        this.response = new JaxbCredentialListWrapper((List<Credential>) current);
        this.additionalParams = new HashMap<>();
    }

    public JaxbCredentialListWrapperBuilder(int requestSize, int listSize, int requestOffset, List<Credential> current,
                        Map<String, Object> additionalParams) {
        this(requestSize, listSize, requestOffset, current);
        this.additionalParams = additionalParams;
    }

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
            previous.put("path", "credential");
            response.setPrevious(uriGenerator.apply(appsUriBuilder, previous));
        }
    }

    @Override
    public void setNextURI() {
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
            next.put("path", "credential");
            response.setNext(uriGenerator.apply(appsUriBuilder, next));
        }
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
        if (!additionalParams.isEmpty()) {
            additionalParams.forEach((k, v) -> {
                last.put(k, v.toString());
            });
        }
        last.put("path", "credential");
        response.setLast(uriGenerator.apply(appsUriBuilder, last));
    }

    @Override
    public void setFirstURI() {
        Map<String, String> first = new HashMap<>();
        first.put("offset", valueOf(0));
        first.put("size", valueOf(requestSize));
        first.put("path", "credential");
        if (!additionalParams.isEmpty()) {
            additionalParams.forEach((k, v) -> {
                first.put(k, v.toString());
            });
        }
        response.setFirst(uriGenerator.apply(appsUriBuilder, first));
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
    
    
}