package saveroll.saveroll.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import saveroll.saveroll.bonus.EffectentBonus;
import saveroll.saveroll.bonus.EquipmentBonus;
import saveroll.saveroll.roll.Roll;

import java.util.*;

public class ConfigManager {
    interface MySQL {
        String getServerUrl();
        String getName();
        String getPassword();
        Map<String, Object> dataSourceProperty();
    }

    interface RollsConfig {
        String getPlaceholder();
        ArrayList<EffectentBonus.ConfigPotionEffectParam> getConfigPotionEffect();
        ArrayList<EquipmentBonus.ConfigEquipItemsParam> getConfigEquipItems();
    }

    private MySQL mySQLConfig = new MySQL() {
        @Override
        public String getServerUrl() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public Map<String, Object> dataSourceProperty() {
            return null;
        }
    };

    private FileConfiguration fileConfiguration;

    private ArrayList<RollsConfig> rolls = new ArrayList<>();

    public List<RollsConfig> getRolls() {
        return Collections.unmodifiableList(rolls);
    }

    private void updateMySQLConfig() {
        ConfigurationSection mySQLSection = fileConfiguration.getConfigurationSection("mysql");
        String serverUrl = mySQLSection.getString("server_url");
        String name = mySQLSection.getString("name");
        String pass = mySQLSection.getString("pass");
        Map<String, Object> dataSourceProperty = mySQLSection.getConfigurationSection("dataSourceProperty").getValues(false);
        mySQLConfig = new MySQL() {
            @Override
            public String getServerUrl() {
                return serverUrl;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getPassword() {
                return pass;
            }

            @Override
            public Map<String, Object> dataSourceProperty() {
                return dataSourceProperty;
            }
        };
    }

    private void updateRolls() {
        ConfigurationSection bonusrolls = fileConfiguration.getConfigurationSection("bonusrolls");
        //assert bonusrolls != null;
        Set<String> namesBonus = bonusrolls.getKeys(false);
        for (String bonus : namesBonus) {
            ConfigurationSection bonusConfigSection = bonusrolls.getConfigurationSection(bonus);
            String placeholder = bonusConfigSection.getString("placeholder");
            RollsConfig equip;
            RollsConfig potion;
            RollsConfig rider;
            //доделать
        }
    }

}
