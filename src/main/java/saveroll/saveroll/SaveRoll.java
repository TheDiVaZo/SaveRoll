package saveroll.saveroll;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import saveroll.logging.JULHandler;
import saveroll.logging.Logger;
import saveroll.saveroll.bonus.EffectentBonus;
import saveroll.saveroll.bonus.EquipmentBonus;
import saveroll.saveroll.bonus.RiderBonus;
import saveroll.saveroll.command.AdminCommand;
import saveroll.saveroll.command.UserCommand;
import saveroll.saveroll.config.ConfigManager;
import saveroll.saveroll.datebase.DateBaseManager;
import saveroll.saveroll.datebase.PermsBaseManager;
import saveroll.saveroll.placeholder.RollExpansion;
import saveroll.saveroll.roll.Roll;
import saveroll.saveroll.roll.RollManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SaveRoll extends JavaPlugin {

    private static SaveRoll instance;

    private ConfigManager configManager = new ConfigManager();
    private RollManager rollManager = new RollManager();
    private DateBaseManager dateBaseManager;

    public ConfigManager getConfigManager() {
        return configManager;
    }
    public RollManager getRollManager() {
        return rollManager;
    }
    public DateBaseManager getDateBaseManager() {
        return dateBaseManager;
    }
    public RollExpansion rollExpansion = new RollExpansion();

    public void setDateBaseManager() {
        dateBaseManager = new PermsBaseManager();
        Logger.info("DateBaseManager has installed");
    }

    @Override
    public void onEnable() {
        Logger.init(new JULHandler(Bukkit.getLogger()));
        Logger.info("Starting...");
        saveDefaultConfig();
        getConfigManager().updateFileConfiguration(getConfig());
        generateRolls();
        registerCommands();
        setDateBaseManager();
        instance = this;
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

    public void generateRolls() {
        Map<String, Roll> rollMap = new HashMap<>();
        for (Map.Entry<String, ConfigManager.RollConfig> rollsConfigEntry : getConfigManager().getRolls().entrySet()) {
            ConfigManager.RollConfig rollConfig = rollsConfigEntry.getValue();
            String nameRoll = rollsConfigEntry.getKey();

            Roll roll = new Roll(nameRoll, rollConfig.getPlaceholder(), rollConfig.getPlaceholderText());
            roll.setEquipmentBonus(EquipmentBonus.generateBonus(rollConfig.getConfigEquipItems()));
            roll.setPotionBonus(EffectentBonus.generateBonus(rollConfig.getConfigPotionEffect()));
            roll.setRiderBonus(RiderBonus.generateBonus(rollConfig.getConfigRider()));

            rollMap.put(nameRoll, roll);
        }
        getRollManager().setRolls(rollMap);
        Logger.info("Rolls has been loaded");
    }

    public void reloadPlugin() {
        reloadConfig();
        saveConfig();
        getConfigManager().updateFileConfiguration(getConfig());
        generateRolls();
        registerCommands();
        rollExpansion.unregister();
        rollExpansion = new RollExpansion();
        rollExpansion.register();
        setDateBaseManager();
        Logger.info("Config has been reloaded");
    }

    private void registerCommands() {
        PaperCommandManager manager = new PaperCommandManager(this);

        manager.registerCommand(new Commands());
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
