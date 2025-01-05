package by.saunear.mW.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import by.saunear.mW.core.Whitelist;
import by.saunear.mW.core.config.GenericConfig;
import by.saunear.mW.l10n.Messages;
import by.saunear.mW.telegram.AuthBot;
import by.saunear.mW.velocity.Commands.mwlCommand;
import by.saunear.mW.velocity.Events.onPlayerJoinEvent;

import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@Plugin(id = "mwl", name = "mWl", version = "0.0.1-SNAPSHOT.jar", authors = { "turboskomorokh" })
public class VelocityProxyPlugin {

    public static @DataDirectory Path pluginFolder;
    public static ProxyServer server;

    private Map<String, Object> wlConfigData, tgConfigData;
    private GenericConfig wlConfig, tgConfig;
    Messages messages;

    public Logger logger;
    public Whitelist wl;

    private AuthBot bot;

    @SuppressWarnings("deprecation")
    private void registerCommands(Whitelist wl) {
        server.getCommandManager().register("mwl", new mwlCommand(logger, messages, wl));
    }

    private void registerEvents(Whitelist wl) {
        server.getEventManager().register(this, new onPlayerJoinEvent(logger, messages, wl));
    }

    @Inject
    public VelocityProxyPlugin(ProxyServer server, Logger logger, @DataDirectory Path pluginFolder) {
        VelocityProxyPlugin.pluginFolder = pluginFolder;
        VelocityProxyPlugin.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        wlConfig = new GenericConfig(pluginFolder, "whitelist.yml");
        tgConfig = new GenericConfig(pluginFolder, "telegram.yml");
        messages = new Messages(pluginFolder, "messages.yml");

        try {
            wlConfigData = wlConfig.load();
            tgConfigData = tgConfig.load();
        } catch (IOException e) {
            logger.error("Failed to load configurations: " + e.getMessage());
            return;
        }

        wl = new Whitelist(wlConfigData, pluginFolder, logger);

        registerCommands(wl);
        registerEvents(wl);
        
        logger.info("Started!");

        bot = new AuthBot(logger, messages, tgConfigData, wl);
        bot.start();
    }

}
