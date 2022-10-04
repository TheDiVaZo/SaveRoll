package saveroll.saveroll;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import saveroll.logging.JULHandler;
import saveroll.logging.Logger;

public final class SaveRoll extends JavaPlugin {

    private static SaveRoll instance;

    private ConfigManager configManager = new ConfigManager(getConfig());

    public ConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public void onEnable() {
        Logger.init(new JULHandler(getLogger()));
        Logger.info("Starting...");
        saveDefaultConfig();
        getConfigManager().updateConfiguration(getConfig());
        registerCommands();
        instance = this;
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new CalculateRoll().register();
        }
    }

    public static SaveRoll getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reloadPlugin() {
        reloadConfig();
        configManager.updateConfiguration(getConfig());
        Logger.info("Config has been reloaded");
    }

    private void registerCommands() {
        PaperCommandManager manager = new PaperCommandManager(this);

        manager.registerCommand(new Commands());
        manager.getCommandCompletions().registerCompletion("rollList", c -> ImmutableList.of("attack", "defend", "escape"));

        manager.setDefaultExceptionHandler((command, registeredCommand, sender, args, t)-> {
            getLogger().warning("Error occurred while executing command "+command.getName());
            return true;
        });
    }
}
