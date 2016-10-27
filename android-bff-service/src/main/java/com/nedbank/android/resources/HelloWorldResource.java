package com.nedbank.android.resources;

import com.entersekt.sg.transakt.Api_002f20_002fTransakt;
import com.entersekt.sg.transakt.ResponseResult;
import com.entersekt.sg.transakt.TransaktWs;
import com.nedbank.android.api.HelloWorld;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.params.DateTimeParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldResource.class);

    public HelloWorldResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public HelloWorld sayHello(@QueryParam("name") Optional<String> name) {
        final String value = String.format(template, name.orElse(defaultName));

        return new HelloWorld(counter.incrementAndGet(), value);
    }


    @GET
    @Path("/validate")
    public String validateToken(@QueryParam("token") Optional<String> token) {
        if (token != null) {
            LOGGER.info("Received a token: {}", token);

            TransaktWs transaktWs = new Api_002f20_002fTransakt().getTransaktPort();
            Holder<ResponseResult> result = new Holder<ResponseResult>();
            Holder<String> subjectId = new Holder<String>();
            Holder<String> timeStamp = new Holder<String>();

            try {
                ((BindingProvider) transaktWs).getRequestContext().put(
                        BindingProvider.USERNAME_PROPERTY, "thoughtworks");
                ((BindingProvider) transaktWs).getRequestContext().put(
                        BindingProvider.PASSWORD_PROPERTY, "Y45wnecS");

                transaktWs.trustTokenVerify(token.get(), result, subjectId, timeStamp);
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
            }

            return result.value.getCode() + ":" + result.value.getMessage();
        } else {
            LOGGER.warn("No received token");
            return null;
        }
    }

}
