package saveroll.saveroll.roll;

import org.bukkit.entity.Player;
import saveroll.saveroll.SaveRoll;

import java.awt.*;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

public class RollManager {
    public static final String ROLL_SYSTEM_NAME_FROM_TEXT_PLACEHOLDER = "[roll-name-system]";
    public static final String ROLL_NAME_FROM_TEXT_PLACEHOLDER = "[roll-name]";

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
        int roll = 0;
        roll += SaveRoll.getInstance().getDateBaseManager().getRollForPlayer(player.getUniqueId(), nameRoll);
        if(!rolls.containsKey(nameRoll)) return roll;
        else return rolls.get(nameRoll).calculateRoll(player) + roll;
    }

    public int calculateRoll(Roll roll, Player player) {
        int rollPlayer = 0;
        rollPlayer += SaveRoll.getInstance().getDateBaseManager().getRollForPlayer(player.getUniqueId(), roll.getSystemName());
        return roll.calculateRoll(player) + rollPlayer;
    }


    public boolean hasRoll(String nameRoll) {
        return rolls.containsKey(nameRoll);
    }

    public String getNameRoll(String params) {
        if(!hasRoll(params)) return null;
        return rolls.get(params).getSystemName();
    }

    public String getSystemNameRoll(String params) {
        if(!hasRoll(params)) return null;
        return rolls.get(params).getSystemName();
    }
}
