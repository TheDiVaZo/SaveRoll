package saveroll.saveroll.typerolls;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


import java.util.Arrays;

import static org.bukkit.Material.*;

public class DefendRoll extends TypesRoll{

    public enum ArmorMaterial {
        DIAMOND_ARMOR,
        NETHERITE_ARMOR,
        GOLDEN_ARMOR,
        LEATHER_ARMOR,
        IRON_ARMOR,
        CHAINMAIL_ARMOR
    }

    private static class FullArmor {
        public static Material[] getArmorForMaterial(ArmorMaterial material) {
            switch (material) {
                case IRON_ARMOR: return new Material[]{IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS};
                case CHAINMAIL_ARMOR: return new Material[]{CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS};
                case GOLDEN_ARMOR: return new Material[]{GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS};
                case DIAMOND_ARMOR: return new Material[]{DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS};
                case LEATHER_ARMOR: return new Material[]{LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS};
                case NETHERITE_ARMOR: return new Material[]{NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS};
                default: throw new IllegalArgumentException("Material is destroyed");
            }
        }
    }

    public DefendRoll() {
        boosts.put("NETHERITE_ARMOR", 0);
        boosts.put("IRON_ARMOR", 0);
        boosts.put("LEATHER_ARMOR", 0);
        boosts.put("CHAINMAIL_ARMOR", 0);
        boosts.put("DIAMOND_ARMOR", 0);
        boosts.put("GOLDEN_ARMOR", 0);
    }

    @Override
    public int calculateRoll(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggins = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        int plusOfRoll = 0;

        Material[] armorPlayer = new Material[]{player.getInventory().getHelmet().getType(), player.getInventory().getChestplate().getType(), player.getInventory().getLeggings().getType(), player.getInventory().getBoots().getType()};

        if(Arrays.equals(armorPlayer, FullArmor.getArmorForMaterial(ArmorMaterial.CHAINMAIL_ARMOR))) plusOfRoll+=getBoost(ArmorMaterial.CHAINMAIL_ARMOR.toString());
        else if(Arrays.equals(armorPlayer, FullArmor.getArmorForMaterial(ArmorMaterial.GOLDEN_ARMOR))) plusOfRoll+=getBoost(ArmorMaterial.GOLDEN_ARMOR.toString());
        else if(Arrays.equals(armorPlayer, FullArmor.getArmorForMaterial(ArmorMaterial.IRON_ARMOR))) plusOfRoll+=getBoost(ArmorMaterial.IRON_ARMOR.toString());
        else if(Arrays.equals(armorPlayer, FullArmor.getArmorForMaterial(ArmorMaterial.LEATHER_ARMOR))) plusOfRoll+=getBoost(ArmorMaterial.LEATHER_ARMOR.toString());
        else if(Arrays.equals(armorPlayer, FullArmor.getArmorForMaterial(ArmorMaterial.DIAMOND_ARMOR))) plusOfRoll+=getBoost(ArmorMaterial.DIAMOND_ARMOR.toString());
        else if(Arrays.equals(armorPlayer, FullArmor.getArmorForMaterial(ArmorMaterial.NETHERITE_ARMOR))) plusOfRoll+=getBoost(ArmorMaterial.NETHERITE_ARMOR.toString());

        return plusOfRoll;
    }
}
