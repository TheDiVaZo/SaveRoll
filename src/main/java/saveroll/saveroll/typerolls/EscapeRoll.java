package saveroll.saveroll.typerolls;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class EscapeRoll extends TypesRoll{

    public EscapeRoll() {
        boosts.put("HORSE", 0);
    }

    @Override
    public int calculateRoll(Player player) {
        if(!player.isInsideVehicle()) return 0;
        if(player.getVehicle().getType().equals(EntityType.HORSE)) return boosts.get("HORSE");
        else return 0;
    }
}
