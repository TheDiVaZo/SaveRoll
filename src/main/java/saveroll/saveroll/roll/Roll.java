package saveroll.saveroll.roll;

import org.bukkit.entity.Player;
import saveroll.saveroll.bonus.Bonus;
import saveroll.saveroll.bonus.EquipmentBonus;
import saveroll.saveroll.bonus.EffectentBonus;
import saveroll.saveroll.bonus.RiderBonus;

public class Roll {
    private String name;
    private String placeholder;
    private String placeholderText;
    private Bonus equipmentBonus;
    private Bonus effectentBonus;
    private Bonus riderBonus;

    public Roll(String name, String placeholder, String placeholderText) {
        this.name = name;
        this.placeholder = placeholder;
        this.placeholderText = placeholderText;
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

    public String getPlaceholderText() {
        return placeholderText;
    }
}
