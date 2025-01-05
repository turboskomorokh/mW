package by.saunear.mW.velocity.Commands;

import java.util.Map;

import org.slf4j.Logger;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;

import by.saunear.mW.core.Whitelist;
import by.saunear.mW.l10n.Messages;
import net.kyori.adventure.text.Component;

public class mwlCommand implements SimpleCommand {

	Logger logger;
	Whitelist wl;

	public mwlCommand(Logger logger, Whitelist wl) {
		this.logger = logger;
		this.wl = wl;
	}

	void printHelp(CommandSource executor) {
		executor.sendMessage(Component.text(Messages.PLUGIN_COMMAND_HELP));
	}
	
	@Override
	public void execute(Invocation invocation) {
		String[] args = invocation.arguments();
		CommandSource executor = invocation.source();
		
		if(!executor.hasPermission("mwl.mwl")) {
			executor.sendMessage(Component.text(Messages.PLUGIN_COMMAND_NO_PERMISSION));
		}
		
		if(args.length == 0) {
			printHelp(executor);
			return;
		}

		if (args.length == 1) {
			switch (args[0]) {
			case "list":
				try {
					Map<Long, String> players = wl.get();
					if(players.size() == 0) {
						executor.sendMessage(Component.text(Messages.PLUGIN_COMMAND_NO_WHITELIST_PLAYERS));
						break;
					}
					for (Map.Entry<Long, String> player : players.entrySet()) {
						executor.sendMessage(Component.text(player.getKey() + " " + player.getValue()));
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
					break;
				}
				break;
			default:
				executor.sendMessage(Component.text(Messages.PLUGIN_COMMAND_NOT_ENOUGH_ARGUMENTS));
				break;
			}
		} else if (args.length >= 2) {
			switch (args[0]) {
			case "add":
				try {
					if (!args[1].matches("^[a-zA-Z0-9-_]+$") || 16 < args[1].length() || args[1].length() < 4) {
						executor.sendMessage(Component.text(Messages.PLUGIN_COMMAND_WRONG_NICKNAME));
						return;
					}
					wl.add(args[1], (long) 0);
				} catch (Exception e) {
					logger.error(e.getMessage());
					executor.sendMessage(
							Component.text(Messages.WHITELIST_ADD_FAILURE.replace("%playerName", args[1])));
					break;
				}
				executor.sendMessage(Component.text(Messages.WHITELIST_ADD_SUCCESS.replace("%playerName", args[1])));
				break;
			case "remove":
				try {
					if (!args[1].matches("^[a-zA-Z0-9-_]+$") || 16 < args[1].length() || args[1].length() < 4) {
						executor.sendMessage(Component.text(Messages.PLUGIN_COMMAND_WRONG_NICKNAME));
						return;
					}
					if(!wl.check(args[1])) {						
						executor.sendMessage(
								Component.text(Messages.WHITELIST_REMOVE_NOT_EXIST.replace("%playerName", args[1])));
						return;
					}
					wl.remove(args[1]);
				} catch (Exception e) {
					logger.error(e.getMessage());
					executor.sendMessage(
							Component.text(Messages.WHITELIST_REMOVE_FAILURE.replace("%playerName", args[1])));
					break;
				}
				executor.sendMessage(Component.text(Messages.WHITELIST_REMOVE_SUCCESS.replace("%playerName", args[1])));
				break;
			}
		}

	}

}
