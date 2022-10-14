package saveroll.saveroll.roll;

import org.bukkit.entity.Player;
import saveroll.saveroll.bonus.EquipmentBonus;
import saveroll.saveroll.bonus.EffectentBonus;
import saveroll.saveroll.bonus.RiderBonus;

public class Roll {
    private String name;
    private String placeholder;
    private EquipmentBonus equipmentBonus;
    private EffectentBonus effectentBonus;
    private RiderBonus riderBonus;

    public Roll(String name, String placeholder) {
        this.name = name;
        this.placeholder = placeholder;
    }

    public EquipmentBonus getEquipmentBonus() {
        return equipmentBonus;
    }

    public void setEquipmentBonus(EquipmentBonus equipmentBonus) {
        this.equipmentBonus = equipmentBonus;
    }

    public EffectentBonus getPotionBonus() {
        return effectentBonus;
    }

    public void setPotionBonus(EffectentBonus effectentBonus) {
        this.effectentBonus = effectentBonus;
    }

    public RiderBonus getRiderBonus() {
        return riderBonus;
    }

    public void setRiderBonus(RiderBonus riderBonus) {
        this.riderBonus = riderBonus;
    }

    public int calculateRoll(Player player) {
        return equipmentBonus.calculateRoll(player) + effectentBonus.calculateRoll(player) + riderBonus.calculateRoll(player);
    }
}
