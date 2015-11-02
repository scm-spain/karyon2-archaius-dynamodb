package com.scmspain.karyon.module.archaius.aws.common;

import com.netflix.governator.guice.BootstrapModule;
import io.netty.buffer.ByteBuf;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.client.HttpClient;
import netflix.karyon.Karyon;
import netflix.karyon.KaryonServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

abstract public class AbstractControllerTest {
    private static KaryonServer server;

    @BeforeClass
    public static void setUpBefore() throws Exception {
        server = Karyon.forApplication(AppServerForTesting.class, (BootstrapModule[]) null);
        server.start();
    }

    @AfterClass
    public static void tearDownAfter() throws Exception {
        if (server != null) {
            server.shutdown();
        }
    }

    protected HttpClient<ByteBuf, ByteBuf> createHttpClient() {
        return RxNetty.createHttpClient("localhost", AppServerForTesting.KaryonRestRouterModuleImpl.DEFAULT_PORT);
    }
}
