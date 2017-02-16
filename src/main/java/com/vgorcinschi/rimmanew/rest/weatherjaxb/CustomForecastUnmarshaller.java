/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.rest.weatherjaxb;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vgorcinschi
 */
public class CustomForecastUnmarshaller implements MessageBodyReader<DailyWeatherReport>{

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type.isAnnotationPresent(XmlRootElement.class);
    }

    @Override
    public DailyWeatherReport readFrom(Class<DailyWeatherReport> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        try {
            JAXBContext context = JAXBContext.newInstance(DailyWeatherReport.class);
            DailyWeatherReport report = (DailyWeatherReport)context.createUnmarshaller().unmarshal(entityStream);
            return report;
        } catch (JAXBException e) {
            throw new ProcessingException("Error deserializing an object", e);
        }
    }
    
}
