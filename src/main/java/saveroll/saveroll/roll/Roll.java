package saveroll.saveroll.roll;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import saveroll.saveroll.bonus.EquipmentBonus;
import saveroll.saveroll.bonus.PotionBonus;
import saveroll.saveroll.bonus.RiderBonus;

public class Roll {
    private String name;
    private String placeholder;
    private EquipmentBonus equipmentBonus;
    private PotionBonus potionBonus;
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

    public PotionBonus getPotionBonus() {
        return potionBonus;
    }

    public void setPotionBonus(PotionBonus potionBonus) {
        this.potionBonus = potionBonus;
    }

    public RiderBonus getRiderBonus() {
        return riderBonus;
    }

    public void setRiderBonus(RiderBonus riderBonus) {
        this.riderBonus = riderBonus;
    }

    public int calculateRoll(Player player) {
        return equipmentBonus.calculateRoll(player) + potionBonus.calculateRoll(player) + riderBonus.calculateRoll(player);
    }
}
