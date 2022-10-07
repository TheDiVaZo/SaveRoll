package saveroll.saveroll;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import saveroll.saveroll.datebase.SQLManager;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private FileConfiguration configuration;
    private SQLManager sqlManager = new SQLManager();

    public SQLManager getSqlManager() {
        return sqlManager;
    }

    public ConfigManager(FileConfiguration configuration) {
        this.configuration = configuration;
    }

    public void updateConfiguration(FileConfiguration configuration) {
        this.configuration = configuration;
        updateSQL();
    }

    public void updateSQL() {
        ConfigurationSection mysql = configuration.getConfigurationSection("mysql");
        assert mysql != null;
        sqlManager.setUrl(mysql.getString("server_url"));
        sqlManager.setRootNickname(mysql.getString("name"));
        sqlManager.setPassword(mysql.getString("pass"));
        if(mysql.isConfigurationSection("dataSourceProperty")) {
            HashMap<String, String> params = new HashMap<>();
            ConfigurationSection dataSourceProperty = mysql.getConfigurationSection("dataSourceProperty");
            for (String key : dataSourceProperty.getKeys(false)) {
                params.put(key, dataSourceProperty.getString(key));
            }
            sqlManager.setParams(params);
        }
    }

}
