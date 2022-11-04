package saveroll.saveroll.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import saveroll.logging.Logger;
import saveroll.saveroll.bonus.EffectentBonus;
import saveroll.saveroll.bonus.EquipmentBonus;
import saveroll.saveroll.bonus.RiderBonus;

import java.util.*;
import java.util.regex.Pattern;

public class ConfigManager {
//    interface MySQL {
//        String getServerUrl();
//        String getName();
//        String getPassword();
//        Map<String, Object> dataSourceProperty();
//    }

    public interface RollConfig {
        String getSystemName();
        String getName();

        ArrayList<EffectentBonus.ConfigPotionEffectParam> getConfigPotionEffect();
        ArrayList<EquipmentBonus.ConfigEquipItemsParam> getConfigEquipItems();
        ArrayList<RiderBonus.ConfigRiderParam> getConfigRider();
    }

//    private MySQL mySQLConfig = new MySQL() {
//        @Override
//        public String getServerUrl() {
//            return null;
//        }
//
//        @Override
//        public String getName() {
//            return null;
//        }
//
//        @Override
//        public String getPassword() {
//            return null;
//        }
//
//        @Override
//        public Map<String, Object> dataSourceProperty() {
//            return null;
//        }
//    };

    public ConfigManager(FileConfiguration fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
    }

    public ConfigManager(){}

    private FileConfiguration fileConfiguration;

    private Map<String, RollConfig> rolls = new HashMap<>();

    private int distanceVisibleRoll = 15;
    private String textFromDiceCommand = "&eИгрок %player_name% кинул ролл на %rollplus_[roll-system-name]_name% %rollpluss_[roll-system-name]_random_count%";

    public void updateFileConfiguration(FileConfiguration fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
        updateRolls();
        updateSettings();
    }

    public Map<String, RollConfig> getRolls() {
        return Collections.unmodifiableMap(rolls);
    }

//    private void updateMySQLConfig() {
//        ConfigurationSection mySQLSection = fileConfiguration.getConfigurationSection("mysql");
//        String serverUrl = mySQLSection.getString("server_url");
//        String name = mySQLSection.getString("name");
//        String pass = mySQLSection.getString("pass");
//        Map<String, Object> dataSourceProperty = mySQLSection.getConfigurationSection("dataSourceProperty").getValues(false);
//        mySQLConfig = new MySQL() {
//            @Override
//            public String getServerUrl() {
//                return serverUrl;
//            }
//
//            @Override
//            public String getName() {
//                return name;
//            }
//
//            @Override
//            public String getPassword() {
//                return pass;
//            }
//
//            @Override
//            public Map<String, Object> dataSourceProperty() {
//                return dataSourceProperty;
//            }
//        };
//    }

    private void updateSettings() {
        distanceVisibleRoll = fileConfiguration.getInt("settings.command.dice.distance");
        textFromDiceCommand = fileConfiguration.getString("settings.command.dice.text");
    }

    private void updateRolls() {
        rolls.clear();
        ConfigurationSection bonusrolls = fileConfiguration.getConfigurationSection("bonusrolls");
        //assert bonusrolls != null;
        Set<String> namesPlusRolls = bonusrolls.getKeys(false);
        for (String systemName : namesPlusRolls) {
            ConfigurationSection bonusConfigSection = bonusrolls.getConfigurationSection(systemName);
            RollConfig rollConfig;
            String gameName = bonusConfigSection.getString("name");
            if(gameName == null) gameName = "Roll"+Math.round(Math.random() * 100000000);


            ArrayList<EquipmentBonus.ConfigEquipItemsParam> configEquipItemsParams = new ArrayList<>();
            ArrayList<EffectentBonus.ConfigPotionEffectParam> configPotionEffectParams = new ArrayList<>();
            ArrayList<RiderBonus.ConfigRiderParam> configRiderParams = new ArrayList<>();

            if(bonusConfigSection.isConfigurationSection("equip")) {
                Map<String, Object> equip = bonusConfigSection.getConfigurationSection("equip").getValues(false);
                for (String key : equip.keySet()) {
                    if(!(equip.get(key) instanceof ConfigurationSection itemComplect)) continue;
                    List<String> items = getAlwaysArrayList("items", itemComplect);
                    List<String> slots = getAlwaysArrayList("slots", itemComplect);
                    int rollSlots = itemComplect.getInt("fill-slot");
                    int additionalRoll = itemComplect.getInt("additional-roll");
                    if(rollSlots == -1) rollSlots = slots.size();
                    configEquipItemsParams.add(EquipmentBonus.generateConfig(items, slots, rollSlots, additionalRoll));
                }
            }
            if(bonusConfigSection.isConfigurationSection("potion")) {
                Map<String, Object> equip = bonusConfigSection.getConfigurationSection("potion").getValues(false);
                for (String key : equip.keySet()) {
                    if(!(equip.get(key) instanceof ConfigurationSection potionComplect)) continue;
                    List<String> effects = getAlwaysArrayList("effects", potionComplect);
                    int countEffects = potionComplect.getInt("count-effects");
                    int additionalRoll = potionComplect.getInt("additional-roll");
                    configPotionEffectParams.add(EffectentBonus.generateConfig(effects, countEffects, additionalRoll));
                }
            }
            if(bonusConfigSection.isConfigurationSection("rider")) {
                Map<String, Object> equip = bonusConfigSection.getConfigurationSection("rider").getValues(false);
                for (String key : equip.keySet()) {
                    Logger.debug(key + " - rider element");
                    if(!(equip.get(key) instanceof ConfigurationSection animalsComplect)) continue;
                    List<String> animals = getAlwaysArrayList("animals", animalsComplect);
                    List<String> armors = getAlwaysArrayList("armors", animalsComplect);
                    Logger.debug(Arrays.toString(armors.toArray(new String[0])) + " updateRolls Config! " +key+ " - element armors");
                    int additionalRoll = animalsComplect.getInt("additional-roll");
                    configRiderParams.add(RiderBonus.generateConfig(animals, armors, additionalRoll));
                }
            }

            String finalGameName = gameName;
            rollConfig = new RollConfig() {
                @Override
                public String getSystemName() {
                    return systemName;
                }

                @Override
                public String getName() {
                    return finalGameName;
                }

                @Override
                public ArrayList<EffectentBonus.ConfigPotionEffectParam> getConfigPotionEffect() {
                    return configPotionEffectParams;
                }

                @Override
                public ArrayList<EquipmentBonus.ConfigEquipItemsParam> getConfigEquipItems() {
                    return configEquipItemsParams;
                }

                @Override
                public ArrayList<RiderBonus.ConfigRiderParam> getConfigRider() {
                    return configRiderParams;
                }

            };
            rolls.put(systemName, rollConfig);
        }
    }

    private List<String> getAlwaysArrayList(String path, ConfigurationSection configurationSection) {
        if(configurationSection.isList(path)) return configurationSection.getStringList(path);
        else if(configurationSection.isString(path)) return new ArrayList<>(){{add(configurationSection.getString(path));}};
        else return new ArrayList<>();
    }

    public int getDistanceVisibleRoll() {
        return distanceVisibleRoll;
    }

    public String getTextFromDiceCommand() {
        return textFromDiceCommand;
    }

}
