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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/")
public class ServerResource {
    private final String template;
    private final String defaultName;
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerResource.class);

    public ServerResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
    }

    @GET
    @Timed
    public List<String> server() {
        List<String> result = new ArrayList<>();
        result.add(String.format(template, defaultName));

        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            ip = InetAddress.getLoopbackAddress();
            result.add("Your current IP address : " + ip);
            result.add("Your current Hostname : " + hostname);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
            result.add(ex.getMessage());
            LOGGER.error("Error", ex);
            System.out.println(ex.toString());
        }
        return result;
    }

}
