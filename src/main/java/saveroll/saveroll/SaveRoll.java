package saveroll.saveroll;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
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
        registerCommands();
        instance = this;
    }

    public static SaveRoll getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reloadPlugin() {
        saveConfig();
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
