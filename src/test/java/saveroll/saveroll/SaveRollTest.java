package saveroll.saveroll;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import saveroll.logging.JULHandler;
import saveroll.logging.Logger;
import saveroll.saveroll.bonus.EquipmentBonus;
import saveroll.saveroll.config.ConfigManager;
import saveroll.saveroll.roll.Roll;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SaveRollTest {

    {
        Logger.init(new JULHandler(java.util.logging.Logger.getAnonymousLogger()));
    }


}