package saveroll.saveroll.config;

import co.aikar.commands.InvalidCommandArgument;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import saveroll.saveroll.config.params.ArmorParam;
import saveroll.saveroll.config.params.CalcParam;
import saveroll.saveroll.config.params.CommandParam;
import saveroll.saveroll.config.params.HorseParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ConfigManager {

    private static FileConfiguration fileConfiguration;

    private ArrayList<Roll> rolls = new ArrayList<>();
    private SQLManager sqlConfig = new SQLManager();

    public static void setFileConfiguration(FileConfiguration fileConfiguration) {
        ConfigManager.fileConfiguration = fileConfiguration;
    }

    public CalcParam getCalcParam(String nameParam) {
        if(ArmorParam.getName().equals(nameParam)) {
            return new ArmorParam();
        }
        else if(HorseParam.getName().equals(nameParam)) {
            return new HorseParam();
        }
        if(CommandParam.getName().equals(nameParam)) {
            return new CommandParam();
        }
        else throw new InvalidCommandArgument("Неправельный аргумент команды getCalcParam");
    }

    public void updateSQL() {
        ConfigurationSection mysql = fileConfiguration.getConfigurationSection("mysql");
        assert mysql != null;
        sqlConfig.setUrl(mysql.getString("server_url"));
        sqlConfig.setUser(mysql.getString("name"));
        sqlConfig.setPassword(mysql.getString("pass"));
        if(mysql.isConfigurationSection("dataSourceProperty")) {
            HashMap<String, String> params = new HashMap<>();
            ConfigurationSection dataSourceProperty = mysql.getConfigurationSection("dataSourceProperty");
            for (String key : dataSourceProperty.getKeys(false)) {
                params.put(key, dataSourceProperty.getString(key));
            }
            sqlConfig.setParams(params);
        }
    }

    public void updateRoll() {
        ConfigurationSection rollsSection = fileConfiguration.getConfigurationSection("rolls");
        assert rollsSection != null;
        for (String nameRoll : rollsSection.getKeys(false)) {
            ConfigurationSection rollSection = rollsSection.getConfigurationSection(nameRoll);
            assert rollSection != null;
            if(rollSection.isString("placeholder") && rollSection.isList("calc")) {
                ArrayList<CalcParam> calcParamsRoll = new ArrayList<>();
                for (String param : rollSection.getStringList("calc")) calcParamsRoll.add(getCalcParam(param.toUpperCase(Locale.ROOT)));
                rolls.add(new Roll(rollSection.getString("placeholder"), calcParamsRoll));
            }
        }
    }

    public void updateSettings() {

    }


}
