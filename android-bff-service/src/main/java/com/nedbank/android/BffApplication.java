package com.nedbank.android;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
    public void run(final BffConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
