package by.saunear.mW.core;

import java.nio.file.Path;
import java.util.Map;

import org.slf4j.Logger;

import by.saunear.mW.database.SQLiteDB;

public class Whitelist {

	SQLiteDB db;
	Map<String, Object> configuration;
	public boolean enabled;
	private Logger logger;

	public Whitelist(Map<String, Object> globalConfiguration, Path pluginFolder, Logger logger) {
		this.configuration = globalConfiguration;
		this.logger = logger;

		try {
			enabled = (boolean) this.configuration.get("enabled");

			db = new SQLiteDB(pluginFolder.toString());
		} catch (RuntimeException e) {
			this.logger.error(e.getMessage());
			enabled = false;
		}
	}

	public boolean check(String playerName) {
		boolean r;
		try {
			r = db.exists(playerName);
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
			r = false;
		}
		return r;
	}

	public boolean add(String playerName, Long id) {
		try {
			db.insert(playerName, id);
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	public boolean remove(String playerName) {
		try {
			db.delete(playerName);
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	public Map<Long, String> get() {
		Map<Long, String> players = null;
		try {
			players	= db.getAll();
		}
		catch (RuntimeException e) {
			logger.error(e.getMessage());
		}
		return players;
	}

}
