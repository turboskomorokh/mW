package by.saunear.mW.l10n;

import by.saunear.mW.core.config.GenericConfig;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class Messages extends GenericConfig {

	private Map<String, Object> messages;

	public Messages(Path configDir, String filename) {
		super(configDir, filename);
		try {
			messages = load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String get(String name) {
		String message;
		try {
			message = (String) messages.get(name);
			if (message == null) {
				throw new NullPointerException("Message not found in the map");
			}
		} catch (Exception emsg) {
			try {
				message = Constants.class.getDeclaredField(name).toGenericString();
			} catch (Exception efield) {
				efield.printStackTrace();
				message = "null";
			}
		}
		return message;
	}
}
