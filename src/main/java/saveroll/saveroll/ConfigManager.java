package saveroll.saveroll;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import saveroll.saveroll.typerolls.AttackRoll;
import saveroll.saveroll.typerolls.DefendRoll;
import saveroll.saveroll.typerolls.EscapeRoll;
import saveroll.saveroll.typerolls.TypesRoll;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private FileConfiguration configuration;

    AttackRoll attackRoll = new AttackRoll();
    DefendRoll defendRoll = new DefendRoll();
    EscapeRoll escapeRoll = new EscapeRoll();

    public AttackRoll getAttackRoll() {
        return attackRoll;
    }

    public DefendRoll getDefendRoll() {
        return defendRoll;
    }

    public EscapeRoll getEscapeRoll() {
        return escapeRoll;
    }

    public ConfigManager(FileConfiguration configuration) {
        this.configuration = configuration;
    }

    public void updateConfiguration(FileConfiguration configuration) {
        this.configuration = configuration;
    }

    public void updateRolls() {
        HashMap<String, TypesRoll> rolls = new HashMap<>();
        rolls.put("defend", defendRoll);
        rolls.put("attack", attackRoll);
        rolls.put("escape", escapeRoll);

        ConfigurationSection rollsSection = configuration.getConfigurationSection("rolls");
        assert rollsSection != null;

        for (Map.Entry<String, TypesRoll> namesAndValuesRolls : rolls.entrySet()) {
            ConfigurationSection rollSection = rollsSection.getConfigurationSection(namesAndValuesRolls.getKey());
            assert rollSection != null;
            for (String boost : rollSection.getKeys(false)) {
                namesAndValuesRolls.getValue().setBoost(boost, rollSection.getInt(boost));
            }
        }
    }



}
