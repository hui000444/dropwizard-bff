package com.nedbank.android.resources;

import com.codahale.metrics.annotation.Timed;
import com.entersekt.sg.transakt.Api_002f20_002fTransakt;
import com.entersekt.sg.transakt.ResponseResult;
import com.entersekt.sg.transakt.TransaktWs;
import com.nedbank.android.api.HelloWorld;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/hello-world")
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
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    public String validateToken(@Context HttpHeaders headers) {
        List<String> tokens = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);

        if (tokens != null && tokens.size() > 0) {
            String token = tokens.get(0).split("\\s+")[1];

            LOGGER.info("Received a token: {}", token);

            TransaktWs transaktWs = new Api_002f20_002fTransakt().getTransaktPort();
            Holder<ResponseResult> result = new Holder<>();
            Holder<String> subjectId = new Holder<>();
            Holder<String> timeStamp = new Holder<>();

            try {
                ((BindingProvider) transaktWs).getRequestContext().put(
                        BindingProvider.USERNAME_PROPERTY, "thoughtworks");
                ((BindingProvider) transaktWs).getRequestContext().put(
                        BindingProvider.PASSWORD_PROPERTY, "Y45wnecS");

                transaktWs.trustTokenVerify(token, result, subjectId, timeStamp);
            } catch (Exception ex) {
                LOGGER.error("Error", ex);
                System.out.println(ex.toString());
            }

            return result.value.getCode() + ":" + result.value.getMessage();
        } else {
            LOGGER.warn("No received token");
            return null;
        }
    }

}
