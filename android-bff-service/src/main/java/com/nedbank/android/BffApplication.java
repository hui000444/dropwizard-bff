package com.nedbank.android;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.nedbank.android.BffConfiguration;

import com.nedbank.android.api.HelloWorld;
import com.nedbank.android.health.TemplateHealthCheck;
import com.nedbank.android.resources.HelloWorldResource;

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
        // TODO: application initialization
    }

    @Override
    public void run(final BffConfiguration configuration, final Environment environment) {
      final HelloWorldResource resource = new HelloWorldResource(configuration.getTemplate(), configuration.getDefaultName());
       environment.jersey().register(resource);

       final TemplateHealthCheck templateCheck = new TemplateHealthCheck(configuration.getTemplate());

       environment.healthChecks().register("template", templateCheck);
    }

}
