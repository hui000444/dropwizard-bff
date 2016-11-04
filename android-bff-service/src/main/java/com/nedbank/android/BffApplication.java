package com.nedbank.android;

import com.nedbank.android.health.TemplateHealthCheck;
import com.nedbank.android.resources.HelloWorldResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class BffApplication extends Application<BffConfiguration> {

    public static void main(final String[] args) throws Exception {
        new BffApplication().run(args);
    }

    @Override
    public String getName() {
        return "Bff";
    }

    @Override
    public void initialize(final Bootstrap<BffConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<BffConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(BffConfiguration sampleConfiguration) {
                return sampleConfiguration.swaggerBundleConfiguration;
            }
        });
    }

    @Override
    public void run(final BffConfiguration configuration, final Environment environment) {
      final HelloWorldResource resource = new HelloWorldResource(configuration.getTemplate(), configuration.getDefaultName());
       environment.jersey().register(resource);

       final TemplateHealthCheck templateCheck = new TemplateHealthCheck(configuration.getTemplate());

       environment.healthChecks().register("template", templateCheck);
    }

}
