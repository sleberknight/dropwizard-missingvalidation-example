package com.fortitudetec.example;

import com.fortitudetec.example.resources.PersonResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

/**
 * This simple Dropwizard example application is the companion code to a blog post showing how automatic validation
 * stops working if the {@code jersey-media-json-jackson} dependency automatically registers the
 * {@code JacksonJaxbJsonProvider}, which then takes precedence over Dropwizard's
 * {@code JacksonMessageBodyProvider}. See comments in {@code pom.xml} and {@link PersonResource}.
 * <p>
 * <i>By default validation will work in this application</i>. To demonstrate how it can stop working, uncomment the
 * {@code jersey-media-json-jackson} dependency as described in the POM file.
 */
public class MissingValidationApplication extends Application<MissingValidationConfiguration> {

    public static void main(String[] args) throws Exception {
        new MissingValidationApplication().run(args);
    }

    @Override
    public String getName() {
        return "Dropwizard Missing Validation Example";
    }

    @Override
    public void run(MissingValidationConfiguration configuration, Environment environment) throws Exception {

        environment.jersey().register(new PersonResource());

    }
}
