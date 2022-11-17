package saveroll.saveroll.roll;

import org.bukkit.entity.Player;
import saveroll.saveroll.bonus.Condition;

public class Roll {
    private String systemName;
    private String name;
    private Condition equipmentCondition;
    private Condition effectentCondition;
    private Condition riderCondition;

    public Roll(String systemName) {
        this.systemName = systemName;
    }

    public Condition getEquipmentBonus() {
        return equipmentCondition;
    }

    public void setEquipmentBonus(Condition equipmentCondition) {
        this.equipmentCondition = equipmentCondition;
    }

    public Condition getPotionBonus() {
        return effectentCondition;
    }

    public void setPotionBonus(Condition effectentCondition) {
        this.effectentCondition = effectentCondition;
    }

    public Condition getRiderBonus() {
        return riderCondition;
    }

    public void setRiderBonus(Condition riderCondition) {
        this.riderCondition = riderCondition;
    }

    public int calculateRoll(Player player) {
        return equipmentCondition.calculateRoll(player) + effectentCondition.calculateRoll(player) + riderCondition.calculateRoll(player);
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
