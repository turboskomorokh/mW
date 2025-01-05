package by.saunear.mW.velocity.Events;

import org.slf4j.Logger;

import com.velocitypowered.api.event.EventHandler;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;

import by.saunear.mW.core.Whitelist;
import by.saunear.mW.l10n.Messages;
import net.kyori.adventure.text.Component;

public class onPlayerJoinEvent implements EventHandler<PreLoginEvent> {
	
	private Whitelist wl;
	private Logger logger;
	
	public onPlayerJoinEvent(Logger logger, Whitelist wl) {
		this.wl = wl;
		this.logger = logger;
	}

	@Subscribe(priority = 1)
	public void execute(PreLoginEvent event) {
		String username = event.getUsername();
		// TODO 30/12/2024 Улучшить, как-нибудь, этого маловато для отказоустойчивости
		// TODO 03/01/2025 Или нет.
		if (!wl.check(username) && wl.enabled) {
			logger.info(Messages.WHITELIST_JOIN_UNREGISTERED_PLAYER.replace("%playerName", username));
			event.setResult(PreLoginEvent.PreLoginComponentResult
					.denied(Component.text(Messages.WHITELIST_JOIN_REGISTER_MESSAGE)));
			return;
		}
		logger.info(Messages.WHITELIST_JOIN_REGISTERED_PLAYER.replace("%playerName", username));
	}

}
