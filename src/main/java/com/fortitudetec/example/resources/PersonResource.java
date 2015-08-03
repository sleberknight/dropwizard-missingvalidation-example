package com.fortitudetec.example.resources;

import com.fortitudetec.example.model.Person;
import org.glassfish.jersey.internal.PropertiesDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/person")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class PersonResource {

    private static final Logger log = LoggerFactory.getLogger(PersonResource.class);

    private static final AtomicLong ID_GENERATOR = new AtomicLong(1L);

    /**
     * If validation does not occur on the {@code Person} argument, chances are the default Dropwizard
     * {@link io.dropwizard.jersey.jackson.JacksonMessageBodyProvider} is being superseded in the list of
     * available {@link javax.ws.rs.ext.MessageBodyReader} instances, which are responsible for de-serializing
     * incoming request bodies.
     *
     * To test this out, first verify that posting JSON with an empty firstName or lastName property in the JSON
     * returns a 422 response. Thn edit the {@code pom.xml} file and uncomment the
     * {@code jersey-media-json-jackson} dependency, re-run the application, and now no validation is run because
     * {@link com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider} takes precedence since it is first in the
     * list of available {@link javax.ws.rs.ext.MessageBodyReader} instances in the
     * {@link org.glassfish.jersey.message.internal.MessageBodyFactory#_getMessageBodyReader(Class, Type, Annotation[], MediaType, List, PropertiesDelegate)}}
     * method.
     */
    @POST
    public Response createPerson(@Valid Person person) {
        log.info("Creating new person from validated (really???) Person object: {}", person);

        Person savedPerson = fakeSavePerson(person);

        URI location = UriBuilder.fromResource(PersonResource.class)
                .path(savedPerson.getId().toString())
                .build();

        return Response.created(location).entity(savedPerson).build();
    }

    private Person fakeSavePerson(Person person) {
        person.setId(ID_GENERATOR.getAndIncrement());
        return person;
    }

}
