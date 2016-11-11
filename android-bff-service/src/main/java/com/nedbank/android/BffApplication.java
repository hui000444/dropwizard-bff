package com.nedbank.android;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nedbank.android.health.TemplateHealthCheck;
import com.nedbank.android.resources.HelloWorldResource;
import com.nedbank.android.resources.ServerResource;
import io.dropwizard.Application;
import io.dropwizard.bundles.assets.ConfiguredAssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

public class BffApplication extends Application<BffConfiguration> {

    public static final String BASE_PATH = "/api/";

    public static void main(final String[] args) throws Exception {
        new BffApplication().run(args);
    }

    @Override
    public String getName() {
        return "Bff";
    }

    @Override
    public void initialize(final Bootstrap<BffConfiguration> bootstrap) {
        bootstrap.addBundle(new ConfiguredAssetsBundle("/webapp/", "/swagger/"));
    }

    @Override
    public void run(final BffConfiguration configuration, final Environment environment) {
        environment.jersey().register(new SwaggerSerializers());
        environment.jersey().register(new HelloWorldResource(configuration.getTemplate(), configuration.getDefaultName()));
        environment.jersey().register(new ServerResource(configuration.getTemplate(), configuration.getDefaultName()));
        environment.jersey().setUrlPattern(BASE_PATH);

        final TemplateHealthCheck templateCheck = new TemplateHealthCheck(configuration.getTemplate());

        environment.healthChecks().register("template", templateCheck);
        environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

        initialSwagger(environment);
    }

    private void initialSwagger(Environment environment) {
        environment.jersey().register(new ApiListingResource());
        BeanConfig config = new BeanConfig();
        config.setTitle("Android BFF");
        config.setVersion("1.0.0");
        config.setBasePath(BASE_PATH);
        config.setSchemes(new String[]{"http"});
        config.setResourcePackage(HelloWorldResource.class.getPackage().getName());
        config.setScan(true);
    }

}
