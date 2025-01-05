package by.saunear.mW.core.config;

import java.util.Map;

public interface IConfig {
    public void create() throws Exception;
    public Map<String, Object> load() throws Exception;
}
