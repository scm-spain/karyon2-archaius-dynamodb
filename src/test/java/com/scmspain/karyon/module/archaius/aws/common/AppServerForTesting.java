package com.scmspain.karyon.module.archaius.aws.common;

import com.google.inject.Singleton;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.governator.annotations.Modules;
import com.scmspain.karyon.module.archaius.aws.ArchaiusDynamoDBModule;
import netflix.karyon.KaryonBootstrap;
import netflix.karyon.archaius.ArchaiusBootstrap;
import netflix.karyon.health.AlwaysHealthyHealthCheck;
import scmspain.karyon.restrouter.KaryonRestRouterModule;

@ArchaiusBootstrap
@KaryonBootstrap(name = "AppServer", healthcheck = AlwaysHealthyHealthCheck.class)
@Singleton
@Modules(include = {
    ArchaiusDynamoDBModule.class,
    AppServerForTesting.KaryonRestRouterModuleImpl.class
})
public interface AppServerForTesting {
    class KaryonRestRouterModuleImpl extends KaryonRestRouterModule {

        public static final int DEFAULT_PORT = 8000;
        public static final int DEFAULT_THREADS = 20;
        private final DynamicPropertyFactory properties = DynamicPropertyFactory.getInstance();

        @Override
        protected void configureServer() {
            int port = properties.getIntProperty("server.port", DEFAULT_PORT).get();
            int threads = properties.getIntProperty("server.threads", DEFAULT_THREADS).get();
            server().port(port).threadPoolSize(threads);

        }

        @Override
        public void configure()
        {
            super.configure();
        }
    }
}
