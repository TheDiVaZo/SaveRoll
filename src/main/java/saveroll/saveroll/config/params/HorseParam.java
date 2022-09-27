package saveroll.saveroll.config.params;

import org.bukkit.entity.Player;

public class HorseParam extends CalcParam{

    static {
        values.put("LEATHER_HORSE_ARMOR", "0");
        values.put("IRON_HORSE_ARMOR", "0");
        values.put("GOLDEN_HORSE_ARMOR", "0");
        values.put("DIAMOND_HORSE_ARMOR", "0");
    }

    @Override
    public float calcValue(Player player) {
        return 0;
    }

    static public String getName() {
        return "HORSE";
    }
}
