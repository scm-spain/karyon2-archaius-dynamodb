package com.scmspain.karyon.module.archaius.aws.common;

import com.google.inject.Singleton;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.governator.annotations.Modules;
import com.scmspain.karyon.module.archaius.aws.ArchaiusDynamoDBModule;
import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import netflix.karyon.KaryonBootstrap;
import netflix.karyon.archaius.ArchaiusBootstrap;
import netflix.karyon.health.AlwaysHealthyHealthCheck;
import netflix.karyon.transport.http.KaryonHttpModule;
import rx.Observable;

@ArchaiusBootstrap
@KaryonBootstrap(name = "AppServer", healthcheck = AlwaysHealthyHealthCheck.class)
@Singleton
@Modules(include = {
    ArchaiusDynamoDBModule.class,
    AppServerForTesting.KaryonRxRouterModuleImpl.class
})
public interface AppServerForTesting {

    class KaryonRxRouterModuleImpl extends KaryonHttpModule<ByteBuf, ByteBuf> {
        public static final int DEFAULT_PORT = 8000;
        public static final int DEFAULT_THREADS = 20;
        private final DynamicPropertyFactory properties = DynamicPropertyFactory.getInstance();

        public KaryonRxRouterModuleImpl() {
            super("httpServerA", ByteBuf.class, ByteBuf.class);
        }

        @Override
        protected void configureServer() {
            bindRouter().toInstance(new SimpleRouter());

            int port = properties.getIntProperty("server.port", DEFAULT_PORT).get();
            int threads = properties.getIntProperty("server.threads", DEFAULT_THREADS).get();
            server().port(port).threadPoolSize(threads);
        }

        private class SimpleRouter implements io.reactivex.netty.protocol.http.server.RequestHandler<ByteBuf, ByteBuf> {
            @Override
            public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
                return Observable.empty();
            }
        }
    }
}
