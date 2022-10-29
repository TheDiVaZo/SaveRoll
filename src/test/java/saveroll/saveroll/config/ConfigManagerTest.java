package saveroll.saveroll.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;
import saveroll.logging.JULHandler;
import saveroll.logging.Logger;
import saveroll.saveroll.bonus.EffectentBonus;
import saveroll.saveroll.bonus.EquipmentBonus;
import saveroll.saveroll.bonus.RiderBonus;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ConfigManagerTest {
    {
        Logger.init(new JULHandler(java.util.logging.Logger.getAnonymousLogger()));
    }
    @Test
    void testConfig() throws IOException, InvalidConfigurationException {
        FileConfiguration configuration = new YamlConfiguration();
        configuration.load(new File("C:\\Users\\thedi\\IdeaProjects\\SaveRoll2.0\\src\\test\\resources\\config.yml"));

        ConfigManager configManager = new ConfigManager(configuration);
        configManager.updateFileConfiguration(configuration);
        for (ConfigManager.RollConfig rollConfig : configManager.getRolls().values()) {
            String text = rollConfig.getPlaceholder() + "\n";
            text += "equip {\n";
            for (EquipmentBonus.ConfigEquipItemsParam configEquipItem : rollConfig.getConfigEquipItems()) {
                text += "   items: " + Arrays.toString(configEquipItem.getItems().toArray(new String[]{})) + "\n";
                text += "   slots: " + Arrays.toString(configEquipItem.getSlots().toArray(new String[]{})) + "\n";
                text += "   additional-rol: " + configEquipItem.getAdditionalRoll() + "\n";
                text += "   fill-slots: " + configEquipItem.getFillSlots() + "\n";
                text += "-------------------------------\n";
            }
            text += "}\n";
            text += "potion {\n";
            for (EffectentBonus.ConfigPotionEffectParam configPotionEffectParam : rollConfig.getConfigPotionEffect()) {
                text += "   effects: " + Arrays.toString(configPotionEffectParam.getEffects().toArray(new String[]{})) + "\n";
                text += "   additional-rol: " + configPotionEffectParam.getAdditionalRoll() + "\n";
                text += "   count-effect: " + configPotionEffectParam.getCountEffects() + "\n";
                text += "-------------------------------\n";
            }
            text += "}\n";
            text += "rider {\n";
            for (RiderBonus.ConfigRiderParam configRiderParam : rollConfig.getConfigRider()) {
                text += "   animals: " + Arrays.toString(configRiderParam.getAnimals().toArray(new String[]{})) + "\n";
                text += "   armors: " + Arrays.toString(configRiderParam.getArmors().toArray(new String[0])) + "\n";
                text += "   additional-rol: " + configRiderParam.getAdditionalRoll() + "\n";
                text += "-------------------------------\n";
            }
            text += "}\n";
            Logger.info(text);
        }

        assertEquals(1,1);
    }

}