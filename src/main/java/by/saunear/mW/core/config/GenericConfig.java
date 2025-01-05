package by.saunear.mW.core.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class GenericConfig implements IConfig {

    protected Path configDir;
    protected String filename;

    public GenericConfig(Path configDir, String filename) {
        this.configDir = configDir;
        this.filename = filename;
    }

    @Override
    public Map<String, Object> load() throws IOException {
        File file = configDir.resolve(filename).toFile();
        if (!file.exists()) {
            create();
        }
        Yaml yaml = new Yaml();
        return yaml.load(Files.newInputStream(file.toPath()));
    }

    @Override
    public void create() throws IOException {
        if (Files.notExists(configDir)) {
            Files.createDirectories(configDir);
        }

        Path filePath = configDir.resolve(filename);
        if (Files.notExists(filePath)) {
            try (InputStream in = getClass().getClassLoader().getResourceAsStream(filename)) {
                if (in == null) {
                    throw new IOException("Resource not found: " + filename);
                }
                Files.copy(in, filePath);
            }
        }
    }
}
