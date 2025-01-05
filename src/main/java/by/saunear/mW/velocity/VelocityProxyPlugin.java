package by.saunear.mW.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import by.saunear.mW.core.Configuration;
import by.saunear.mW.core.Whitelist;
import by.saunear.mW.telegram.AuthBot;
import by.saunear.mW.velocity.Commands.mwlCommand;
import by.saunear.mW.velocity.Events.onPlayerJoinEvent;

import org.slf4j.Logger;
import java.nio.file.Path;

@Plugin(id = "mwl", name = "mWl", version = "0.0.1-SNAPSHOT.jar", description = "Somehow it should work", authors = {"turboskomorokh"})
public class VelocityProxyPlugin {
    
	public static @DataDirectory Path pluginFolder;
	public static ProxyServer server;
	Configuration config, telegram;

	public Logger logger;
    public Whitelist wl;

    private AuthBot bot;


    @SuppressWarnings("deprecation")
	private void registerCommands() {
        server.getCommandManager().register("mwl", new mwlCommand(logger, wl));
    }

    private void registerEvents() {
    	server.getEventManager().register(this, new onPlayerJoinEvent(logger, wl));
    }

    @Inject
    public VelocityProxyPlugin(ProxyServer server, Logger logger, @DataDirectory Path pluginFolder) {
    	VelocityProxyPlugin.pluginFolder = pluginFolder;
    	VelocityProxyPlugin.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    	telegram = new Configuration(pluginFolder.resolve("telegram.yml"));
    	config = new Configuration(pluginFolder.resolve("config.yml"));
    	wl = new Whitelist(config, pluginFolder, logger);

    	registerCommands();
        registerEvents();

        logger.info("Whitelist started!");

        bot = new AuthBot(logger, telegram, wl);

        bot.start();
    }
}
