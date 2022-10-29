package saveroll.saveroll.roll;

import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;

public class RollManager {
    private Map<String, Roll> rolls;

    public RollManager(Map<String, Roll> rolls) {
        this.rolls = rolls;
    }

    public RollManager(){}

    public void setRolls(Map<String, Roll> rolls) {
        this.rolls = rolls;
    }

    public Map<String, Roll> getRolls() {
        return Collections.unmodifiableMap(rolls);
    }

    public int calculateRoll(String nameRoll, Player player) {
        if(!rolls.containsKey(nameRoll)) return 0;
        else return rolls.get(nameRoll).calculateRoll(player);
    }

    public String getPlaceholderText(String nameRoll) {
        if(!rolls.containsKey(nameRoll)) return "";
        else return rolls.get(nameRoll).getPlaceholderText();
    }
}
