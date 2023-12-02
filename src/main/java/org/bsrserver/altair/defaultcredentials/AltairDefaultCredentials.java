package org.bsrserver.altair.defaultcredentials;

import java.nio.file.Path;

import org.slf4j.Logger;
import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;

@Plugin(
        id = "altairdefaultcredentials",
        name = "Altair Default Credentials",
        version = "1.1.1",
        url = "https://www.bsrserver.org:8443/",
        description = "Altair 默认用户名密码",
        authors = {"Andy Zhang"}
)
public class AltairDefaultCredentials {
    public static final String DEFAULT_PASSWORD = "123456";
    private final ProxyServer proxyServer;
    private final Logger logger;
    private final Path dataDirectory;
    private final Data data;

    public ProxyServer getProxyServer() {
        return proxyServer;
    }

    public Logger getLogger() {
        return logger;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public Data getData() {
        return data;
    }

    @Inject
    public AltairDefaultCredentials(ProxyServer proxyServer, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxyServer = proxyServer;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.data = new Data(this);
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        // register event
        proxyServer.getEventManager().register(this, new ServerConnectedEventListener(this));

        // register command
        proxyServer.getCommandManager().register(
                proxyServer.getCommandManager().metaBuilder("altairdefaultcredentials").build(),
                new Command(this)
        );
    }
}
