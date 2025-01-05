package by.saunear.mW.core;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Configuration {

	Map<String, Object> data = new HashMap<>();

	public Path path;

	public Configuration(Path path) {
		this.path = path;
		load();
	}

	public void save() {
		Yaml yaml = new Yaml();
		try (FileWriter writer = new FileWriter(path.toString())) {
			yaml.dump(data, writer);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void load() {
		Yaml yaml = new Yaml();
		try (FileReader reader = new FileReader(path.toString())) {
			data = yaml.load(reader);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public Object get(String parameter) {
		return data.get(parameter);
	}
}
