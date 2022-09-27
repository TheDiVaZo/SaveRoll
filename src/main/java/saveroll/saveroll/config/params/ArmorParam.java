package saveroll.saveroll.config.params;

import org.bukkit.entity.Player;

public class ArmorParam extends CalcParam {

    static {
        values.put("FULL_NETHERITE", "0");
        values.put("FULL_DIAMOND", "0");
        values.put("FULL_IRON", "0");
        values.put("FULL_GOLDEN", "0");
        values.put("FULL_CHAINMAIL", "0");
        values.put("FULL_LEATHER", "0");
        values.put("DIAMOND_BOOTS", "0");
        values.put("DIAMOND_CHESTPLATE", "0");
        values.put("DIAMOND_LEGGINGS", "0");
        values.put("DIAMOND_HELMET", "0");
        values.put("IRON_BOOTS", "0");
        values.put("IRON_CHESTPLATE", "0");
        values.put("IRON_LEGGINGS", "0");
        values.put("IRON_HELMET", "0");
        values.put("GOLDEN_BOOTS", "0");
        values.put("GOLDEN_CHESTPLATE", "0");
        values.put("GOLDEN_LEGGINGS", "0");
        values.put("GOLDEN_HELMET", "0");
        values.put("CHAINMAIL_BOOTS", "0");
        values.put("CHAINMAIL_CHESTPLATE", "0");
        values.put("CHAINMAIL_LEGGINGS", "0");
        values.put("CHAINMAIL_HELMET", "0");
        values.put("NETHERITE_BOOTS", "0");
        values.put("NETHERITE_CHESTPLATE", "0");
        values.put("NETHERITE_LEGGINGS", "0");
        values.put("NETHERITE_HELMET", "0");
        values.put("LEATHER_BOOTS", "0");
        values.put("LEATHER_CHESTPLATE", "0");
        values.put("LEATHER_LEGGINGS", "0");
        values.put("LEATHER_HELMET", "0");

    }

    @Override
    public float calcValue(Player player) {
        return 0;
    }

    static public String getName() {
        return "ARMOR";
    }
}
