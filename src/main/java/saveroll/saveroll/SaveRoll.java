package saveroll.saveroll;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import saveroll.errors.NotExistObjectFromStringException;
import saveroll.errors.NotMatchPatternException;
import saveroll.logging.JULHandler;
import saveroll.logging.Logger;
import saveroll.saveroll.bonus.EffectentCondition;
import saveroll.saveroll.bonus.EquipmentCondition;
import saveroll.saveroll.bonus.RiderCondition;
import saveroll.saveroll.command.AdminCommand;
import saveroll.saveroll.command.UserCommand;
import saveroll.saveroll.config.ConfigManager;
import saveroll.saveroll.datebase.DateBaseManager;
import saveroll.saveroll.datebase.PermsBaseManager;
import saveroll.saveroll.placeholder.RollExpansion;
import saveroll.saveroll.roll.Roll;
import saveroll.saveroll.roll.RollManager;

import java.util.HashMap;
import java.util.Map;

public class SaveRoll extends JavaPlugin {

    private static SaveRoll instance;

    private ConfigManager configManager = new ConfigManager();
    private RollManager rollManager = new RollManager();
    private DateBaseManager dateBaseManager;

    public ConfigManager getConfigManager() {
        if(configManager == null) configManager = new ConfigManager();
        return configManager;
    }
    public RollManager getRollManager() {
        if(rollManager == null) rollManager = new RollManager();
        return rollManager;
    }
    public DateBaseManager getDateBaseManager() {
        if(dateBaseManager == null) setDateBaseManager();
        return dateBaseManager;
    }
    public RollExpansion rollExpansion = new RollExpansion();

    public void setDateBaseManager() {
        if(dateBaseManager == null) {
            dateBaseManager = new PermsBaseManager();
            Logger.info("DateBaseManager has installed");
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        Logger.init(new JULHandler(Bukkit.getLogger()));
        Logger.info("Starting...");
        saveDefaultConfig();
        getConfigManager().updateFileConfiguration(getConfig());
        try {
            generateRolls();
        } catch (NotExistObjectFromStringException | NotMatchPatternException e) {
            Logger.error(e.getMessage());
            e.printStackTrace();
        }
        registerCommands();
        setDateBaseManager();
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            rollExpansion = new RollExpansion();
            rollExpansion.register();
        }
    }

    public static SaveRoll getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void generateRolls() throws NotExistObjectFromStringException, NotMatchPatternException {
        Map<String, Roll> rollMap = new HashMap<>();
        for (Map.Entry<String, ConfigManager.RollConfig> rollsConfigEntry : getConfigManager().getRolls().entrySet()) {
            ConfigManager.RollConfig rollConfig = rollsConfigEntry.getValue();
            String nameRoll = rollsConfigEntry.getKey();

            Roll roll = new Roll(nameRoll);
            roll.setName(rollConfig.getName());
            roll.setSystemName(rollConfig.getSystemName());
            roll.setEquipmentBonus(EquipmentCondition.generateBonus(rollConfig.getConfigEquipItems()));
            roll.setPotionBonus(EffectentCondition.generateBonus(rollConfig.getConfigPotionEffect()));
            roll.setRiderBonus(RiderCondition.generateBonus(rollConfig.getConfigRider()));
            Logger.debug("Rider bonus SET");

            rollMap.put(nameRoll, roll);
        }
        getRollManager().setRolls(rollMap);

        Logger.info("Rolls has been loaded");
    }

    public void reloadPlugin() throws NotExistObjectFromStringException, NotMatchPatternException {
        reloadConfig();
        saveConfig();
        getConfigManager().updateFileConfiguration(getConfig());
        generateRolls();
        registerCommands();
        rollExpansion.unregister();
        rollExpansion = new RollExpansion();
        rollExpansion.register();
        setDateBaseManager();
        Logger.setDebugMode(getConfig().getBoolean("settings.debug"));
        Logger.info("Config has been reloaded");
        Logger.debug("Debug Mode has been enabled");
    }

    private void registerCommands() {
        PaperCommandManager manager = new PaperCommandManager(this);

        manager.getCommandCompletions().registerCompletion("rollList", c -> configManager.getRolls().keySet().stream().toList());
        manager.registerCommand(new AdminCommand());
        manager.registerCommand(new UserCommand());

        manager.setDefaultExceptionHandler((command, registeredCommand, sender, args, t)-> {
            getLogger().warning("Error occurred while executing command "+command.getName());
            return true;
        });
        Logger.info("Command has been register");
    }
}
