package saveroll.saveroll.roll;

import org.bukkit.entity.Player;
import saveroll.saveroll.bonus.Bonus;

public class Roll {
    private String systemName;
    private String name;
    private Bonus equipmentBonus;
    private Bonus effectentBonus;
    private Bonus riderBonus;

    public Roll(String systemName) {
        this.systemName = systemName;
    }

    public Bonus getEquipmentBonus() {
        return equipmentBonus;
    }

    public void setEquipmentBonus(Bonus equipmentBonus) {
        this.equipmentBonus = equipmentBonus;
    }

    public Bonus getPotionBonus() {
        return effectentBonus;
    }

    public void setPotionBonus(Bonus effectentBonus) {
        this.effectentBonus = effectentBonus;
    }

    public Bonus getRiderBonus() {
        return riderBonus;
    }

    public void setRiderBonus(Bonus riderBonus) {
        this.riderBonus = riderBonus;
    }

    public int calculateRoll(Player player) {
        return equipmentBonus.calculateRoll(player) + effectentBonus.calculateRoll(player) + riderBonus.calculateRoll(player);
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
